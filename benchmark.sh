#!/bin/bash
# Script para benchmarking de algoritmos em grafos
# Autor: Joyce Vitória

# Cores para feedback visual
G='\033[0;32m' # Verde
B='\033[0;34m' # Azul
Y='\033[1;33m' # Amarelo
R='\033[0;31m' # Vermelho
N='\033[0m'    # Normal

# Banner simples
echo -e "${B}=========================================${N}"
echo -e "${B}     BENCHMARK DE ALGORITMOS EM GRAFOS   ${N}"
echo -e "${B}=========================================${N}"

# Função para mensagens
msg() { echo -e "${G}[*]${N} $1"; }
err() { echo -e "${R}[!]${N} $1"; exit 1; }

# Variáveis globais
BENCHMARK_CLASS=""
RESULT_FILE="benchmark-results.json"
HTML_FILE="resultados.html"
QUICK_MODE=false
ALGORITHM_NAME=""
RUN_ALL=false
SHOW_RESULTS=false

# Função para mostrar ajuda
show_help() {
  echo "Uso: $0 [opções]"
  echo
  echo "Opções:"
  echo "  --toposort        Benchmark de Ordenação Topológica"
  echo "  --all             Executar todos os benchmarks"
  echo "  --quick           Modo rápido (menos iterações)"
  echo "  --results         Listar arquivos de resultados disponíveis"
  echo "  --help            Mostrar esta ajuda"
  exit 0
}

# Processamento simplificado de argumentos
while [[ $# -gt 0 ]]; do
  case "$1" in
    --toposort|--topo)
      BENCHMARK_CLASS="br.ufcg.computacao.benchmark.OrdenacaoTopologicaBenchmark"
      RESULT_FILE="toposort-benchmark.json"
      HTML_FILE="toposort-resultados.html"
      ALGORITHM_NAME="Ordenação Topológica"
      shift ;;
    --all)
      RUN_ALL=true
      shift ;;
    --results)
      SHOW_RESULTS=true
      shift ;;
    --quick)
      QUICK_MODE=true
      shift ;;
    --help|-h)
      show_help ;;
    *)
      echo "Opção desconhecida: $1. Use --help para ver opções."
      exit 1 ;;
  esac
done

# Lista os resultados disponíveis
list_results() {
  msg "Listando resultados disponíveis..."
  echo
  local results=$(find . -name "*-resultados.html" -o -name "resultados.html" | sort)
  
  if [ -z "$results" ]; then
    echo -e "Nenhum resultado de benchmark encontrado."
    echo -e "Execute primeiro um benchmark com: $0 --toposort"
    return
  fi
  
  echo -e "Resultados de benchmark encontrados:\n"
  
  while IFS= read -r file; do
    # Extrair o nome do algoritmo a partir do nome do arquivo
    local algo_name=""
    case "$file" in
      *toposort*) algo_name="Ordenação Topológica" ;;
      *) algo_name="" ;;
    esac
    
    # Remover o ./ do início do nome do arquivo
    file=$(echo "$file" | sed 's|^\./||')
    
    if [ -n "$algo_name" ]; then
      echo -e "$file - $algo_name"
    else
      echo -e "$file"
    fi
  done <<< "$results"
  
  echo
  echo -e "Para visualizar um resultado, abra o arquivo HTML em um navegador."
  echo -e "Exemplo:"
  echo -e "  firefox toposort-resultados.html"
}

# Executar um único benchmark
run_benchmark() {
  local algo_class=$1
  local result_file=$2
  local algo_name=$3
  
  show_step "Executando benchmark para $algo_name"
  
  # Configuração JMH baseada no modo
  local jmh_params="$([[ "$QUICK_MODE" = true ]] && echo "-wi 1 -i 2 -f 1" || echo "-wi 3 -i 3 -f 1")"
  [[ "$QUICK_MODE" = true ]] && show_step "Modo rápido ativado (menos iterações)"

  # Executar benchmark
  show_step "Compilando e executando benchmarks..."
  if ! ./gradlew jmh --no-configuration-cache --quiet -PjmhInclude="$algo_class" -PjmhParams="$jmh_params -rf json -rff $result_file"; then
    show_error "Erro ao executar o benchmark. Verifique se o projeto compila corretamente."
  fi

  show_step "Gerando página HTML para visualização..."

  # Gerar HTML para visualização
  show_step "Gerando página HTML para visualização..."
  create_html "$algo_name" "$result_file" "$HTML_FILE"
  
  echo -e "\n${GREEN}Algoritmo: $algo_name${NC}"
  echo -e "${GREEN}Resultados salvos em: $result_file${NC}"
  echo -e "${GREEN}Visualização disponível em: $HTML_FILE${NC}"
  echo -e "   Para visualizar os resultados, abra o arquivo em um navegador."
}

# Função para criar página HTML
create_html() {
  local algo_name=$1
  local result_file=$2
  local html_file=$3

  cat > "$html_file" << EOF
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Benchmark: $algo_name</title>
  <style>
    body {font-family:Arial,sans-serif; max-width:1200px; margin:0 auto; padding:20px; background:#f8f9fa;}
    .container {background:white; border-radius:8px; box-shadow:0 0 10px rgba(0,0,0,0.1); padding:20px;}
    h1 {text-align:center; border-bottom:2px solid #3498db; padding-bottom:10px; color:#2c3e50;}
    .card {background:white; border-radius:8px; padding:20px; margin:20px 0; box-shadow:0 2px 5px rgba(0,0,0,0.1);}
    table {width:100%; border-collapse:collapse; margin-bottom:20px;}
    table, th, td {border:1px solid #ddd;}
    th, td {padding:12px; text-align:left;}
    th {background-color:#f2f2f2;}
    .chart-container {height:400px; margin:20px 0;}
      background-color: #f9f9f9;
    }
    tr:hover {
      background-color: #f1f1f1;
    }
    .chart-container {
      height: 400px;
      margin: 20px 0;
    }
    .summary {
      line-height: 1.8;
    }
    .benchmark-title {
      color: #3498db;
      border-bottom: 2px solid #eee;
      padding-bottom: 10px;
      margin-bottom: 20px;
    }
  </style>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
  <div class="container">
    <h1>Análise de Desempenho: <span id="algorithm-name">Algoritmos em Grafos</span></h1>
    
    <div class="summary">
      <p>Esta página apresenta os resultados dos benchmarks realizados usando JMH (Java Microbenchmark Harness).</p>
      <p>Os testes avaliaram o desempenho dos algoritmos com diferentes números de vértices e densidades de grafo.</p>
    </div>
    
    <div class="card">
      <h2 class="benchmark-title">Resultados dos Benchmarks</h2>
      <div id="results-table">Carregando resultados...</div>
    </div>
    
    <div class="card">
      <h2 class="benchmark-title">Análise por Número de Vértices</h2>
      <div class="chart-container">
        <canvas id="verticesChart"></canvas>
      </div>
      <div id="vertices-analysis" class="summary"></div>
    </div>
    
    <div class="card">
      <h2 class="benchmark-title">Análise por Densidade</h2>
      <div class="chart-container">
        <canvas id="densidadeChart"></canvas>
      </div>
      <div id="densidade-analysis" class="summary"></div>
    </div>
    
    <div class="card">
      <h2 class="benchmark-title">Conclusão</h2>
      <div id="conclusion" class="summary">
        Analisando os resultados...
      </div>
    </div>
  </div>

  <script>
    // Função para formatar números científicos
    function formatNumber(num) {
      if (num < 0.001) {
        return num.toExponential(2);
      }
      return num.toFixed(6);
    }

    // Função para carregar e processar os resultados
    async function loadResults() {
      try {
        const resultFile = '$result_file';
        
        const response = await fetch(resultFile);
        if (!response.ok) {
          throw new Error(\`Erro ao carregar o arquivo: \${response.statusText}\`);
        }
        
        const data = await response.json();
        if (!data || data.length === 0) {
          throw new Error('Arquivo de resultados vazio ou inválido');
        }
        
        processResults(data);
      } catch (error) {
        document.getElementById('results-table').innerHTML = \`<p>Erro: \${error.message}</p>\`;
        console.error('Erro:', error);
      }
    }

    // Função para processar os resultados
    function processResults(data) {
      // Agrupar por benchmark
      const benchmarks = {};
      
      data.forEach(item => {
        const fullName = item.benchmark;
        const nameParts = fullName.split('.');
        const methodName = nameParts[nameParts.length - 1];
        const className = nameParts[nameParts.length - 2];
        const simpleName = \`\${className}.\${methodName}\`;
        
        if (!benchmarks[simpleName]) {
          benchmarks[simpleName] = [];
        }
        
        benchmarks[simpleName].push({
          densidade: item.params.densidade,
          numVertices: item.params.numVertices,
          score: item.primaryMetric.score,
          error: item.primaryMetric.scoreError,
          unit: item.primaryMetric.scoreUnit
        });
      });
      
      // Criar tabelas HTML
      let tableHtml = '';
      
      for (const benchmark in benchmarks) {
        const results = benchmarks[benchmark].sort((a, b) => {
          if (a.densidade !== b.densidade) {
            return a.densidade - b.densidade;
          }
          return a.numVertices - b.numVertices;
        });
        
        tableHtml += \`
          <h3>\${benchmark}</h3>
          <table>
            <thead>
              <tr>
                <th>Densidade</th>
                <th>Vértices</th>
                <th>Tempo (\${results[0].unit})</th>
                <th>Erro</th>
              </tr>
            </thead>
            <tbody>
        \`;
        
        results.forEach(result => {
          tableHtml += \`
            <tr>
              <td>\${result.densidade}</td>
              <td>\${result.numVertices}</td>
              <td>\${formatNumber(result.score)}</td>
              <td>\${result.error ? formatNumber(result.error) : 'N/A'}</td>
            </tr>
          \`;
        });
        
        tableHtml += \`
            </tbody>
          </table>
        \`;
      }
      
      document.getElementById('results-table').innerHTML = tableHtml;
      
      // Criar gráficos
      createCharts(benchmarks);
      
      // Gerar análises
      generateAnalysis(benchmarks);
    }

    // Função para criar gráficos
    function createCharts(benchmarks) {
      const benchmarkNames = Object.keys(benchmarks);
      if (benchmarkNames.length === 0) return;
      
      // Gráfico por número de vértices
      const verticesCtx = document.getElementById('verticesChart').getContext('2d');
      
      // Extrair valores únicos de densidade e vértices
      const allResults = Object.values(benchmarks).flat();
      const densidades = [...new Set(allResults.map(r => r.densidade))].sort((a, b) => a - b);
      const vertices = [...new Set(allResults.map(r => r.numVertices))].sort((a, b) => a - b);
      
      // Escolher uma densidade constante (a primeira)
      const fixedDensity = densidades[0];
      
      // Preparar datasets para o gráfico
      const verticesDatasets = benchmarkNames.map((name, index) => {
        const results = benchmarks[name].filter(r => r.densidade === fixedDensity)
                                       .sort((a, b) => a.numVertices - b.numVertices);
        
        // Cores para diferentes algoritmos
        const colors = ['#4e73df', '#1cc88a', '#f6c23e', '#e74a3b', '#36b9cc'];
        
        return {
          label: name,
          data: vertices.map(v => {
            const result = results.find(r => r.numVertices === v);
            return result ? result.score : null;
          }),
          borderColor: colors[index % colors.length],
          backgroundColor: 'transparent',
          borderWidth: 2,
          pointRadius: 4,
          tension: 0.1
        };
      });
      
      new Chart(verticesCtx, {
        type: 'line',
        data: {
          labels: vertices,
          datasets: verticesDatasets
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            x: {
              title: {
                display: true,
                text: 'Número de Vértices'
              },
              grid: {
                color: 'rgba(0, 0, 0, 0.05)'
              }
            },
            y: {
              title: {
                display: true,
                text: 'Tempo (ms/op)'
              },
              beginAtZero: true,
              grid: {
                color: 'rgba(0, 0, 0, 0.05)'
              }
            }
          },
          plugins: {
            title: {
              display: true,
              text: \`Desempenho por Número de Vértices (Densidade = \${fixedDensity})\`,
              font: {
                size: 16
              }
            },
            legend: {
              position: 'bottom'
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  const value = context.raw;
                  return \`\${context.dataset.label}: \${formatNumber(value)} ms/op\`;
                }
              }
            }
          }
        }
      });
      
      // Gráfico por densidade
      const densidadeCtx = document.getElementById('densidadeChart').getContext('2d');
      
      // Escolher um número fixo de vértices (o maior)
      const fixedVertices = vertices[vertices.length - 1];
      
      // Preparar datasets para o gráfico
      const densidadeDatasets = benchmarkNames.map((name, index) => {
        const results = benchmarks[name].filter(r => r.numVertices === fixedVertices)
                                      .sort((a, b) => a.densidade - b.densidade);
        
        // Cores para diferentes algoritmos
        const colors = ['#4e73df', '#1cc88a', '#f6c23e', '#e74a3b', '#36b9cc'];
        
        return {
          label: name,
          data: densidades.map(d => {
            const result = results.find(r => r.densidade === d);
            return result ? result.score : null;
          }),
          borderColor: colors[index % colors.length],
          backgroundColor: 'transparent',
          borderWidth: 2,
          pointRadius: 4,
          tension: 0.1
        };
      });
      
      new Chart(densidadeCtx, {
        type: 'line',
        data: {
          labels: densidades,
          datasets: densidadeDatasets
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            x: {
              title: {
                display: true,
                text: 'Densidade'
              },
              grid: {
                color: 'rgba(0, 0, 0, 0.05)'
              }
            },
            y: {
              title: {
                display: true,
                text: 'Tempo (ms/op)'
              },
              beginAtZero: true,
              grid: {
                color: 'rgba(0, 0, 0, 0.05)'
              }
            }
          },
          plugins: {
            title: {
              display: true,
              text: \`Desempenho por Densidade (Vértices = \${fixedVertices})\`,
              font: {
                size: 16
              }
            },
            legend: {
              position: 'bottom'
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  const value = context.raw;
                  return \`\${context.dataset.label}: \${formatNumber(value)} ms/op\`;
                }
              }
            }
          }
        }
      });
    }

    // Função para gerar análises dos resultados
    function generateAnalysis(benchmarks) {
      const benchmarkNames = Object.keys(benchmarks);
      if (benchmarkNames.length === 0) return;
      
      // Análise por número de vértices
      const verticesAnalysis = document.getElementById('vertices-analysis');
      
      // Extrair valores únicos
      const allResults = Object.values(benchmarks).flat();
      const densidades = [...new Set(allResults.map(r => r.densidade))].sort((a, b) => a - b);
      const vertices = [...new Set(allResults.map(r => r.numVertices))].sort((a, b) => a - b);
      
      // Densidade fixa para análise (a primeira)
      const fixedDensity = densidades[0];
      
      let verticesHtml = \`<h3>Análise para Densidade \${fixedDensity}</h3><p>\`;
      
      benchmarkNames.forEach(name => {
        const results = benchmarks[name].filter(r => r.densidade === fixedDensity)
                                      .sort((a, b) => a.numVertices - b.numVertices);
        
        if (results.length >= 2) {
          const smallest = results[0];
          const largest = results[results.length - 1];
          
          const ratio = largest.score / smallest.score;
          const vertexRatio = largest.numVertices / smallest.numVertices;
          
          verticesHtml += \`<strong>\${name}:</strong> Quando o número de vértices aumenta de \${smallest.numVertices} para \${largest.numVertices} (\${vertexRatio}x), \`;
          verticesHtml += \`o tempo aumenta de \${formatNumber(smallest.score)} para \${formatNumber(largest.score)} (\${ratio.toFixed(2)}x). \`;
          
          // Análise de complexidade
          if (ratio < vertexRatio) {
            verticesHtml += \`Isso sugere complexidade sublinear. \`;
          } else if (ratio < vertexRatio * 1.5) {
            verticesHtml += \`Isso aproxima-se de uma complexidade linear O(V). \`;
          } else if (ratio < vertexRatio * vertexRatio) {
            verticesHtml += \`Isso sugere complexidade próxima de O(V log V). \`;
          } else {
            verticesHtml += \`Isso sugere complexidade quadrática ou maior O(V²). \`;
          }
          
          verticesHtml += \`<br><br>\`;
        }
      });
      
      verticesAnalysis.innerHTML = verticesHtml + "</p>";
      
      // Análise por densidade
      const densidadeAnalysis = document.getElementById('densidade-analysis');
      
      // Vértices fixos para análise (o maior)
      const fixedVertices = vertices[vertices.length - 1];
      
      let densidadeHtml = \`<h3>Análise para \${fixedVertices} Vértices</h3><p>\`;
      
      benchmarkNames.forEach(name => {
        const results = benchmarks[name].filter(r => r.numVertices === fixedVertices)
                                      .sort((a, b) => a.densidade - b.densidade);
        
        if (results.length >= 2) {
          const lowest = results[0];
          const highest = results[results.length - 1];
          
          const ratio = highest.score / lowest.score;
          const densityRatio = highest.densidade / lowest.densidade;
          
          densidadeHtml += \`<strong>\${name}:</strong> Quando a densidade aumenta de \${lowest.densidade} para \${highest.densidade} (\${densityRatio.toFixed(1)}x), \`;
          densidadeHtml += \`o tempo aumenta de \${formatNumber(lowest.score)} para \${formatNumber(highest.score)} (\${ratio.toFixed(2)}x). \`;
          
          if (ratio < 1.2) {
            densidadeHtml += \`A densidade tem pouco impacto no desempenho. \`;
          } else if (ratio < 2) {
            densidadeHtml += \`A densidade tem impacto moderado no desempenho. \`;
          } else {
            densidadeHtml += \`A densidade tem grande impacto no desempenho. \`;
          }
          
          densidadeHtml += \`<br><br>\`;
        }
      });
      
      densidadeAnalysis.innerHTML = densidadeHtml + "</p>";
      
      // Conclusão geral
      const conclusion = document.getElementById('conclusion');
      
      // Encontrar o melhor algoritmo para o maior caso
      let bestAlgo = null;
      let bestTime = Infinity;
      
      benchmarkNames.forEach(name => {
        const largeResult = benchmarks[name].find(r => 
          r.numVertices === fixedVertices && r.densidade === densidades[densidades.length - 1]
        );
        
        if (largeResult && largeResult.score < bestTime) {
          bestTime = largeResult.score;
          bestAlgo = name;
        }
      });
      
      let conclusionHtml = \`
        <h3>Resumo da Análise</h3>
        <p>
          Os benchmarks foram executados com variações de número de vértices (\${Math.min(...vertices)} a \${Math.max(...vertices)}) 
          e densidades (\${Math.min(...densidades)} a \${Math.max(...densidades)}).
        </p>
      \`;
      
      if (bestAlgo) {
        conclusionHtml += \`
          <p>
            <strong>Algoritmo mais rápido:</strong> \${bestAlgo} obteve o melhor desempenho para o cenário mais complexo
            (\${fixedVertices} vértices, densidade \${densidades[densidades.length - 1]}), com tempo de \${formatNumber(bestTime)} ms/op.
          </p>
        \`;
      }
      
      conclusionHtml += \`
        <p>
          <strong>Observações finais:</strong><br>
      \`;
      
      if (benchmarkNames.some(name => name.includes('Toposort') || name.includes('topologica'))) {
        conclusionHtml += \`
          - A ordenação topológica demonstra comportamento próximo ao esperado teoricamente O(V+E).<br>
          - O algoritmo escala bem com o aumento do número de vértices.<br>
        \`;
      }
      
      conclusionHtml += \`</p>\`;
      conclusion.innerHTML = conclusionHtml;
    }

    // Definir o nome do algoritmo na página
    function setAlgorithmName() {
      document.getElementById('algorithm-name').textContent = "$algo_name";
    }
    
    // Carregar resultados quando a página carregar
    window.addEventListener('DOMContentLoaded', function() {
      setAlgorithmName();
      loadResults();
    });
  </script>
</body>
</html>
EOF

  # Mensagem final
  echo -e "\n${GREEN}=== Benchmark concluído com sucesso! ===${NC}"
  echo -e "Algoritmo: ${YELLOW}$algo_name${NC}"
  echo -e "Resultados salvos em: ${BLUE}$result_file${NC}"
  echo -e "Visualização disponível em: ${BLUE}$HTML_FILE${NC}"
  echo -e "Para visualizar os resultados, abra o arquivo em um navegador."
}

# Função para executar todos os benchmarks
run_all_benchmarks() {
  # Definir algoritmos disponíveis
  declare -A ALGORITHMS=(
    ["Ordenação Topológica"]="br.ufcg.computacao.benchmark.OrdenacaoTopologicaBenchmark:toposort-benchmark.json:toposort-resultados.html"
  )

  echo -e "${BLUE}==== Executando benchmarks para todos os algoritmos ====${NC}"
  
  # Executar cada benchmark
  for algo_name in "${!ALGORITHMS[@]}"; do
    IFS=':' read -r class result_file html_file <<< "${ALGORITHMS[$algo_name]}"
    
    echo -e "${YELLOW}------------------------------------------------${NC}"
    echo -e "${YELLOW}Executando benchmark para $algo_name...${NC}"
    echo -e "${YELLOW}------------------------------------------------${NC}"
    
    run_benchmark "$class" "$result_file" "$algo_name"
    
    echo -e "\n${GREEN}Benchmark para $algo_name concluído.${NC}\n"
  done

  echo -e "${BLUE}=== Benchmark concluído com sucesso! ===${NC}"
  echo -e "\nOs resultados estão disponíveis nos seguintes arquivos HTML:"
  for algo_name in "${!ALGORITHMS[@]}"; do
    IFS=':' read -r _ _ html_file <<< "${ALGORITHMS[$algo_name]}"
    echo -e "- ${BLUE}$html_file${NC} ($algo_name)"
  done
  echo -e "\nAbra esses arquivos em um navegador para visualizar os resultados."
}

# Executar os benchmarks
if [ "$SHOW_RESULTS" = true ]; then
  list_results
elif [ "$RUN_ALL" = true ]; then
  run_all_benchmarks
else
  # Se nenhum algoritmo específico foi escolhido, mostrar ajuda
  if [ -z "$BENCHMARK_CLASS" ]; then
    show_help
  fi
  run_benchmark "$BENCHMARK_CLASS" "$RESULT_FILE" "$ALGORITHM_NAME"
fi
