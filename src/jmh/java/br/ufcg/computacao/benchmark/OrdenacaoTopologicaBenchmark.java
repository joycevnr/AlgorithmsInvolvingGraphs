// package br.ufcg.computacao.benchmark;

// import java.util.List;
// import java.util.Random;
// import java.util.concurrent.TimeUnit;

// import org.openjdk.jmh.annotations.Benchmark;
// import org.openjdk.jmh.annotations.BenchmarkMode;
// import org.openjdk.jmh.annotations.Fork;
// import org.openjdk.jmh.annotations.Measurement;
// import org.openjdk.jmh.annotations.Mode;
// import org.openjdk.jmh.annotations.OutputTimeUnit;
// import org.openjdk.jmh.annotations.Param;
// import org.openjdk.jmh.annotations.Scope;
// import org.openjdk.jmh.annotations.Setup;
// import org.openjdk.jmh.annotations.State;
// import org.openjdk.jmh.annotations.Warmup;
// import org.openjdk.jmh.infra.Blackhole;

// import br.ufcg.computacao.graph.Grafo;
// import br.ufcg.computacao.toposort.OrdenacaoTopologica;

// /**
//  * Benchmark para avaliar o desempenho da Ordenação Topológica
//  * com diferentes tamanhos e densidades de grafos.
//  *
//  * @author Joyce Vitória Nascimento Rodrigues
//  */
// @BenchmarkMode(Mode.AverageTime)
// @OutputTimeUnit(TimeUnit.MILLISECONDS)
// @State(Scope.Benchmark)
// @Fork(value = 1)
// @Warmup(iterations = 2, time = 3)
// @Measurement(iterations = 3, time = 5)
// public class OrdenacaoTopologicaBenchmark {

//     @Param({"100", "500", "1000", "5000"})
//     private int numVertices;

//     @Param({"0.1", "0.3", "0.5"})
//     private double densidade;

//     private Grafo grafo;
//     private OrdenacaoTopologica ordenacaoTopologica;
//     private Random random;

//     @Setup
//     public void setup() {
//         this.grafo = new Grafo(numVertices);
//         this.random = new Random(42);

//         gerarGrafoAciclicoDirecionado(numVertices, densidade);
        
//         this.ordenacaoTopologica = new OrdenacaoTopologica(grafo);
//     }

//     private void gerarGrafoAciclicoDirecionado(int numVertices, double densidade) {
//         // garanto que é um DAG: adicionar arestas apenas de vértices menores para maiores
//         int maxArestas = (numVertices * (numVertices - 1)) / 2;
//         int numArestas = (int) (maxArestas * densidade);
        
//         for (int i = 0; i < numArestas; i++) {
//             int origem = random.nextInt(numVertices - 1);
//             int destino = origem + 1 + random.nextInt(numVertices - origem - 1);
//             if (!grafo.existeAresta(origem, destino)) {
//                 grafo.adicionarAresta(origem, destino);
//             }
//         }
//     }

//     @Benchmark
//     public void ordenacaoTopologica(Blackhole blackhole) {
//         List<Integer> resultado = ordenacaoTopologica.ordenar();
//         blackhole.consume(resultado);
//     }
// }

// // repositório: https://github.com/openjdk/jmh
