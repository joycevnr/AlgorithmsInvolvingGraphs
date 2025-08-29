package br.ufcg.computacao.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Classe responsável por executar os benchmarks JMH.
 * 
 * @author Joyce Vitória Nascimento Rodrigues
 */
public class BenchmarkRunner {
    
    public static void main(String[] args) throws RunnerException {
        String includePattern = ".*";
        
        // Se um argumento for fornecido, usa-o como padrão de inclusão
        if (args.length > 0) {
            includePattern = args[0];
        }
        
        Options options = new OptionsBuilder()
            .include(includePattern)
            .warmupIterations(1)
            .measurementIterations(2)
            .forks(1)
            .result("resultados/jmh-results.json")
            .build();
        
        new Runner(options).run();
    }
}
