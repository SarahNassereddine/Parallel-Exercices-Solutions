#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

#define ARRAY_SIZE 1000000
#define NUM_THREADS 20

typedef struct{
    int start;
    int end; 
    int* array;
} t_data;
void* processArray(void* attr) {
    t_data* data=(t_data*)attr;
    for (int i = data->start; i < data->end; i++) {
        data->array[i] =  data->array[i] *  data->array[i] + 1; // Some computation
    }
}

int main() {
    int *array = (int*)malloc(ARRAY_SIZE * sizeof(int));
    int chunckSize= ARRAY_SIZE/NUM_THREADS;

    pthread_t threads[NUM_THREADS];

    // Initialize array
    for (int i = 0; i < ARRAY_SIZE; i++) {
        array[i] = i;
    }
    
    clock_t start = clock();
    for(int i=0;i<NUM_THREADS;i++){
        t_data* data= malloc(sizeof(t_data));
      
        data->array=array;
        data->start=i*chunckSize;
        data->end= data->start+chunckSize;
          if(i==NUM_THREADS-1){
            data->end=ARRAY_SIZE;
        }
        pthread_create(&threads[i],NULL, processArray, data);
    }

    for(int i=0;i<NUM_THREADS;i++){
        pthread_join(threads[i],NULL);
    }
    clock_t end = clock();
    
    double time_taken = ((double)(end - start)) / CLOCKS_PER_SEC;
    printf("Time taken: %f seconds\n", time_taken);
    
    free(array);
    return 0;
}
