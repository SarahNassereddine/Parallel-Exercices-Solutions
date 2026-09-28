#include<stdio.h>
#include<pthread.h>
#include<stdlib.h>
 
#define Buffer_Limit 20
#define NUM_THREADS 5
int Buffer_Index_Value = 0;
char *Buffer_Queue;
pthread_mutex_t lock= PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t canProduce = PTHREAD_COND_INITIALIZER;
 pthread_cond_t canConsume = PTHREAD_COND_INITIALIZER;

 int ProducerTime=1;


void *Consumer(void *arg)
{
    while(1)
    {
        // TODO: Implement synchronization
        pthread_mutex_lock(&lock);
        while(ProducerTime){
            pthread_cond_wait(&canConsume,&lock);
        }
        
        printf("Consumer:%d\n", Buffer_Index_Value--);
        if(Buffer_Index_Value==0){
            ProducerTime=1;
            pthread_cond_broadcast(&canProduce);
        }  
      
        pthread_mutex_unlock(&lock);
            
    }    
}
 
void *Producer(void *arg)
{    
    while(1)
    { 
        // TODO: Implement synchronization
          pthread_mutex_lock(&lock);
        while(!ProducerTime){
            pthread_cond_wait(&canProduce,&lock);
        }
        
        Buffer_Queue[Buffer_Index_Value++] = '@';
        printf("Producer:%d\n", Buffer_Index_Value);
        if(Buffer_Index_Value==Buffer_Limit){
            ProducerTime=0;
            pthread_cond_broadcast(&canConsume);
        }
      
        pthread_mutex_unlock(&lock);
    }    
}
 
int main()
{    
    pthread_t producer_thread_ids[NUM_THREADS], consumer_thread_ids[NUM_THREADS];
     Buffer_Queue = (char *) malloc(sizeof(char) * Buffer_Limit);            
  
    for(int i=0;i<NUM_THREADS;i++){
  pthread_create(&producer_thread_ids[i], NULL, Producer, NULL);
  pthread_create(&consumer_thread_ids[i], NULL, Consumer, NULL);
    }
    
   
         for(int i=0;i<NUM_THREADS;i++){
  pthread_join(producer_thread_ids[i], NULL);
    pthread_join(consumer_thread_ids[i], NULL);
         }
  
    return 0;
}
