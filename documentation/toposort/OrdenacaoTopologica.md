### **Ordenação Topológica**

#### **Contextualização**

Em teoria dos grafos, a ordenação topológica de um grafo acíclico direcionado (DAG Directed Acyclic Graph) é uma ordenação linear de seus vértices, tal que para toda aresta direcionada de um vértice `u` para um vértice `v`, `u` vem antes de `v` na ordenação. Um DAG é um tipo de grafo que possui arestas com direção e não contém ciclos, ou seja, não é possível partir de um vértice e retornar a ele seguindo as direções das arestas.

Essa estrutura é fundamental para modelar problemas que envolvem dependências e pré-requisitos. Por exemplo, ao compilar um projeto de software, alguns arquivos precisam ser processados antes de outros. Da mesma forma, em uma grade curricular, certas disciplinas devem ser cursadas antes de suas sequências. A ordenação topológica nos fornece uma sequência válida para executar essas tarefas.

#### **O Problema**

O desafio consiste em determinar uma sequência de execução para um conjunto de tarefas com interdependências, por exemplo, disciplinas com pré-requisitos(como nosso curso) ou etapas de um projeto. É preciso encontrar uma ordem linear que respeite todas as dependências e, ao mesmo tempo, detectar se o sistema é impossível de ser resolvido por conter um ciclo (uma dependência circular, como A depender de B e B depender de A). A solução para este problema é a **ordenação topológica**, um algoritmo que estabelece uma sequência de execução válida e identifica a existência de ciclos, sendo a abordagem realizada com Busca em Profundidade (DFS) uma das mais eficazes para implementá-lo.

#### **O Algoritmo Passo a Passo**

A implementação do algoritmo requer o controle do estado de cada vértice durante a busca. Nisso, foi usada três estruturas de dados auxiliares:

  * `visitado[]`: um array booleano para marcar os vértices que já foram visitados em algum momento.
  * `noCaminhoAtual[]`: um array booleano para rastrear os vértices que estão na pilha de recursão da busca atual. (ajuda em detectar ciclos)
  * `pilhaFinalizacao`: uma pilha que armazena os vértices na ordem em que eles são finalizados.

**1. Inicialização**

O método principal inicializa as estruturas de dados e percorre todos os vértices do grafo, iniciando uma DFS para cada vértice que ainda não foi visitado.

```java
public List<Integer> ordenar() {
    int n = grafo.getNumVertices();
    List<Integer> resultado = new ArrayList<>();
    Stack<Integer> pilhaFinalizacao = new Stack<>();
    boolean[] visitado = new boolean[n];
    boolean[] noCaminhoAtual = new boolean[n];

    for (int i = 0; i < n; i++) {
        if (!visitado[i]) {
            // Se dfsUtil retornar true, um ciclo foi encontrado.
            if (dfsUtil(i, visitado, noCaminhoAtual, pilhaFinalizacao)) {
                throw new IllegalArgumentException("O grafo contém ciclos e não pode ser ordenado topologicamente.");
            }
        }
    }
}
```

**2. O DFS Modificado e a Detecção de Ciclos**

O parte central deste algoritmo é a função recursiva `dfsUtil`. Para cada vértice `v` visitado, ela faz o seguinte:

1.  Marca `v` como `visitado` e como parte do `noCaminhoAtual`.
2.  Para cada vizinho de `v`, verifica:
      * Se o vizinho não foi visitado, chama a recursão para ele.
      * Se o vizinho já foi visitado E está `noCaminhoAtual`, significa que encontramos uma aresta de volta (*back edge*). Isso forma um ciclo, e o algoritmo para.
3.  Depois de visitar todos os vizinhos de `v` (ou seja, explorar todo o caminho a partir dele), `v` é removido de `noCaminhoAtual` e adicionado à `pilhaFinalizacao`.

```java
private boolean dfsUtil(int v, boolean[] visitado, boolean[] noCaminhoAtual, 
                        Stack<Integer> pilhaFinalizacao) {
    visitado[v] = true;
    noCaminhoAtual[v] = true;
    
    for (Integer adjacente : grafo.getAdjacentes(v)) {
        if (!visitado[adjacente]) {
            if (dfsUtil(adjacente, visitado, noCaminhoAtual, pilhaFinalizacao)) {
                return true; // Ciclo detectado e propagado pela recursão
            }
        }
        else if (noCaminhoAtual[adjacente]) {
            return true; // Ciclo detectado
        }
    }
    
    // Todos os descendentes de 'v' foram visitados. Agora 'v' está finalizado.
    noCaminhoAtual[v] = false;
    pilhaFinalizacao.push(v);
    return false; // Nenhum ciclo encontrado neste caminho
}
```

**3. Construção do Resultado Final**

Após o laço principal terminar (garantindo que todos os vértices foram visitados), a `pilhaFinalizacao` contém os vértices na ordem inversa da ordenação topológica. Basta desempilhá-los para uma lista para obter o resultado final.

```java
while (!pilhaFinalizacao.isEmpty()) {
    resultado.add(pilhaFinalizacao.pop());
}
return resultado;
```

#### **Análise de Complexidade**

Saber a eficiência do algoritmo é essencial, então abaixo destaco as suas grandes vantagens:

  * **Complexidade de Tempo: $O(V + E)$**
    O algoritmo DFS visita cada vértice (`V`) e cada aresta (`E`) exatamente uma vez em um grafo direcionado. Todas as outras operações (marcação em arrays, empilhar) são de tempo constante.

  * **Complexidade de Espaço: $O(V)$**
    O espaço é necessário para armazenar os arrays de controle (`visitado` e `noCaminhoAtual`), a pilha de finalização e a pilha de recursão do sistema, que no pior caso pode ter o tamanho de `V`.

#### **Benchmark de Desempenho**

A eficiência do algoritmo foi validada através de benchmarks utilizando a biblioteca JMH. Foram gerados grafos acíclicos direcionados (DAGs) de diferentes tamanhos (número de vértices) e densidades (proporção de arestas), e o tempo médio de execução foi medido. Os resultados confirmam a escalabilidade linear do algoritmo, alinhada com sua complexidade teórica de $O(V + E)$.

--GRÁFICOS



#### **Aplicações**

A ordenação topológica é amplamente utilizada em problemas do mundo real, como:

  * **Gerenciamento de Projetos:** Determinar a ordem de execução de tarefas em um cronograma, onde algumas tarefas são pré-requisitos para outras.
  * **Compiladores:** Na compilação de código-fonte, a ordenação topológica define a ordem correta de compilação, já que muitas vezes os módulos ou arquivos dependem uns dos outros. .
  * **Resolução de Dependências:** Em sistemas de gerenciamento de pacotes (como Maven, npm ou pip), a ordenação topológica é usada para determinar em que ordem os pacotes devem ser instalados para satisfazer todas as dependências.
  * **Grades Curriculares:** Montar uma sequência válida de disciplinas a serem cursadas, respeitando os pré-requisitos de cada uma, como na nossa realidade do curso, para cursar LEDA, foi necessário ter pago, por exemplo, LP2.

#### **Contribuições**

  * **Autora:** Joyce Vitória Nascimento Rodrigues

#### **Bibliografia**

1.  CORMEN, T. H.; LEISERSON, C. E.; RIVEST, R. L.; STEIN, C. **Introduction to Algorithms**. 3rd ed. MIT Press, 2009.
2.  FEOFILOFF, P.; KOHAYAKAWA, Y.; WAKABAYASHI, Y. **Uma Introdução Sucinta à Teoria dos Algoritmos**. Instituto de Matemática e Estatística da USP (IME-USP), 2011.
3.  TARJAN, R. E. (1972). **Depth-First Search and Linear Graph Algorithms**. SIAM Journal on Computing, 1(2), 146-160.