# Algoritmo Dijkstra
**autora: Maria Eduarda Capela Cabral Pinheiro da Silva**

## Introdução
Nessa documentação está o resultado da pesquisa teórica sobre o Algoritmo de Dijkstra que calcula a menor distância entre um vértice de um grafo até outro. Também se encontra a explicação sobre suas implementações, com e sem o uso de uma fila de prioridade, e uma breve discussão sobre o desempenho e a eficiência dessas implementações.  

## História e motivação
O Algoritmo de Dijkstra, como o nome já indica, foi desenvolvido pelo matemático holandês Edsger W. Dijkstra(1930-2002). De acordo com uma entrevista que ele deu em 2001 para a revista Communications of the ACM, a história por trás do algoritmo começa com a inauguração do computador ARMAC em 1956, quando Dijkstra precisava achar um problema que pudesse mostrar a utilização prática do computador. O problema deveria demonstrar o uso matemático do ARMAC, mas também tinha que ser simples o suficiente para que pessoas que não trabalhassem com matemática pudessem entender o problema e a solução. Com base nessas restrições, Dijkstra escolheu um problema prático e fácil de entender: Escolhendo duas, de 64 cidades na Holanda, qual é o menor caminho entre elas? Mas o algoritmo só foi escrito durante uma viagem com sua noiva em Amsterdã, quando pararam para tomar um café, onde ele desenvolveu um algoritmo que recebendo um grafo, conjunto de vértices/nós conectados entre si por arestas com valores positivos, retornaria o menor caminho entre um ponto e outro, em apenas 20 minutos. Por não ter acesso a papel e lápis no momento, Dijkstra disse que teve que forçar sua mente a evitar complicações desnecessárias. O Algoritmo de Dijkstra só foi publicado em 1959, três anos depois, porém é usado até hoje para diversos problemas de logística e é a base para diversos sistemas de navegação como o GPS. Então por que aprender esse algoritmo? As suas aplicações são inúmeras, mas se baseiam em procurar a rota mais curta, mais barata e mais rápida.
       
## Funcionamento
A entrada padrão recebe um grafo, em formato de matriz de adjacência ou lista de adjacência, junto com a origem, normalmente representada por um inteiro. Pode também receber o destino, também representado por um inteiro, se o objetivo for receber só a distância mínima entre a origem e o destino. Nossas implementações receberam a origem (chamaremos de raiz) e uma matriz de adjacência. Usaremos matriz de adjacência para representar os grafos, para podermos observar o peso entre as arestas que conectam os vértices. A matriz de adjacência consiste em uma matriz A, onde __A[i][j]__ é a aresta que liga o vértice i até o vértice j, e o valor é 0(zero) significa que não existe uma aresta ligando esses dois vértices..

Já o retorno será um array de arrays de inteiros contendo dois arrays: O primeiro representa as menores distâncias entre o vértice da origem e o vértice I, sendo I um índice do array de distâncias, o segundo representa o array de "pais", vértice responsável por chegar no vértice J, sendo J um índice do array de pais. Implementamos também um método extra `java public String printCaminho(int[] pais, int destino)` que usa o array de pais resultante do Algoritmo de Dijkstra para mostrar visualmente o menor caminho entre a origem e o destino. 

**Dijkstra sem fila de prioridade**
Vamos começar a explicar o passo a passo do funcionamento de `java public int[][] dijkstra_semFila(int[][] grafo, int raiz)`, nossa implementação inicial do Algoritmo de Dijkstra sem usar fila de prioridade.
Primeiro testamos se a raiz/origem que é fornecida é válida, ou seja, não é um numero negativo e esta representado pelo grafo. Depois atribuimos a variável vértices com o tamanho do array, que é igual ao número total de vértices.

```java 
        if(raiz<0 || raiz>grafo.length) throw new IndexOutOfBoundsException("Origem inválida");
		int vertices = grafo.length;
```

O proximo passo é inicializar os arrays de distâncias mínimas, chamaremos de distancias, de vértices que já incluimos no caminho, chamaremos de visitados, e de vértices que usaremos pra montar o caminho, chamaremos de pais. Já que estamos procurando a menor distância entre os vértices e a origem, podemos supor que o oposto disso, a distancia entre um vértice não conectado ou com distância desconhecida, é o infinito, que representamos com uma constante `java final int infinito = Integer.MAX_VALUE`. Também inicializamos a distância da raiz até ela mesma como 0(zero);

```java 
        int[] distancias = new int[vertices];
		boolean[] visitados = new boolean[vertices];
		int[] pais = new int[vertices];
		for(int i =0; i<vertices; i++) {
			distancias[i] = infinito;
			pais[i] = -1;
		}
		distancias[raiz] = 0;
```
Agora entramos em um loop finito `java for(int i =0; i<vertices-1; i++)` que vai passar por todos os vértices menos o último e executará dois passos. O primeiro será procurar o vértice mais perto do vértice anterior( a soma da aresta que liga os dois com a distância já percorrida aumenta menos a distância total), começando pela origem, por meio do método `java private int distanciaMinima(int[] distancias, int[] visitados)` e depois marcar esse vértice como visitado.

```java
		int atual = distanciaMinima(distancias, visitados);
		visitados[atual]=true;
```
O modo que o método distanciaMinima(int[] distancias, int[] visitados) funciona é simples. Assumindo a distância mínima(min) como infinito e que o próximo vértice, que chamaremos de minIndex, não existe (é -1), vamos passar pelo array de distancias, que tem tamanho igual ao número de vértices, vendo se o vértice i já foi incluido no caminho e se a distância da origem até ele é menor que min, se sim atualizamos min e o minIndex. Quando acabarmos o loop retornamos o minIndex, o próximo vértice com menor distancia para adicionar no caminho.

```java
   private int distanciaMinima(int[] distancias, boolean[] visitados) {
		int min = infinito;
		int minIndex = -1;
		for(int i =0; i<distancias.length; i++) { 
			if(!visitados[i] && distancias[i]<=min) {
				min = distancias[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
```
Depois de descobrir o próximo vértice, entramos no segundo passo do loop, vamos atualizar as distancias da origem até os vértices que ainda não foram visitados, atualizar os pais desses vértices e incluir o atual no caminho impresso. Isso só pode acontecer se o valor da aresta de atual para o vértice j não for zero, se o vértice j não está includo no caminho (!visitados[j]), se a distância da origem para atual não for infinita e se novaDistancia(distância de atual até o vértice j mais a distância da origem até atual) for menor que a distancia da origem até o vértice j. Uma dúvida que tivemos ao estudar o algoritmo foi: como vamos entrar nesse if, se inicializamos todas as distancias com infinito? Basta lembrar que também atribuimos a distancia da origem até ela mesmo como zero, entao ao entrar pela primeira vez no if, todas os outros vertices que estao conectados a origem, tem suas distancias atualizadas pra distancias menores que o infinito.

```java
        for(int j = 0; j<vertices;j++){ 
			int aresta = grafo[atual][j];
			if(aresta!=0 && !visitados[j] && distancias[atual]!=infinito ){ 
				int novaDistancia = distancias[atual]+aresta;
				if(novaDistancia < distancias[j] ){
					distancias[j] = novaDistancia;
					pais[j] = atual;
				}
			}
````
Finalmente, inicializamos o array menor_caminho, que conterá as distancias e os pais que formam o menor caminho, e retornamos ele. 

```java
		int[][] menor_caminho = new int[2][];
		menor_caminho[0] = distancias;
		menor_caminho[1] = pais;
		return menor_caminho;
```

Porém perceba que podemos estar passando pelo grafo inteiro, mesmo já tendo encontrado as distancias minimas entre os vértices e a origem, já que estamos sempre repetindo distanciaMinima(int[] distancias, int[] visitados) várias vezes para encontrar o próximo vértice a ser incluído no caminho. Como poderíamos melhorar isso? Como fazer a segunda parte do loop apenas com vértices que podem estar no caminho, ignorando os que não vão ser incluidos no caminho? Usando uma fila de prioridade(PriorityQueue) que guardará apenas os vértices que podem ser incluídos no caminho .

**Dijkstra com fila de prioridade**
A assinatura dessa implementação é quase igual a outra: `java public int[][] dijkstra_comFila(ArrayList<ArrayList<Integer>> lista, int raiz)`, assim como o início do código. Vamos testar a origem, inicializar vertices, distancias, visitados e pais e declarar a distância entre a origem e ela como zero.

```java
       if(raiz<0 || raiz>grafo.length) throw new IndexOutOfBoundsException("Origem inválida");
       int vertices = grafo.length;
     	int[] distancias = new int[vertices];
   		boolean[] visitados = new boolean[vertices];
   		int[] pais = new int[vertices];
			for(int i =0; i<vertices; i++) {
			distancias[i] = infinito;
			pais[i] = -1;
		}   		
		distancias[raiz] = 0;
```   

Só que vamos inicializar uma nova variavel fila, que será uma fila de prioridade que organizará os vértices inseridos nela com base na distancia deles até a origem/raiz, quem tem a menor distancia fica na frente. E vamos adicionar a fila a origem, já que ela é indispensavel nos caminhos que saem dela.

```java
  		PriorityQueue<Integer> fila = new PriorityQueue<>(Comparator.comparingInt(i -> distancias[i]));
       fila.add(raiz);
```
Vamos então entrar em um loop que só vai parar quando a fila estiver vazia, `java while (!fila.isEmpty())`. Dentro do loop, tiramos o primeiro vértice da fila e chamamos ele de atual, se atual já estiver incluido no caminho vamos pular os proximos passos  o loop e voltar ao inicio do loop, se não foi incluido, vamos inclui-lo marcando ele como visitado e seguimos para a segunda parte do loop.

```java
       int atual = fila.poll();

 		if (visitados[atual]) continue;
       visitados[atual] = true;
```
A segunda parte do loop é a mesma que a do algoritmo sem fila, vamos entrar em um loop finito e atualizar as distâncias dos vértices não visitados, adicionando a elas o valor da aresta de atual até eles, e atualizar os pais deles, se as condições já explicadas forem atendidas, com um passo a mais: adicionar os vértices que tiveram suas distâncias e pais atualizados na fila de prioridade. Desse modo, a fila só ficará vazia se todas as distancias até os vertices estiverem as menores possíveis.

```java
       for (int j = 0; j < vertices; j++) {
            int aresta = grafo[atual][j];
            if (aresta > 0 && !visitados[j]) {
                int novaDistancia = distancias[atual] + aresta;
                if (novaDistancia < distancias[j]) {
                    distancias[j] = novaDistancia;
                    pais[j] = atual;
                    fila.add(j); 
                }
            }
       }
```
E de novo montamos o array menor_caminho com as menores distancias e os vértices necessários para montar os menores caminhos.

```java
        int[][] menor_caminho = new int[2][];
		menor_caminho[0] = distancias;
		menor_caminho[1] = pais;
		return menor_caminho;  
```

## Custo e desempenho

O algoritmo de Dijkstra sem fila de prioridade tem dentro de um loop de custo O(V), onde V é o número de vertices, outros dois custos lineares, distanciaMinima(int[] distancias, int[] visitados) tem custo O(V) e a segunda parte do loop também, o que totaliza um custo de O(2V²), o que torna essa implementação O(V²), custo bastante alto. Porém, se o número de arestas se aproxima de V², então o custo se torna aceitavel. Logo, a implementação de Dijkstra sem fila de prioridade é mais recomendada para grafos mais densos, com mais conecções entre seus vértices.

Já a implementação com fila de prioridade tem um loop que depende do tamanho da fila, que pode ser no máximo V, onde V é o numero de vertices. Temos que manter em mente que, devido a PriorityQueue ser uma implementação de Heap, o custo de inserções e de remoções é O(logN), nesse caso O(logV). Dentro do loop while acontece primeiro as remoções, O(logV), e depois um loop O(v) com as inserções, ou seja, O(VlogV + VlogV) é o custo aparente desse loop externo. Entretanto, as adições dependem mais do número A de arestas do que do número de vertices, ja que não ocorre inserção na fila caso a aresta seja nula, então o custo fica O(VlogV + AlogV)= O((V+A)logV). Portanto, essa implementação é mais eficiente que a sem fila se (V+A)logV<V. Além disso o custo diminui para O(VlogV) se o número de arestas for consideravelmente menor que o número de vértices, ou seja, se o grafo é esparso, com poucas conecções entre os vértices.

Conclusão:
1. Algoritmo de Dijkstra sem fila de prioridade tem custo O(V²) e é mais recomendado para grafos densos.
2. Algoritmo de Dijkstra com fila de prioridade é mais eficiente e tem custo O((V+A)logV) e é mais indicado pra grafos esparsos.

---
###### Fontes

An Interview with Edsger W. Dijkstra — Entrevista com o criador do algoritmo, oferecendo insights sobre seu raciocínio; site:https://cacm.acm.org/news/an-interview-with-edsger-w-dijkstra/

Algoritmos para Grafos: Algoritmo de Dijkstra — Material formal e detalhado com explicações teóricas, pseudocódigo e análise de complexidade; site:https://www.ime.usp.br/~pf/algoritmos_para_grafos/aulas/dijkstra.html

Algoritmo de Dijkstra: Entendendo o Caminho Mínimo em Grafos Ponderados — Explicação clara e prática, voltada a quem busca entender a lógica e aplicação real do algoritmo; site:https://elemarjr.com/clube-de-estudos/artigos/algoritmo-de-dijkstra-entendendo-o-caminho-minimo-em-grafos-ponderados/

Algoritmo de Caminho de Custo Mínimo de Dijkstra – Uma Introdução Detalhada e Visual — Abordagem passo a passo com diagramas e exemplos visuais; site:https://www.freecodecamp.org/portuguese/news/algoritmo-de-caminho-de-custo-minimo-de-dijkstra-uma-introducao-detalhada-e-visual/ 
