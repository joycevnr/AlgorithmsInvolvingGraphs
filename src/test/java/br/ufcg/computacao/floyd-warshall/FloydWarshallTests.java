import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FloydWarshallTests {

    private FloydWarshall fw;
    private static final int INF = Integer.MAX_VALUE / 2;

    @BeforeEach
    void setUp(){
        this.fw = new FloydWarshall();
    }

    @Test
    void testGrafo() {
        int[][] matriz = {
            {0,   3,  10, INF},
            {INF, 0,   1,  7},
            {INF, INF, 0,  2},
            {INF, INF, INF, 0}
        };

        int[][] esperado = {
            {0, 3, 4, 6},
            {INF, 0, 1, 3},
            {INF, INF, 0, 2},
            {INF, INF, INF, 0}
        };

        int[][] resultado = fw.calcularDistancias(matriz);

        assertArrayEquals(esperado[0], resultado[0]);
        assertArrayEquals(esperado[1], resultado[1]);
        assertArrayEquals(esperado[2], resultado[2]);
        assertArrayEquals(esperado[3], resultado[3]);
    }

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

        int[][] resultado = fw.calcularDistancias(matriz);

        assertArrayEquals(esperado[0], resultado[0]);
        assertArrayEquals(esperado[1], resultado[1]);
    }

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

        int[][] resultado = fw.calcularDistancias(matriz);

        assertArrayEquals(esperado[0], resultado[0]);
        assertArrayEquals(esperado[1], resultado[1]);
        assertArrayEquals(esperado[2], resultado[2]);
    }

}
