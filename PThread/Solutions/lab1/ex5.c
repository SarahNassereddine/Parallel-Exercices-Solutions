#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#define NUM_READERS 5
#define NUM_WRITERS 5

int shared_data = 0;
int reader_count = 0;

// TODO: Add read-write lock
pthread_rwlock_t rwlock = PTHREAD_RWLOCK_INITIALIZER;

void* reader(void *arg) {
    int id = *((int*)arg);
    // Acquire read lock
    pthread_rwlock_rdlock(&rwlock);
    // Read shared_data
    printf("Reader %d read: %d\n", id, shared_data);
    // Release read lock
    pthread_rwlock_unlock(&rwlock);
    return NULL;
}

void* writer(void *arg) {
    int id = *((int*)arg);
    // Acquire write lock
       pthread_rwlock_wrlock(&rwlock);
    // Write to shared_data
    shared_data++;
    printf("Writer %d wrote: %d\n", id, shared_data);
    // Release write lock
 pthread_rwlock_unlock(&rwlock);
    return NULL;
}

int main() {
    // Create multiple reader and writer threads
    pthread_t writerThread, readerThread;
        pthread_t writers[NUM_WRITERS];
        pthread_t readers[NUM_READERS];

  
    // Create producer and consumer threads
    for(int i=0;i<NUM_READERS;i++){
        int* id=malloc(sizeof(int));
        *id=i;
          pthread_create(&readers[i], NULL, &reader, id);
    }
    for(int i=0;i<NUM_WRITERS;i++){
        int* id=malloc(sizeof(int));
        *id=i;
          pthread_create(&writers[i], NULL, &writer, id);
    }
    // Join threads
    for(int i=0;i<NUM_WRITERS;i++){
           pthread_join(writers[i],NULL);
    }
     for(int i=0;i<NUM_READERS;i++){
           pthread_join(readers[i],NULL);
    }
 
    return 0;
}
