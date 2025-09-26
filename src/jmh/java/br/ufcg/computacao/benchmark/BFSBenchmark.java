package br.ufcg.computacao.benchmark;

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
import br.ufcg.computacao.bfs.Grafo;

/**
 * Benchmark para avaliar o desempenho do algoritmo BFS (Busca em Largura)
 * com diferentes tamanhos e densidades de grafos.
 *
 * @author Joyce Vitória Nascimento Rodrigues
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 2, time = 3)
@Measurement(iterations = 3, time = 5)
public class BFSBenchmark {

    @Param({"100", "500", "1000", "5000"})
    private int numVertices;

    @Param({"0.1", "0.3", "0.5"})
    private double densidade;

    private Grafo grafo;
    private BFS bfs;
    private Random random;
    private int verticeOrigem;

    @Setup
    public void setup() {
        this.grafo = new Grafo(numVertices, false); // Grafo não direcionado para BFS
        this.random = new Random(42);

        gerarGrafoNaoDirecionado(numVertices, densidade);
        
        // Escolhe um vértice aleatório como origem para a busca
        this.verticeOrigem = random.nextInt(numVertices);
        this.bfs = new BFS(grafo);
    }

    private void gerarGrafoNaoDirecionado(int numVertices, double densidade) {
        // Para grafo não direcionado: máximo de arestas = n*(n-1)/2
        int maxArestas = (numVertices * (numVertices - 1)) / 2;
        int numArestas = (int) (maxArestas * densidade);
        
        int arestasAdicionadas = 0;
        while (arestasAdicionadas < numArestas) {
            int origem = random.nextInt(numVertices);
            int destino = random.nextInt(numVertices);
            
            if (origem != destino && !grafo.contemAresta(origem, destino)) {
                // Para grafo não direcionado, adiciona apenas uma aresta (a classe cuida da bidirecionalidade)
                grafo.adicionarAresta(origem, destino);
                arestasAdicionadas++;
            }
        }
    }

    @Benchmark
    public void buscaEmLargura(Blackhole blackhole) {
        bfs.run(verticeOrigem);
        blackhole.consume(bfs.getDistancias());
        blackhole.consume(bfs.getPredecessores());
        blackhole.consume(bfs.getOrdemVisita());
    }
}
