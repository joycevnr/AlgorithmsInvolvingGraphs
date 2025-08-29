package br.ufcg.computacao.benchmark;

/**
 * Classe principal para inicializar os benchmarks.
 * Esta classe serve como ponto de entrada para executar 
 * os benchmarks via comando Gradle.
 *
 * @author Joyce Vitória Nascimento Rodrigues
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando benchmarks...");
        
        if (args.length > 0) {
            // Executa benchmark específico
            BenchmarkRunner.main(new String[]{args[0]});
        } else {
            // Executa todos os benchmarks
            BenchmarkRunner.main(new String[]{".*Benchmark.*"});
        }
        
        System.out.println("Benchmarks concluídos!");
    }
}
