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

        int[][] resultado = fw.calcularDistancias(matriz);

        for (int i = 0; i < 4; i++)
            assertArrayEquals(esperado[i], resultado[i]);
    
    }

    @Test
    void testGrafo4x4() {
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

        int[][] resultado = fw.calcularDistancias(matriz);

        for (int i = 0; i < 5; i++)
            assertArrayEquals(esperado[i], resultado[i]);
    }

    @Test
    void testGrafo5x5() {
    int[][] matriz = {
        {0,   2,   INF,  1,   INF},
        {2,   0,   3,    2,   INF},
        {INF, 3,   0,    4,   5},
        {1,   2,   4,    0,   3},
        {INF, INF, 5,    3,   0}
    };

    int[][] esperado = {
        {0, 2, 5, 1, 4},
        {2, 0, 3, 2, 5},
        {5, 3, 0, 4, 5},
        {1, 2, 4, 0, 3},
        {4, 5, 5, 3, 0}
    };

    int[][] resultado = fw.calcularDistancias(matriz);

    for (int i = 0; i < 5; i++) 
        assertArrayEquals(esperado[i], resultado[i]);
    
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

            for (int i = 0; i < 3; i++) 
                assertArrayEquals(esperado[i], resultado[i]);
    }

}

