#include<stdio.h>
#include<pthread.h>
#include<stdlib.h>
 
#define Buffer_Limit 25
#define NUM_THREADS 5
#define CHUNCK_SIZE 5
 

int Buffer_Index_Value = 0;
char *Buffer_Queue;
pthread_mutex_t lock= PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t canProduce = PTHREAD_COND_INITIALIZER;
 pthread_cond_t canConsume = PTHREAD_COND_INITIALIZER;

 int ProducerTime=1;
int lastConsumerThreadId=-1;
int lastProducerThreadId=-1;
int doneConsumers[NUM_THREADS];
int donProducers[NUM_THREADS];

void *Consumer(void *arg)
{ int id= *(int*)arg;
    
    while(1)
    {
        // TODO: Implement synchronization
        pthread_mutex_lock(&lock);
        while(ProducerTime || doneConsumers[id]){
            pthread_cond_wait(&canConsume,&lock);
        }
      doneConsumers[id]=1;
      
        for(int i=0;i<CHUNCK_SIZE;i++){
    printf("Consumer %d: %d\n",id, Buffer_Index_Value--);
        }
    
        if(Buffer_Index_Value==0){
            ProducerTime=1;
            for(int i=0;i<NUM_THREADS;i++){
                doneConsumers[i]=0;
            }
            pthread_cond_broadcast(&canProduce);
        }  
        else{
            pthread_cond_broadcast(&canConsume);}
        
        pthread_mutex_unlock(&lock);
            
    }    
}
 
void *Producer(void *arg)
{    int id= *(int*)arg;
  
    while(1)
    { 
        // TODO: Implement synchronization
          pthread_mutex_lock(&lock);
        while(!ProducerTime || donProducers[id]){
            pthread_cond_wait(&canProduce,&lock);
        }
       
       donProducers[id]=1;

           for(int i=0;i<CHUNCK_SIZE;i++){
               Buffer_Queue[Buffer_Index_Value++] = '@';
        printf("Producer %d: %d\n",id, Buffer_Index_Value);}
        if(Buffer_Index_Value==Buffer_Limit){
            ProducerTime=0;
           for(int i=0;i<NUM_THREADS;i++){
            donProducers[i]=0;
           }
            pthread_cond_broadcast(&canConsume);

        }
        else
        pthread_cond_broadcast(&canProduce);
     
        pthread_mutex_unlock(&lock);
    }    
}
 
int main()
{    
    pthread_t producer_thread_ids[NUM_THREADS], consumer_thread_ids[NUM_THREADS];
     Buffer_Queue = (char *) malloc(sizeof(char) * Buffer_Limit);            
  for(int i=0;i<NUM_THREADS;i++){
    doneConsumers[i]=0;
    donProducers[i]=0;
  }
    for(int i=0;i<NUM_THREADS;i++){
        int* id=malloc(sizeof(int));
        *id=i;
  pthread_create(&producer_thread_ids[i], NULL, Producer, id);
    }
       for(int i=0;i<NUM_THREADS;i++){
        int* id=malloc(sizeof(int));
        *id=i;

  pthread_create(&consumer_thread_ids[i], NULL, Consumer, id);
    }
   
         for(int i=0;i<NUM_THREADS;i++){
  pthread_join(producer_thread_ids[i], NULL);
    pthread_join(consumer_thread_ids[i], NULL);
         }
  
    return 0;
}
