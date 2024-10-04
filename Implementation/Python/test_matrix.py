from implementation_py import matrix_multiplication
import pytest

@pytest.mark.parametrize("sizeA, sizeB", [
    ((10, 10), (10, 10)),
    ((25, 25), (25, 25)),
    ((50, 50), (50, 50)),
    ((75, 75), (75, 75)),
    ((100, 100), (100, 100)),
    ((125, 125), (125, 125)),
    ((150, 150), (150, 150))
])
def test_matrix_multiplication(benchmark, sizeA, sizeB):

    benchmark(matrix_multiplication, sizeA, sizeB)
