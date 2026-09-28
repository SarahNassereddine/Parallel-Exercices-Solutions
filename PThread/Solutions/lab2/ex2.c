#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define N 10
#define NUM_THREADS 3

int X;
int arr[N]; 
pthread_mutex_t lock= PTHREAD_MUTEX_INITIALIZER;


// TODO: Implement thread-safe functions
// 4. void* minMax(void* arg)
int in=0;
int max;
int min;
 void* maxmin(void* arg){
  while(1){
  
    pthread_mutex_lock(&lock);
      if(in==N){
        pthread_mutex_unlock(&lock);
        return NULL;
    }
    if(arr[in]>max){
        max=arr[in];
    }
    if(arr[in]<min){
        min=arr[in];
    }
    in++;
    pthread_mutex_unlock(&lock);
  }
    
 }
int main() {
    for(int i=0;i<N;i++){
        arr[i]=i;

    }
    max=arr[0];
    min=arr[0];
    // TODO: Initialize data, create threads, join threads, and print results
  
    pthread_t threads[NUM_THREADS];
    for(int i=0;i<NUM_THREADS;i++){
      
        
        pthread_create(&threads[i],NULL,maxmin,NULL);
    }
    for (int i = 0; i < NUM_THREADS; i++)
    {
       pthread_join(threads[i],NULL) ;
    }
    printf("max: %d  min: %d",max,min);
    
    return 0;
}
