# Ordenação Topológica com DFS: Estrutura e Algoritmo

## Introdução

**Autora: Joyce Vitória Nascimento Rodrigues**

A **ordenação topológica** é um algoritmo fundamental em teoria dos grafos que gera uma sequência linear dos vértices de um grafo direcionado acíclico (DAG), de tal forma que para cada aresta (u, v), o vértice u aparece antes do vértice v na sequência. Esta ordenação representa uma solução para problemas que envolvem dependências sequenciais entre elementos, como cronogramas de projetos, compilação de código-fonte e pré-requisitos de cursos.

Importante ressaltar que a ordenação topológica só é possível em grafos direcionados que não possuem ciclos (DAGs). Se o grafo contiver um ciclo, não é possível estabelecer uma ordem topológica, pois existiriam dependências circulares.

Desse modo, este documento explora em detalhes a implementação da ordenação topológica usando o algoritmo de Busca em Profundidade (DFS - Depth-First Search).

## Referências Acadêmicas

Este documento baseia-se em pesquisas acadêmicas sobre algoritmos em grafos, particularmente o algoritmo de ordenação topológica apresentadas abaixo:

1. FEOFILOFF, P.; KOHAYAKAWA, Y.; WAKABAYASHI, Y. **Uma Introdução Sucinta à Teoria dos Algoritmos**. Instituto de Matemática e Estatística da USP (IME-USP), 2011. Capítulo sobre Ordenação Topológica e Componentes Fortemente Conexas. [Link para referência](https://www.ime.usp.br/~pf/algoritmos_para_grafos/)

2. CORMEN, T. H.; LEISERSON, C. E.; RIVEST, R. L.; STEIN, C. **Introduction to Algorithms**. 3rd ed. MIT Press, 2009. [Link para referência](https://mitpress.mit.edu/books/introduction-algorithms-third-edition) - Referência internacional amplamente utilizada em disciplinas de estruturas de dados avançadas.

## Aplicações

A ordenação topológica tem várias aplicações práticas, incluindo:

1. **Agendamento de tarefas**: Determinar a ordem de execução de tarefas que possuem dependências
2. **Compilação de software**: Ordenar a compilação de módulos que dependem uns dos outros
3. **Currículo de cursos**: Estabelecer a sequência de disciplinas que têm pré-requisitos
4. **Processamento de planilhas**: Calcular células que dependem de valores de outras células

## Algoritmo de Ordenação Topológica usando DFS

O algoritmo de ordenação topológica usando Busca em Profundidade (DFS - Depth-First Search) funciona da seguinte maneira:

1. **Inicialização**: Cria uma lista vazia para armazenar o resultado e marcadores para acompanhar vértices visitados.
2. **DFS modificado**: Executa um DFS a partir de cada vértice não visitado:
   - Visita recursivamente todos os vértices adjacentes não visitados
   - Depois de explorar todos os adjacentes de um vértice, adiciona o vértice ao início da lista de resultado
3. **Detecção de ciclos**: Durante o DFS, verifica se há ciclos no grafo. Se um ciclo for detectado, a ordenação topológica não é possível.
4. **Resultado**: A lista final, construída na ordem inversa das finalizações do DFS, é a ordenação topológica.

### Complexidade do Algoritmo

- **Tempo**: O(V + E), onde V é o número de vértices e E é o número de arestas
- **Espaço**: O(V), para armazenar os estados dos vértices e a pilha de recursão

## Algoritmo de Ordenação Topológica usando DFS

A ordenação topológica com DFS é um algoritmo elegante que explora as características recursivas da busca em profundidade para determinar a ordem correta dos vértices.

### Princípio Básico

O princípio fundamental da ordenação topológica com DFS baseia-se no fato de que, em um grafo direcionado acíclico, os vértices que não possuem arestas de saída (também chamados de "sumidouros") devem aparecer no final da ordenação. Estendendo este princípio, um vértice deve aparecer depois de todos os seus sucessores na ordenação.

### Passos do Algoritmo

#### 1. Inicialização

O primeiro passo do algoritmo é inicializar as estruturas de dados necessárias:

```java
public List<Integer> ordenar() {
    int n = grafo.getNumVertices();
    List<Integer> resultado = new ArrayList<>();
    Stack<Integer> pilhaFinalizacao = new Stack<>();
    boolean[] visitado = new boolean[n];
    boolean[] noCaminhoAtual = new boolean[n];
```

- **resultado**: Lista que armazenará a ordenação topológica final
- **pilhaFinalizacao**: Pilha para rastrear a ordem de finalização dos vértices durante o DFS
- **visitado**: Array para marcar vértices já visitados pelo DFS
- **noCaminhoAtual**: Array para detectar ciclos no grafo

#### 2. Execução do DFS Modificado

Para cada vértice não visitado, o algoritmo executa o DFS:

```java
for (int i = 0; i < n; i++) {
    if (!visitado[i]) {
        if (dfsUtil(i, visitado, noCaminhoAtual, pilhaFinalizacao)) {
            throw new IllegalArgumentException("O grafo contém ciclos e não pode ser ordenado topologicamente.");
        }
    }
}
```

O método `dfsUtil` é a parte central do algoritmo:

```java
private boolean dfsUtil(int v, boolean[] visitado, boolean[] noCaminhoAtual, 
                        Stack<Integer> pilhaFinalizacao) {
    visitado[v] = true;
    noCaminhoAtual[v] = true;
    
    for (Integer adjacente : grafo.getAdjacentes(v)) {
        if (!visitado[adjacente]) {
            if (dfsUtil(adjacente, visitado, noCaminhoAtual, pilhaFinalizacao)) {
                return true; // Ciclo detectado
            }
        }
        else if (noCaminhoAtual[adjacente]) {
            return true; // Ciclo detectado
        }
    }
    
    noCaminhoAtual[v] = false;
    pilhaFinalizacao.push(v);
    return false; // Sem ciclos
}
```

#### 3. Detecção de Ciclos

Um aspecto crucial do algoritmo é a detecção de ciclos, que é feita usando o array `noCaminhoAtual`. Se um vértice já visitado está no caminho atual do DFS, então há um ciclo.

#### 4. Construção do Resultado

A ordenação topológica é obtida desempilhando os vértices da pilha de finalização:

```java
while (!pilhaFinalizacao.isEmpty()) {
    resultado.add(pilhaFinalizacao.pop());
}
```

#### Detecção de Ciclos
