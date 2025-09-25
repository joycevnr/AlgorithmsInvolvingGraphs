package br.ufcg.computacao.benchmark;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import br.ufcg.computacao.bfs.BFS;
import br.ufcg.computacao.graph.Grafo;

/**
 * Benchmark para avaliar o desempenho da BFS (Busca em Largura)
 * com diferentes tamanhos e densidades de grafos.
 *
 * Moldado a partir de OrdenacaoTopologicaBenchmark, com adaptações
 * mínimas para a API de BFS (origem e suporte a grafos dirigidos/não-dirigidos).
 *
 * @author Gustavo Luiz Ferreira de Souza
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 2, time = 3)
@Measurement(iterations = 3, time = 5)
public class BfsBenchmark {

    /** Número de vértices do grafo. */
    @Param({"100", "500", "1000", "5000", "10000"})
    private int numVertices;

    /** Densidade aproximada do grafo (0.0 a 1.0). */
    @Param({"0.1", "0.3", "0.5"})
    private double densidade;

    /** Grafo dirigido? (adaptação em relação ao benchmark de toposort, que sempre usava DAG). */
    @Param({"true", "false"})
    private boolean direcionado;

    /** Vértice de origem para a BFS. */
    @Param({"0"})
    private int origem;

    /** Semente fixa para reprodutibilidade. */
    @Param({"42"})
    private long seed;

    private Grafo grafo;
    private Random random;

    @Setup
    public void setup() {
        this.random = new Random(seed);
        this.grafo  = new Grafo(numVertices, direcionado);
        gerarGrafoAleatorio(numVertices, densidade, direcionado);
        // valida a origem dentro do intervalo
        if (origem < 0 || origem >= numVertices) {
            throw new IllegalArgumentException("Origem inválida: " + origem + " (deve estar em [0," + (numVertices - 1) + "])");
        }
    }

    /**
     * Gera um grafo aleatório com ~densidade desejada.
     * Para grafo dirigido, tenta ~ p * n*(n-1) arestas.
     * Para não-dirigido, tenta ~ p * n*(n-1)/2 arestas (inserção simétrica).
     *
     * Observação: não checamos duplicata antes de inserir; assumimos que a
     * implementação de Grafo evita duplicidade de vizinhos. Caso contrário,
     * substitua por uma checagem (ex.: existeAresta/contemAresta).
     */
    private void gerarGrafoAleatorio(int n, double p, boolean dirigido) {
        if (n <= 1 || p <= 0.0) return;

        long maxArestas = dirigido ? (long) n * (n - 1) : (long) n * (n - 1) / 2;
        long alvo = Math.max(0, Math.min(maxArestas, Math.round(maxArestas * p)));

        long adicionadas = 0;
        // limite de tentativas para evitar loop longo em densidades altas
        long tentativasMax = Math.max(alvo * 5, 1_000_000);

        for (long tent = 0; adicionadas < alvo && tent < tentativasMax; tent++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u == v) continue;

            if (dirigido) {
                grafo.adicionarAresta(u, v);
                adicionadas++;
            } else {
                // para não-dirigido, adiciona “ambas” via API da estrutura
                // (a implementação de Grafo não-dirigido já duplica simetricamente)
                grafo.adicionarAresta(Math.min(u, v), Math.max(u, v));
                adicionadas++;
            }
        }
    }

    /**
     * Benchmark: executa a BFS a partir de {@code origem} e consome as distâncias.
     * Você pode trocar para consumir a ordem de visita se quiser comparar variações.
     */
    @Benchmark
    public void bfsDistancias(Blackhole blackhole) {
        BFS bfs = new BFS(grafo);
        int[] dist = bfs.distancias(origem);
        blackhole.consume(dist);
    }

    /**
     * (Opcional) Outro alvo de benchmark:
     * percorre e consome a ordem de visita para evitar DCE.
     */
    @Benchmark
    public void bfsOrdemVisita(Blackhole blackhole) {
        BFS bfs = new BFS(grafo);
        List<Integer> ordem = bfs.visitar(origem);
        blackhole.consume(ordem);
    }
}