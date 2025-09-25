package br.ufcg.computacao.bfs;

import br.ufcg.computacao.bfs.BFS;
import br.ufcg.computacao.graph.Grafo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Benchmark simples para o BFS.
 *
 * Opções:
 *   --n <int>            número de vértices (obrigatório)
 *   --directed           grafo direcionado (padrão: falso)
 *   --oneBased           arquivo de entrada usa índices 1..n (padrão: 0..n-1)
 *   --input <path>       arquivo com arestas "u v" (ou "u,v") por linha
 *   --gen <m>            gerar m arestas aleatórias (ignora --input)
 *   --seed <long>        semente do gerador (padrão: 42)
 *   --source <v>         origem (padrão: 0 ou 1 se --oneBased)
 *   --warmup <k>         execuções de aquecimento (padrão: 2)
 *   --repeat <k>         execuções medidas (padrão: 5)
 *   --csv                imprime em CSV
 *   --noHeader           não imprime o cabeçalho do CSV
 */
public class Main {

    public static void main(String[] args) {
        try {
            Config cfg = parseArgs(args);
            validarConfig(cfg);

            Grafo g = new Grafo(cfg.n);
            long m;

            if (cfg.inputPath != null) {
                m = carregarArestasDeArquivo(g, cfg.inputPath, cfg.oneBased);
            } else if (cfg.genEdges > 0) {
                m = gerarArestasAleatorias(g, cfg.genEdges, cfg.seed);
            } else {
                m = contarArestas(g);
            }

            int origem = (cfg.source != null) ? cfg.source : (cfg.oneBased ? 1 : 0);
            if (cfg.oneBased) origem--;

            // warmup
            BFS bfs = new BFS(g);
            for (int i = 0; i < cfg.warmup; i++) bfs.run(origem);

            // medições
            long[] temposNs = new long[cfg.repeat];
            long picoMemBytes = 0L;

            for (int i = 0; i < cfg.repeat; i++) {
                gcPause();
                long memBefore = usedMemoryBytes();
                long t0 = System.nanoTime();
                bfs.run(origem);
                long t1 = System.nanoTime();
                long memAfter = usedMemoryBytes();
                temposNs[i] = t1 - t0;
                picoMemBytes = Math.max(picoMemBytes, Math.max(memBefore, memAfter));
            }

            // estatísticas
            double min = Double.POSITIVE_INFINITY, max = 0, sum = 0;
            for (long ns : temposNs) {
                double ms = ns / 1_000_000.0;
                sum += ms; min = Math.min(min, ms); max = Math.max(max, ms);
            }
            double avg = sum / temposNs.length;
            double memMB = picoMemBytes / (1024.0 * 1024.0);

            // saída
            if (cfg.csv) {
                if (!cfg.noHeader) {
                    System.out.println("n,m,directed,origin,warmup,repeat,avg_ms,min_ms,max_ms,mem_peak_mb");
                }
                System.out.printf(Locale.US, "%d,%d,%b,%d,%d,%d,%.3f,%.3f,%.3f,%.2f%n",
                        g.getNumVertices(), m, cfg.directed, (cfg.oneBased ? origem + 1 : origem),
                        cfg.warmup, cfg.repeat, avg, min, max, memMB);
            } else {
                System.out.println("==== BFS Benchmark ====");
                System.out.println("n=" + g.getNumVertices() + ", m=" + m +
                        ", directed=" + cfg.directed +
                        ", origin=" + (cfg.oneBased ? (origem + 1) : origem));
                System.out.println("warmup=" + cfg.warmup + ", repeat=" + cfg.repeat);
                System.out.printf(Locale.US, "tempo(ms): avg=%.3f, min=%.3f, max=%.3f%n", avg, min, max);
                System.out.printf(Locale.US, "memória pico (MB): %.2f%n", memMB);
            }

        } catch (IllegalArgumentException iae) {
            System.err.println("Erro: " + iae.getMessage());
            System.exit(2);
        } catch (IOException ioe) {
            System.err.println("Falha ao ler arquivo: " + ioe.getMessage());
            System.exit(3);
        }
    }

    // ----------------- Config/Args -----------------

    private static class Config {
        Integer n;
        boolean directed = false;
        boolean oneBased = false;
        String inputPath = null;
        int genEdges = 0;
        long seed = 42L;
        Integer source = null;
        int warmup = 2;
        int repeat = 5;
        boolean csv = false;
        boolean noHeader = false;
    }

    private static Config parseArgs(String[] args) {
        Config c = new Config();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            switch (a) {
                case "--n": c.n = parseInt(args, ++i, "--n"); break;
                case "--directed": c.directed = true; break;
                case "--oneBased": c.oneBased = true; break;
                case "--input": c.inputPath = requireValue(args, ++i, "--input"); break;
                case "--gen": c.genEdges = parseInt(args, ++i, "--gen"); break;
                case "--seed": c.seed = parseLong(args, ++i, "--seed"); break;
                case "--source": c.source = parseInt(args, ++i, "--source"); break;
                case "--warmup": c.warmup = parseInt(args, ++i, "--warmup"); break;
                case "--repeat": c.repeat = parseInt(args, ++i, "--repeat"); break;
                case "--csv": c.csv = true; break;
                case "--noHeader": c.noHeader = true; break;
                default: throw new IllegalArgumentException("Argumento desconhecido: " + a);
            }
        }
        return c;
    }

    private static void validarConfig(Config c) {
        if (c.n == null || c.n <= 0) throw new IllegalArgumentException("Informe --n > 0.");
        if (c.source != null && c.oneBased && (c.source < 1 || c.source > c.n))
            throw new IllegalArgumentException("Source inválida (1-based).");
        if (c.source != null && !c.oneBased && (c.source < 0 || c.source >= c.n))
            throw new IllegalArgumentException("Source inválida (0-based).");
        if (c.genEdges < 0) throw new IllegalArgumentException("--gen não pode ser negativo.");
        if (c.warmup < 0 || c.repeat <= 0) throw new IllegalArgumentException("--warmup >= 0 e --repeat > 0.");
    }

    private static int parseInt(String[] args, int idx, String flag) {
        String v = requireValue(args, idx, flag);
        try { return Integer.parseInt(v); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Valor inválido para " + flag + ": " + v); }
    }
    private static long parseLong(String[] args, int idx, String flag) {
        String v = requireValue(args, idx, flag);
        try { return Long.parseLong(v); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Valor inválido para " + flag + ": " + v); }
    }
    private static String requireValue(String[] args, int idx, String flag) {
        if (idx >= args.length) throw new IllegalArgumentException("Faltou valor para " + flag + ".");
        return args[idx];
    }

    // ----------------- I/O/Utils -----------------

    private static long carregarArestasDeArquivo(Grafo g, String path, boolean oneBased) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) continue;
                String[] parts = linha.split("\\s+|,");
                if (parts.length < 2) throw new IllegalArgumentException("Linha inválida: \"" + linha + "\"");
                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);
                if (oneBased) { u--; v--; }
                g.adicionarAresta(u, v);
                count++;
            }
        }
        return count;
    }

    private static long gerarArestasAleatorias(Grafo g, int m, long seed) {
        Random rnd = new Random(seed);
        int n = g.getNumVertices(), adicionadas = 0;
        while (adicionadas < m) {
            int u = rnd.nextInt(n), v = rnd.nextInt(n);
            if (u == v) continue;
            if (!g.existeAresta(u, v)) { g.adicionarAresta(u, v); adicionadas++; }
        }
        return adicionadas;
    }

    private static long contarArestas(Grafo g) {
        long soma = 0;
        for (int v = 0; v < g.getNumVertices(); v++) soma += g.getAdjacentes(v).size();
        return soma;
    }

    private static void gcPause() {
        try { System.gc(); Thread.sleep(10); } catch (InterruptedException ignored) {}
    }

    private static long usedMemoryBytes() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }
}
