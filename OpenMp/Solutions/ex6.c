#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <omp.h>

void task1()
{
    printf("Task 1: Processing data...\n");
    sleep(2);
    printf("Task 1: Done\n");
}

void task2()
{
    printf("Task 2: Reading file...\n");
    sleep(2);
    printf("Task 2: Done\n");
}

void task3()
{
    printf("Task 3: Computing...\n");
    sleep(2);
    printf("Task 3: Done\n");
}

int main()
{
#pragma omp parallel sections
    {
#pragma omp section
        {
            task1();
        }
#pragma omp  section
        {
            task2();
        }
#pragma omp  section
        {
            task3();
        }
    }
    return 0;
}
