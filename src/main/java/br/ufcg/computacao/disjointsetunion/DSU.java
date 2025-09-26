package br.ufcg.computacao.disjointsetunion;

/**
 * <p>
 * Implementação básica da estrutura de dados <strong>Disjoint Set Union (DSU)</strong>,
 * também conhecida como <strong>Union-Find</strong>.
 * Essa estrutura mantém uma coleção de conjuntos disjuntos, permitindo operações
 * para criar conjuntos unitários, unir conjuntos e encontrar o representante
 * (ou líder) de um elemento.
 * </p>
 *
 * <p>
 * Esta versão é a forma mais simples do DSU, sem heurísticas de otimização
 * (como <i>Union by Size/Rank</i> ou <i>Path Compression</i>). 
 * Por isso, a complexidade das operações pode chegar a $O(n)$ no pior caso.
 * </p>
 *
 * <p>
 * Aplicações clássicas incluem:
 * <ul>
 *   <li>Verificação de conectividade em grafos</li>
 *   <li>Detecção de ciclos</li>
 *   <li>Algoritmo de Kruskal para árvores geradoras mínimas</li>
 * </ul>
 * </p>
 * 
 * @author Augusto de Brito Lopes
 *     Estrutura baseada em implementações clássicas de Union-Find
 */
public class DSU {
     
    private int[] parent;
    
    /**
     * Construtor que inicializa a estrutura {@code parent}.
     * 
     * @param n número de elementos (tamanho do universo).
     */
    public DSU(int n){
        this.parent = new int[n];
    }

    /**
     * Inicializa um conjunto contendo apenas o elemento {@code v}.
     * <p>
     * Cada elemento é seu próprio pai, formando um conjunto unitário.
     * </p>
     * 
     * @param v elemento que será inicializado como conjunto próprio.
     */
    public void makeSet(int v){
        this.parent[v] = v;
    }

    /**
     * Une os conjuntos que contêm os elementos {@code a} e {@code b}.
     * <p>
     * Caso estejam em conjuntos diferentes, o representante de {@code b}
     * passa a ser o representante de {@code a}.
     * </p>
     * 
     * @param a elemento pertencente ao primeiro conjunto.
     * @param b elemento pertencente ao segundo conjunto.
     */
    public void unionSet(int a, int b){
        a = findSet(a);
        b = findSet(b);
        if(a != b){
            this.parent[b] = a;
        }
    }

    /**
     * Encontra o representante (líder) do conjunto ao qual o elemento {@code v} pertence.
     * <p>
     * Esta versão não utiliza compressão de caminho, o que pode resultar em chamadas recursivas
     * mais profundas em casos degenerados.
     * </p>
     * 
     * @param v elemento cujo representante será encontrado.
     * @return o representante (líder) do conjunto de {@code v}.
     */
    public int findSet(int v){
        if(this.parent[v] == v)
            return v;
        return findSet(this.parent[v]);
    }

}
