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
 * 
 * - Parâmetros: numVertices e densidade
 * - Geração de grafo: mesmo estilo (arestas u -> v com u < v), garantindo DAG
 * - Adaptação mínima para BFS: parâmetro de origem.
 *
 * Autor: Gustavo Luiz Ferreira de Souza
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

    /** Origem para a BFS (adaptação necessária para o algoritmo). */
    @Param({"0"})
    private int origem;

    private Grafo grafo;
    private BFS bfs;
    private Random random;

    @Setup
    public void setup() {
        this.random = new Random(42);
        this.grafo  = new Grafo(numVertices);

        // grafo direcionado acíclico
        gerarGrafoAciclicoDirecionado(numVertices, densidade);

        if (origem < 0 || origem >= numVertices) {
            throw new IllegalArgumentException(
                "Origem inválida: " + origem + " (deve estar em [0," + (numVertices - 1) + "])");
        }

        this.bfs = new BFS(grafo);
    }

    /**
     * Gera um DAG: arestas sempre de um vértice menor para um maior (u < v),
     * evitando ciclos e respeitando a densidade desejada.
     */
    private void gerarGrafoAciclicoDirecionado(int n, double p) {
        if (n <= 1 || p <= 0.0) return;

        int maxArestas = (n * (n - 1)) / 2;
        int numArestas = (int) Math.round(maxArestas * p);

        for (int i = 0; i < numArestas; i++) {
            int u = random.nextInt(n - 1);
            int v = u + 1 + random.nextInt(n - u - 1); // garante u < v
            if (!grafo.existeAresta(u, v)) {
                grafo.adicionarAresta(u, v);
            }
        }
    }

    /** Executa a BFS e consome o vetor de distâncias. */
    @Benchmark
    public void bfsDistancias(Blackhole blackhole) {
        int[] d = bfs.distancias(origem);
        blackhole.consume(d);
    }

    /** Executa a BFS e consome a ordem de visita (variação opcional). */
    @Benchmark
    public void bfsOrdemVisita(Blackhole blackhole) {
        List<Integer> ordem = bfs.visitar(origem);
        blackhole.consume(ordem);
    }
}
