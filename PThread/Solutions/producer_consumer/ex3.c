#include<stdio.h>
#include<pthread.h>
#include<stdlib.h>
 
#define Buffer_Limit 10
#define CHUNCK_SIZE 2
 
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
        while(Buffer_Index_Value==0){
            pthread_cond_wait(&canConsume,&lock);
        }
        
      
        for(int i=0;i<CHUNCK_SIZE;i++){
  printf("Consumer:%d\n", Buffer_Index_Value--);
        }
            pthread_cond_broadcast(&canProduce);
          
      
        pthread_mutex_unlock(&lock);
            
    }    
}
 
void *Producer(void *arg)
{    
    while(1)
    { 
        // TODO: Implement synchronization
          pthread_mutex_lock(&lock);
        while(Buffer_Index_Value ==Buffer_Limit){
            pthread_cond_wait(&canProduce,&lock);
        }
       
         
        for(int i=0;i<CHUNCK_SIZE;i++){
 
        Buffer_Queue[Buffer_Index_Value++] = '@';
        printf("Producer:%d\n", Buffer_Index_Value);
        }
            pthread_cond_broadcast(&canConsume);
        
      
        pthread_mutex_unlock(&lock);
    }    
}
 
int main()
{    
    pthread_t producer_thread_id, consumer_thread_id;
    Buffer_Queue = (char *) malloc(sizeof(char) * Buffer_Limit);            
    pthread_create(&producer_thread_id, NULL, Producer, NULL);
    pthread_create(&consumer_thread_id, NULL, Consumer, NULL);
    pthread_join(producer_thread_id, NULL);
    pthread_join(consumer_thread_id, NULL);
    return 0;
}
