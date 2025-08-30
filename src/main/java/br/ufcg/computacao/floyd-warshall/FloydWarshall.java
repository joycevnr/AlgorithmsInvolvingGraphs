public class FloydWarshall{

    private static final int INFINITO = Integer.MAX_VALUE / 2; //overflow em inf + inf, usar inf/2

    // utilizando matriz de adjacências
    // método brute-force para achar distancias, calcula todas as possibilidades e atualiza a matriz de distancias
    // não é guardado o caminhos até ele, apenas a distancia menor possivel
    public int[][] calcularDistancias(int[][] grafo){
        
        int numVert = grafo.length;
        int[][] distancias = copy(grafo); //não alterar o original

        for (int k = 0; k < numVert; k++) // nó intermediario
            for (int i = 0; i < numVert; i++) // nó de saida
                for (int j = 0; j < numVert; j++){ // nó de destino

                    if (distancias[i][k] != INFINITO && distancias[k][j] != INFINITO) //se os nõs são conectados                        
                        if (distancias[i][k] + distancias[k][j] < distancias[i][j]) //se encontrar um caminho menor, passando pelo intermediario
                            distancias[i][j] = distancias[i][k] + distancias[k][j]; // atualiza caminho
                }
                
        return distancias;
    }

    private int[][] copy(int[][] grafo){
        
        int numVert = grafo.length;
        int[][] copia = new int[numVert][numVert];

        for (int i = 0; i < numVert; i++)
            for (int j = 0; j < numVert; j++)
                copia[i][j] = grafo[i][j];

        return copia;

    }

    public void mostrarMatriz(int[][] grafo){
           
        for (int saida = 0; saida < grafo.length; saida++){
            System.out.println("Saindo de: " + saida);
            for (int chegada = 0; chegada < grafo.length; chegada++){
                if (grafo[saida][chegada] != 0 && grafo[saida][chegada] != INFINITO)
                    System.out.println(grafo[saida][chegada] + " unidades até " + chegada);
            }
            System.out.println("---");
        }

    }

    public static void main (String[] args){
        int[][] matriz = {
            {0,       3,       10,      INFINITO},
            {INFINITO, 0,       1,       7},
            {INFINITO, INFINITO, 0,       2},
            {INFINITO, INFINITO, INFINITO, 0}
        };

        FloydWarshall fw = new FloydWarshall();
        fw.mostrarMatriz(matriz);
        System.out.println("\n\n");
        fw.mostrarMatriz(fw.calcularDistancias(matriz));
    }

}