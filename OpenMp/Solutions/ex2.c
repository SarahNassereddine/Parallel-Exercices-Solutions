#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>

#define SIZE 10000000

int main() {
    omp_set_num_threads(4);
    double *array = (double*)malloc(SIZE * sizeof(double));
       // const int ARRAY_SIZE= SIZE;
    
    // Initialize array
    for (int i = 0; i < SIZE; i++) {
        array[i] = 1.0;
    }
     double start = omp_get_wtime();
    double sum = 0.0;
    #pragma omp parallel for reduction(+: sum)
    for (int i = 0; i < SIZE; i++) {
        sum += array[i];
    } double end = omp_get_wtime();
    double time_taken = (double)(end - start);
   
    
    printf("Sum: %f\n", sum);
     printf("Time taken: %f seconds\n", time_taken);
    free(array);
    return 0;
}
//0.008 parallel
//remarque firstprivate expects a constante variable if we use directelly SIZE it is considered before comile as an integer 1000000 not a constant var it needs something that has space in memory and will fork and use a local variable having the same name..