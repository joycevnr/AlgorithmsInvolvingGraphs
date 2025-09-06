package br.ufcg.computacao.floydwarshall;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Classe de testes unitários para a classe {@code FloydWarshall}.
 * <p>
 * Contém testes para:
 * <ul>
 * <li>Verificação das menores distâncias para todos os pares de vértices.</li>
 * <li>Verificação da reconstrução dos caminhos mínimos.</li>
 * <li>Comportamento com grafos direcionados, não direcionados, com ciclos e desconectados.</li>
 * </ul>
 * <p>
 * Os testes utilizam diferentes grafos, instanciados localmente em cada método de teste:
 * <ul>
 * <li>Um grafo 4x4 direcionado acíclico.</li>
 * <li>Dois grafos 5x5 não direcionados e conectados com topologias distintas.</li>
 * <li>Um grafo 2x2 desconectado para testar a ausência de caminhos.</li>
 * <li>Um grafo 3x3 direcionado com um ciclo para validar o comportamento em ciclos.</li>
 * </ul>
 * <p>
 * Os grafos são representados como matrizes de adjacência contendo pesos inteiros.
 * A constante {@code INF} representa a ausência de uma aresta.
 *
 * @author Gleydson Fabricio Rodrigues de Moura
 */
class FloydWarshallTests {

    private static final int INF = FloydWarshall.INFINITO;

    /**
     * Testa o cálculo de distâncias e caminhos em um grafo 4x4.
     * Verifica se caminhos indiretos mais curtos são encontrados corretamente.
     */
    @Test
    void testGrafo4x4DiagonalSuperior() {
        int[][] matriz = {
            {0,   3,  10, INF},
            {INF, 0,   1,  7},
            {INF, INF, 0,  2},
            {INF, INF, INF, 0}
        };

        int[][] esperado = {
            {0,     3,   4, 6},
            {INF,   0,   1, 3},
            {INF, INF,   0, 2},
            {INF, INF, INF, 0}
        };

        FloydWarshall fw = new FloydWarshall(matriz);
        int[][] resultado = fw.getDistancias();

        for (int i = 0; i < 4; i++)
            assertArrayEquals(esperado[i], resultado[i]);

        assertEquals(List.of(0, 1), fw.getCaminho(0, 1));
        assertEquals(List.of(0, 1, 2), fw.getCaminho(0, 2));
        assertEquals(List.of(0, 1, 2, 3), fw.getCaminho(0, 3));
        assertTrue(fw.getCaminho(3, 0).isEmpty()); // sem caminho

    }

    /**
     * Testa o cálculo de distâncias e caminhos em um grafo 5x5 não direcionado e conectado.
     * Valida a corretude do algoritmo em um cenário de grafo padrão.
     */
    @Test
    void testGrafo5x5() {
        int[][] matriz = {
            {0,   7,   INF, INF,  2},
            {7,   0,   1,   INF, INF},
            {INF, 1,   0,   3,   INF},
            {INF, INF, 3,   0,   4},
            {2,   INF, INF, 4,   0}
        };

        int[][] esperado = {
            {0, 7, 8, 6, 2},
            {7, 0, 1, 4, 8},
            {8, 1, 0, 3, 7},
            {6, 4, 3, 0, 4},
            {2, 8, 7, 4, 0}
        };

        FloydWarshall fw = new FloydWarshall(matriz);
        int[][] resultado = fw.getDistancias();

        for (int i = 0; i < 5; i++)
            assertArrayEquals(esperado[i], resultado[i]);

        assertEquals(List.of(0, 4), fw.getCaminho(0, 4));
        assertEquals(List.of(0, 4, 3), fw.getCaminho(0, 3));
        assertEquals(List.of(1, 2, 3), fw.getCaminho(1, 3));
        assertEquals(List.of(3, 2, 1), fw.getCaminho(3, 1));
    }

    /**
     * Verifica o comportamento do algoritmo para um grafo desconectado.
     * Garante que as distâncias entre vértices sem caminho permaneçam infinitas
     * e que a reconstrução do caminho retorne uma lista vazia.
     */
    @Test
    void testGrafoSemCaminhos() {
        int[][] matriz = {
            {0,   INF},
            {INF, 0}
        };

        int[][] esperado = {
            {0,   INF},
            {INF, 0}
        };

        FloydWarshall fw = new FloydWarshall(matriz);
        int[][] resultado = fw.getDistancias();

        assertArrayEquals(esperado[0], resultado[0]);
        assertArrayEquals(esperado[1], resultado[1]);

        assertTrue(fw.getCaminho(0, 1).isEmpty());
        assertTrue(fw.getCaminho(1, 0).isEmpty());

    }

    /**
     * Verifica o comportamento do algoritmo para um grafo direcionado com um ciclo
     * de pesos não-negativos. Assegura que os caminhos que passam pelo ciclo são
     * calculados corretamente.
     */
    @Test
    void testGrafoComCiclo() {
        int[][] matriz = {
            {0,   1,   INF},
            {INF, 0,   2},
            {4,   INF, 0}
        };

        int[][] esperado = {
            {0, 1, 3},
            {6, 0, 2},
            {4, 5, 0}
        };

        FloydWarshall fw = new FloydWarshall(matriz);
        int[][] resultado = fw.getDistancias();

        for (int i = 0; i < 3; i++)
            assertArrayEquals(esperado[i], resultado[i]);

        assertEquals(List.of(0, 1, 2), fw.getCaminho(0, 2));
        assertEquals(List.of(2, 0, 1), fw.getCaminho(2, 1));
        assertEquals(List.of(1, 2, 0), fw.getCaminho(1, 0));

    }

}

