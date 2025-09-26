package br.ufcg.computacao.disjointsetunion;

/**
 * <p>
 * Implementação da estrutura de dados <strong>Disjoint Set Union (DSU)</strong>,
 * também conhecida como <strong>Union-Find</strong>. 
 * Essa estrutura é usada para gerenciar uma coleção de conjuntos disjuntos, 
 * permitindo operações eficientes de união de conjuntos e de busca do representante
 * (ou líder) de um elemento.
 * </p>
 *
 * <p>
 * Esta implementação utiliza as técnicas de <i>Union by Size</i> (união pelo tamanho do conjunto) 
 * e <i>Path Compression</i> (compressão de caminho), garantindo tempo quase constante 
 * por operação, com complexidade amortizada de $O(\log n)$.
 * </p>
 * 
 * <p>
 * Aplicações comuns incluem:
 * <ul>
 *   <li>Detecção de ciclos em grafos não direcionados</li>
 *   <li>Algoritmo de Kruskal (Árvore Geradora Mínima)</li>
 *   <li>Componentes conectados em grafos</li>
 *   <li>Problemas de conectividade dinâmica</li>
 * </ul>
 * </p>
 * 
 * @author Augusto de Brito Lopes
 *     Estrutura baseada em implementações clássicas de Union-Find
 */
public class DSULogn {
    private int[] parent;
    private int[] size;
    
    /**
     * Construtor que inicializa as estruturas {@code parent} e {@code size}.
     * 
     * @param n número de elementos (tamanho do universo).
     */
    public DSULogn(int n){
        this.parent = new int[n];
        this.size = new int[n];
    }

    /**
     * Inicializa um conjunto contendo apenas o elemento {@code v}.
     * 
     * <p>
     * Cada elemento é seu próprio pai inicialmente, e o tamanho do conjunto é 1.
     * </p>
     * 
     * @param v elemento que será inicializado como um conjunto próprio.
     */
    public void makeSet(int v){
        this.parent[v] = v;
        this.size[v] = 1;
    }

    /**
     * Une os conjuntos que contêm os elementos {@code a} e {@code b}.
     * <p>
     * Caso estejam em conjuntos diferentes, aplica a heurística de união pelo tamanho
     * para garantir melhor balanceamento, anexando o conjunto menor ao maior.
     * </p>
     * 
     * @param a elemento pertencente ao primeiro conjunto.
     * @param b elemento pertencente ao segundo conjunto.
     */
    public void unionSet(int a, int b){
        a = findSet(a);
        b = findSet(b);
        if(a != b){
            if(this.size[a] < this.size[b]){
                int aux = a;
                a = b;
                b = aux;
            }
            this.parent[b] = a;
            this.size[a] += this.size[b];
        }
    }

    /**
     * Encontra o representante (líder) do conjunto ao qual o elemento {@code v} pertence.
     * 
     * <p>
     * Utiliza a técnica de <i>Path Compression</i>, que reduz a altura da árvore
     * ao tornar cada nó visitado apontar diretamente para o representante.
     * </p>
     * 
     * @param v elemento cujo representante será encontrado.
     * @return o representante (líder) do conjunto de {@code v}.
     */
    public int findSet(int v){
        if(this.parent[v] == v)
            return v;
        return this.parent[v] = findSet(this.parent[v]);
    }

}
