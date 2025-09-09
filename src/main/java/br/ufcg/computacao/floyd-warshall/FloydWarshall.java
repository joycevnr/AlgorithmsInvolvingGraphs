package br.ufcg.computacao.floydwarshall;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implementação do algoritmo de <strong>Floyd-Warshall</strong> para encontrar os
 * <strong>menores caminhos entre todos os pares de vértices</strong> em um grafo direcionado
 * e ponderado. O algoritmo é executado uma única vez e gera uma matriz de distâncias
 * mínimas e uma matriz de sucessores que permite reconstruir os caminhos.
 * </p>
 *
 * <p>
 * O grafo é representado por uma matriz de adjacência (<i>n</i>x<i>n</i>), onde cada
 * posição {@code A[i][j]} armazena o peso da aresta do vértice <i>i</i> para o vértice <i>j</i>.
 * A ausência de uma aresta é representada pelo valor {@code INFINITO}.
 * </p>
 *
 * <p>
 * O tempo de execução do algoritmo é $O(V^3)$, onde <i>V</i> é o número de vértices,
 * tornando-o adequado quando é necessário calcular todas as distâncias entre pares de vértices.
 * </p>
 * 
 * @author Gleydson Fabricio Rodrigues de Moura
 * 
 */
public class FloydWarshall {

    /**
     * Representa valor "infinito" na matriz de adjacências.
     * É definido como {@code Integer.MAX_VALUE / 2} para evitar overflow em operações de soma.
     */
    public static final int INFINITO = Integer.MAX_VALUE / 2;

    // Matriz que armazena as distâncias mínimas entre todos os pares de vértices.
    private int[][] distancias; 

    // Matriz para reconstrução dos caminhos mais curtos.
    private int[][] sucessores;
    
    /**
     * Construtor que inicializa e executa o algoritmo de Floyd-Warshall.
     *
     * @param matrizAdjacencia A matriz de adjacência que representa o grafo.
     */

    public FloydWarshall(int[][] matrizAdjacencia){
        inicializar(matrizAdjacencia);
        calcularDistancias();
    }


    /**
     * Construtor vazio, útil em testes de desempenho, inicialização e algoritmo iniciados individualmente
     */
    public FloydWarshall(){}

    /**
     * Inicializa as matrizes de distâncias e de sucessores com base na matriz de adjacência fornecida.
     *
     * @param grafo A matriz de adjacência original do grafo.
     */
    public void inicializar(int[][] grafo){

        int n = grafo.length;
        this.distancias = new int[n][n];
        this.sucessores = new int[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++){
                distancias[i][j] = grafo[i][j]; //copia da matriz

                //preenche matriz de sucessores
                if (grafo[i][j] != INFINITO && i != j)
                    sucessores[i][j] = j;
                else 
                    sucessores[i][j] = -1;
            }

    }
    
    /**
     * Executa o algoritmo de Floyd-Warshall para calcular a menor distância entre todos
     * os pares de vértices.
     * <p>
     * O algoritmo itera sobre todos os vértices possíveis ({@code k}) como nós intermediários,
     * atualizando as distâncias entre todos os pares de vértices ({@code i} e {@code j}).
     * Adicionalmente, ele armazena o predecessor de cada vértice no caminho mais curto,
     * permitindo a reconstrução completa dos caminhos após a execução.
     * </p>
     */
    public void calcularDistancias(){

        int n = distancias.length;
        for (int k = 0; k < n; k++)// nó intermediário
            for (int i = 0; i < n; i++) // nó de saída
                for (int j = 0; j < n; j++) // nó de destino
                    if (distancias[i][k] != INFINITO && distancias[k][j] != INFINITO) 
                        if (distancias[i][k] + distancias[k][j] < distancias[i][j]){ 
                            distancias[i][j] = distancias[i][k] + distancias[k][j]; // atualiza a distância
                            sucessores[i][j] = sucessores[i][k]; //guarda o nó intermediário;
                        }
    }


    /**
     * Reconstrói e retorna o caminho mais curto do vértice {@code u} para o vértice {@code v}.
     *
     * @param u O vértice de origem.
     * @param v O vértice de destino.
     * @return Uma lista de inteiros que representa o caminho. Se não houver caminho, retorna
     * uma lista vazia.
     */
    public List<Integer> getCaminho(int u, int v) {

        List<Integer> caminho = new ArrayList<>();
        if (sucessores[u][v] == -1) return caminho; //sem caminho

        caminho.add(u);
        while (u != v){
            u = sucessores[u][v];
            caminho.add(u);
        }

        return caminho;

    }
    
    /**
     * Retorna a menor distância do vértice {@code u} para o vértice {@code v}.
     *
     * @param u O vértice de origem.
     * @param v O vértice de destino.
     * @return A menor distância.
     */    
    public int getDistancia(int u, int v){
        return distancias[u][v];
    }

    /**
     * Retorna a matriz de distâncias mínimas calculada pelo algoritmo.
     *
     * @return Uma matriz de inteiros onde `distancias[i][j]` é a menor distância entre o vértice `i` e o vértice `j`.
     */
    public int[][] getDistancias(){
        return this.distancias;
    }

    /**
     * Retorna a matriz de sucessores que armazena os caminhos mais curtos.
     *
     * @return Uma matriz de inteiros onde `sucessores[i][j]` é o próximo vértice no caminho mais curto de `i` para `j`.
     */
    public int[][] getSucessores(){
        return this.sucessores;
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

        FloydWarshall fw = new FloydWarshall(matriz);
        System.out.println(fw.getCaminho(0, 1));
        
    }
}
