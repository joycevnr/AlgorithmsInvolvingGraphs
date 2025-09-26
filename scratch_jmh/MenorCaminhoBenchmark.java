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

import br.ufcg.computacao.dijkstra.Dijkstra;
import br.ufcg.computacao.floyd-warshall.FloydWarshall;
/**
 * Benchmark para avaliar o desempenho do algoritmo e Dijkstra e de Floyd-Warshall para encontrar o menor caminho entre dois pontos,
 * com diferentes tamanhos e densidades de grafos.
 *
 * @author Maria Eduarda Capela Cabral Pinheiro da Silva
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
public class MenorCaminhoBenchmark {
	@Param({"10", "100", "500", "1000", "1500", "2000", "2500", "3000"})
    private int numVertices;

    @Param({"0.1", "0.3", "0.5", "0.7", "1.0"})
    private double densidade;

    private int[][] grafo;
    private Dijkstra dijkstra;
    private FloydWarshall floydwarshall;
    private Random random;

    @Setup
    public void setup() {
        this.grafo = new int[numVertices][numVertices];
        this.random = new Random(42);

        gerarGrafoDirecionado(numVertices, densidade);
        
        this.dijkstra = new Dijkstra();
    }

    private void gerarGrafoDirecionado(int numVertices, double densidade) {
    	int maxArestas = (numVertices * (numVertices - 1)) / 2;
        int numArestas = (int) (maxArestas * densidade);
        int arestasCriadas = 0;
        while (arestasCriadas < numArestas) {
            int origem = random.nextInt(numVertices);
            int destino = random.nextInt(numVertices);
            if (origem != destino && grafo[origem][destino] == 0) {
                grafo[origem][destino] = random.nextInt(10)+1; 
                arestasCriadas++;
            }
        }
    }

    @Benchmark
    public void dijkstraSemFila(Blackhole blackhole) {
        int[][] resultado = dijkstra.dijkstra_semFila(grafo, 0);
        blackhole.consume(resultado);
    }
    @Benchmark
    public void dijkstraComFila(Blackhole blackhole) {
        int[][] resultado = dijkstra.dijkstra_comFila(grafo, 0);
        blackhole.consume(resultado);
    }
    @Benchmark
    public void floydWarshall(Blackhole blackhole) {
        this.floydwarshall = new FloydWarshall(grafo);
        int[][] resultado = floydwarshall.getdistancias();
        blackhole.consume(resultado);
    }
}
