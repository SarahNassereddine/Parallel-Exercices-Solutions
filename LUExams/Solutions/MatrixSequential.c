#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>

#define SIZE 5 // Size of the matrices (5x5)
#define MIN_VALUE 1
#define MAX_VALUE 5

void initialize_matrix(int matrix[SIZE][SIZE]) {
    int i, j;
    for (i = 0; i < SIZE; i++) {
        for (j = 0; j < SIZE; j++) {
            matrix[i][j] = MIN_VALUE + rand() % (MAX_VALUE - MIN_VALUE + 1);
        }
    }
}

void display_matrix(const char* name, int matrix[SIZE][SIZE]) {
    int i, j;
    printf("\nMatrix %s:\n", name);
    for (i = 0; i < SIZE; i++) {
        for (j = 0; j < SIZE; j++) {
            printf("%3d ", matrix[i][j]);
        }
        printf("\n");
    }
}

int main() {
    int A[SIZE][SIZE], B[SIZE][SIZE], C[SIZE][SIZE], D[SIZE][SIZE];
    int sum = 0;
    int max_val = 0;
    int i, j, k;

    // Seed the random number generator
    srand(time(NULL));

    // Initialize matrices

    #pragma omp parallel sections num_threads (2)
     {
        #pragma omp section
        {
    initialize_matrix(A);
    printf("Matrix A initialized by thread %d\n", omp_get_thread_num());
}
    #pragma omp section
    {
    initialize_matrix(B);
    printf("Matrix B initialized by thread %d\n", omp_get_thread_num());
    }
     }
    // Display the initialized matrices (main thread only)
    printf("\nInitialization complete. Displaying matrices:\n");
    display_matrix("A", A);
    display_matrix("B", B);

    // Set number of threads for parallel operations
    omp_set_num_threads(4);
    printf("\nStarting parallel operations using %d threads...\n", omp_get_max_threads());

    // Matrix addition: C = A + B
    #pragma omp parallel
    {
        #pragma omp for private(i,j)
    for (i = 0; i < SIZE; i++) {
        for (j = 0; j < SIZE; j++) {
            C[i][j] = A[i][j] + B[i][j];
        }
    }

    // Matrix multiplication: D = A * B
    #pragma omp for private(i,j,k)
    for (i = 0; i < SIZE; i++) {
        for (j = 0; j < SIZE; j++) {
           
            D[i][j] = 0;
            for (k = 0; k < SIZE; k++) {
                
                D[i][j] += A[i][k] * B[k][j];
                 
            }
        }
    }

    // Calculate sum of all elements in A
    #pragma omp for reduction(+:sum) private(i,j)
    for (i = 0; i < SIZE; i++) {
        for (j = 0; j < SIZE; j++) {
            sum += A[i][j];
        }
    }

    // Find maximum value in B
    #pragma omp for reduction(max: max_val) private(i,j)
    for (i = 0; i < SIZE; i++) {
        for (j = 0; j < SIZE; j++) {
            if (B[i][j] > max_val) {
                max_val = B[i][j];
            }
        }
    }
    }
    printf("\nParallel operations completed\n");

    // Display results (main thread only)
    printf("\nResults:\n");
    display_matrix("C (A+B)", C);
    display_matrix("D (A*B)", D);

    printf("\nFinal Results:\n");
    printf("Sum of all elements in A: %d\n", sum);
    printf("Maximum value in B: %d\n", max_val);

    return 0;
}