package br.ufcg.computacao.toposort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.ufcg.computacao.toposort.graph.Grafo;

/**
 * Testes unitários para a classe {@link OrdenacaoTopologica}.
 * 
 * <p>
 * Esta classe contém testes que verificam o funcionamento correto do algoritmo
 * de ordenação topológica em diversos cenários. São testados diferentes tipos
 * de grafos para garantir que o algoritmo funcione corretamente em todas as situações
 * esperadas.
 * </p>
 * 
 * <p>
 * Cenários testados:
 * </p>
 * <ul>
 * <li>Grafo simples direcionado acíclico (DAG)</li>
 * <li>Grafo mais complexo representando dependências entre disciplinas
 * acadêmicas</li>
 * <li>Grafo com ciclos (deve lançar exceção)</li>
 * <li>Grafo vazio (caso especial)</li>
 * </ul>
 * 
 * <p>
 * A validação dos resultados é feita verificando se a ordem topológica
 * retornada respeita todas as restrições de precedência definidas pelas arestas do grafo.
 * </p>
 * 
 * @author Joyce Vitória Nascimento Rodrigues
 * @version 1.0
 * @see OrdenacaoTopologica
 * @see br.ufcg.computacao.toposort.graph.Grafo
 */
public class OrdenacaoTopologicaTest {

    private Grafo grafoSimples;
    private Grafo grafoDisciplinas;
    private Grafo grafoCiclico;

    /**
     * Configura os grafos de teste antes de cada teste.
     * 
     * <p>
     * Esta configuração inicializa três tipos diferentes de grafos:
     * </p>
     * <ol>
     * <li>grafoSimples: Um DAG simples com 6 vértices e dependências variadas</li>
     * <li>grafoDisciplinas: Um grafo representando pré-requisitos entre disciplinas
     * acadêmicas</li>
     * <li>grafoCiclico: Um grafo com um ciclo para testar a detecção de ciclos</li>
     * </ol>
     */
    @BeforeEach
    public void setUp() {
        // Grafo simples com 6 vértices formando um DAG
        grafoSimples = new Grafo(6);
        grafoSimples.adicionarAresta(5, 2);
        grafoSimples.adicionarAresta(5, 0);
        grafoSimples.adicionarAresta(4, 0);
        grafoSimples.adicionarAresta(4, 1);
        grafoSimples.adicionarAresta(2, 3);
        grafoSimples.adicionarAresta(3, 1);

        // Grafo representando dependências entre disciplinas
        grafoDisciplinas = new Grafo(8);

        grafoDisciplinas.adicionarAresta(0, 2);
        grafoDisciplinas.adicionarAresta(1, 2);
        grafoDisciplinas.adicionarAresta(2, 3);
        grafoDisciplinas.adicionarAresta(2, 5);
        grafoDisciplinas.adicionarAresta(3, 4);
        grafoDisciplinas.adicionarAresta(6, 7);

        // Grafo com ciclo
        grafoCiclico = new Grafo(3);
        grafoCiclico.adicionarAresta(0, 1);
        grafoCiclico.adicionarAresta(1, 2);
        grafoCiclico.adicionarAresta(2, 0); // Cria um ciclo
    }

    /**
     * Testa a ordenação topológica em um grafo simples.
     */
    @Test
    @DisplayName("Deve ordenar corretamente um grafo simples")
    void testOrdenacaoGrafoSimples() {
        OrdenacaoTopologica ordenacao = new OrdenacaoTopologica(grafoSimples);
        List<Integer> resultado = ordenacao.ordenar();


        verificarOrdemTopologicaValida(grafoSimples, resultado);
        assertEquals(6, resultado.size(), "A ordenação deve conter todos os vértices do grafo");
    }

    /**
     * Testa a ordenação topológica em um grafo representando dependências entre
     * disciplinas acadêmicas.
     * 
     */
    @Test
    @DisplayName("Deve ordenar corretamente um grafo de dependências entre disciplinas")
    void testOrdenacaoDisciplinas() {
        OrdenacaoTopologica ordenacao = new OrdenacaoTopologica(grafoDisciplinas);
        List<Integer> resultado = ordenacao.ordenar();

        verificarOrdemTopologicaValida(grafoDisciplinas, resultado);

        // Verifica algumas dependências específicas
        assertTrue(resultado.indexOf(0) < resultado.indexOf(2),
                "Matemática Discreta deve vir antes de Estrutura de Dados");
        assertTrue(resultado.indexOf(1) < resultado.indexOf(2),
                "Programação 1 deve vir antes de Estrutura de Dados");
        assertTrue(resultado.indexOf(2) < resultado.indexOf(3),
                "Estrutura de Dados deve vir antes de Algoritmos e Grafos");
        assertTrue(resultado.indexOf(3) < resultado.indexOf(4),
                "Algoritmos e Grafos deve vir antes de Inteligência Artificial");
    }

    /**
     * Testa o comportamento do algoritmo quando aplicado a um grafo que contém
     * ciclos.
     * 
     * <p>
     * A ordenação topológica só é possível em grafos direcionados acíclicos (DAGs),
     * portanto o algoritmo deve detectar a presença de ciclos e lançar uma exceção
     * apropriada.
     * </p>
     * 
     * <p>
     * Este teste verifica se:
     * </p>
     * <ul>
     * <li>Uma exceção do tipo {@link IllegalArgumentException} é lançada quando o
     * grafo contém ciclos</li>
     * </ul>
     */
    @Test
    @DisplayName("Deve lançar exceção para grafo com ciclo")
    void testGrafoComCiclo() {
        OrdenacaoTopologica ordenacao = new OrdenacaoTopologica(grafoCiclico);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ordenacao.ordenar();
        });

        String mensagemEsperada = "O grafo contém ciclos";
        String mensagemAtual = exception.getMessage();

        assertTrue(mensagemAtual.contains(mensagemEsperada),
                "A mensagem de exceção deve conter '" + mensagemEsperada + "'");
    }

    /**
     * Testa o comportamento do algoritmo quando aplicado a um grafo vazio.
     * 
     * <p>
     * O resultado esperado é uma lista vazia, já que não há vértices para ordenar.
     * </p>
     */
    @Test
    @DisplayName("Deve funcionar para grafo vazio")
    void testGrafoVazio() {
        Grafo grafoVazio = new Grafo(0);
        OrdenacaoTopologica ordenacao = new OrdenacaoTopologica(grafoVazio);
        List<Integer> resultado = ordenacao.ordenar();

        assertTrue(resultado.isEmpty(), "A ordenação de um grafo vazio deve ser vazia");
    }

    /**
     * Método auxiliar para verificar se uma ordenação topológica é válida.
     * 
     * <p>
     * Uma ordenação topológica é considerada válida se, para cada aresta (u, v) no
     * grafo, o vértice u aparece antes do vértice v na sequência ordenada. 
     * </p>
     * 
     * @param grafo o grafo direcionado original
     * @param ordem a lista de vértices representando uma possível ordenação
     *              topológica
     * @throws AssertionError se a ordenação não for válida
     */
    private void verificarOrdemTopologicaValida(Grafo grafo, List<Integer> ordem) {
        // Mapa para armazenar a posição de cada vértice na ordenação
        int[] posicao = new int[grafo.getNumVertices()];
        for (int i = 0; i < ordem.size(); i++) {
            posicao[ordem.get(i)] = i;
        }

        // Verificação do respeito das dependências
        for (int u = 0; u < grafo.getNumVertices(); u++) {
            for (int v : grafo.getAdjacentes(u)) {
                assertTrue(posicao[u] < posicao[v],
                        "Ordem topológica inválida: vértice " + u + " deve vir antes de " + v);
            }
        }
    }
}
