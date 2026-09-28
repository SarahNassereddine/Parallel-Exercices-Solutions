#include <pthread.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>


//Vary those variables in order to test your parallelized code.
#define NUM_DOORS 20
#define NUM_PEOPLE 500


int door_in_use[NUM_DOORS];
int door_counts[NUM_DOORS];
pthread_mutex_t lock= PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t canEnter= PTHREAD_COND_INITIALIZER;


void* use_door(void* arg) {
    int id = *(int*)arg;
    int chosen_door = -1;

    //Find a free door, if found enter the door.
       pthread_mutex_lock(&lock);
    printf("Person %d is looking for an available Door \n", id);

    for (int i = 0; i < NUM_DOORS; i++) {
   
        if (door_in_use[i] == 0) {
            //Pass through the door - sleep : Simulation that the person does something..
            chosen_door = i;
            printf("Person %d is entering Door without waiting %d\n", id, chosen_door+1);
            door_in_use[chosen_door]=1;
    pthread_mutex_unlock(&lock);
            sleep(1);
            pthread_mutex_lock(&lock);
            door_counts[chosen_door]++;
            door_in_use[chosen_door]=0;
            printf("Person %d has exited Door %d\n", id, chosen_door+1);
            pthread_cond_broadcast(&canEnter);
                pthread_mutex_unlock(&lock);
        
            break;
        }
    }


        //Wait for an available door if all doors are busy
    if (chosen_door == -1) {
        chosen_door = id % NUM_DOORS; // The thread will take a specified door to wait on it. - DO NOT MODIFY -
        printf("Person %d waiting at Door %d...\n", id, chosen_door);
        while(door_in_use[chosen_door]){
            pthread_cond_wait(&canEnter,&lock);
        }
        // Pass through the door after waiting - sleep : Simulation that the person does something..
        printf("Person %d is entering (after waiting) Door %d\n", id, chosen_door);
        door_in_use[chosen_door]=1;
        pthread_mutex_unlock(&lock);
        sleep(1);
        pthread_mutex_lock(&lock);
        door_in_use[chosen_door]=0;
        door_counts[chosen_door]++;
        // Exit after waiting
        printf("Person %d has exited Door %d\n", id, chosen_door);
        pthread_cond_broadcast(&canEnter);
        pthread_mutex_unlock(&lock);
    }
    return NULL;
}

int main() {
    pthread_t people[NUM_PEOPLE];
    int ids[NUM_PEOPLE];

    // Initialization
    for (int i = 0; i < NUM_DOORS; i++) {
        door_in_use[i] = 0;
        door_counts[i] = 0;
    }
    // Create people threads
    for (int i = 0; i < NUM_PEOPLE; i++) {
        ids[i] = i + 1;
        pthread_create(&people[i], NULL, use_door, &ids[i]);
    }
    // Wait for all threads
    for (int i = 0; i < NUM_PEOPLE; i++) {
        pthread_join(people[i], NULL);
    }

    long sum = 0;
    // Print door usage statistics
    printf("\nDoor usage statistics:\n");
    for (int i = 0; i < NUM_DOORS; i++) {
        printf("Door %d: %d people\n", i+1, door_counts[i]);
        sum= sum + door_counts[i];
    }
    printf("Total : %ld \n", sum);

    return 0;
}
