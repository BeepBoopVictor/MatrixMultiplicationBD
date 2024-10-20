import json
import matplotlib.pyplot as plt

python_file = "Python\\Python_metrics.json"
cpp_file    = "C++\\MatrixMultiplicationCpp\\cmake-build-debug\\C_metrics.json"
java_file   = "Java\\MatrixMultiplication\\Java_metrics.json"

with open(cpp_file, 'r') as cpp_file:
    cpp_data = json.load(cpp_file)

with open(python_file, 'r') as python_file:
    python_data = json.load(python_file)

with open(java_file, 'r') as java_file:
    java_data = json.load(java_file)

cpp_sizes = [entry['size'] for entry in cpp_data]
cpp_times = [entry['memory_usage'] for entry in cpp_data]

python_sizes = [entry['size'] for entry in python_data]
python_times = [entry['memory_usage'] for entry in python_data]

java_sizes = [entry['size'] for entry in java_data]
java_times = [entry['memory_usage'] for entry in java_data]

plt.figure(figsize=(10, 6))
plt.plot(cpp_sizes, cpp_times, marker='o', label='C++', color='blue')
plt.plot(python_sizes, python_times, marker='o', label='Python', color='green')
plt.plot(java_sizes, java_times, marker='o', label='Java', color='red')

plt.xscale('log')
plt.xlabel('Matrix Size (n)')
plt.ylabel('Memory usage')
plt.title('Memory usage vs Matrix Size for C++, Python, and Java')
plt.legend()

plt.grid(True)
plt.savefig('MemoryUsage.png')
plt.show()
