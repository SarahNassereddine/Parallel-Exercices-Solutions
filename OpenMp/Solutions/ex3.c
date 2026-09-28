#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define SIZE 10000000

int main() {
    omp_set_num_threads(4);
    double *a = (double*)malloc(SIZE * sizeof(double));
    double *b = (double*)malloc(SIZE * sizeof(double));
    const int ARRAY_SIZE=SIZE;
    // Initialize vectors
    for (int i = 0; i < SIZE; i++) {
        a[i] = 1.0;
        b[i] = 2.0;
    }
    
    double dot_product = 0.0;
    double start= omp_get_wtime();
    #pragma omp parallel for reduction(+: dot_product) schedule(guided)
    for (int i = 0; i < SIZE; i++) {
        
        dot_product += a[i] * b[i];
    }double end = omp_get_wtime();
    double time_taken = (double)(end - start);
   
    
    printf("Dot product: %f\n", dot_product);
     printf("Time taken: %f seconds\n", time_taken);
    
    free(a);
    free(b);
    return 0;
}
//guided Time taken: 0.013000 seconds
//dynamic Time taken: 0.331000 seconds
//dynamic with cunck size 100 Time taken: 0.013000 seconds
//static Time taken: 0.011000 seconds

