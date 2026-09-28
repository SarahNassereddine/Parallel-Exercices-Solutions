#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define BUFFER_SIZE 10
#define NUM_ITEMS 100
#define NUM_THREADS_EACH 5

int buffer[BUFFER_SIZE];
int count = 0; // Number of items in buffer
int in = 0;   // Index for producer
int out = 0;  // Index for consumer

// TODO: Add mutex and condition variables

pthread_mutex_t lock= PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t canProduce = PTHREAD_COND_INITIALIZER;
pthread_cond_t canConsume = PTHREAD_COND_INITIALIZER;

void* producer(void *arg) {
    int id=*((int*)arg);
    for (int i = 0; i < NUM_ITEMS; i++) {
        pthread_mutex_lock(&lock);
        while(count==BUFFER_SIZE){
            pthread_cond_wait(&canProduce,&lock);
        }
        count++;
        buffer[in]=i;
        in= (in+1)%BUFFER_SIZE;
     
        // Wait if buffer is full
        // Add item to buffer
        // Signal consumer
        printf("producer %d Produced: %d\n",id, i);
           pthread_cond_broadcast(&canConsume);
           pthread_mutex_unlock(&lock);
    }
    return NULL;
}

void* consumer(void *arg) {
     int id=*((int*)arg);
    for (int i = 0; i < NUM_ITEMS; i++) {

        // Wait if buffer is empty
        // Remove item from buffer
        // Signal producer
        pthread_mutex_lock(&lock);
        while(count==0){
            pthread_cond_wait(&canConsume, &lock);
        }
        count--;
        int item=buffer[out];
        out=(out+1)%BUFFER_SIZE;
        printf("consumer %d Consumed: %d\n",id, i);
        pthread_cond_broadcast(&canProduce);
        pthread_mutex_unlock(&lock);
    }
    return NULL;
}

int main() {
    // Create producer and consumer threads
    // Join threads


    pthread_t producers[NUM_THREADS_EACH];
    pthread_t consumers[NUM_THREADS_EACH];
    for(int i=0;i<NUM_THREADS_EACH;i++){
        int* pid= malloc(sizeof(int));
        *pid=i;
          int* cid= malloc(sizeof(int));
        *cid=i;
        pthread_create(&producers[i],NULL,producer, pid);
          pthread_create(&consumers[i],NULL,consumer, cid);
    }
    for(int i=0;i<NUM_THREADS_EACH;i++){
        pthread_join(producers[i],NULL);
        pthread_join(consumers[i],NULL);
    }
    return 0;
}
