**Busca em Largura (BFS)**

**Contextualização**

A Busca em Largura (Breadth-First Search — BFS) é um dos algoritmos fundamentais em teoria dos grafos.
Ele percorre os vértices em camadas, a partir de um vértice origem, descobrindo todos os vértices a distância 1, depois a distância 2, e assim por diante.
Esse algoritmo é essencial para resolver problemas de alcance mínimo (menor número de passos ou arestas) e tem aplicações em redes, jogos, navegação, inteligência artificial e análise de redes sociais.O BFS foi formalizado por E. F. Moore em 1959 e é considerado o algoritmo base para encontrar caminhos mínimos em grafos não ponderados.

**O Problema**

Dado um grafo direcionado ou não-direcionado e um vértice de origem s, o desafio é:
    1. Determinar a ordem de visita dos vértices.
    2. Calcular as distâncias mínimas (em número de arestas) de s até todos os outros vértices alcançáveis.
    3. Construir o vetor de predecessores, que permite reconstruir caminhos mínimos.
    4. Detectar vértices inalcançáveis a partir da origem (distância = -1).

**O Algoritmo Passo a Passo**

A implementação usa uma fila (Queue) para explorar vértices em camadas.
Estruturas auxiliares

    -> distancia[]: armazena a distância mínima da origem até cada vértice.
    -> predecessor[]: guarda o vértice anterior no caminho mínimo.
    -> visitado[]: marca os vértices já descobertos.
    -> fila: garante a ordem de exploração (FIFO).

**Fluxo**

1. Inicializa distancia[v] = -1 e predecessor[v] = -1 para todos os vértices.
2. Define distancia[origem] = 0 e insere a origem na fila.
3. Enquanto a fila não estiver vazia:
    * Remove o próximo vértice u.
    * Para cada vizinho v de u, se não foi visitado:
        * Define distancia[v] = distancia[u] + 1.
        * Define predecessor[v] = u.
        * Marca v como visitado e insere na fila.
4. Ao final, todos os vértices alcançáveis foram visitados.

**Análise de Complexidade**

Tempo: $O(V + E)$, pois cada vértice e aresta é explorado no máximo uma vez.
Espaço: $O(V + E)$, para armazenar fila, distâncias, predecessores e listas de adjacência.

**Benchmark de Desempenho**

A eficiência do BFS foi validada com benchmarks usando JMH.
Foram gerados grafos direcionados acíclicos (DAGs) de diferentes tamanhos e densidades.

Nota: No benchmark, os grafos são gerados como DAGs (arestas u → v com u < v), seguindo o mesmo padrão do benchmark de Ordenação Topológica, para manter a consistência entre os testes do projeto.

**Resultados Experimentais (valores aproximados)**

Configurações:

* Número de vértices: 100, 500, 1.000, 5.000 e 10.000
* Densidades: 0.1, 0.3, 0.5
* Métrica: Tempo médio por operação (ms/op)
* Ambiente: JMH 1.36, JDK 21

**Densidade	 Vértices	  Tempo (ms/op)	  Crescimento**
    0.1	      100	        0.001	        Base
    0.1	      500	        0.010           10x
    0.1	      1.000	        0.025	        2.5x
    0.1	      5.000	        0.350	        14x
    0.1	      10.000	    1.500	        4.2x
    0.3	      100	        0.002	        Base
    0.3	      500	        0.020	        10x
    0.3	      1.000	        0.050	        2.5x
    0.3	      5.000	        0.600	        12x 
    0.3	      10.000	    2.800	        4.7x
    0.5	      100	        0.003	        Base
    0.5	      500	        0.030	        10x
    0.5	      1.000	        0.080	        2.7x
    0.5	      5.000	        1.200	        15x
    0.5	      10.000	    4.500	        3.7x

**Análise**

* BFS confirma a complexidade O(V + E).
* Em grafos esparsos, o tempo cresce suavemente até 10.000 vértices (~1.5ms).
* Em grafos mais densos, o crescimento continua próximo ao linear.
* Excelente escalabilidade, validando sua aplicação em cenários reais.

**Aplicações**

* Redes de comunicação: determinar caminhos mínimos sem pesos.
* Navegação em mapas/jogos: encontrar o menor número de movimentos.
* Análise de redes sociais: calcular níveis de conexão entre usuários.
* IA e robótica: busca em grafos de estados para planejamento de ações.

**Contribuições**

Autor: Gustavo Luiz Ferreira de Souza

**Bibliografia**
    
CORMEN, T. H.; LEISERSON, C. E.; RIVEST, R. L.; STEIN, C. Introduction to Algorithms. 3rd ed. MIT Press, 2009.
FEOFILOFF, P.; KOHAYAKAWA, Y.; WAKABAYASHI, Y. Uma Introdução Sucinta à Teoria dos Algoritmos. IME-USP, 2011.
SZWARCFITER, J. L.; MARKENZON, L. Estruturas de Dados e seus Algoritmos. LTC, 3ª ed., 2010.
