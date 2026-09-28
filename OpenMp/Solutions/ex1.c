#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <omp.h>

#define SIZE 10000000

int main() {
    omp_set_num_threads(4);
    double *array = (double*)malloc(SIZE * sizeof(double));
    
    // Initialize array
    for (int i = 0; i < SIZE; i++) {
        array[i] = i;
    }
    
    double start = omp_get_wtime();
    #pragma omp parallel
    // Process array
    #pragma omp for
    for (int i = 0; i < SIZE; i++) {
        //printf("Thread %d processing\n", omp_get_thread_num());
        array[i] = array[i] * array[i] + 1.0;
    }
    
    double end = omp_get_wtime();
    double time_taken = ((double)(end - start)) ;
    printf("Time taken: %f seconds\n", time_taken);
    
    free(array);
    return 0;
}
