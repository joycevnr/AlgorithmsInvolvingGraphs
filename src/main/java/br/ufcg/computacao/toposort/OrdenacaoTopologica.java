package br.ufcg.computacao.toposort;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import br.ufcg.computacao.graph.Grafo;

/**
 * Implementação do algoritmo de ordenação topológica usando busca em profundidade (DFS).
 * 
 * <p>O algoritmo utiliza uma adaptação da busca em profundidade (DFS) para determinar
 * a ordem de finalização dos vértices, que é então utilizada para construir a ordenação
 * topológica. A implementação também inclui detecção de ciclos, já que a ordenação
 * topológica só é possível em grafos direcionados acíclicos.</p>
 * 
 * <p>Referências acadêmicas:
 * <ul>
 *   <li>Feofiloff, P., Kohayakawa, Y., & Wakabayashi, Y. (2011). Uma Introdução Sucinta à Teoria dos Algoritmos.
 *       Instituto de Matemática e Estatística da USP (IME-USP). Disponível em: 
 *       <a href="https://www.ime.usp.br/~pf/algoritmos-em-grafos/">https://www.ime.usp.br/~pf/algoritmos-em-grafos/</a></li>
 *   <li>Szwarcfiter, J. L., & Markenzon, L. (2010). Estruturas de Dados e seus Algoritmos (3ª ed.). 
 *       Rio de Janeiro: LTC. Capítulo 7: Grafos - Algoritmos de Ordenação Topológica.</li>
 *   <li>Tarjan, R. E. (1972). Depth-First Search and Linear Graph Algorithms. 
 *       SIAM Journal on Computing, 1(2), 146-160.</li>
 * </ul></p>
 * 
 * @author Joyce Vitória Nascimento Rodrigues
 * @version 1.0
 */
public class OrdenacaoTopologica {
    
    private final Grafo grafo;
    
    /**
     * Construtor que recebe o grafo a ser ordenado topologicamente.
     * 
     * @param grafo o grafo direcionado acíclico a ser ordenado
     */
    public OrdenacaoTopologica(Grafo grafo) {
        this.grafo = grafo;
    }
    
    /**
     * Executa a ordenação topológica usando o algoritmo DFS.
     * 
     * @return uma lista com os vértices ordenados topologicamente
     */
    public List<Integer> ordenar() {
        // Número total de vértices
        int n = grafo.getNumVertices();
        
        // Lista para armazenar o resultado da ordenação topológica
        List<Integer> resultado = new ArrayList<>();
        
        // Pilha para armazenar vértices em ordem de finalização do DFS
        Stack<Integer> pilhaFinalizacao = new Stack<>();
        
        // Array para marcar vértices visitados
        boolean[] visitado = new boolean[n];
        
        // Array para detectar ciclos (vértices no caminho atual do DFS)
        boolean[] noCaminhoAtual = new boolean[n];
        
        // Executa DFS a partir de cada vértice não visitado
        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                if (dfsUtil(i, visitado, noCaminhoAtual, pilhaFinalizacao)) {
                    throw new IllegalArgumentException("O grafo contém ciclos e não pode ser ordenado topologicamente.");
                }
            }
        }
        
        // Constrói o resultado a partir da pilha de finalização
        while (!pilhaFinalizacao.isEmpty()) {
            resultado.add(pilhaFinalizacao.pop());
        }
        
        return resultado;
    }
    
    /**
     * Função de utilidade para o DFS que visita recursivamente todos os vértices
     * e detecta ciclos no grafo.
     * 
     * @param v vértice atual
     * @param visitado array para marcar vértices visitados
     * @param noCaminhoAtual array para detectar ciclos
     * @param pilhaFinalizacao pilha para armazenar vértices em ordem de finalização
     * @return true se um ciclo foi detectado, false caso contrário
     */
    private boolean dfsUtil(int v, boolean[] visitado, boolean[] noCaminhoAtual, Stack<Integer> pilhaFinalizacao) {
        // Marca o vértice atual como visitado e como parte do caminho atual
        visitado[v] = true;
        noCaminhoAtual[v] = true;
        
        // Visita todos os vértices adjacentes
        for (Integer adjacente : grafo.getAdjacentes(v)) {
            // Se o vértice adjacente ainda não foi visitado, faz uma chamada recursiva
            if (!visitado[adjacente]) {
                if (dfsUtil(adjacente, visitado, noCaminhoAtual, pilhaFinalizacao)) {
                    return true; // Ciclo detectado
                }
            }
            // Se o vértice adjacente já foi visitado e está no caminho atual, há um ciclo
            else if (noCaminhoAtual[adjacente]) {
                return true; // Ciclo detectado
            }
        }
        
        // Remove o vértice do caminho atual e o adiciona à pilha de finalização
        noCaminhoAtual[v] = false;
        pilhaFinalizacao.push(v);
        
        return false; // Não há ciclos
    }
}
