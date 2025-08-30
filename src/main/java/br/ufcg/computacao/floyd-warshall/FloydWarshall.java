public class FloydWarshall{

    private static final int INFINITO = Integer.MAX_VALUE;

    // utilizando matriz de adjacências
    // método brute-force para achar distancias, calcula todas as possibilidades e atualiza a matriz de distancias
    public int calcularDistancias(int[][] grafo){
        
        int numVert = grafo.length;
        int[][] distancias = copy(grafo); //não alterar o original

        for (int k = 0; i < numVert; k++) // nó intermediario
            for (int i = 0; i < numVert; i++) // nó de saida
                for (int j = 0; i < numVert; j++){ // nó de destino

                    if (distancias[i][k] != INFINITO && distancias[k][j] != INFINITO) //se os nõs são conectados                        
                        if (distancia[i][k] + distancia[k][j] < distancia[i][j])
                            distancia[i][j] = distancia[i][k] + distancia[k][j];
                } 





    }

    private int[][] copy(int[][] grafo){
        
        int numVert = grafo.length;
        int copia = new int[numVert][numVert];

        for (int i = 0; i < numVert; i++)
            for (int j = 0; j < numVert; j++)
                copia[i][j] = grafo[i][j];

        return copia;

    }


}