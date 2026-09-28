//112872_SARAH_NASSEREDDINE
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <time.h>

#define THREAD_COUNT 10
#define OUTLET_COUNT 3

pthread_mutex_t lock=PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t canConnect=PTHREAD_COND_INITIALIZER;
int connectedCount=0; 

void* use_lamp(void* arg) {
    int thread_id = *(int*)arg;
    pthread_mutex_lock(&lock);
    printf("[Thread %d] Wanting electrical current...\n", thread_id);
    while(connectedCount==OUTLET_COUNT){
        pthread_cond_wait(&canConnect,&lock);
    }
    connectedCount++;
    printf("[Thread %d] Connected to power strip. Lamp is ON.\n", thread_id);
    pthread_mutex_unlock(&lock);
    sleep(1); // Lamp remains on for 1 second
    pthread_mutex_lock(&lock);
    printf("[Thread %d] Disconnected. Lamp is OFF.\n", thread_id);
    connectedCount--;
    pthread_cond_broadcast(&canConnect);
    pthread_mutex_unlock(&lock);
    free(arg);
    return NULL;
}

int main() {
    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    pthread_t threads[THREAD_COUNT];

    for (int i = 0; i < THREAD_COUNT; i++) {
        int* id = malloc(sizeof(int));
        *id = i + 1;
        pthread_create(&threads[i],NULL,use_lamp,id);

    }
    for(int i=0;i<THREAD_COUNT;i++){
        pthread_join(threads[i],NULL);
    }

    clock_gettime(CLOCK_MONOTONIC, &end);
    double time_taken = (end.tv_sec - start.tv_sec) + (end.tv_nsec - start.tv_nsec) / 1e9;
    printf("Execution time: %.6f seconds\n", time_taken);

    return 0;
}
