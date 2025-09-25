Algoritmo BFS (Busca em Largura)

@autor: Gustavo Luiz Ferreira de Souza


Introdução

Esta documentação apresenta a pesquisa teórica e prática sobre o Algoritmo de Busca em Largura (Breadth-First Search — BFS) 
aplicado a grafos direcionados e não-direcionados. O objetivo é calcular:

1. A ordem de visita dos vértices;
2. As distâncias mínimas em número de arestas a partir de uma origem;
3. O vetor de predecessores, que permite reconstruir caminhos mínimos.

Além da explicação detalhada do funcionamento do algoritmo, este documento descreve a implementação em Java, a execução de 
testes unitários (JUnit 5) e benchmarks para análise de desempenho.


História e motivação

O algoritmo BFS é um dos algoritmos mais fundamentais em teoria dos grafos e estruturas de dados. 
Ele foi formalizado por E. F. Moore em 1959 e rapidamente se tornou um dos métodos básicos para percorrer grafos. 
Sua motivação central era resolver problemas de alcance mínimo (menor número de passos ou movimentos), como em autômatos finitos 
ou redes de comunicação. O BFS é considerado um algoritmo não ponderado de caminho mínimo, pois garante encontrar a menor 
distância em termos de número de arestas entre um vértice origem e todos os outros alcançáveis. 
Diferente de algoritmos como Dijkstra, que lida com pesos nas arestas, o BFS trabalha em cenários onde todas as arestas 
têm custo uniforme. Sua aplicação é ampla, desde sistemas de navegação, jogos, roteamento em redes, análise de redes sociais, 
até problemas de inteligência artificial (como busca em grafos de estados).


Funcionamento

O BFS recebe como entrada um grafo 𝐺 = (𝑉,𝐸), um vértice de origem (inteiro entre 0 e n−1, ou 1..n no modo --oneBased) e percorre 
os vértices em camadas: primeiro visita todos os vértices a distância 1, depois distância 2, e assim por diante.

A implementação neste projeto está organizada na estrutura:

src/
 └─ br/
    └─ ufcg/
       └─ computacao/
          ├─ graph/      → Grafo.java
          ├─ bfs/        → BFS.java, BFSTest.java
          └─ benchmark/  → Main.java

API programática
Grafo g = new Grafo(n, /* direcionado? */ false);
BFS bfs = new BFS(g);

bfs.run(origem);

List<Integer> ordem = bfs.getOrdemVisita();
int[] dist = bfs.getDistancias();
int[] pred = bfs.getPredecessores();

Métodos didáticos
List<Integer> visita = bfs.visitar(origem);
int[] d = bfs.distancias(origem);
List<Integer> caminho = bfs.caminhoMaisCurto(origem, destino);

Atalho estático
int[] d2 = BFS.distances(g, origem);


Vértices fora do intervalo [0..n−1] lançam IllegalArgumentException.

Distância -1 indica vértice inalcançável.

Implementação

Assinatura principal:

public void run(int origem)


O algoritmo segue o seguinte fluxo:
	1. Validação da origem (IllegalArgumentException se inválida).
	2. Inicialização dos vetores:
		- distancias[i] = -1 (não alcançado),
		- pred[i] = -1 (sem predecessor),
		- visitados[i] = false.
		A distância da origem para ela mesma é 0.
	3. Inicialização de uma fila (Queue) com a origem.
	4. Enquanto a fila não estiver vazia:
		- Retira o próximo vértice u;
		- Para cada vizinho v de u, se não foi visitado:
		- Define dist[v] = dist[u] + 1;
		- Define pred[v] = u;
		- Marca visitados[v] = true;
		- Enfileira v.

Exemplo de uso (Benchmark)
Arquivo edges.txt (1-based):
1 2
1 3
2 4
1 5
5 6

Execução no Eclipse → Run Configurations → Arguments:
--n 6 --input edges.txt --oneBased --source 1 --warmup 1 --repeat 3

Saída típica:

==== BFS Benchmark ====
n=6, m=5, directed=false, origin=1
warmup=1, repeat=3
tempo(ms): avg=0.003, min=0.002, max=0.003
memória pico (MB): 1.97

Testes (JUnit 5)
O arquivo BFSTest.java contém testes unitários que validam:
- Distâncias corretas em grafos não-direcionados e direcionados.
- Vértices inalcançáveis (distância = -1).
- Reconstrução de caminho mínimo.
- Ordem determinística de visita.
- Exceções para índices inválidos.
- Método estático BFS.distances.

Para executar:
Clique direito em BFSTest.java → Run As → JUnit Test

Complexidade

Tempo: 𝑂(𝑛+𝑚)O(n+m), onde n é o número de vértices e m o número de arestas.

Espaço: 𝑂(𝑛+𝑚)O(n+m), pois utiliza fila e armazenamento de vizinhos.

Conclusão

BFS garante encontrar a menor distância em número de arestas em grafos não ponderados.

- Sua implementação em Java com fila e listas de adjacência é simples, eficiente e escalável.

- É um dos algoritmos mais indicados para problemas de alcance mínimo, caminhos mais curtos 
em grafos não ponderados e aplicações em redes sociais, navegação e IA.

Fontes
	- Feofiloff, Kohayakawa & Wakabayashi — IME-USP
	- Szwarcfiter & Markenzon — LTC
	- Cormen, Leiserson, Rivest & Stein — MIT Press