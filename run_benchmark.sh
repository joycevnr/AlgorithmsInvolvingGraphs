#!/bin/bash
# Benchmark para Algoritmos em Grafos
# Autor: Joyce Vitória

# Cores para feedback
G='\033[0;32m' # Verde
Y='\033[1;33m' # Amarelo
R='\033[0;31m' # Vermelho
B='\033[0;34m' # Azul
N='\033[0m'    # Normal

# Funções
msg() { echo -e "${G}[*]${N} $1"; }
err() { echo -e "${R}[!]${N} $1"; exit 1; }

# Variáveis
BENCHMARK_CLASS=""
RESULT_FILE=""
ALGORITHM_NAME=""
QUICK_MODE=false

# Configuração dos algoritmos
setup_algorithm() {
  case "$1" in
    --toposort)
      BENCHMARK_CLASS="br.ufcg.computacao.benchmark.OrdenacaoTopologicaBenchmark"
      RESULT_FILE="toposort-benchmark.json"
      ALGORITHM_NAME="Ordenação Topológica"
      ;;
    --menorcaminho)
      BENCHMARK_CLASS="br.ufcg.computacao.benchmark.MenorCaminhoBenchmark"
      RESULT_FILE="menorcaminho-benchmark.json"
      ALGORITHM_NAME="Menor Caminho (Comparativo): Dijkstra e Floyd-Warshall"
      ;;
    --bfs)
      BENCHMARK_CLASS="br.ufcg.computacao.benchmark.BFSBenchmark"
      RESULT_FILE="bfs-benchmark.json"
      ALGORITHM_NAME="Busca em Largura (BFS)"
      ;;
    --dsu)
      BENCHMARK_CLASS="br.ufcg.computacao.benchmark.DisjointSetUnionBenchmark"
      RESULT_FILE="dsu-benchmark.json"
      ALGORITHM_NAME="Union-Find (DSU)"
      ;;
    *) return 1 ;;
  esac
  return 0
}

# Mostrar ajuda
show_help() {
  echo -e "${B}Benchmark de Algoritmos em Grafos${N}"
  echo
  echo "Uso: $0 <algoritmo> [opções]"
  echo
  echo "Algoritmos disponíveis:"
  echo "  --toposort       Ordenação Topológica"
  echo "  --bfs            Busca em Largura"
  echo "  --dsu            Union-Find (DSU)"
  echo "  --menorcaminho   Menor Caminho (Comparativo): Dijkstra e Floyd-Warshall"
  echo
  echo "Opções:"
  echo "  --quick, -q      Modo rápido (menos iterações)"
  echo "  --help, -h       Mostrar esta ajuda"
  exit 0
}

# Processar argumentos da linha de comando
ALGORITHM=""
while [[ $# -gt 0 ]]; do
  case "$1" in
    --toposort|--dijkstra|--bfs|--dsu|--floyd|--menorcaminho)
      if [ -n "$ALGORITHM" ]; then
        err "Especifique apenas um algoritmo por vez"
      fi
      ALGORITHM="$1"
      ;;
    --quick|-q) QUICK_MODE=true ;;
    --help|-h) show_help ;;
    *) echo "Opção inválida: $1. Use --help para ajuda."; exit 1 ;;
  esac
  shift
done

# Verificar se um algoritmo foi especificado
if [ -z "$ALGORITHM" ]; then
  err "Erro: Especifique um algoritmo para executar. Use --help para ver as opções."
fi

# Configurar variáveis com base no algoritmo escolhido
if ! setup_algorithm "$ALGORITHM"; then
  err "Algoritmo inválido: $ALGORITHM"
fi

# Banner
echo -e "${Y}===== BENCHMARK: $ALGORITHM_NAME =====${N}"

# Configurar parâmetros do JMH
if [ "$QUICK_MODE" = true ]; then
  JMH_PARAMS="-wi 1 -i 2 -f 1"
  msg "Modo rápido ativado"
else
  JMH_PARAMS="-wi 3 -i 3 -f 1"
fi

# Executar benchmark via Gradle
msg "Executando benchmark para $ALGORITHM_NAME..."
if ! ./gradlew jmh --no-configuration-cache --quiet \
    -PjmhInclude="$BENCHMARK_CLASS" \
    -PjmhParams="$JMH_PARAMS -rf json"; then
  err "Erro ao executar benchmark. Verifique os logs do Gradle."
fi

# Filtrar o resultado JSON para conter apenas os dados do benchmark executado
msg "Filtrando resultados..."
JMH_RESULT_FILE="build/results/jmh/results.json"
if [ -f "$JMH_RESULT_FILE" ]; then
  python3 -c "
import json
with open('$JMH_RESULT_FILE', 'r') as f:
    data = json.load(f)
filtered = [item for item in data if '$BENCHMARK_CLASS' in item['benchmark']]
with open('$RESULT_FILE', 'w') as f:
    json.dump(filtered, f, indent=2)
"
else
  err "Arquivo de resultado JMH ($JMH_RESULT_FILE) não encontrado"
fi

# Gerar gráficos como imagens PNG usando o script Python
msg "Gerando gráficos como imagens PNG..."
if ! python3 gerar_grafico.py "$RESULT_FILE"; then
  err "Erro ao gerar os gráficos com o script Python."
fi

# Mensagem final de sucesso
echo
msg "Benchmark concluído!"
echo -e "${G}Algoritmo: ${Y}$ALGORITHM_NAME${N}"
echo -e "${G}Resultados JSON salvos em: ${Y}$RESULT_FILE${N}"
echo -e "${G}Gráficos salvos com o prefixo: ${Y}$(basename $RESULT_FILE .json)${N}"