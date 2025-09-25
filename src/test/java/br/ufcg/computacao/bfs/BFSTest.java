package br.ufcg.computacao.bfs;

import br.ufcg.computacao.graph.Grafo;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BFSTest {

    private Grafo sampleUndirected() {
        Grafo g = new Grafo(6);
        addUndirected(g, 0,1);
        addUndirected(g, 0,2);
        addUndirected(g, 1,3);
        addUndirected(g, 0,4);
        addUndirected(g, 4,5);
        return g;
    }

    private Grafo sampleDirected() {
        Grafo g = new Grafo(6);
        g.adicionarAresta(0,1);
        g.adicionarAresta(1,3);
        g.adicionarAresta(0,2);
        g.adicionarAresta(2,3);
        g.adicionarAresta(3,4);
        return g;
    }

    @Test
    public void distanciasNaoDirecionado() {
        BFS bfs = new BFS(sampleUndirected());
        assertArrayEquals(new int[]{0,1,1,2,1,2}, bfs.distancias(0));
    }

    @Test
    public void distanciasDirecionado() {
        BFS bfs = new BFS(sampleDirected());
        assertArrayEquals(new int[]{0,1,1,2,3,-1}, bfs.distancias(0));
    }

    @Test
    public void inalcancaveis() {
        Grafo g = new Grafo(4);
        addUndirected(g, 0,1); // 2 e 3 isolados
        BFS bfs = new BFS(g);
        assertArrayEquals(new int[]{0,1,-1,-1}, bfs.distancias(0));
    }

    @Test
    public void caminhoMaisCurtoReconstrucao() {
        BFS bfs = new BFS(sampleUndirected());
        List<Integer> path = bfs.caminhoMaisCurto(2, 3); // 2->0->1->3
        assertFalse(path.isEmpty());
        assertEquals(2, path.get(0));
        assertEquals(3, path.get(path.size()-1));
        int[] d = bfs.distancias(2);
        assertEquals(d[3] + 1, path.size());
    }

    @Test
    public void ordemDeterministicaAdjOrdenada() {
        Grafo g = new Grafo(5);
        addUndirected(g, 0,2);
        addUndirected(g, 0,1);
        addUndirected(g, 1,3);
        addUndirected(g, 1,4);
        BFS bfs = new BFS(g);
        List<Integer> ordem = bfs.visitar(0);
        assertEquals(Arrays.asList(0,1,2,3,4), ordem);
    }

    @Test
    public void indicesInvalidos() {
        Grafo g = new Grafo(3);
        BFS bfs = new BFS(g);
        assertThrows(IllegalArgumentException.class, () -> bfs.distancias(-1));
        assertThrows(IllegalArgumentException.class, () -> bfs.caminhoMaisCurto(0, 3));
    }

    @Test
    public void metodoEstaticoDistances() {
        int[] d = BFS.distances(sampleDirected(), 0);
        assertArrayEquals(new int[]{0,1,1,2,3,-1}, d);
    }

    private static void addUndirected(Grafo g, int u, int v) {
        if (!g.existeAresta(u, v)) g.adicionarAresta(u, v);
        if (!g.existeAresta(v, u)) g.adicionarAresta(v, u);
    }
}
