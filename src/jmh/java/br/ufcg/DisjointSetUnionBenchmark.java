package br.ufcg;

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
 * Benchmark para avaliar o desempenho das duas versões da estrutura
 * Disjoint Set Union (DSU): a implementação simples (O(n)) e a versão
 * otimizada com Union by Size + Path Compression (O(log n)).
 *
 * São executadas operações de união e busca em conjuntos de diferentes
 * tamanhos, a fim de comparar a eficiência das abordagens.
 *
 * @author Augusto de Brito Lopes
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
public class DisjointSetUnionBenchmark {

    @Param({"1000", "5000", "10000", "20000", "50000"})
    private int numElementos;

    private int[][] operacoes; // pares de operações union(a,b)
    private DSU dsuON;
    private DSULogn dsuOlogN;
    private Random random;

    @Setup
    public void setup() {
        this.random = new Random(42);

        // gera operações aleatórias de união entre pares
        this.operacoes = new int[numElementos][2];
        for (int i = 0; i < numElementos; i++) {
            int a = random.nextInt(numElementos);
            int b = random.nextInt(numElementos);
            operacoes[i][0] = a;
            operacoes[i][1] = b;
        }

        // inicializa DSU O(n)
        dsuON = new DSU(numElementos);
        for (int i = 0; i < numElementos; i++) {
            dsuON.makeSet(i);
        }

        // inicializa DSU O(log n)
        dsuOlogN = new DSULogn(numElementos);
        for (int i = 0; i < numElementos; i++) {
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

