# **Algoritmo de Floyd-Warshall**

## **Contextualização**

Em teoria dos grafos, o algoritmo de Floyd-Warshall é um método de programação dinâmica para encontrar os caminhos mais curtos em um grafo direcionado e ponderado. A principal característica do algoritmo é sua capacidade de calcular a menor distância entre todos os pares de vértices do grafo em uma única execução. Diferente de algoritmos como Dijkstra ou Bellman-Ford, que encontram o caminho mais curto a partir de um único vértice de origem, o Floyd-Warshall constrói uma matriz de distâncias que contém a solução para todas as origens e destinos possíveis.

Essa abordagem é especialmente útil em problemas onde as consultas de caminhos mais curtos entre quaisquer dois nós são frequentes. Por exemplo, em uma rede de roteadores, pode ser necessário conhecer a rota mais eficiente entre quaisquer dois pontos da rede. Da mesma forma, em sistemas de logística, calcular a distância mínima entre todos os pares de depósitos e pontos de entrega otimiza o planejamento de rotas.

## **O Problema**

O desafio é encontrar a rota de menor custo (distância, tempo, etc.) entre cada par de nós em um grafo. Executar um algoritmo de caminho mínimo de fonte única (como Dijkstra) a partir de cada vértice é uma solução possível, mas pode ser ineficiente, especialmente em grafos densos. O algoritmo de Floyd-Warshall oferece uma alternativa elegante e determinística que resolve o problema de forma mais direta, com uma complexidade de tempo que depende apenas do número de vértices, independentemente do número de arestas. A solução gera uma matriz final de distâncias mínimas e uma matriz de sucessores que permite reconstruir qualquer um desses caminhos.

## **O Algoritmo Passo a Passo**

A implementação do algoritmo utiliza duas matrizes principais para armazenar seus resultados intermediários e finais:

  * `distancias[][]`: uma matriz que armazena o custo do caminho mais curto conhecido até o momento entre cada par de vértices (i, j).

  * `sucessores[][]`: uma matriz auxiliar que armazena o próximo nó no caminho mais curto de i para j, permitindo a reconstrução do caminho.

**1. Inicialização**

O método de inicialização prepara as matrizes com base na matriz de adjacência do grafo. A matriz distancias é uma cópia direta dos pesos das arestas, e a matriz sucessores é preenchida para indicar o próximo nó em um caminho direto.

```java
public void inicializar(int[][] grafo){
    int n = grafo.length;
    this.distancias = new int[n][n];
    this.sucessores = new int[n][n];

    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++){
            distancias[i][j] = grafo[i][j]; //copia da matriz

            //preenche matriz de sucessores
            if (grafo[i][j] != INFINITO && i != j)
                sucessores[i][j] = j;
            else 
                sucessores[i][j] = -1;
        }
}
```

**2. O Cálculo Iterativo das Distâncias**

Esta é a parte central do algoritmo. Ele consiste em três laços aninhados que iteram sobre todos os vértices. Para cada par de vértices (i, j), o algoritmo testa se passar por um vértice intermediário k pode encurtar o caminho.
O laço externo itera sobre k, que representa o vértice intermediário permitido.
Os laços internos iteram sobre todos os pares de vértices de origem i e destino j.
A condição distancias[i][k] + distancias[k][j] < distancias[i][j] verifica se o caminho de i para j via k é mais curto que o caminho direto conhecido.
Se for mais curto, a distância distancias[i][j] é atualizada, e o sucessor no caminho de i para j passa a ser o mesmo do caminho de i para k.

```java

public void calcularDistancias(){
    int n = distancias.length;
    for (int k = 0; k < n; k++) // nó intermediário
        for (int i = 0; i < n; i++) // nó de saída
            for (int j = 0; j < n; j++) // nó de destino
                if (distancias[i][k] != INFINITO && distancias[k][j] != INFINITO) 
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]){ 
                        distancias[i][j] = distancias[i][k] + distancias[k][j]; // atualiza a distância
                        sucessores[i][j] = sucessores[i][k]; //guarda o nó intermediário;
                    }
}
```

**3. Reconstrução do Caminho**

Após a execução do algoritmo, a matriz distancias contém os custos mínimos. Para obter o caminho real entre um vértice u e v, utilizamos a matriz sucessores. Partindo de u, seguimos os sucessores até chegarmos a v.

```java
public List<Integer> getCaminho(int u, int v) {
    List<Integer> caminho = new ArrayList<>();
    if (sucessores[u][v] == -1) return caminho; //sem caminho

    caminho.add(u);
    while (u != v){
        u = sucessores[u][v];
        caminho.add(u);
    }
    return caminho;
}
```

## **Análise de Complexidade**
Compreender a eficiência do algoritmo é fundamental para decidir quando usá-lo.

  * **Complexidade de Tempo: O(V³)**
  A complexidade é dominada pelos três laços aninhados, cada um executando V vezes (onde V é o número de vértices). Isso torna o algoritmo muito previsível, mas custoso para grafos muito grandes.

  * **Complexidade de Espaço: O(V²)**
  O espaço é necessário para armazenar as matrizes de distancias e sucessores, ambas de dimensão V×V.

## **Bibliografia**
1. FLOYD, R. W. (1962). Algorithm 97: Shortest Path. Communications of the ACM, 5(6), 345.
2. WARSHALL, S. (1962). A theorem on Boolean matrices. Journal of the ACM, 9(1), 11-12.
3. CORMEN, T. H.; LEISERSON, C. E.; RIVEST, R. L.; STEIN, C. Introduction to Algorithms. 3rd ed. MIT Press, 2009. 