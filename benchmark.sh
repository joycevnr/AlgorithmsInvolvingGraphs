# #!/bin/bash
# # Benchmark para Algoritmos em Grafos
# # Autor: Joyce Vitória

# # Cores para feedback
# G='\033[0;32m' # Verde
# Y='\033[1;33m' # Amarelo
# R='\033[0;31m' # Vermelho
# B='\033[0;34m' # Azul
# N='\033[0m'    # Normal

# # Funções
# msg() { echo -e "${G}[*]${N} $1"; }
# err() { echo -e "${R}[!]${N} $1"; exit 1; }

# # Variáveis
# BENCHMARK_CLASS=""
# RESULT_FILE=""
# HTML_FILE=""
# ALGORITHM_NAME=""
# QUICK_MODE=false

# # Configuração dos algoritmos
# setup_algorithm() {
#   case "$1" in
#     --toposort)
#       BENCHMARK_CLASS="br.ufcg.computacao.benchmark.OrdenacaoTopologicaBenchmark"
#       RESULT_FILE="toposort-benchmark.json"
#       HTML_FILE="toposort-resultados.html"
#       ALGORITHM_NAME="Ordenação Topológica"
#       ;;
#     --dijkstra)
#       BENCHMARK_CLASS="br.ufcg.computacao.benchmark.DijkstraBenchmark"
#       RESULT_FILE="dijkstra-benchmark.json"
#       HTML_FILE="dijkstra-resultados.html"
#       ALGORITHM_NAME="Dijkstra"
#       ;;
#     --bfs)
#       BENCHMARK_CLASS="br.ufcg.computacao.benchmark.BFSBenchmark"
#       RESULT_FILE="bfs-benchmark.json"
#       HTML_FILE="bfs-resultados.html"
#       ALGORITHM_NAME="Busca em Largura (BFS)"
#       ;;
#     --dsu)
#       BENCHMARK_CLASS="br.ufcg.computacao.benchmark.DSUBenchmark"
#       RESULT_FILE="dsu-benchmark.json"
#       HTML_FILE="dsu-resultados.html"
#       ALGORITHM_NAME="Union-Find (DSU)"
#       ;;
#     --floyd)
#       BENCHMARK_CLASS="br.ufcg.computacao.benchmark.FloydWarshallBenchmark"
#       RESULT_FILE="floyd-benchmark.json"
#       HTML_FILE="floyd-resultados.html"
#       ALGORITHM_NAME="Floyd-Warshall"
#       ;;
#     *) return 1 ;;
#   esac
#   return 0
# }

# # Mostrar ajuda
# show_help() {
#   echo -e "${B}Benchmark de Algoritmos em Grafos${N}"
#   echo
#   echo "Uso: $0 <algoritmo> [opções]"
#   echo
#   echo "Algoritmos disponíveis:"
#   echo "  --toposort    Ordenação Topológica"
#   echo "  --dijkstra    Dijkstra"
#   echo "  --bfs         Busca em Largura"
#   echo "  --dsu         Union-Find (DSU)"
#   echo "  --floyd       Floyd-Warshall"
#   echo
#   echo "Opções:"
#   echo "  --quick       Modo rápido (menos iterações)"
#   echo "  --help        Mostrar esta ajuda"
#   exit 0
# }

# # Processar argumentos
# ALGORITHM=""
# while [[ $# -gt 0 ]]; do
#   case "$1" in
#     --toposort|--dijkstra|--bfs|--dsu|--floyd)
#       if [ -n "$ALGORITHM" ]; then
#         err "Especifique apenas um algoritmo por vez"
#       fi
#       ALGORITHM="$1"
#       ;;
#     --quick|-q) QUICK_MODE=true ;;
#     --help|-h) show_help ;;
#     *) echo "Opção inválida: $1. Use --help para ajuda."; exit 1 ;;
#   esac
#   shift
# done

# # Verificar se algoritmo foi especificado
# if [ -z "$ALGORITHM" ]; then
#   echo "Erro: Especifique um algoritmo"
#   show_help
# fi

# # Configurar algoritmo
# if ! setup_algorithm "$ALGORITHM"; then
#   err "Algoritmo inválido: $ALGORITHM"
# fi

# # Banner
# echo -e "${Y}===== BENCHMARK: $ALGORITHM_NAME =====${N}"

# # Configurar JMH
# if [ "$QUICK_MODE" = true ]; then
#   JMH_PARAMS="-wi 1 -i 2 -f 1"
#   msg "Modo rápido ativado"
# else
#   JMH_PARAMS="-wi 3 -i 3 -f 1"
# fi

# # Executar benchmark
# msg "Executando benchmark para $ALGORITHM_NAME..."
# if ! ./gradlew jmh --no-configuration-cache --quiet \
#     -PjmhInclude="$BENCHMARK_CLASS" \
#     -PjmhParams="$JMH_PARAMS -rf json"; then
#   err "Erro ao executar benchmark"
# fi

# # Filtrar apenas o benchmark específico do resultado
# msg "Filtrando resultados..."
# JMH_RESULT_FILE="build/results/jmh/results.json"
# if [ -f "$JMH_RESULT_FILE" ]; then
#   python3 -c "
# import json
# with open('$JMH_RESULT_FILE', 'r') as f:
#     data = json.load(f)
# filtered = [item for item in data if '$BENCHMARK_CLASS' in item['benchmark']]
# with open('$RESULT_FILE', 'w') as f:
#     json.dump(filtered, f, indent=2)
# "
# else
#   err "Arquivo $JMH_RESULT_FILE não encontrado"
# fi

# # Criar HTML
# msg "Gerando visualização HTML..."
# create_html() {
#   cat > "$HTML_FILE" << EOF
# <!DOCTYPE html>
# <html lang="pt-BR">
# <head>
#   <meta charset="UTF-8">
#   <title>Benchmark: $ALGORITHM_NAME</title>
#   <style>
#     body {font-family:Arial,sans-serif; max-width:1200px; margin:0 auto; padding:20px; background:#f8f9fa;}
#     .container {background:white; border-radius:8px; box-shadow:0 0 10px rgba(0,0,0,0.1); padding:20px;}
#     h1 {text-align:center; border-bottom:2px solid #3498db; padding-bottom:10px; color:#2c3e50;}
#     .card {background:white; border-radius:8px; padding:20px; margin:20px 0; box-shadow:0 2px 5px rgba(0,0,0,0.1);}
#     table {width:100%; border-collapse:collapse; margin-bottom:20px;}
#     table, th, td {border:1px solid #ddd;}
#     th, td {padding:12px; text-align:left;}
#     th {background-color:#f2f2f2;}
#     .chart-container {height:400px; margin:20px 0;}
#   </style>
#   <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
# </head>
# <body>
#   <div class="container">
#     <h1>Benchmark: $ALGORITHM_NAME</h1>
    
#     <div class="card">
#       <h2>Resultados</h2>
#       <div id="results-table">Carregando...</div>
#     </div>
    
#     <div class="card">
#       <h2>Desempenho por Vértices</h2>
#       <div class="chart-container">
#         <canvas id="verticesChart"></canvas>
#       </div>
#     </div>
    
#     <div class="card">
#       <h2>Desempenho por Densidade</h2>
#       <div class="chart-container">
#         <canvas id="densidadeChart"></canvas>
#       </div>
#     </div>
#   </div>

#   <script>
#     function formatNumber(num) {
#       if (typeof num !== 'number' || isNaN(num)) return 'N/A';
#       if (num < 0.001) return num.toExponential(2);
#       return num.toFixed(6);
#     }

#     async function loadResults() {
#       try {
#         const response = await fetch('$RESULT_FILE');
#         if (!response.ok) throw new Error(\`Erro: \${response.statusText}\`);
        
#         const data = await response.json();
#         if (!data || data.length === 0) throw new Error('Arquivo vazio');
        
#         processResults(data);
#       } catch (error) {
#         document.getElementById('results-table').innerHTML = 
#           '<p>Para ver os gráficos, execute: <code>python3 -m http.server 8080</code> e acesse <a href="http://localhost:8080/$HTML_FILE">http://localhost:8080/$HTML_FILE</a></p>';
#       }
#     }

#     function processResults(data) {
#       const results = [];
      
#       data.forEach(item => {
#         results.push({
#           densidade: parseFloat(item.params.densidade),
#           numVertices: parseInt(item.params.numVertices),
#           score: item.primaryMetric.score,
#           error: item.primaryMetric.scoreError === "NaN" ? null : item.primaryMetric.scoreError,
#           unit: item.primaryMetric.scoreUnit
#         });
#       });
      
#       createTable(results);
#       createCharts(results);
#     }

#     function createTable(results) {
#       const sorted = results.sort((a, b) => a.densidade - b.densidade || a.numVertices - b.numVertices);
      
#       let html = '<table><thead><tr><th>Densidade</th><th>Vértices</th><th>Tempo (' + sorted[0].unit + ')</th><th>Erro</th></tr></thead><tbody>';
      
#       sorted.forEach(r => {
#         html += \`<tr><td>\${r.densidade}</td><td>\${r.numVertices}</td><td>\${formatNumber(r.score)}</td><td>\${r.error ? formatNumber(r.error) : 'N/A'}</td></tr>\`;
#       });
      
#       html += '</tbody></table>';
#       document.getElementById('results-table').innerHTML = html;
#     }

#     function createCharts(results) {
#       const densidades = [...new Set(results.map(r => r.densidade))].sort();
#       const vertices = [...new Set(results.map(r => r.numVertices))].sort();
      
#       // Gráfico por vértices (densidade = 0.1)
#       const verticesData = vertices.map(v => {
#         const item = results.find(r => r.numVertices === v && r.densidade === 0.1);
#         return item ? item.score : null;
#       });
      
#       new Chart(document.getElementById('verticesChart'), {
#         type: 'line',
#         data: {
#           labels: vertices,
#           datasets: [{
#             label: 'Tempo (ms/op)',
#             data: verticesData,
#             borderColor: '#4e73df',
#             backgroundColor: 'transparent',
#             borderWidth: 2
#           }]
#         },
#         options: {
#           responsive: true,
#           maintainAspectRatio: false,
#           scales: {
#             x: { title: { display: true, text: 'Número de Vértices' } },
#             y: { title: { display: true, text: 'Tempo (ms/op)' }, beginAtZero: true }
#           }
#         }
#       });
      
#       // Gráfico por densidade (vértices = 1000)
#       const densidadeData = densidades.map(d => {
#         const item = results.find(r => r.densidade === d && r.numVertices === 1000);
#         return item ? item.score : null;
#       });
      
#       new Chart(document.getElementById('densidadeChart'), {
#         type: 'line',
#         data: {
#           labels: densidades,
#           datasets: [{
#             label: 'Tempo (ms/op)',
#             data: densidadeData,
#             borderColor: '#1cc88a',
#             backgroundColor: 'transparent',
#             borderWidth: 2
#           }]
#         },
#         options: {
#           responsive: true,
#           maintainAspectRatio: false,
#           scales: {
#             x: { title: { display: true, text: 'Densidade' } },
#             y: { title: { display: true, text: 'Tempo (ms/op)' }, beginAtZero: true }
#           }
#         }
#       });
#     }

#     window.addEventListener('DOMContentLoaded', loadResults);
#   </script>
# </body>
# </html>
# EOF
# }

# create_html

# # Resultado final
# echo
# msg "Benchmark concluído!"
# echo -e "${G}Algoritmo: ${Y}$ALGORITHM_NAME${N}"
# echo -e "${G}Resultados: ${Y}$RESULT_FILE${N}"
# echo -e "${G}Visualização: ${Y}$HTML_FILE${N}"
# echo
# echo -e "Para ver os gráficos:"
# echo -e "  1. Execute: ${Y}python3 -m http.server 8080${N}"
# echo -e "  2. Acesse: ${Y}http://localhost:8080/$HTML_FILE${N}"
