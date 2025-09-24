# Disjoint Set Union

## Contextualização

Para tratar algoritmos com grafos, muitas vezes é necessário gerenciar conjuntos disjuntos de elementos e responder rapidamente a perguntas sobre conectividade: “esses dois elementos pertencem ao mesmo conjunto?” ou “qual é o líder desse conjunto?”.  
Para isso, a estrutura **Disjoint Set Union (DSU)**, também conhecida como **Union-Find**, é amplamente utilizada.  

O DSU mantém uma coleção de conjuntos disjuntos, oferecendo operações eficientes para:  
- Criar conjuntos unitários.  
- Unir dois conjuntos distintos.  
- Encontrar o líder do conjunto de um elemento.  

Essa estrutura de dados é a base de diversos algoritmos fundamentais em grafos e teoria da computação.

## Desafio

O desafio é manipular conjuntos disjuntos de forma eficiente, permitindo realizar operações de união e busca sem que o custo cresça de maneira significativa.  
Uma solução ingênua, sem otimizações, pode chegar a **O(n)** no pior caso para cada operação. No entanto, com heurísticas adequadas, o tempo pode ser reduzido para **O(logn) amortizado**, mesmo em grandes instâncias.  

O DSU é essencial em problemas que exigem relacionar múltiplos nós, como:  
- **Detecção de ciclos** em grafos não direcionados.  
- **Algoritmo de Kruskal**, para encontrar a **árvore geradora mínima**.  
- **Componentes conectados** em grafos.  
- **Problemas de conectividade dinâmica**, onde arestas ou conexões são adicionadas ao longo do tempo.  

## O Algoritmo Passo a Passo

A estrutura básica do DSU envolve três operações fundamentais:  

  * `makeSet(v)`: cria um conjunto contendo apenas o elemento `v`.  
  * `findSet(v)`: encontra o representante do conjunto ao qual `v` pertence.  
  * `unionSet(a, b)`: une os conjuntos que contêm `a` e `b`.  

## Implementação trivial

### 1. Inicialização

Cada elemento começa como seu próprio representante. Todas as informações sobre os conjuntos de elementos serão mantidas em um array parent:

```java
public void makeSet(int v){
    this.parent[v] = v;
}
```

### 2. Operação Find (Busca do Representante)

Na operação `findSet` subimos recursivamente nos líderes dos vértices até encontrar o líder do conjunto todo (raiz), ou seja, um vértice cuja referência do pai seja ele mesmo.  

```java
public int findSet(int v){
    if(this.parent[v] == v)
        return v;
    return findSet(this.parent[v]);
}
```

### 3. Operação Union (União de Conjuntos)

A operação unionSet é feita para combinar dois conjuntos. Primeiro encontramos o representante de **a** e depois o representante de **b**. Se os líderes forem iguais, não precisamos fazer nada, eles já estão no mesmo conjunto. Mas, caso contrário, indicamos um dos representantes como o pai do outro, sem ordem de prioridade.   


```java
public void unionSet(int a, int b){
    a = findSet(a);
    b = findSet(b);
    if(a != b){
        this.parent[b] = a;
    }
}
```

## Implementação otimizada

### 1. Inicialização

Na versão otimizada (DSULogn), é feita a mesma coisa da versão simples, mas agora temos um array com o size de cada vértice (será bastante útil na hora de unir dois conjuntos), cada conjunto é iniciado com tamanho 1.   

```java
public void makeSet(int v){
    this.parent[v] = v;
    this.size[v] = 1;
}
```

### 2. Operação Find (Busca do líder)

Na versão otimizada, aplica-se a técnica de **Path Compression**. Essa técnica trabalha da seguinte forma: durante a busca pelo representante de um elemento, atualizar os ponteiros de todos os nós visitados para apontarem diretamente ao representante. Esse processo acontece no desenrolar da pilha da recursão.

```java
public int findSet(int v){
    if(this.parent[v] == v)
        return v;
    return this.parent[v] = findSet(this.parent[v]);
}
```

### 3. Operação Union (União dos conjuntos)

Na versão otimizada, aplica-se a heurística **Union by Size**, que consiste em: anexar a árvore com a classificação mais baixa àquela com a classificação mais alta, nesse caso a classificação é o tamanho, ou seja, àquela com menor tamanho terá àquela com maior tamanho como representante. Isso mantém as árvores balanceadas e melhora a eficiência. 

```java
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
```

## Análise de eficiência

### 1. Versão simples (clássica) — DSU

- Operações implementadas: `makeSet`, `unionSet`, `findSet`, sem otimizações adicionais.  
- A implementação de `findSet` é recursiva pura: sobe pela cadeia de pais até encontrar o root, sem modificações no caminho.  
- Em `unionSet`, simplesmente une o root de `b` ao root de `a` (sem considerar tamanho ou profundidade).

#### Complexidade teórica

- No pior caso, a estrutura de conjuntos pode degenerar em uma cadeia linear: cada nó apontando para o próximo.  
- Nesse cenário, uma chamada `findSet(v)` pode percorrer até **O(n)**.  
- `unionSet` (que chama `findSet` duas vezes) também pode ter custo **O(n)**.  

Essa versão é aceitável para casos pequenos, mas não escala bem para valores grandes de **n** ou muitas operações.

---

### 2. Versão otimizada — DSULogn

A classe **DSULogn** incorpora duas heurísticas fundamentais:

- **Path Compression**: durante `findSet`, todos os nós no caminho passam a apontar diretamente para o representante.  
- **Union by Size**: o conjunto menor é anexado ao maior, evitando crescimento excessivo da altura da árvore.

#### Complexidade teórica

- Com **Union by Size** mas sem *Path Compression*: cada operação tem custo **O(log n)**.  
- Com **Union by Size** + *Path Compression*: cada operação tem custo **O(α(n)) amortizado** onde α(n) é a função inversa de Ackermann, que cresce muito lentamente, tornando as operações praticamente constantes.

### Representação gráfica 
---

## Conclusão

A estrutura **Disjoint Set Union** é simples, mas extremamente poderosa.  
Enquanto a versão básica já resolve muitos problemas, a versão otimizada com **Union by Size** e **Path Compression** garante desempenho eficiente mesmo em grandes grafos e aplicações intensivas.  
Essa combinação de simplicidade e eficiência explica por que o DSU é uma das estruturas de dados mais utilizadas em algoritmos de grafos.
