/**
 * Implementação do algoritmo de Floyd-Warshall para cálculo de distâncias mínimas em grafos.
 * <p>
 * O grafo é representado por uma matriz de adjacência (nxn), onde cada posição A[n][m]
 * indica a distância entre o vértice n e o vértice m.
 * </p>
 *
 * <ul>
 *     <li>Utiliza uma abordagem brute-force para testar todos os caminhos possíveis.</li>
 *     <li>Atualiza apenas as distâncias mínimas entre pares de vértices, sem armazenar os caminhos percorridos.</li>
 *     <li>O valor {@code INFINITO} é usado para representar ausência de aresta entre vértices.</li>
 * </ul>
 */
public class FloydWarshall {

    /**
     * Representa valor "infinito" na matriz de adjacências.
     * É definido como {@code Integer.MAX_VALUE / 2} para evitar overflow em operações de soma.
     */
    private static final int INFINITO = Integer.MAX_VALUE / 2;

    /**
     * Calcula a matriz de distâncias mínimas entre todos os pares de vértices de um grafo,
     * utilizando o algoritmo de Floyd-Warshall.
     *
     * @param grafo matriz de adjacência representando o grafo
     * @return matriz com as distâncias mínimas entre todos os pares de vértices
     */
    public int[][] calcularDistancias(int[][] grafo) {
        int numVert = grafo.length;
        int[][] distancias = copy(grafo); // não alterar o original

        for (int k = 0; k < numVert; k++)// nó intermediário
            for (int i = 0; i < numVert; i++) // nó de saída
                for (int j = 0; j < numVert; j++) // nó de destino
                    if (distancias[i][k] != INFINITO && distancias[k][j] != INFINITO) 
                        if (distancias[i][k] + distancias[k][j] < distancias[i][j]) 
                            distancias[i][j] = distancias[i][k] + distancias[k][j]; // atualiza caminho, se menor

        return distancias;
    }

    /**
     * Cria uma cópia da matriz de adjacência do grafo.
     *
     * @param grafo matriz original
     * @return cópia da matriz
     */
    private int[][] copy(int[][] grafo) {
        int numVert = grafo.length;
        int[][] copia = new int[numVert][numVert];

        for (int i = 0; i < numVert; i++) {
            for (int j = 0; j < numVert; j++) {
                copia[i][j] = grafo[i][j];
            }
        }
        return copia;
    }

    /**
     * Exibe no console as distâncias entre os vértices do grafo.
     * <p>
     * Apenas imprime distâncias diferentes de zero e diferentes de {@code INFINITO}.
     * </p>
     *
     * @param matriz matriz de adjacência ou matriz de distâncias mínimas
     */
    public void mostrarDistancias(int[][] matriz) {
        for (int saida = 0; saida < matriz.length; saida++) {
            System.out.println("Saindo de: " + saida);
            for (int chegada = 0; chegada < matriz.length; chegada++) {
                if (matriz[saida][chegada] != 0 && matriz[saida][chegada] != INFINITO) {
                    System.out.println(matriz[saida][chegada] + " unidades até " + chegada);
                }
            }
            System.out.println("---");
        }
    }

    /**
     * Exemplo de uso do algoritmo Floyd-Warshall.
     * Mostra a matriz original e, em seguida, a matriz com as distâncias mínimas.
     *
     * @param args argumentos de linha de comando (não utilizado)
     */
    public static void main(String[] args) {
        int[][] matriz = {
            {0,        3,       10,       INFINITO},
            {INFINITO, 0,        1,       7},
            {INFINITO, INFINITO, 0,       2},
            {INFINITO, INFINITO, INFINITO, 0}
        };

        FloydWarshall fw = new FloydWarshall();
        fw.mostrarDistancias(matriz);
        System.out.println("\n");
        fw.mostrarDistancias(fw.calcularDistancias(matriz));
    }
}
