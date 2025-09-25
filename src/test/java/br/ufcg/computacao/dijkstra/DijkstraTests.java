package br.ufcg.computacao.dijkstra;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Stack;
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
 * {@code grafoMenorD}: um grafo 3x3 denso pequeno 
 * {@code grafoMaiorD}: um grafo 5x5 denso mais complexo
 * {@code grafoMenorE}: um grafo 3x3 esparso pequeno 
 * {@code grafoMaiorE}: um grafo 5x5 esparso mais complexo
 * 
 * Os grafos são representados como matrizes de adjacência contendo pesos inteiros.
 * 
 * @autor Maria Eduarda Capela Cabral Pinheiro da Silva
 * 
 */

class DijkstraTests {
	private int[][] grafoMenorD;
	private int[][] grafoMaiorD;
	private int[][] grafoMenorE;
	private int[][] grafoMaiorE;
	private Dijkstra dijkstra;

	/**
	 * Inicializa os grafos e a instância da classe Dijkstra antes de cada teste.
	 */
	@BeforeEach
	void setUp() throws Exception {
		this.dijkstra = new Dijkstra();
		this.grafoMenorD = new int[][]{{0, 1, 2}, {1, 0, 3}, {2, 3, 0}}; 
		this.grafoMaiorD = new int[][] {{0, 1, 2, 5, 4}, {1, 0, 1, 1, 3}, {2, 1, 0, 4, 5}, {5, 1, 4, 0, 1}, {4, 3, 5, 1, 0}};
		this.grafoMenorE = new int[][]{{0, 1, 0}, {1, 0, 1}, {0, 1, 0}}; 
		this.grafoMaiorE = new int[][] {{0, 0, 1, 0, 1}, {0, 0, 2, 3, 8}, {1, 2, 0, 0, 0}, {0, 3, 0, 0, 0}, {1, 8, 0, 0, 0}};
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
			dijkstra.dijkstra_comFila(grafoMenorD, -1);
		});
		assertThrows(IndexOutOfBoundsException.class, () -> {
			dijkstra.dijkstra_comFila(grafoMenorD, 100);
		});
		assertThrows(IndexOutOfBoundsException.class, () -> {
			dijkstra.dijkstra_semFila(grafoMenorD, -1);
		});
		assertThrows(IndexOutOfBoundsException.class, () -> {
			dijkstra.dijkstra_semFila(grafoMenorD, 100);
		});
	}

	/**
	 * Testa se a exceção {@code IndexOutOfBoundsException} lançada pelo método
	 * contém a mensagem correta: {@code "Origem inválida"}.
	 */
	@Test
	void testExceptionMessage() {
		try {
			dijkstra.dijkstra_comFila(grafoMenorD, -1);
		} catch (Exception e) {
			assertEquals("Origem inválida", e.getMessage());
		}
		try {
			dijkstra.dijkstra_comFila(grafoMenorD, 100);
		} catch (Exception e) {
			assertEquals("Origem inválida", e.getMessage());
		}
		try {
			dijkstra.dijkstra_semFila(grafoMenorD, -1);
		} catch (Exception e) {
			assertEquals("Origem inválida", e.getMessage());
		}
		try {
			dijkstra.dijkstra_semFila(grafoMenorD, 100);
		} catch (Exception e) {
			assertEquals("Origem inválida", e.getMessage());
		}
	}

	/**
	 * Testa se o método {@code dijkstra_semFila} retorna as menores distâncias
	 * corretas
	 * entre a origem e os demais vértices para diferentes grafos.
	 */
	@Test
	void testMenorCaminhoSemFilaDistancias() {
		int[][] caminho = dijkstra.dijkstra_semFila(grafoMenorD, 0); // calcula as menores distancias e os melhores
																			// caminhos entre o vertice 0 e os outros
																			// vertices
		assertEquals(0, caminho[0][0]); // de um vertice até ele mesmo a distancia é zero
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);

		caminho = dijkstra.dijkstra_semFila(grafoMaiorD, 0);
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);
		assertEquals(2, caminho[0][3]);
		assertEquals(3, caminho[0][4]);

		caminho = dijkstra.dijkstra_semFila(grafoMaiorD, 1);
		assertEquals(1, caminho[0][2]);
		assertEquals(1, caminho[0][3]);
		assertEquals(2, caminho[0][4]);

		caminho = dijkstra.dijkstra_semFila(grafoMaiorD, 2);
		assertEquals(2, caminho[0][3]);
		assertEquals(3, caminho[0][4]);

		caminho = dijkstra.dijkstra_semFila(grafoMaiorD, 3);
		assertEquals(1, caminho[0][4]);
		
		caminho = dijkstra.dijkstra_semFila(grafoMenorE, 0); 
		assertEquals(0, caminho[0][0]); 	
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);
		
		caminho = dijkstra.dijkstra_semFila(grafoMaiorE, 0);
		assertEquals(3, caminho[0][1]);
		assertEquals(1, caminho[0][2]);
		assertEquals(6, caminho[0][3]);
		assertEquals(1, caminho[0][4]);

		caminho = dijkstra.dijkstra_semFila(grafoMaiorE, 1);
		assertEquals(2, caminho[0][2]);
		assertEquals(3, caminho[0][3]);
		assertEquals(4, caminho[0][4]);

		caminho = dijkstra.dijkstra_semFila(grafoMaiorE, 2);
		assertEquals(5, caminho[0][3]);
		assertEquals(2, caminho[0][4]);

		caminho = dijkstra.dijkstra_semFila(grafoMaiorE, 3);
		assertEquals(7, caminho[0][4]);
	}

	/**
	 * Testa se o método {@code printCaminho} reconstrói corretamente os caminhos
	 * mínimos
	 * a partir do vetor de pais gerado pelo algoritmo de Dijkstra sem uso de fila
	 * de prioridade.
	 */
	@Test
	void testMenorCaminhoSemFilaCaminho() {
		int[][] caminho = dijkstra.dijkstra_semFila(grafoMenorD, 0); // calcula as menores distancias e os melhores
																			// caminhos entre o vertice 0 e os outros
																			// vertices
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));

		caminho = dijkstra.dijkstra_semFila(grafoMaiorD, 0);
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("0->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("0->1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.dijkstra_semFila(grafoMaiorD, 1);
		assertEquals("1->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.dijkstra_semFila(grafoMaiorD, 2);
		assertEquals("2->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("2->1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.dijkstra_semFila(grafoMaiorD, 3);
		assertEquals("3->4", dijkstra.printCaminho(caminho[1], 4));
		
		caminho = dijkstra.dijkstra_semFila(grafoMenorE, 0); 
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->1->2", dijkstra.printCaminho(caminho[1], 2));

		caminho = dijkstra.dijkstra_semFila(grafoMaiorE, 0);
		assertEquals("0->2->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("0->2->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("0->4", dijkstra.printCaminho(caminho[1], 4));
		
		caminho = dijkstra.dijkstra_semFila(grafoMaiorE, 1);
		assertEquals("1->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("1->2->0->4", dijkstra.printCaminho(caminho[1], 4));
		
		caminho = dijkstra.dijkstra_semFila(grafoMaiorE, 2);
		assertEquals("2->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("2->0->4", dijkstra.printCaminho(caminho[1], 4));
		
		caminho = dijkstra.dijkstra_semFila(grafoMaiorE, 3);
		assertEquals("3->1->2->0->4", dijkstra.printCaminho(caminho[1], 4));
	}

	/**
	 * Testa se o método {@code dijkstra_comFila} retorna as menores distâncias
	 * corretas
	 * entre a origem e os demais vértices para diferentes grafos.
	 */
	@Test
	void testMenorCaminhoComFilaDistancias() {
		int[][] caminho = dijkstra.dijkstra_comFila(grafoMenorD, 0); // calcula as menores distancias e os melhores
																			// caminhos entre o vertice 0 e os outros
																			// vertices
		assertEquals(0, caminho[0][0]); // de um vertice até ele mesmo a distancia é zero
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);

		caminho = dijkstra.dijkstra_comFila(grafoMaiorD, 0);
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);
		assertEquals(2, caminho[0][3]);
		assertEquals(3, caminho[0][4]);

		caminho = dijkstra.dijkstra_comFila(grafoMaiorD, 1);
		assertEquals(1, caminho[0][2]);
		assertEquals(1, caminho[0][3]);
		assertEquals(2, caminho[0][4]);

		caminho = dijkstra.dijkstra_comFila(grafoMaiorD, 2);
		assertEquals(2, caminho[0][3]);
		assertEquals(3, caminho[0][4]);

		caminho = dijkstra.dijkstra_comFila(grafoMaiorD, 3);
		assertEquals(1, caminho[0][4]);
		
		caminho = dijkstra.dijkstra_comFila(grafoMenorE, 0); 
		assertEquals(0, caminho[0][0]); 	
		assertEquals(1, caminho[0][1]);
		assertEquals(2, caminho[0][2]);
		
		caminho = dijkstra.dijkstra_comFila(grafoMaiorE, 0);
		assertEquals(3, caminho[0][1]);
		assertEquals(1, caminho[0][2]);
		assertEquals(6, caminho[0][3]);
		assertEquals(1, caminho[0][4]);

		caminho = dijkstra.dijkstra_comFila(grafoMaiorE, 1);
		assertEquals(2, caminho[0][2]);
		assertEquals(3, caminho[0][3]);
		assertEquals(4, caminho[0][4]);

		caminho = dijkstra.dijkstra_comFila(grafoMaiorE, 2);
		assertEquals(5, caminho[0][3]);
		assertEquals(2, caminho[0][4]);

		caminho = dijkstra.dijkstra_comFila(grafoMaiorE, 3);
		assertEquals(7, caminho[0][4]);
	}

	/**
	 * Testa se o método {@code printCaminho} reconstrói corretamente os caminhos
	 * mínimos
	 * a partir do vetor de pais gerado pelo algoritmo de Dijkstra com uso de fila e
	 * prioridade.
	 */
	@Test
	void testMenorCaminhoComFilaCaminho() {
		int[][] caminho = dijkstra.dijkstra_comFila(grafoMenorD, 0);
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));

		caminho = dijkstra.dijkstra_comFila(grafoMaiorD, 0);
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("0->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("0->1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.dijkstra_comFila(grafoMaiorD, 1);
		assertEquals("1->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.dijkstra_comFila(grafoMaiorD, 2);
		assertEquals("2->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("2->1->3->4", dijkstra.printCaminho(caminho[1], 4));

		caminho = dijkstra.dijkstra_comFila(grafoMaiorD, 3);
		assertEquals("3->4", dijkstra.printCaminho(caminho[1], 4));
		
		caminho = dijkstra.dijkstra_comFila(grafoMenorE, 0); 
		assertEquals("0->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->1->2", dijkstra.printCaminho(caminho[1], 2));

		caminho = dijkstra.dijkstra_comFila(grafoMaiorE, 0);
		assertEquals("0->2->1", dijkstra.printCaminho(caminho[1], 1));
		assertEquals("0->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("0->2->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("0->4", dijkstra.printCaminho(caminho[1], 4));
		
		caminho = dijkstra.dijkstra_comFila(grafoMaiorE, 1);
		assertEquals("1->2", dijkstra.printCaminho(caminho[1], 2));
		assertEquals("1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("1->2->0->4", dijkstra.printCaminho(caminho[1], 4));
		
		caminho = dijkstra.dijkstra_comFila(grafoMaiorE, 2);
		assertEquals("2->1->3", dijkstra.printCaminho(caminho[1], 3));
		assertEquals("2->0->4", dijkstra.printCaminho(caminho[1], 4));
		
		caminho = dijkstra.dijkstra_comFila(grafoMaiorE, 3);
		assertEquals("3->1->2->0->4", dijkstra.printCaminho(caminho[1], 4));
	}
}
