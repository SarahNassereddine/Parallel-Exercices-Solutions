#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <omp.h>

void main(){

    omp_lock_t lock;
    omp_init_lock(&lock);
    #pragma omp parallel for
        for(int i=0;i<10;i++){
           omp_set_lock(&lock);
           printf("%d \n",i);
           omp_unset_lock(&lock);
        }

    omp_destroy_lock(&lock);
}