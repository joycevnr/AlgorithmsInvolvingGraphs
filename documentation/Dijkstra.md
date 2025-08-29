#Algoritmo Dijkstra
@autora: Maria Eduarda Capela Cabral Pinheiro da Silva

##Introdução
Nessa documentação está o resultado da pesquisa teórica sobre o Algoritmo de Dijkstra que calcula a menor distância entre um verticede um grafo até outro. Também se encontra a explicação sobre suas implementações, com e sem o uso de uma fila de prioridade, e uma breve discussão sobre seus desempenhos e eficiência.  

###História e motivação
O Algoritmo de Dijkstra, como o nome já indica, foi desenvolvido pelo matématico holandês Edsger W. Dijkstra(1930-2002).De acordo com uma entrevista que ele deu em 2001 para a revista Communications of the ACM, a história por trás do algritmo começa com a inauguração do computaor ARMAC em 1956, quando Dijkstra precisava achar um problema que pudesse mostrar a utilização prática do computador. O problema deveria demostrar o uso matématico do ARMAC, mas também tinha que ser simples o suficiente para que pessoas que não trabalhassem com matématica pudessem entender o problema e a solução. Com base nessas restrições, Dijkstra escolheu um problema prático e fácil de entender: Escolhendo duas, de 64 cidades na Holanda, qual é o menor caminho entre elas?.Mas o algoritmo só foi escrito durante uma viagem com sua noiva em Amsterdã, quando pararam pra tomar um café, ele desenvolveu um algoritmo que recebendo um grafo, conjunto de vértices/nós conectados entre si por arestas com valores positivos, retornaria o menor caminho entre um ponto e outro, em apenas **20 minutos**. Por não ter acesso a papel e lapis no momento, Dijkstra disse que teve que forçar sua mente a evitar complicações desnecessárias. O Algoritmo de Dijkstra só foi publicado em 1959, três anos depois, porém é usado até hoje pra diversos problemas de logística e é a base para diversos sistemas de navegação como o GPS. Então por que aprender esse algoritmo? As suas aplicações são enumeras, mas se baseiam em procurar a rota mais curta, mais barata e mais rapida.
       
####Funcionamento
A entrada padrão recebe um grafo, em formato de matriz de adjacência ou lista de adjacência, junto com a origem, normalmente representada por um inteiro. Pode também receber o destino, também representado por um inteiro, se o objetivo for receber só a distancia mínima entre a origem e o destino. Nossas implementações receberam a origem(chamaremos de raiz) e uma lista de adjacência. Usaremos lista de adjacência pra representar os grafos, para podermos observar o peso entre as arestas que conectam os vertices. A lista de adjacência consiste em uma lista I de listas, cada lista J dentro dela representa uma lista de arestas que conectam o vertice I aos outros vertices cujos indices são iguais aos contidos na lista J. O valor 0(zero) significa que não a aresta que conecta o vertice I ao vertice J.

EX:    GRAFO           LISTA:     (0)(1)(2)(3)   ARESTA(0, 1) = 4 --> lista.get(0).get(1)==4
       1     2               (0) {{0, 4, 1, 0}
   (0)---(2)---(3)           (1)  {4, 0, 3, 2}
     \    |    /             (2)  {1, 3, 0, 2}
      \   |3  /              (3)  {0, 2, 2, 0}}
      4\  |  /2    
        \ | /
         \|/
         (1)

Já o retorno da nossa implementação será um array e inteiro contendo dois arrays também de inteiro: O primeiro representa as menores distancia entre o vertice da origem e o vertice I, sendo I um indice do array de distancias, o segundo representa o array de "pais", vertice responsavel por chegar no vertice J, sendo J um indice do array de pais. Implementamos também um método extra printCaminho(int[] pais, int destino) que usa o array de pais resultante do Algoritmo de Dijkstra para mostrar visualmente o menor caminho entre a origem e o destino. 

**Dijkstra sem fila de prioridade**
Vamos começar a explicar o passo a passo do funcionamento de ```java public int[][] menor_caminho_semFila(ArrayList<ArrayList<Integer>> lista, int raiz)```, nossa implementação inicial do Algoritmo de Dijkstra sem usar fila de prioridade.
Primeiro testamos se a raiz/origem que é fornecida é valida, ou seja, não é um numero negativo e esta representado pelo grafo, depois atribuimos a variavel vertices com o tamanho da lista, que é igual ao numero total e vertices.
```java 
        if(raiz<0 || raiz>lista.size()) throw new IndexOutOfBoundsException("Origem inválida");
		int vertices = lista.size();
```
O proximo passo é inicializar os arrays de distancias minimas, chamaremos de distancias, de vertices que já incluimos no caminho, chamaremos de visitados, e de vertices que usaremos pra montar o caminho, chamaremos de pais. Já que estamos procurando a menor distancia entre os vertices e a origem, podemos supor que o oposto disso, a distancia entre um vertice não conectado ou a distancia desconhecida, é o infinito, que representamos com uma constante ```java final int infinito = Integer.MAX_VALUE```. Também inicializamos a distancia da raiz ate ela mesma como 0(zero);
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
Agora entramos em um loop finito ```java for(int i =0; i<vertices-1; i++)``` que vai passar pela lista inteira e executará dois passo. O primeiro séra procurar o vertice mais perto do vertice anterior,ou seja, a soma da aresta que liga os dois com a distancia ja percorrida aumenta menos a distancia total, começando pela origem, por meio no metodo privado distanciaMinima(int[] distancias, int[] visitados) e depois marcar esse vertice como visitado.
```java
            int proximoVertice = distanciaMinima(distancias, visitados);
			visitados[proximoVertice]=true;
```
O modo que o metodo distanciaMinima(int[] distancias, int[] visitados) funciona é simples. Assumindo a distancia minima como infinito e que o proximo vertice, que chamaremos de minIndex, não existe(é -1), vamos passar pelo array de distancias, que tem tamanho igual ao número de vertices, vendo se o vertice i ja foi incluido no caminho e se a distancia da origem até ele é menor que a distancia minima(min), se sim atualizamos a distancia minima e o minIndex. Quando acabarmos o loop retornamos o minIndex, o proximo vertice com menor distancia para adicionar no caminho.
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
Depois e descobrir o proximo vértice, entramos no segundo passo do loop, vamos atualizar as distancias da origem até os vertices não visitados, somando a elas a aresta que liga os vertices não visitados com o proximoVertice com a distancia ja percorrida(novaDistancia), e atualizar os pais dos vertices, para incluir o proximoVertice no caminho impresso. Isso só pode acontecer se o valor da aresta do proximoVertice para os outros vertices não for nula, se esses outros vertices não estçao ja incluidos no caminho(!visitados[j]) para não andarmos em circulos, se a novaDistancia for menor que a distancia que ja temos para o vertice j, para não aumentarmos a distancia total desnecessariamente, e se a distancia da origem pro proximoVertice não for infinita. Uma dúvida que tivemos ao estudar o algoritmo foi: como vamos entrar nesse if, se inicializamos todas as distancias com infinito? Basta lembrar que também atribuimos a distancia da origem até ela mesmo como zero, entao ao entrar pela primeira vez no if, todas os outros vertices que estao conectados a origem, tem suas distancias atualizadas pra distancias menores que o infinito.
```java
       for(int j = 0; j<vertices;j++) {
			int aresta = lista.get(proximoVertice).get(j);
			if(aresta!=0 && !visitados[j] && distancias[proximoVertice]!=infinito ) {
				int novaDistancia = distancias[proximoVertice]+aresta;
				if(novaDistancia < distancias[j] ){
					distancias[j] = novaDistancia;
					pais[j] = proximoVertice;
				}	
			}
		}
```
Finalmente, inicializamos o array  menor_caminho, que conterá as distancias e os pais que formaram o menor caminho, e retornamos ele. Porém perceba que podemos estar passando pelo grafo inteiro, mesmo já tendo encontrado as distancias minimas entre os vertices e a origem, ja que estamos sempre repetindo distanciaMinima(int[] distancias, int[] visitados) varias vezes para encontrar o proximo vertice a ser incluido no caminho. Como poderiamos melhorar isso? Como podemos ignorar vertices que tem arestas muito grandes e que não vão ser incluidos no caminho? Usando uma fila de prioridade(PriorityQueue).

**Dijkstra com fila de prioridade**
A assinatura dessa implementação é quase igual a outra: ```java public int[][] menor_caminho_comFila(ArrayList<ArrayList<Integer>> lista, int raiz)```, assim como o inicio o codigo. Vamos testar a origem, inicializar vertices, distancias, visitados e pais e a distancia entre a origem e ela mesma será zero.
```java
        if(raiz<0 || raiz>lista.size()) throw new IndexOutOfBoundsException("Origem inválida");
        int vertices = lista.size();
     	int[] distancias = new int[vertices];
   		boolean[] visitados = new boolean[vertices];
   		int[] pais = new int[vertices];
		
		for(int i =0; i<vertices; i++) {
			distancias[i] = infinito;
			pais[i] = -1;
		}   		
		
		distancias[raiz] = 0;
```   
Só que vamos inicializar uma nova variavel fila, que será uma fila de prioridade que organizará os vertices inseridos nela com base na distancia deles até a raiz, quem tem a menor distancia fica na frente. E vamos adicionar a fila a origem, já que ela é indispensavel nos caminhos que saem dela.
```java
        PriorityQueue<Integer> fila = new PriorityQueue<>(Comparator.comparingInt(i -> distancias[i]));
        fila.add(raiz);
```
Vamos então entrar em um loop que só vai parar quando a fila estiver vazia, ```java while (!fila.isEmpty())```. Dentro do loop, tiramos o primeiro vertice da fila e chamamos ele de atual, se atual ja estiver incluido no caminho vamos pular os proximos passos  o loop e voltar ao inicio do loop, se não foi incluido vamos inclui-lo marcando ele como visitado e seguimos pra segunda parte do loop.
```java
       int atual = fila.poll();

       if (visitados[atual]) continue;
       visitados[atual] = true;
```
A segunda parte do loop é a mesma que a do algoritmo sem fila que fizemos com proximoVertice faremos com atual, vamos entrar em um loop finito e atualizar os pais e as distancias dos vertices não visitados adicionando a elas o valor da aresta de atual até eles, se as condições ja explicadas forem atendidas, com um passo a mais: adicionar os vertices que tiveram suas distancias e pais atualizados na fila de prioridade. Desse modo, a fila só ficará vazia se todas as distancias até os vertices estiverem as menores possiveis e não fizemos calculos desnecessarios de vertices que só iriam aumentar o caminho sem necessidade.
```java
       for (int j = 0; j < vertices; j++) {
            int aresta = lista.get(atual).get(j);
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
E de novo montamos o array menor_caminho com as menores distancias e os vertices necessarios pra montar os caminho.
```java
        int[][] menor_caminho = new int[2][];
		menor_caminho[0] = distancias;
		menor_caminho[1] = pais;
		return menor_caminho;  
```

#####Custo e desempenho

O algoritmo de Dijkstra sem fila de prioridade tem dentro de um loop de custo O(V), onde V é o número de vertices, outros dois custos lineares, distanciaMinima(int[] distancias, int[] visitados) tem custo O(V) e a segunda parte do loop também, o que totaliza um custo de O(2V²), o que torna essa implementação O(V²), custo bastante alto. Porém, se o numero de arestas se aproxima de V², então o custo se torna aceitavel. Logo, a implementação de Dijkstra sem fila de prioridade é mais recomendada para grafos mais ensos, com mais conecções entre seus vertices.
Já a implementação com fila de prioridade tem um loop que depende do tamanho da fila, que pode ser no maximo V, onde V é o numero de vertices. Temos que manter em mente que, devido a PriorityQueue ser uma implementação de Heap, o custo de inserções e de remoções é O(logN), nesse caso O(logV). Dentro do loop while acontece primeiro as remoções, O(logV), e depois um loop O(v) com as inserções, ou seja, O(VlogV + VlogV) é o custo aparente desse loop externo. Entretanto, as adições dependem mais do numero A de arestas o que do numero de vertice, ja que nao ocorre inserção na fila caso a aresta seja nula, então o custo fica O(VlogV + AlogV)= O((V+A)logV). Portanto, essa implementação é mais eficiente que a sem fila se (V+A)logV<V²,além disso o custo diminui pra VlogV se o numero de arestas for consideravelmente menor que o numero e vertices, ou seja, se o grafo é esparso, com poucas conecções entre os vertices.
Conclusão:
1. Algoritmo de Dijkstra sem fila de prioridade tem custo O(V²) e é mais recomendado para grafos densos.
2. Algoritmo de Dijkstra com fila de prioridade é mais eficiente e tem custo O((V+A)logV) e é mais indicado pra grafos esparsos.

---
######Fontes
An Interview with Edsger W. Dijkstra — Entrevista com o criador do algoritmo, oferecendo insights sobre seu raciocínio; site:https://cacm.acm.org/news/an-interview-with-edsger-w-dijkstra/
Algoritmos para Grafos: Algoritmo de Dijkstra — Material formal e detalhado com explicações teóricas, pseudocódigo e análise de complexidade; site:https://www.ime.usp.br/~pf/algoritmos_para_grafos/aulas/dijkstra.html
Algoritmo de Dijkstra: Entendendo o Caminho Mínimo em Grafos Ponderados — Explicação clara e prática, voltada a quem busca entender a lógica e aplicação real do algoritmo; site:https://elemarjr.com/clube-de-estudos/artigos/algoritmo-de-dijkstra-entendendo-o-caminho-minimo-em-grafos-ponderados/
Algoritmo de Caminho de Custo Mínimo de Dijkstra – Uma Introdução Detalhada e Visual — Abordagem passo a passo com diagramas e exemplos visuais; site:https://www.freecodecamp.org/portuguese/news/algoritmo-de-caminho-de-custo-minimo-de-dijkstra-uma-introducao-detalhada-e-visual/ 
