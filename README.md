# Análise Comparativa de Algoritmos em Grafos

Este projeto contém implementações de cinco algoritmos de grafos e um sistema integrado para benchmarking e visualização dos resultados.

## Índice
- [Análise Comparativa de Algoritmos em Grafos](#análise-comparativa-de-algoritmos-em-grafos)
  - [Índice](#índice)
  - [Algoritmos Implementados](#algoritmos-implementados)
  - [Estrutura do Projeto](#estrutura-do-projeto)
  - [Configuração Inicial](#configuração-inicial)
    - [Requisitos de Sistema](#requisitos-de-sistema)
    - [Clonando o Repositório](#clonando-o-repositório)
  - [Executando Testes](#executando-testes)
    - [Executar todos os testes](#executar-todos-os-testes)
    - [Executar testes de uma classe específica](#executar-testes-de-uma-classe-específica)
  - [Executando Benchmarks](#executando-benchmarks)
  - [Visualização dos Resultados](#visualização-dos-resultados)
    - [Análises Disponíveis](#análises-disponíveis)
  - [Licença](#licença)

## Algoritmos Implementados

1. **Ordenação Topológica com DFS**: Ordenação dos vértices em um grafo direcionado acíclico.
2. **Algoritmo de Dijkstra**
3. **Busca em Largura (BFS)**:
4. **Union-Find (DSU)**
5. **Floyd-Warshall**

## Estrutura do Projeto
Este trabalho, desenvolvido para a disciplina de Laboratório de Estrutura de Dados, tem como objetivo implementar e analisar o desempenho de cinco algoritmos clássicos em grafos. Buscamos compreender suas características, limitações e cenários de aplicação ideais, mensurando sua performance em tempo de execução e uso de memória através de benchmarks com diferentes cargas de dados (tamanho e densidade dos grafos). (rever)

```
.
├── src/
│   ├── main/java/        # Implementações dos algoritmos
│   ├── test/java/        # Testes unitários
│   └── jmh/java/         # Benchmarks JMH
├── documentation/        # Documentação detalhada de cada algoritmo
├── benchmark.sh          # Script unificado para todos os benchmarks
└── *-resultados.html     # Visualizações geradas pelos benchmarks
```

## Configuração Inicial

### Requisitos de Sistema
- JDK 11 ou superior
- Gradle 7.0+ (ou use o wrapper incluído: `./gradlew`)
- Git

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

O sistema de benchmark foi simplificado em um único script que executa os testes e gera visualizações HTML dos resultados.

```bash
# Tornar o script executável
chmod +x benchmark.sh

# Executar o benchmark para um algoritmo específico
./benchmark.sh --toposort    # Ordenação Topológica
./benchmark.sh --dijkstra    # Algoritmo de Dijkstra
./benchmark.sh --bfs         # Busca em Largura (BFS)
./benchmark.sh --dsu         # Union-Find (DSU)
./benchmark.sh --floyd       # Floyd-Warshall

# Executar o benchmark em modo rápido (menos iterações)
./benchmark.sh --toposort --quick

# Executar todos os benchmarks sequencialmente
./benchmark.sh --all

# Executar todos em modo rápido
./benchmark.sh --all --quick

# Ver resultados existentes sem executar benchmarks
./benchmark.sh --results

# Ver ajuda
./benchmark.sh --help
```

## Visualização dos Resultados

Após a execução, os resultados serão salvos em arquivos JSON e visualizações HTML serão geradas:

- `toposort-resultados.html`: Resultados de Ordenação Topológica
- `dijkstra-resultados.html`: Resultados de Dijkstra
- `bfs-resultados.html`: Resultados de Busca em Largura
- `dsu-resultados.html`: Resultados de Union-Find
- `floyd-resultados.html`: Resultados de Floyd-Warshall

Abra estes arquivos HTML em um navegador para visualizar tabelas de resultados e gráficos comparativos.

### Análises Disponíveis

As visualizações incluem diversos tipos de análise:

1. **Tempo de execução por número de vértices**: Como o algoritmo se comporta com o crescimento do número de vértices.
2. **Tempo de execução por densidade do grafo**: Como o algoritmo se comporta com grafos mais densos ou esparsos.
3. **Comparação entre algoritmos**: Para algoritmos com diferentes implementações ou variantes.


## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

