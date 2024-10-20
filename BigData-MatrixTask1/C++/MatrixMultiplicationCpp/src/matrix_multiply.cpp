#include <iostream>
#include <vector>
#include <random>
#include <chrono>
#include <fstream>
#include <windows.h>
#include <psapi.h>

void init_matrix(std::vector<std::vector<double>>& matrix, int n) {
    std::mt19937 gen(0);    
    std::uniform_real_distribution<> dis(0.0, 1.0);

    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            matrix[i][j] = dis(gen);
}

void matrix_multiply(const std::vector<std::vector<double>>& A, 
                     const std::vector<std::vector<double>>& B, 
                     std::vector<std::vector<double>>& C, int n) {

    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j)
            for (int k = 0; k < n; ++k)
                C[i][j] += A[i][k] * B[k][j];
}

void log_metrics(std::ofstream & file, int n, double execution_time, double memory_usage, double cpu_usage, bool is_last) {
    file << "{\n";
    file << "  \"size\": " << n << ",\n";
    file << "  \"execution_time\": " << execution_time << ",\n";
    file << "  \"memory_usage\": " << memory_usage << ",\n";
    file << "  \"cpu_usage\": " << cpu_usage * 100 << "\n";
    if (is_last) {
        file << "}\n";
    } else {
        file << "},\n";
    }
}

double get_memory_usage() {
    PROCESS_MEMORY_COUNTERS pmc;
    if (GetProcessMemoryInfo(GetCurrentProcess(), &pmc, sizeof(pmc))) {
        return static_cast<double>(pmc.WorkingSetSize) / 1024.0 / 1024.0; // MB
    }
    return 0.0;
}

double get_cpu_usage() {
    FILETIME ft_idle, ft_kernel, ft_user;
    GetSystemTimes(&ft_idle, &ft_kernel, &ft_user);
    ULARGE_INTEGER li_kernel, li_user;
    li_kernel.LowPart = ft_kernel.dwLowDateTime;
    li_kernel.HighPart = ft_kernel.dwHighDateTime;
    li_user.LowPart = ft_user.dwLowDateTime;
    li_user.HighPart = ft_user.dwHighDateTime;

    return (li_kernel.QuadPart + li_user.QuadPart) / 10000000.0;
}

int main() {
    int sizes[] = {10, 25, 50, 75, 100, 250};
    std::ofstream file("C_metrics.json");
    file << "[\n";

    for (int i = 0; i < 6; ++i) {
        int n = sizes[i];
        auto start = std::chrono::high_resolution_clock::now();

        std::vector<std::vector<double>> A(n, std::vector<double>(n));
        std::vector<std::vector<double>> B(n, std::vector<double>(n));
        std::vector<std::vector<double>> C(n, std::vector<double>(n, 0.0));

        init_matrix(A, n);
        init_matrix(B, n);

        double memory_before = get_memory_usage();
        double cpu_before = get_cpu_usage();

        matrix_multiply(A, B, C, n);

        auto end = std::chrono::high_resolution_clock::now();
        std::chrono::duration<double> elapsed = end - start;

        double execution_time = elapsed.count();
        double memory_after = get_memory_usage();
        double cpu_after = get_cpu_usage();

        double memory_usage = memory_after - memory_before;
        double cpu_usage = cpu_after - cpu_before;

        bool is_last = (i == 5);
        log_metrics(file, n, execution_time, memory_usage, cpu_usage, is_last);
    }
    file << "]\n";
    file.close();
    return 0;
}
