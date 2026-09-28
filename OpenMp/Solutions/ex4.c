#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <omp.h>

#define SIZE 1000

int main() {
    double **A = (double**)malloc(SIZE * sizeof(double*));
    double **B = (double**)malloc(SIZE * sizeof(double*));
    double **C = (double**)malloc(SIZE * sizeof(double*));
    
    for (int i = 0; i < SIZE; i++) {
        A[i] = (double*)malloc(SIZE * sizeof(double));
        B[i] = (double*)malloc(SIZE * sizeof(double));
        C[i] = (double*)malloc(SIZE * sizeof(double));
    }
    
    // Initialize matrices
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            A[i][j] = i + j;
            B[i][j] = i * j;
        }
    }
    
   double start= omp_get_wtime();
    
    // Matrix multiplication
    #pragma omp parallel for
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            C[i][j] = 0.0;
            for (int k = 0; k < SIZE; k++) {
                C[i][j] += A[i][k] * B[k][j];
            }
        }
    }
    
    double end= omp_get_wtime();
    
    double time_taken = ((double)(end - start));
    printf("Time taken: %f seconds\n", time_taken);
    
    // Free memory
    for (int i = 0; i < SIZE; i++) {
        free(A[i]);
        free(B[i]);
        free(C[i]);
    }
    free(A);
    free(B);
    free(C);
    
    return 0;
}
