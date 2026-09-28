#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define N 10
#define NUM_THREADS 3

int X;
int arr[N]; 
pthread_mutex_t lock= PTHREAD_MUTEX_INITIALIZER;


// TODO: Implement thread-safe functions
// 1. void* factorial(void* arg)

typedef struct 
{
    int start;
    int end;
    
} thread_data;

int result=1;
 void* factorial(void* arg){
    thread_data* data= arg;
    for(int i=data->start+1;i<=data->end;i++){
        pthread_mutex_lock(&lock);
        result*=i;
        pthread_mutex_unlock(&lock);
    }
 }
int main() {
    
    // TODO: Initialize data, create threads, join threads, and print results
  
    X=5;
    int chunckSize=X/NUM_THREADS;
    pthread_t threads[NUM_THREADS];
    for(int i=0;i<NUM_THREADS;i++){
        thread_data* data= malloc(sizeof(thread_data));
        data->start=i*chunckSize;
        data->end=data->start+chunckSize;
        if(i==NUM_THREADS-1){
            data->end= X;
        }
        pthread_create(&threads[i],NULL,factorial,data);
    }
    for (int i = 0; i < NUM_THREADS; i++)
    {
       pthread_join(threads[i],NULL) ;
    }
    printf("factorial: %d", result);
    
    return 0;
}
