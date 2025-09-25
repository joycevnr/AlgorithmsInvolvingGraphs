import json
import sys
import matplotlib.pyplot as plt

def process_results(data):
    """Processa os dados brutos do JMH para um formato mais fácil de usar."""
    results = []
    for item in data:
        # Extrai o nome do método de benchmark (ex: 'dijkstraComFila')
        benchmark_name = item['benchmark'].split('.')[-1]
        results.append({
            'algorithm': benchmark_name,
            'densidade': float(item['params']['densidade']),
            'numVertices': int(item['params']['numVertices']),
            'score': item['primaryMetric']['score'],
            'unit': item['primaryMetric']['scoreUnit']
        })
    return results

def create_charts(results, output_prefix):
    """Cria e salva os gráficos a partir dos resultados processados."""
    # Identifica automaticamente todos os algoritmos/métodos presentes no resultado
    algorithms = sorted(list(set(r['algorithm'] for r in results)))
    densidades = sorted(list(set(r['densidade'] for r in results)))
    vertices = sorted(list(set(r['numVertices'] for r in results)))
    
    unit = results[0]['unit'] if results else 'score'
    # Gera um conjunto de cores distintas para os gráficos
    colors = plt.cm.viridis_r([i / len(algorithms) for i in range(len(algorithms))])

    # --- Gráfico 1: Desempenho por Vértices (Densidade fixa em 0.1) ---
    plt.figure(figsize=(12, 7))
    # Para cada algoritmo encontrado, desenha uma linha no gráfico
    for i, algo in enumerate(algorithms):
        # Filtra os pontos de dados para este algoritmo e densidade fixa
        data_points = [r for r in results if r['algorithm'] == algo and r['densidade'] == 0.1]
        if not data_points: continue
        
        x_vals = [r['numVertices'] for r in data_points]
        y_vals = [r['score'] for r in data_points]
        
        # Ordena os pontos pelo eixo X para garantir que a linha seja desenhada corretamente
        sorted_points = sorted(zip(x_vals, y_vals))
        if sorted_points:
            x_sorted, y_sorted = zip(*sorted_points)
            plt.plot(x_sorted, y_sorted, marker='o', linestyle='-', label=algo, color=colors[i])

    plt.title('Desempenho por Número de Vértices (Densidade de Arestas = 0.1)')
    plt.xlabel('Número de Vértices')
    plt.ylabel(f'Tempo ({unit})')
    plt.legend()
    plt.grid(True, which='both', linestyle='--', linewidth=0.5)
    plt.tight_layout()
    vertices_filename = f'{output_prefix}_por_vertices.png'
    plt.savefig(vertices_filename)
    print(f"[*] Gráfico salvo como: {vertices_filename}")
    plt.close()

    # --- Gráfico 2: Desempenho por Densidade (Vértices fixos em 1000) ---
    # Este gráfico só será gerado se houver dados para 1000 vértices
    results_for_1000_vertices = [r for r in results if r['numVertices'] == 1000]
    if results_for_1000_vertices:
        plt.figure(figsize=(12, 7))
        for i, algo in enumerate(algorithms):
            data_points = [r for r in results_for_1000_vertices if r['algorithm'] == algo]
            if not data_points: continue

            x_vals = [r['densidade'] for r in data_points]
            y_vals = [r['score'] for r in data_points]

            sorted_points = sorted(zip(x_vals, y_vals))
            if sorted_points:
                x_sorted, y_sorted = zip(*sorted_points)
                plt.plot(x_sorted, y_sorted, marker='o', linestyle='-', label=algo, color=colors[i])

        plt.title('Desempenho por Densidade de Arestas (Número de Vértices = 1000)')
        plt.xlabel('Densidade de Arestas')
        plt.ylabel(f'Tempo ({unit})')
        plt.legend()
        plt.grid(True, which='both', linestyle='--', linewidth=0.5)
        plt.tight_layout()
        densidade_filename = f'{output_prefix}_por_densidade.png'
        plt.savefig(densidade_filename)
        print(f"[*] Gráfico salvo como: {densidade_filename}")
        plt.close()

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Erro: Forneça o caminho para o arquivo JSON como argumento.")
        sys.exit(1)
        
    json_file = sys.argv[1]
    
    try:
        with open(json_file, 'r') as f:
            data = json.load(f)
        
        if not data:
            print(f"[!] Erro: O arquivo '{json_file}' está vazio ou não contém dados válidos.")
            sys.exit(1)
            
        results = process_results(data)
        # Define o nome do arquivo de saída com base no nome do JSON
        output_prefix = json_file.rsplit('.', 1)[0]
        create_charts(results, output_prefix)
        
    except FileNotFoundError:
        print(f"[!] Erro: Arquivo de resultados '{json_file}' não encontrado.")
    except json.JSONDecodeError:
        print(f"[!] Erro: O arquivo '{json_file}' não é um JSON válido.")
    except Exception as e:
        print(f"Ocorreu um erro inesperado: {e}")