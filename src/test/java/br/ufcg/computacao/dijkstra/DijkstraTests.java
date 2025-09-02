package br.ufcg.computacao.dijkstra;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de testes unitários para os métodos do algoritmo de Dijkstra,
 * implementados na classe {@code Dijkstra}.
 * 
 * Contém testes para:
 * Verificação de exceções para índices inválidos
 * Verificação das menores distâncias calculadas
 * Verificação dos caminhos mínimos
 *
 * 
 * Os testes utilizam dois grafos:
 * 
 * {@code grafoMenor}: um grafo 3x3 pequeno
 * {@code grafoMaior}: um grafo 5x5 mais complexo
 * 
 * 
 * Os grafos são representados como listas de adjacência contendo pesos
 * inteiros.
 * 
 * @autor Maria Eduarda Capela Cabral Pinheiro da Silva
 * 
 */

class DijkstraTests {
	private ArrayList<ArrayList<Integer>> grafoMenor;
	private ArrayList<ArrayList<Integer>> grafoMaior;
	private Dijkstra dijkstra;

	/**
	 * Inicializa os grafos e a instância da classe Dijkstra antes de cada teste.
	 * Converte matrizes de adjacência em listas de adjacência.
	 */
	@BeforeEach
	void setUp() throws Exception {
		this.grafoMenor = new ArrayList<ArrayList<Integer>>();
		this.grafoMaior = new ArrayList<ArrayList<Integer>>();
		this.dijkstra = new Dijkstra();
		int[][] grafo1 = { { 0, 1, 2 }, { 1, 0, 3 }, { 2, 3, 0 } };
		int[][] grafo2 = { { 0, 1, 2, 5, 4 }, { 1, 0, 1, 1, 3 }, { 2, 1, 0, 4, 5 }, { 5, 1, 4, 0, 1 },
				{ 4, 3, 5, 1, 0 } };
		for (int i = 0; i < grafo1.length; i++) {
			ArrayList<Integer> novo = new ArrayList<>();
			for (int j = 0; j < grafo1.length; j++) {
				novo.add(grafo1[i][j]);
			}
			grafoMenor.add(novo);
		}
		for (int i = 0; i < grafo2.length; i++) {
			ArrayList<Integer> novo = new ArrayList<>();
			for (int j = 0; j < grafo2.length; j++) {
				novo.add(grafo2[i][j]);
			}
			grafoMaior.add(novo);
		}
	}

	/**
	 * Testa se o método lança {@code IndexOutOfBoundsException} ao receber índices
	 * inválidos
	 * como origem para o algoritmo de Dijkstra (menores que 0 ou maiores que o
	 * número de vértices).
	 */
	@Test
	void testException() {
		assertThrows(IndexOutOfBoundsException.class, () -> {
			dijkstra.menor_caminho_comFila(grafoMenor, -1);
		});
		assertThrows(IndexOutOfBoundsException.class, () -> {
			dijkstra.menor_caminho_comFila(grafoMenor, 100);
		});
		assertThrows(IndexOutOfBoundsException.class, () -> {
			dijkstra.menor_caminho_semFila(grafoMenor, -1);
		});
		assertThrows(IndexOutOfBoundsException.class, () -> {
			dijkstra.menor_caminho_semFila(grafoMenor, 100);
		});
	}

	/**
	 * Testa se a exceção {@code IndexOutOfBoundsException} lançada pelo método
	 * contém a mensagem correta: {@code "Origem inválida"}.
	 */
	@Test
	void testExceptionMessage() {
		try {
			dijkstra.menor_caminho_comFila(grafoMenor, -1);
		} catch (Exception e) {
			assertEquals("Origem inválida", e.getMessage());
		}
		try {
			dijkstra.menor_caminho_comFila(grafoMenor, 100);
		} catch (Exception e) {
			assertEquals("Origem inválida", e.getMessage());
		}
		try {
			dijkstra.menor_caminho_semFila(grafoMenor, -1);
		} catch (Exception e) {
			assertEquals("Origem inválida", e.getMessage());
		}
		try {
			dijkstra.menor_caminho_semFila(grafoMenor, 100);
		} catch (Exception e) {
			assertEquals("Origem inválida", e.getMessage());
		}
	}

	/**
	 * Testa se o método {@code menor_caminho_semFila} retorna as menores distâncias
	 * corretas
	 * entre a origem e os demais vértices para diferentes grafos.
	 */
	@Test
	void testMenorCaminhoSemFilaDistancias() {
		int[][] caminho = dijkstra.menor_caminho_semFila(grafoMenor, 0); // calcula as menores distancias e os melhores
																			// caminhos entre o vertice 0 e os outros
																			// vertices
		assertEquals(0, caminho[0][0]); // de um vertice até ele mesmo a distancia é zero
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);

		caminho = dijkstra.menor_caminho_semFila(grafoMaior, 0);
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);
		assertEquals(2, caminho[0][3]);
		assertEquals(3, caminho[0][4]);

		caminho = dijkstra.menor_caminho_semFila(grafoMaior, 1);
		assertEquals(1, caminho[0][2]);
		assertEquals(1, caminho[0][3]);
		assertEquals(2, caminho[0][4]);

		caminho = dijkstra.menor_caminho_semFila(grafoMaior, 2);
		assertEquals(2, caminho[0][3]);
		assertEquals(3, caminho[0][4]);

		caminho = dijkstra.menor_caminho_semFila(grafoMaior, 3);
		assertEquals(1, caminho[0][4]);
	}

	/**
	 * Testa se o método {@code printCaminho} reconstrói corretamente os caminhos
	 * mínimos
	 * a partir do vetor de pais gerado pelo algoritmo de Dijkstra sem uso de fila
	 * de prioridade.
	 */
	@Test
	void testMenorCaminhoSemFilaCaminho() {
		int[][] caminho = dijkstra.menor_caminho_semFila(grafoMenor, 0); // calcula as menores distancias e os melhores
																			// caminhos entre o vertice 0 e os outros
																			// vertices
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));

		caminho = dijkstra.menor_caminho_semFila(grafoMaior, 0);
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("0->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("0->1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.menor_caminho_semFila(grafoMaior, 1);
		assertEquals("1->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.menor_caminho_semFila(grafoMaior, 2);
		assertEquals("2->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("2->1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.menor_caminho_semFila(grafoMaior, 3);
		assertEquals("3->4", dijkstra.printCaminho(caminho[1], 4));
	}

	/**
	 * Testa se o método {@code menor_caminho_comFila} retorna as menores distâncias
	 * corretas
	 * entre a origem e os demais vértices para diferentes grafos.
	 */
	@Test
	void testMenorCaminhoComFilaDistancias() {
		int[][] caminho = dijkstra.menor_caminho_comFila(grafoMenor, 0); // calcula as menores distancias e os melhores
																			// caminhos entre o vertice 0 e os outros
																			// vertices
		assertEquals(0, caminho[0][0]); // de um vertice até ele mesmo a distancia é zero
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);

		caminho = dijkstra.menor_caminho_comFila(grafoMaior, 0);
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);
		assertEquals(2, caminho[0][3]);
		assertEquals(3, caminho[0][4]);

		caminho = dijkstra.menor_caminho_comFila(grafoMaior, 1);
		assertEquals(1, caminho[0][2]);
		assertEquals(1, caminho[0][3]);
		assertEquals(2, caminho[0][4]);

		caminho = dijkstra.menor_caminho_comFila(grafoMaior, 2);
		assertEquals(2, caminho[0][3]);
		assertEquals(3, caminho[0][4]);

		caminho = dijkstra.menor_caminho_comFila(grafoMaior, 3);
		assertEquals(1, caminho[0][4]);
	}

	/**
	 * Testa se o método {@code printCaminho} reconstrói corretamente os caminhos
	 * mínimos
	 * a partir do vetor de pais gerado pelo algoritmo de Dijkstra com uso de fila e
	 * prioridade.
	 */
	@Test
	void testMenorCaminhoComFilaCaminho() {
		int[][] caminho = dijkstra.menor_caminho_comFila(grafoMenor, 0);
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));

		caminho = dijkstra.menor_caminho_comFila(grafoMaior, 0);
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("0->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("0->1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.menor_caminho_comFila(grafoMaior, 1);
		assertEquals("1->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.menor_caminho_comFila(grafoMaior, 2);
		assertEquals("2->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("2->1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.menor_caminho_comFila(grafoMaior, 3);
		assertEquals("3->4", dijkstra.printCaminho(caminho[1], 4));
	}
}
