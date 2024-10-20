import random, time, psutil, json, os

def init_matrix(n):
    A = [[random.random() for _ in range(n)] for _ in range(n)]
    B = [[random.random() for _ in range(n)] for _ in range(n)]
    C = [[0 for _ in range(n)] for _ in range(n)]

    return A, B, C

def mult_matrix(A, B, C, n):
     for i in range(n):
        for j in range(n):
            for k in range(n):
                C[i][j] += A[i][k] * B[k][j]


def log_metrics(n, execution_time, memory_usage, cpu_usage):
    data = {
        "size": n,
        "execution_time": execution_time,
        "memory_usage": memory_usage,
        "cpu_usage": cpu_usage
    }

    if not os.path.exists('Python_metrics.json'):
        with open('metrics.json', 'w') as f:
            json.dump([data], f, indent=4)
    else:
        with open('metrics.json', 'r+') as f:
            existing_data = json.load(f)
            existing_data.append(data)
            f.seek(0)
            json.dump(existing_data, f, indent=4)


if __name__ == "__main__":
    sizes = [10, 25, 50, 75, 100, 250, 500]
    for n in sizes:
        A, B, C = init_matrix(n)
        process = psutil.Process(os.getpid())

        start_time = time.time()
        start_cpu = psutil.cpu_percent(interval=None)
        start_memory = process.memory_info().rss  # en bytes
        mult_matrix(A, B, C, n)
        execution_time = time.time() - start_time
        end_cpu = psutil.cpu_percent(interval=None)
        end_memory = process.memory_info().rss

        cpu_usage = end_cpu - start_cpu
        memory_usage = (end_memory - start_memory) / (1024 * 1024)
        log_metrics(n, execution_time, memory_usage, cpu_usage)
    