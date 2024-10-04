import random


def matrix_multiplication(lengthA: tuple, lengthB: tuple) -> list:
    # Length: tuple(rows, cols)
    rowsA, colsA = lengthA
    rowsB, colsB = lengthB

    A = [[random.random() for _ in range(colsA)] for _ in range(rowsA)]
    B = [[random.random() for _ in range(colsB)] for _ in range(rowsB)]
    C = [[0 for _ in range(colsB)] for _ in range(rowsA)]



    if len(A[0]) == len(B):
        for i in range(rowsA):
            for j in range(colsB):
                for k in range(colsA):
                    C[i][j] += A[i][k] * B[k][j]

    return C

