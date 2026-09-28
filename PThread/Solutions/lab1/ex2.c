#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define NUM_THREADS 100
#define VECTOR_SIZE 1000000
pthread_mutex_t lock =PTHREAD_MUTEX_INITIALIZER;
typedef struct{
    double* a;
    double* b;
    double* sum;
    int start;
    int end;
} thread_data_t;


void* dotProduct(void* args) {
    thread_data_t* data= (thread_data_t*)args;
    for (int i = data->start; i < data->end; i++) {
        pthread_mutex_lock(&lock);
       *(data->sum) += data->a[i] * data->b[i];
          pthread_mutex_unlock(&lock);
    }
  
}

int main() {
    double *a = (double*)malloc(VECTOR_SIZE * sizeof(double));
    double *b = (double*)malloc(VECTOR_SIZE * sizeof(double));
    double sum=0;

     int chunckSize= VECTOR_SIZE/NUM_THREADS;
    pthread_t threads[NUM_THREADS];


    
    // Initialize vectors
    for (int i = 0; i < VECTOR_SIZE; i++) {
        a[i] = 1.0;
        b[i] = 2.0;
    }
      for(int i=0;i<NUM_THREADS;i++){
        thread_data_t* data= malloc(sizeof(thread_data_t));
        data->a=a;
        data->b=b;
        data->sum=&sum;
        data->start=i*chunckSize;
        data->end= data->start+chunckSize;
          if(i==NUM_THREADS-1){
            data->end=VECTOR_SIZE;
        }
        pthread_create(&threads[i], NULL, dotProduct, data);
      }
       for(int i=0;i<NUM_THREADS;i++){
        pthread_join(threads[i],NULL);
    }
  
    printf("Dot product: %f\n", sum);
    
    free(a);
    free(b);
    return 0;
}
