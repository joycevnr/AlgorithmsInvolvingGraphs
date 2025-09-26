package br.ufcg.computacao.bfs;

import org.junit.jupiter.api.Test;

import br.ufcg.computacao.bfs.Grafo;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BFSTest {

    // ----- grafos de exemplo -----
    private Grafo sampleUndirected() {
        Grafo g = new Grafo(6, false); // não-direcionado
        g.adicionarAresta(0,1);
        g.adicionarAresta(0,2);
        g.adicionarAresta(1,3);
        g.adicionarAresta(0,4);
        g.adicionarAresta(4,5);
        return g;
    }

    private Grafo sampleDirected() {
        Grafo g = new Grafo(6, true); // direcionado
        g.adicionarAresta(0, 1);
        g.adicionarAresta(1, 3);
        g.adicionarAresta(0, 2);
        g.adicionarAresta(2, 3);
        g.adicionarAresta(3, 4);
        return g;
    }

    @Test
    public void distanciasNaoDirecionado() {
        BFS bfs = new BFS(sampleUndirected());
        assertArrayEquals(new int[] { 0, 1, 1, 2, 1, 2 }, bfs.distancias(0));
    }

    @Test
    public void distanciasDirecionado() {
        BFS bfs = new BFS(sampleDirected());
        assertArrayEquals(new int[] { 0, 1, 1, 2, 3, -1 }, bfs.distancias(0));
    }

    @Test
    public void inalcancaveis() {
        Grafo g = new Grafo(4, false);
        g.adicionarAresta(0, 1); // 2 e 3 ficam isolados
        BFS bfs = new BFS(g);
        assertArrayEquals(new int[] { 0, 1, -1, -1 }, bfs.distancias(0));
    }

    @Test
    public void caminhoMaisCurtoReconstrucao() {
        BFS bfs = new BFS(sampleUndirected());
        List<Integer> path = bfs.caminhoMaisCurto(2, 3); // esperado: 2->0->1->3
        assertFalse(path.isEmpty());
        assertEquals(2, path.get(0));
        assertEquals(3, path.get(path.size() - 1));
        int[] d = bfs.distancias(2);
        assertEquals(d[3] + 1, path.size()); // |caminho| = dist + 1
    }

    @Test
    public void ordemDeterministicaAdjOrdenada() {
        Grafo g = new Grafo(5, false);
        g.adicionarAresta(0, 2);
        g.adicionarAresta(0, 1);
        g.adicionarAresta(1, 3);
        g.adicionarAresta(1, 4);
        BFS bfs = new BFS(g);
        List<Integer> ordem = bfs.visitar(0);
        assertEquals(Arrays.asList(0, 1, 2, 3, 4), ordem);
    }

    @Test
    public void indicesInvalidos() {
        Grafo g = new Grafo(3, false);
        BFS bfs = new BFS(g);
        assertThrows(IllegalArgumentException.class, () -> bfs.distancias(-1));
        assertThrows(IllegalArgumentException.class, () -> bfs.caminhoMaisCurto(0, 3));
    }

    @Test
    public void metodoEstaticoDistances() {
        int[] d = BFS.distances(sampleDirected(), 0);
        assertArrayEquals(new int[] { 0, 1, 1, 2, 3, -1 }, d);
    }
}
