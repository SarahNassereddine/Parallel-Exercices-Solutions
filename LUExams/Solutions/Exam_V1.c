#include <stdio.h>
#include <stdlib.h>
#include <windows.h> // Windows OS - Library to use Sleep function
// #include <unistd.h> // Linux OS - Library to use sleep function
#include <omp.h>

// Enum for traffic light states
typedef enum
{
    GREEN,
    YELLOW,
    RED
} TrafficLightState;

void displayGreen();
void displayYellow();
void displayRed();

int main()
{
  
    TrafficLightState state = RED;
    omp_lock_t lock;
    omp_init_lock(&lock);

#pragma omp parallel num_threads(3)
    {

        while (1)
        {
#pragma omp sections
            {

#pragma omp section
                {
                    omp_set_lock(&lock);
                    if (state == RED)
                    {
                        displayGreen();
                        state = GREEN;
                    }
                    omp_unset_lock(&lock);
                }
#pragma omp section
                {
                    omp_set_lock(&lock);
                    if (state == GREEN)
                    {
                        displayYellow();
                        state = YELLOW;
                    }

                    omp_unset_lock(&lock);
                }
#pragma omp section
                {
                    omp_set_lock(&lock);
                    if (state == YELLOW)
                    {
                        state = RED;
                        displayRed();
                    }

                    omp_unset_lock(&lock);
                }
            }
        }
    }

    return 0;
}

void displayGreen()
{

    printf("GO\n");
    Sleep(1000); // Green light for 1 seconds (1000 milliseconds)
}

void displayYellow()
{
    printf("Prepare to STOP\n");
    Sleep(1000); // Yellow light for 1 second (1000 milliseconds)
    // sleep(1); // Red light for 1 second - Linux OS
}

void displayRed()
{
    printf("STOP\n");
    Sleep(2000); // Red light for 2 seconds (2000 milliseconds)
    // sleep(2); // Red light for 2 seconds - Linux OS
}
