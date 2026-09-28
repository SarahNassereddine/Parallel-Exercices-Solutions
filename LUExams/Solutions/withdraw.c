#include<stdio.h>
#include<omp.h>

int main() {
    const int REPS = 25;
    int i;
    double balance = 0.0;
  	int tid;
    printf("\nYour starting bank account balance is %0.2f\n", balance);
    // simulate many deposits        
    #pragma omp parallel for num_threads (16) private(i,tid) reduction(+:balance)      
    for (i = 0; i < REPS; i++) {
		tid = omp_get_thread_num();
        printf("I am thread %d and I will deposit %d*%d = %d  $ \n", tid,tid, i, i*tid);                    
        balance += tid*i;
    }
    printf("\nAfter %d  deposits, your balance is %0.2f\n", REPS, balance);
	// simulate many withdraws  
     #pragma omp parallel for num_threads (8) private(i,tid) reduction(+:balance)                    
    for (i = 0; i < REPS; i++) {
    	tid = omp_get_thread_num();
        printf("I am thread %d and I will withdraw %d*%d = %d  $\n", tid,tid, i, i*tid);                     
        balance += -tid*i;
    }
    printf("\nAfter %d withdrawals, your balance is %0.2f\n\n", REPS, balance);
    return 0;
}

