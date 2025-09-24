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
  - [Análises Detalhadas dos Algoritmos](#análises-detalhadas-dos-algoritmos)
    - [1. Ordenação Topológica (DFS)](#1-ordenação-topológica-dfs)
      - [Complexidade Teórica](#complexidade-teórica)
      - [Aplicações Práticas](#aplicações-práticas)
      - [Resultados dos Benchmarks](#resultados-dos-benchmarks)
      - [Análise dos Resultados](#análise-dos-resultados)
    - [2. Algoritmo de Dijkstra](#2-algoritmo-de-dijkstra)
  - [Licença](#licença)

## Algoritmos Implementados

1. **Ordenação Topológica com DFS** : Ordenação dos vértices em um grafo direcionado acíclico
2. **Algoritmo de Dijkstra** : Caminho mais curto de origem única em grafos com pesos não-negativos
3. **Union-Find (DSU)** : Estrutura de dados para operações de união e busca eficientes
4. **Floyd-Warshall** : Caminho mais curto entre todos os pares de vértices
5. **Busca em Largura (BFS)** : Em desenvolvimento 

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
- `ALGORITMO-resultados.html`: Visualização completa de cada algoritmo
- `ALGORITMO-benchmark.json`: Dados brutos do JMH para análises customizadas

## Análises Detalhadas dos Algoritmos

### 1. Ordenação Topológica (DFS)
**Autora:** Joyce Vitória Nascimento Rodrigues

#### Complexidade Teórica
- **Tempo:** O(V + E) - visita cada vértice e aresta exatamente uma vez
- **Espaço:** O(V) - para arrays de controle e pilha de recursão

#### Aplicações Práticas
- Gerenciamento de projetos (ordem de tarefas com dependências)
- Compiladores (ordem de compilação de módulos)
- Grades curriculares (sequência de disciplinas com pré-requisitos)
- Resolução de dependências (Maven, npm, pip)

#### Resultados dos Benchmarks

| Densidade | Vértices | Tempo (ms/op) |
|-----------|----------|---------------|
| 0.1       | 10       | 0.000108      |
| 0.1       | 100      | 0.002136      |
| 0.1       | 500      | 0.036018      |
| 0.1       | 1000     | 0.139974      |
| 0.3       | 10       | 0.000098      |
| 0.3       | 100      | 0.003716      |
| 0.3       | 500      | 0.085508      |
| 0.3       | 1000     | 0.352546      |
| 0.5       | 10       | 0.000128      |
| 0.5       | 100      | 0.004929      |
| 0.5       | 500      | 0.126963      |
| 0.5       | 1000     | 0.499249      |

#### Análise dos Resultados
- **Escalabilidade:** Crescimento quase-linear confirmando complexidade O(V + E)
- **Impacto da densidade:** Densidade 0.5 é ~3.57x mais lenta que densidade 0.1 (1000 vértices)
- **Performance:** De 0.0001ms (10 vértices) até 0.5ms (1000 vértices, densidade 0.5)
- **Verificação empírica:** Comportamento real alinhado com complexidade teórica

**Visualização completa:** `toposort-resultados.html` com gráficos interativos

---

### 2. Algoritmo de Dijkstra


## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

