package br.ufcg.computacao.toposort.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementação de um grafo direcionado utilizando lista de adjacência.
 * 
 * <p>A estrutura de dados utilizada é um {@code Map<Integer, List<Integer>>}, onde
 * cada chave é um vértice e seu valor associado é uma lista dos vértices adjacentes.
 * 
 * <p>Baseado nos estudos de:
 * <ul>
 *   <li>Feofiloff, P., Kohayakawa, Y., & Wakabayashi, Y. (2011). Uma Introdução Sucinta à Teoria dos Algoritmos.
 *       Instituto de Matemática e Estatística da USP (IME-USP). Disponível em: 
 *       <a href="https://www.ime.usp.br/~pf/algoritmos-em-grafos/">https://www.ime.usp.br/~pf/algoritmos-em-grafos/</a></li>
 *   <li>Szwarcfiter, J. L., & Markenzon, L. (2010). Estruturas de Dados e seus Algoritmos (3ª ed.). 
 *       Rio de Janeiro: LTC. Capítulo 7: Grafos - Representação de Grafos e Algoritmos Básicos.</li>
 *   <li>Ziviani, N. (2006). Projeto de Algoritmos com Implementações em Java e C++. 
 *       São Paulo: Cengage Learning. Capítulo 9: Algoritmos em Grafos.</li>
 * </ul></p>
 * 
 * @author Joyce Vitória Nascimento Rodrigues
 * @version 1.0
 */
public class Grafo {
    
    // Mapa que associa cada vértice a sua lista de adjacência
    private final Map<Integer, List<Integer>> listaAdj;
    
    // Número de vértices no grafo
    private final int numVertices;
    
    /**
     * Construtor para criar um grafo com um número específico de vértices.
     * 
     * @param numVertices número de vértices no grafo
     */
    public Grafo(int numVertices) {
        this.numVertices = numVertices;
        this.listaAdj = new HashMap<>();
        
        // Inicialização da lista de adjacência para cada vértice
        for (int i = 0; i < numVertices; i++) {
            listaAdj.put(i, new ArrayList<>());
        }
    }
    
    /**
     * Adiciona uma aresta direcionada do vértice origem para o vértice destino.
     * 
     * @param origem vértice de origem
     * @param destino vértice de destino
     */
    public void adicionarAresta(int origem, int destino) {
        // vértices  válidos
        if (origem >= 0 && origem < numVertices && destino >= 0 && destino < numVertices) {
            listaAdj.get(origem).add(destino);
        } else {
            throw new IllegalArgumentException("Vértices inválidos: " + origem + ", " + destino);
        }
    }
    
    /**
     * Retorna a lista de vértices adjacentes (sucessores) de um determinado vértice.
     * 
     * @param vertice o vértice para o qual se deseja obter a lista de adjacência
     * @return lista de vértices adjacentes
     */
    public List<Integer> getAdjacentes(int vertice) {
        if (vertice >= 0 && vertice < numVertices) {
            return listaAdj.get(vertice);
        } else {
            throw new IllegalArgumentException("Vértice inválido: " + vertice);
        }
    }
    
    /**
     * Retorna o número de vértices no grafo.
     * 
     * @return número de vértices
     */
    public int getNumVertices() {
        return numVertices;
    }
    
    /**
     * Retorna o conjunto de vértices do grafo.
     * 
     * @return conjunto de vértices
     */
    public Set<Integer> getVertices() {
        return listaAdj.keySet();
    }
    
    /**
     * Verifica se existe uma aresta entre dois vértices.
     * 
     * @param origem vértice de origem
     * @param destino vértice de destino
     * @return true se existe uma aresta, false caso contrário
     */
    public boolean existeAresta(int origem, int destino) {
        if (origem >= 0 && origem < numVertices && destino >= 0 && destino < numVertices) {
            return listaAdj.get(origem).contains(destino);
        }
        return false;
    }
    
    /**
     * Retorna uma representação em string do grafo.
     * 
     * @return representação em string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            sb.append(i).append(" -> ");
            for (Integer v : listaAdj.get(i)) {
                sb.append(v).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
