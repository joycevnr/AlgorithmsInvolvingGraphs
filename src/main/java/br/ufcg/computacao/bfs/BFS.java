package br.ufcg.computacao.bfs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * Implementação do algoritmo de Busca em Largura (BFS).
 *
 * <p>O algoritmo percorre o grafo em camadas a partir de um vértice origem,computando: 
 * (i) ordem de visita;
 * (ii) distâncias mínimas em número de arestas e; 
 * (iii) predecessores para reconstrução de caminho em grafos não ponderados.</p>
 *
 * <p>Referências acadêmicas:
 * <ul>
 *   <li>Feofiloff, P., Kohayakawa, Y., &amp; Wakabayashi, Y. (2011).
 *       <em>Uma Introdução Sucinta à Teoria dos Algoritmos</em>. IME-USP.
 *       Disponível em: https://www.ime.usp.br/~pf/algoritmos-em-grafos/</li>
 *   <li>https://www.geeksforgeeks.org/dsa/breadth-first-search-or-bfs-for-a-graph/.
 *       <em>Estruturas de Dados e seus Algoritmos</em> (3ª ed.). LTC. Cap. 7.</li>
 *   <li>Cormen, T. H., Leiserson, C. E., Rivest, R. L., &amp; Stein, C. (2009).
 *       <em>Introduction to Algorithms</em> (3rd ed.). MIT Press. Cap. 22.</li>
 * </ul></p>
 *
 * @author Gustavo Luiz Ferreira de Souza
 * @version 1.0
 */
public class BFS {

    /** Grafo alvo (direcionado ou não). */
    private final Grafo grafo;

    /** Cache da última execução para a mesma origem. */
    private int ultimaOrigem = -1;
    private List<Integer> ordemVisita = new ArrayList<>();
    private int[] distancia = new int[0];
    private int[] predecessor = new int[0];

    /**
     * Constrói uma BFS para o {@link Grafo} informado.
     * @param grafo grafo (não nulo)
     * @throws IllegalArgumentException se {@code grafo} for nulo
     */
    public BFS(Grafo grafo) {
        if (grafo == null) throw new IllegalArgumentException("Grafo não pode ser nulo.");
        this.grafo = grafo;
    }

   
    // API de execução (útil para testes/benchmark) — mantém cache por origem

    /**
     * Executa a BFS a partir de {@code origem}. Se já houver resultados para a
     * mesma origem (e tamanho do grafo não mudou), o cache é reutilizado.
     * @param origem vértice de partida (0 ≤ origem &lt; n)
     * @throws IllegalArgumentException se {@code origem} for inválida
     */
    public void run(int origem) {
        validarVertice(origem);

        final int n = grafo.getNumVertices();
        if (origem == ultimaOrigem && distancia.length == n) return;

        ordemVisita = new ArrayList<>(n);
        distancia   = new int[n];
        predecessor = new int[n];
        Arrays.fill(distancia, -1);
        Arrays.fill(predecessor, -1);

        boolean[] visitado = new boolean[n];
        Deque<Integer> fila = new ArrayDeque<>();
        visitado[origem] = true; 
        distancia[origem] = 0; 
        fila.addLast(origem);

        while (!fila.isEmpty()) {
            int u = fila.removeFirst();
            ordemVisita.add(u);
            for (int v : grafo.getAdjacentes(u)) {
                if (!visitado[v]) {
                    visitado[v] = true;
                    distancia[v] = distancia[u] + 1;
                    predecessor[v] = u;
                    fila.addLast(v);
                }
            }
        }
        ultimaOrigem = origem;
    }

    // Retorna cópia da ordem de visita da última execução.
    public List<Integer> getOrdemVisita() { 
    	return new ArrayList<>(ordemVisita); 
    }

    // Retorna cópia do vetor de distâncias da última execução.
    public int[] getDistancias() { 
    	return distancia.clone(); 
    }

    // Retorna cópia do vetor de predecessores da última execução.
    public int[] getPredecessores() { 
    	return predecessor.clone(); 
    }

    // Atalho: executa BFS e retorna apenas o vetor de distâncias.
    public static int[] distances(Grafo g, int origem) {
        BFS b = new BFS(g); b.run(origem); return b.getDistancias();
    }

    // Executa a BFS e devolve a ordem de visita.
    public List<Integer> visitar(int origem) { 
    	run(origem); return getOrdemVisita(); 
    }

    // Executa a BFS e devolve as distâncias mínimas a partir da origem. */
    public int[] distancias(int origem) { 
    	run(origem); 
    	return getDistancias(); 
    }

    /**
     * Reconstrói um caminho mais curto entre {@code origem} e {@code destino}.
     * Retorna lista vazia se {@code destino} for inalcançável.
     */
    public List<Integer> caminhoMaisCurto(int origem, int destino) {
        validarVertice(origem); validarVertice(destino); run(origem);
        List<Integer> caminho = new ArrayList<>();
        if (distancia[destino] == -1) return caminho;
        for (int v = destino; v != -1; v = predecessor[v]) caminho.add(0, v);
        return caminho;
    }


    // Utilitários

    /** Verifica se {@code v} está em [0, n−1]. */
    private void validarVertice(int v) {
        int n = grafo.getNumVertices();
        if (v < 0 || v >= n)
            throw new IllegalArgumentException("Vértice inválido: " + v + " (deve estar em [0," + (n - 1) + "]).");
    }
}