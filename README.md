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
  - [Visualização dos Resultados](#visualização-dos-resultados-1)
    - [Arquivos Gerados](#arquivos-gerados)
  - [Licença](#licença)

## Algoritmos Implementados

1. **Ordenação Topológica com DFS** : Ordenação dos vértices em um grafo direcionado acíclico.
2. **Algoritmo de Dijkstra** 
3. **Union-Find (DSU)** 
4. **Floyd-Warshall** 
5. **Busca em Largura (BFS)** 

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

**Algoritmos:**
```bash
./benchmark.sh --toposort     # Ordenação Topológica
./benchmark.sh --dijkstra     # Dijkstra
./benchmark.sh --bfs   
./benchmark.sh --dsu          # Union-Find (DSU)
./benchmark.sh --floyd        # Floyd-Warshall

# Modo rápido (menos iterações)
./benchmark.sh --toposort --quick

# Ver ajuda
./benchmark.sh --help
```

### Visualização dos Resultados

Após executar o benchmark, o próprio script fornece instruções claras de como visualizar os gráficos interativos.

## Visualização dos Resultados

O sistema gera automaticamente visualizações HTML interativas com:

### Arquivos Gerados
- `ALGORITMO-resultados.html`: Visualização completa da Ordenação Topológica
- `ALGORITMO-benchmark.json`: Dados brutos do JMH para análises customizadas

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

