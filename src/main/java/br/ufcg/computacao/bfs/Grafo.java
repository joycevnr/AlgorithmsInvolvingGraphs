package br.ufcg.computacao.bfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estrutura de grafo com listas de adjacência.
 * Suporta grafo direcionado ou não-direcionado.
 * Mantém vizinhos sem duplicar e em ordem crescente (determinismo).
 */
public class Grafo {

    private final int n;
    private final boolean direcionado;
    @SuppressWarnings("unchecked")
    private final List<Integer>[] adj;

    /** Constrói um grafo direcionado por padrão. */
    public Grafo(int n) { this(n, true); }

    public Grafo(int n, boolean direcionado) {
        if (n <= 0) throw new IllegalArgumentException("n deve ser > 0");
        this.n = n;
        this.direcionado = direcionado;
        this.adj = new ArrayList[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
    }

    public int getNumVertices() { return n; }

    public boolean isDirecionado() { return direcionado; }

    /** Lista de adjacentes (visão somente leitura). */
    public List<Integer> getAdjacentes(int v) {
        validar(v);
        return Collections.unmodifiableList(adj[v]);
    }

    /** Retorna true se existir aresta u->v. */
    public boolean contemAresta(int u, int v) {
        validar(u); validar(v);
        return adj[u].contains(v);
    }

    /** Adiciona aresta u->v (e v->u se não-direcionado), sem duplicar e mantendo ordenado. */
    public void adicionarAresta(int u, int v) {
        validar(u); validar(v);
        inserirOrdenadoSemDuplicar(u, v);
        if (!direcionado) inserirOrdenadoSemDuplicar(v, u);
    }

    // ---------- utilitários ----------

    private void validar(int v) {
        if (v < 0 || v >= n) throw new IllegalArgumentException("Vértice inválido: " + v);
    }

    private void inserirOrdenadoSemDuplicar(int u, int v) {
        List<Integer> lu = adj[u];
        int idx = Collections.binarySearch(lu, v);
        if (idx < 0) lu.add(-idx - 1, v); // insere mantendo a ordem
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int u = 0; u < n; u++) sb.append(u).append(" -> ").append(adj[u]).append('\n');
        return sb.toString();
    }
}