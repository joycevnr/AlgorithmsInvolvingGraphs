# Análise Comparativa de Algoritmos em Grafos

Este projeto contém implementações de cinco algoritmos de grafos e um sistema integrado para benchmarking e visualização dos resultados.

## Índice
- [Análise Comparativa de Algoritmos em Grafos](#análise-comparativa-de-algoritmos-em-grafos)
  - [Índice](#índice)
  - [Algoritmos Implementados](#algoritmos-implementados)
  - [Configuração Inicial](#configuração-inicial)
    - [Requisitos de Sistema](#requisitos-de-sistema)
    - [Clonando o Repositório](#clonando-o-repositório)
  - [Executando Testes](#executando-testes)
    - [Executar todos os testes](#executar-todos-os-testes)
    - [Executar testes de uma classe específica](#executar-testes-de-uma-classe-específica)
  - [Executando Benchmarks](#executando-benchmarks)
    - [Visualização dos Resultados](#visualização-dos-resultados)
  - [Visualização dos Resultados](#visualização-dos-resultados-1)
    - [Arquivos Gerados](#arquivos-gerados)
  - [Análises Detalhadas dos Algoritmos](#análises-detalhadas-dos-algoritmos)
    - [1. Ordenação Topológica (DFS)](#1-ordenação-topológica-dfs)
      - [Complexidade Teórica](#complexidade-teórica)
      - [Aplicações Práticas](#aplicações-práticas)
      - [Resultados dos Benchmarks](#resultados-dos-benchmarks)
      - [Análise dos Resultados](#análise-dos-resultados)
    - [2. Algoritmo de Dijkstra e Floyd-Warshall](#2-algoritmo-de-dijkstra-e-floyd-warshall)
    - [3. Algoritmo de Disjoint Set Union (DSU)](#3-algoritmo-de-disjoint-set-union-dsu)
    - [4. Algoritmo de Busca em Largura (BFS)](#4-algoritmo-de-busca-em-largura-bfs)
  - [Licença](#licença)
  - [Autores](#autores)

## Algoritmos Implementados

- **Ordenação Topológica (DFS)**: Ordena vértices em grafo direcionado acíclico
- **Dijkstra**: Menor caminho de origem única  
- **Union-Find (DSU)**: Operações de união e busca
- **Floyd-Warshall**: Menor caminho entre todos os pares
- **BFS**: Busca em largura (em desenvolvimento)

```bash
.
├── src/
│   ├── main/java/        # Implementações dos algoritmos
│   ├── test/java/        # Testes unitários
│   └── jmh/java/         # Benchmarks JMH
├── documentation/        # Documentação detalhada de cada algoritmo
├── run_benchmark.sh      # Script unificado para todos os benchmarks
└── gerar_grafico         # Adição de algoritmos para geração de gráficos
```

## Configuração Inicial

### Requisitos de Sistema
- JDK 11 ou superior
- Gradle 7.0+ (ou use o wrapper incluído: `./gradlew`)
- Git
- Matplotlib

### Clonando o Repositório
```bash
git clone https://github.com/joycevnr/AlgorithmsInvolvingGraphs.git
cd AlgorithmsInvolvingGraphs
```

## Executando Testes

### Executar todos os testes
```bash
./gradlew test
```

### Executar testes de uma classe específica
```bash
./gradlew test --tests "br.ufcg.computacao.toposort.OrdenacaoTopologicaTest"
```

## Executando Benchmarks

**Tornar o arquivo executável:**
```bash
chmod +x run_benchamark.sh
```

**Algoritmos:**
```bash
./run_benchmark.sh --toposort         # Ordenação Topológica
./run_benchmark.sh --bfs   
./run_benchmark.sh --dsu              # Union-Find (DSU)
./run_benchmark.sh --menorcaminho     # Menor Caminho: Dijkstra e Floyd-Warshall

# Modo rápido (menos iterações)
./run_benchmark.sh --toposort --quick

# Ver ajuda
./run_benchmark.sh --help
```

### Visualização dos Resultados

Após executar o benchmark, o próprio script fornece instruções claras de como visualizar os gráficos interativos.

## Visualização dos Resultados

O sistema gera automaticamente visualizações HTML interativas com:

### Arquivos Gerados
- `ALGORITMO-benchmark.json`: Dados brutos do JMH para análises customizadas
- `ALGORITMO-benchmark.png`: Gráficos gerados a partir dos dados do JMH para análises customizadas


## Análises Detalhadas dos Algoritmos

### 1. Ordenação Topológica (DFS)

#### Complexidade Teórica
- **Tempo:** O(V + E) - visita cada vértice e aresta exatamente uma vez
- **Espaço:** O(V) - para arrays de controle e pilha de recursão

#### Aplicações Práticas
- Gerenciamento de projetos (ordem de tarefas com dependências)
- Compiladores (ordem de compilação de módulos)
- Grades curriculares (sequência de disciplinas com pré-requisitos)
- Resolução de dependências (Maven, npm, pip)

#### Resultados dos Benchmarks

| Densidade | Vértices | Tempo (ms/op) | Crescimento vs Densidade 0.1 |
|-----------|----------|---------------|------------------------------|
| **0.1**   | 100      | 0.002         | Base                        |
| **0.1**   | 500      | 0.036         | 18x                         |
| **0.1**   | 1.000    | 0.134         | 3.7x                        |
| **0.1**   | 5.000    | 5.564         | 41.5x                       |
| **0.1**   | 10.000   | 26.137        | 4.7x                        |
| **0.3**   | 100      | 0.004         | 2x                          |
| **0.3**   | 500      | 0.084         | 2.3x                        |
| **0.3**   | 1.000    | 0.342         | 2.6x                        |
| **0.3**   | 5.000    | 16.880        | 3.0x                        |
| **0.3**   | 10.000   | 61.736        | 2.4x                        |
| **0.5**   | 100      | 0.005         | 2.5x                        |
| **0.5**   | 500      | 0.126         | 3.5x                        |
| **0.5**   | 1.000    | 0.490         | 3.7x                        |
| **0.5**   | 5.000    | 26.755        | 4.8x                        |
| **0.5**   | 10.000   | 75.238        | 2.9x                        |

#### Análise dos Resultados
- **Escalabilidade Excelente:** De 0.002ms (100 vértices) até 75ms (10k vértices) - crescimento controlado
- **Validação O(V+E):** Comportamento sub-quadrático confirmado empiricamente em todos os testes
- **Impacto da Densidade:** Densidade 0.5 é 2-5x mais lenta que densidade 0.1, confirmando dependência das arestas
- **Performance Profissional:** 10.000 vértices processados em ~75ms demonstra eficiência para aplicações reais

Para acessoa a documentação detalhada do algoritmo completa [acesse](documentation/toposort/OrdenacaoTopologica.md).

---

### 2. Algoritmo de Dijkstra e Floyd-Warshall 

O algoritmo de Floyd-Warshall é uma solução clássica para o problema de encontrar os caminhos mínimos entre todos os pares de vértices em grafos ponderados. Ele utiliza programação dinâmica e funciona de maneira iterativa, considerando a cada passo um vértice intermediário e atualizando as distâncias entre todos os pares de vértices. Sua complexidade de tempo é O(V³), devido aos três laços aninhados, o que o torna previsível, mas pouco escalável para grafos muito grandes. Em relação ao espaço, consome O(V²), já que precisa armazenar matrizes de distâncias e sucessores. Apesar do custo elevado, o algoritmo é bastante útil em contextos onde é necessário obter informações globais de conectividade, como em sistemas de transporte, análise de acessibilidade em mapas e jogos, além de aplicações de roteamento em redes de comunicação. Já o algoritmo de Dijkstra tem uma ideia parecida com o de Floyd-Warshall, porém, ao invés de calcular a menor distância de todos os vértices, ele calcula apenas a de um vértice origem já determinada. Ele escolhe a cada passo o vértice com menor distância acumulada a partir da origem e atualizando as distâncias dos vizinhos. Seu desempenho varia de acordo com a estrutura de dados utilizada: com matriz de adjacência, a complexidade é O(V^2), adequada para grafos pequenos ou densos; com filas de prioridade, que priorizam vértices com menores distâncias, como heaps binários, a complexidade melhora para O((V+A)log⁡V), o que o torna eficiente em grafos esparsos. Dijkstra é amplamente utilizado em aplicações práticas, como sistemas de navegação e redes de computadores.

Para acessoa a documentação detalhada do algoritmo completa do [Dijkstra](documentation/dijkstra/Dijkstra.md) e do [Dijkstra](documentation/floydwarshall/FloydWarshall.md).

### 3. Algoritmo de Disjoint Set Union (DSU)

O Disjoint Set Union (DSU), também conhecido como Union-Find, é uma estrutura de dados fundamental para lidar com conjuntos disjuntos. Ela permite determinar rapidamente se dois elementos pertencem ao mesmo conjunto e unir conjuntos distintos. A eficiência do DSU depende das heurísticas empregadas: na versão básica, sem otimizações, as operações podem custar até O(n) no pior caso; já com técnicas como Union by Size/Rank e Path Compression, o custo é reduzido para tempo quase constante, com complexidade amortizada de O(log n). Essa eficiência torna o DSU essencial em algoritmos de grafos, como na detecção de ciclos e no algoritmo de Kruskal para árvores geradoras mínimas, além de aplicações em problemas de conectividade dinâmica e redes.

### 4. Algoritmo de Busca em Largura (BFS)

O algoritmo de Busca em Largura (BFS) é uma das soluções fundamentais para o problema de percorrer grafos e encontrar caminhos mínimos em grafos não ponderados. Ele funciona de forma em camadas, visitando primeiro todos os vértices a distância 1 da origem, depois os a distância 2, e assim sucessivamente, garantindo sempre a menor quantidade de arestas até cada vértice alcançável. A implementação utiliza uma fila para gerenciar os vértices a explorar, além de vetores auxiliares para armazenar distâncias, predecessores e a ordem de visita. Sua complexidade de tempo é O(V+A), onde V é o número de vértices e A o número de arestas, sendo eficiente tanto para grafos esparsos quanto para grafos densos. Por isso, a BFS é amplamente utilizada em aplicações práticas como análise de redes sociais, sistemas de roteamento em redes de computadores, jogos de tabuleiro e problemas de inteligência artificial que envolvem busca em grafos de estados.

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.


## Autores


| Nome do Integrante                                | Perfil no GitHub                                   |
| ------------------------------------------------- | -------------------------------------------------- |
| *<ins>-Augusto de Brito Lopes-</ins>* | *<ins>[@AugustoBritoLopes](https://github.com/AugustoBritoLopes)</ins>* |
| *<ins>-Gleydson Fabricio Rodrigues de Moura-</ins>* | *<ins>[@gleydsonfabricio](https://github.com/gleydsonfabricio)</ins>* |
| *<ins>-Gustavo Luiz Ferreira de Souza-</ins>* | *<ins>[@TenGustavo](https://github.com/TenGustavo)</ins>* |
| *<ins>Joyce Vitória Nascimento Rodrigues</ins>* | *<ins>[@joycevnr](https://github.com/joycevnr)</ins>* |
| *<ins>Maria Eduarda Capela Cabral Pinheiro da Silva</ins>* | *<ins>[@Eduarda-Cabral](https://github.com/Eduarda-Cabral)</ins>* |

<br>



