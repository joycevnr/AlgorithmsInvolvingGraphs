# Análise Comparativa de Algoritmos em Grafos

## Índices

1.  [Introdução](#introdução)
    -   Visão geral e objetivo do projeto.
2.  [Estrutura do Projeto](#estrutura-do-projeto)
    -   Organização de arquivos e pacotes.
    -   Algoritmos e responsáveis.
3.  [Geração de Dados](#geração-de-dados)
    -   Metodologia para criação dos grafos de teste.
4.  [Análise de Desempenho](#análise-de-desempenho)
    -   [Algoritmo de Dijkstra](#análise-dijkstra)
    -   [Busca em Largura (BFS)](#análise-bfs)
    -   [Ordenação Topológica (DFS)](#análise-ordenação-topológica)
    -   [Disjoint Set Union (DSU)](#análise-dsu)
    -   [Algoritmo de Floyd-Warshall](#análise-floyd-warshall)
    -   [Conclusão da Análise](#conclusão-da-análise)
5.  [Como Executar o Projeto](#como-executar-o-projeto)
6.  [Colaboradores](#colaboradores)

---

## Introdução

--- criar introdução.

Este trabalho, desenvolvido para a disciplina de Laboratório de Estrutura de Dados, tem como objetivo implementar e analisar o desempenho de cinco algoritmos clássicos em grafos. Buscamos compreender suas características, limitações e cenários de aplicação ideais, mensurando sua performance em tempo de execução e uso de memória através de benchmarks com diferentes cargas de dados (tamanho e densidade dos grafos). (rever)

---

## Estrutura do Projeto

O projeto foi organizado em uma estrutura monolítica com Gradle, centralizando todo o código-fonte na pasta `src` e separando os diferentes componentes em pacotes Java. Pastas adicionais foram criadas para organizar a documentação, os dados de teste e os resultados dos benchmarks.

#### Estrutura de Arquivos
```
AlgorithmsInvolvingGraphs/
│
├── build.gradle              
├── settings.gradle             
├── README.md                    
│
├── documentation/            
├── data-generator/              # Scripts para criar os grafos de teste
├── results/                     # Resultados dos benchmarks (CSVs, gráficos)
│
└── src/
    ├── main/java/br/ufcg/computacao/
    │   ├── graph/                 # Estrutura de Grafo, comum a todos
    │   ├── dijkstra/              # (Maria Eduarda)
    │   ├── bfs/                   # (Gustavo)
    │   ├── toposort/              # (Joyce)
    │   ├── dsu/                   # (Augusto)
    │   ├── floydwarshall/         # (Gleydson)
    │   └── benchmark/             # Código para executar os testes de performance
    │
    └── test/java/br/ufcg/computacao/
        └── ...                    # Testes unitários para cada algoritmo
```

#### Algoritmos e Responsáveis

| Algoritmo | Responsável |
| :--- | :--- |
| **Dijkstra** (Menor Caminho) | Maria Eduarda |
| **Busca em Largura (BFS)** | Gustavo |
| **Ordenação Topológica (DFS)** | Joyce |
| **Disjoint Set Union (DSU)** | Augusto |
| **Algoritmo de Floyd-Warshall** | Gleydson |

---

## Geração de Dados


---

## Análise de Desempenho

Nesta seção, analisamos o comportamento de cada algoritmo ao ser submetido a grafos de tamanhos crescentes. As métricas foram coletadas através do módulo `benchmark` e os gráficos foram gerados para visualização.

### Análise: Algoritmo de Dijkstra
*( gráfico de desempenho do Dijkstra)*

**Discussão:**


### Análise: Busca em Largura (BFS)
*(gráfico de desempenho do BFS)*

**Discussão:**


### Análise: Ordenação Topológica
*(gráfico de desempenho da Ordenação Topológica)*

**Discussão:**


.....

### Análise: Disjoint Set Union (DSU)
...

### Análise: Algoritmo de Floyd-Warshall
...

### Conclusão da Análise
----

---

## Como Executar o Projeto

**Pré-requisitos:** JDK 17+ e Git instalados. O Gradle Wrapper está incluído no projeto.

**1. Clonar o Repositório:**
```bash
git clone [https://github.com/joycevnr/AlgorithmsInvolvingGraphs.git](https://github.com/joycevnr/AlgorithmsInvolvingGraphs.git)
cd AlgorithmsInvolvingGraphs
```

**2. Executar os Testes Unitários:**
Para validar a corretude de um algoritmo específico (ex: Ordenação Topológica):
```bash
./gradlew test --tests "*OrdenacaoTopologicaTest"
```

**3. Executar o Benchmark de Performance:**
Para rodar a análise de desempenho de todos os algoritmos:
```bash
./gradlew run
```

---

## Colaboradores

Este projeto foi desenvolvido por estudantes do curso de Ciência da Computação da Universidade Federal de Campina Grande (UFCG):

