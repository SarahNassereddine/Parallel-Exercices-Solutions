//112872_SARAH_NASSEREDDINE
#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define ARRAY_SIZE 1000000

int main() {
    int *array = malloc(ARRAY_SIZE * sizeof(int));
    if (array == NULL) return 1;

    // Initialize array with random-like values
    for (int i = 0; i < ARRAY_SIZE; i++) {
        array[i] = (i * 33) % 10000;
    }

    double sum = 0.0;
    int max_val = array[0];
    int min_val = array[0];
    int count_special = 0;

    double start_time = omp_get_wtime();

    // Task 1: Compute sum
    #pragma omp parallel sections
    {
        #pragma omp section
        {
            printf("Thread %d is calculating sum \n",omp_get_thread_num());
             for (int i = 0; i < ARRAY_SIZE; i++) {
        sum += array[i];
    }
        }
         

    // Task 2: Find maximum

        #pragma omp section
        {
               printf("Thread %d is calculating max \n",omp_get_thread_num());
  for (int i = 0; i < ARRAY_SIZE; i++) {
        if (array[i] > max_val) {
            max_val = array[i];
        }
    }
        }
  

    // Task 3: Find minimum
    #pragma omp section
    {
        printf("Thread %d is calculating min \n",omp_get_thread_num());
     for (int i = 0; i < ARRAY_SIZE; i++) {
        if (array[i] < min_val) {
            min_val = array[i];
        }
    }
    }

   
    }
  

    // Task 4: Count elements divisible by 7

    #pragma omp parallel for reduction(+:count_special)
    for (int i = 0; i < ARRAY_SIZE; i++) {
        if (array[i] % 7 == 0) {
            count_special++;
        }
    }

    double end_time = omp_get_wtime();

    printf("Sum: %.1f\n", sum);
    printf("Max: %d\n", max_val);
    printf("Min: %d\n", min_val);
    printf("Count Divisible by 7: %d\n", count_special);
    printf("Execution time: %.6f seconds\n", end_time - start_time);

    free(array);
    return 0;
}
