package br.ufcg.computacao.dijkstra;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * A classe {@code Dijkstra} implementa o algoritmo de Dijkstra
 * para encontrar o caminho mínimo entre vértices em um grafo com pesos positivos.
 * 
 * Existem duas implementações desse algoritmo: uma implementação simples sem
 * fila de prioridade
 * e outra otimizada com fila de prioridade.
 * 
 * O grafo deve ser representado como uma matriz de adjacência, onde cada elemento A[i][j] da matriz A, representa uma aresta
 * entre o vértice i e o vértice j, se o valor for zero significa que não há aresta que liga os dois vértices.
 * 
 * Exemplo de matriz de adjacência como int[][]:
 * 
 * {@code
 *  grafo[i][j] = peso da aresta de i para j (0 se não existe uma aresta que conecta i até j ou se i=j)
 * }
 * 
 * @author Maria Eduarda Capela Cabral Pinheiro da Silva
 */

public class Dijkstra {

	/**
	 * Constante que representa o valor de infinito (utilizado como inicialização).
	 */
	private final int infinito = Integer.MAX_VALUE;

	/**
	 * Construtor da classe Dijkstra.
	 */
	public Dijkstra() {

	}

	/**
	 * Retorna o índice do vértice com a menor distância ainda não visitado.
	 *
	 * @param distancias Vetor de distâncias mínimas (da raiz até o vertice
	 *                   correspondente ao indice) conhecidas até agora.
	 * @param visitados  Vetor de vértices já visitados.
	 * @return Índice do vértice com menor distância não visitado.
	 */
	private int distanciaMinima(int[] distancias, boolean[] visitados) {
		int min = infinito;
		int minIndex = -1;
		for (int i = 0; i < distancias.length; i++) {
			if (!visitados[i] && distancias[i] <= min) {
				min = distancias[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
     * Algoritmo de Dijkstra (versão sem fila de prioridade) para encontrar
     * o menor caminho de um vértice de origem até todos os outros vértices.
     *
     * @param grafo Matriz de adjacência representando o grafo com pesos.
     * @param raiz Vértice de origem.
     * @return Um array 2D de inteiros: a primeira linha contém as menores distâncias da raiz para todos os vértices.
     * 									a segunda linha contém os "pais" de cada vértice no caminho mínimo.
     * @throws IndexOutOfBoundsException se o índice do vértice de origem (`raiz`) for inválido (menor que 0 ou maior que o número de vértices).
     */
	public int[][] menor_caminho_semFila(int[][] grafo, int raiz){
        if(raiz<0 || raiz>grafo.length) throw new IndexOutOfBoundsException("Origem inválida");
		int vertices = grafo.length;
		int[] distancias = new int[vertices];
		boolean[] visitados = new boolean[vertices];
		int[] pais = new int[vertices];

		for (int i = 0; i < vertices; i++) {
			distancias[i] = infinito;
			pais[i] = -1;
		}
		distancias[raiz] = 0;
		for(int i =0; i<vertices-1; i++) {
			int atual = distanciaMinima(distancias, visitados);
			visitados[atual]=true;
			
			for(int j = 0; j<vertices;j++) {
				int aresta = grafo[atual][j];
				if(aresta!=0 && !visitados[j] && distancias[atual]!=infinito ) {
					int novaDistancia = distancias[atual]+aresta;
					if(novaDistancia < distancias[j] ){
						distancias[j] = novaDistancia;
						pais[j] = atual;
					}	
				}
			}
		}

		int[][] menor_caminho = new int[2][];
		menor_caminho[0] = distancias;
		menor_caminho[1] = pais;
		return menor_caminho;
	}

	/**
     * Algoritmo de Dijkstra (versão com fila de prioridade) para encontrar
     * o menor caminho de um vértice de origem até todos os outros vértices.
     *
     * @param grafo Matriz de adjacência representando o grafo com pesos.
     * @param raiz Vértice de origem.
     * @return Um array 2D de inteiros: a primeira linha contém as menores distâncias da raiz para todos os vértices.
     * 									a segunda linha contém os "pais" de cada vértice no caminho mínimo.
     * @throws IndexOutOfBoundsException se o índice do vértice de origem (`raiz`) for inválido (menor que 0 ou maior que o número de vértices).
     */
	public int[][] menor_caminho_comFila(int[][] grafo, int raiz) {
        if(raiz<0 || raiz>grafo.length) throw new IndexOutOfBoundsException("Origem inválida");
        int vertices = grafo.length;
     	int[] distancias = new int[vertices];
   		boolean[] visitados = new boolean[vertices];
   		int[] pais = new int[vertices];
		
		for(int i =0; i<vertices; i++) {
			distancias[i] = infinito;
			pais[i] = -1;
		}

		distancias[raiz] = 0;
		PriorityQueue<Integer> fila = new PriorityQueue<>(Comparator.comparingInt(i -> distancias[i]));
		fila.add(raiz);

		while (!fila.isEmpty()) {

			int atual = fila.poll();
            if (visitados[atual]) continue;
            visitados[atual] = true;
            
            for (int j = 0; j < vertices; j++) {
                int aresta = grafo[atual][j];
                if (aresta > 0 && !visitados[j]) {
                    int novaDistancia = distancias[atual] + aresta;
                    if (novaDistancia < distancias[j]) {
                        distancias[j] = novaDistancia;
                        pais[j] = atual;
                        fila.add(j); 
                    }
                }
            }
        }
        
        int[][] menor_caminho = new int[2][];
		menor_caminho[0] = distancias;
		menor_caminho[1] = pais;
		return menor_caminho;
	}

	/**
	 * Reconstrói e retorna o caminho mínimo de um vértice de origem até o vértice
	 * de destino,
	 * com base no vetor de pais gerado pelo algoritmo de Dijkstra.
	 *
	 * O caminho é retornado como uma String no formato "origem->...->destino".
	 *
	 * @param pais    Vetor de pais, onde pais[v] representa o vértice anterior no
	 *                caminho mínimo até v.
	 *                Esse vetor deve ser gerado por um algoritmo como Dijkstra.
	 * @param destino O vértice de destino para o qual se deseja reconstruir o
	 *                caminho.
	 * @return Uma String representando o caminho mínimo da origem até o destino,
	 *         ou apenas o destino se ele for o único vértice acessado.
	 *         Exemplo: "0->1->2".
	 */
	public String printCaminho(int[] pais, int destino) {
		String caminho = "";

		Stack<Integer> percurso = new Stack<>();
		for (int v = destino; v != -1; v = pais[v]) {
			percurso.push(v);
		}
		if (!percurso.isEmpty())
			caminho += percurso.pop();
		while (!percurso.isEmpty()) {
			caminho += "->" + percurso.pop();
		}
		return caminho;
	}
}
