#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define SIZE 10000000

int main() {
    int *array = (int*)malloc(SIZE * sizeof(int));
    
    // Initialize array
    for (int i = 0; i < SIZE; i++) {
        array[i] = i % 100;
    }
    double start= omp_get_wtime();
    int count = 0;
    #pragma omp parallel for 
    for (int i = 0; i < SIZE; i++) {
        if (array[i] > 50) {
            #pragma omp atomic
            
            count++;
        }
    }
      double end= omp_get_wtime();
    
    double time_taken = ((double)(end - start));
    printf("Time taken: %f seconds\n", time_taken);
    
    printf("Count: %d\n", count);
    free(array);
    return 0;
}
