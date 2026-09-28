#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <omp.h>

void main(){
    omp_set_nested(1);
    #pragma omp parallel num_threads(2)
    {
        int p=omp_get_thread_num();
        printf("Parent %d\n",omp_get_thread_num());
        #pragma omp parallel num_threads(2)
        {int c=omp_get_thread_num();
        printf("Child Thread %d (My Parent is Thread %d) executing loop2 \n ",c,p);
        }
    }
}