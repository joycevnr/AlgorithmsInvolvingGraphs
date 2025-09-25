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

import br.ufcg.computacao.disjointsetunion.DSU;
import br.ufcg.computacao.disjointsetunion.DSULogn;

/**
 * Benchmark para comparar duas versões da estrutura Disjoint Set Union (DSU):
 * - DSU simples (O(n))
 * - DSU otimizado com Union by Size + Path Compression (O(log n))
 *
 * A densidade controla a quantidade de operações de união simuladas,
 * análogo à densidade de arestas em grafos.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
public class DisjointSetUnionBenchmark {
    
    @Param({ "100", "500", "1000", "5000" })
    private int numVertices;

    @Param({ "0.1", "0.3", "0.5" })
    private double densidade;

    private int[][] operacoes; // pares (a,b) para union
    private DSU dsuON;
    private DSULogn dsuOlogN;
    private Random random;

    @Setup
    public void setup() {
        this.random = new Random(42);

        // número de operações baseado na densidade
        int maxOperacoes = (numVertices * (numVertices - 1)) / 2;
        int numOperacoes = (int) (maxOperacoes * densidade);

        this.operacoes = new int[numOperacoes][2];
        for (int i = 0; i < numOperacoes; i++) {
            int a = random.nextInt(numVertices);
            int b = random.nextInt(numVertices);
            operacoes[i][0] = a;
            operacoes[i][1] = b;
        }

        // inicializa DSU O(n)
        dsuON = new DSU(numVertices);
        for (int i = 0; i < numVertices; i++) {
            dsuON.makeSet(i);
        }

        // inicializa DSU O(log n)
        dsuOlogN = new DSULogn(numVertices);
        for (int i = 0; i < numVertices; i++) {
            dsuOlogN.makeSet(i);
        }
    }

    @Benchmark
    public void benchmarkDSUON(Blackhole blackhole) {
        for (int[] op : operacoes) {
            dsuON.unionSet(op[0], op[1]);
            blackhole.consume(dsuON.findSet(op[0]));
        }
    }

    @Benchmark
    public void benchmarkDSUOlogN(Blackhole blackhole) {
        for (int[] op : operacoes) {
            dsuOlogN.unionSet(op[0], op[1]);
            blackhole.consume(dsuOlogN.findSet(op[0]));
        }
    }
}