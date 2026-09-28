#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

#define NUM_ROUNDS 10

typedef struct {
    int id;
    int* p1_choice;
    int* p2_choice;
} player_data;

// Function for the players to make a choice (rock, paper, or scissors)
void *player_choice(void *arg) {
    // Seed the random number generator uniquely for each thread
    player_data* data =(player_data*)arg;
    srand(time(NULL) ^ pthread_self());

    // Simulate the player's choice: 0 (rock), 1 (paper), or 2 (scissors)
    int choice = rand() % 3;
  
    if(data->id==0){
       *(data->p1_choice)=choice;
    }
    else{
      *( data->p2_choice)=choice;
    }
  
return NULL;
}

const char *choices[] = {"Rock", "Paper", "Scissors"};

// Function to determine the winner of a round
const char *determine_winner(int p1, int p2) {
    if (p1 == p2) return "Draw";
    if ((p1 == 0 && p2 == 2) || (p1 == 1 && p2 == 0) || (p1 == 2 && p2 == 1)) return "Player 1 wins";
    return "Player 2 wins";
}

int main() {

    int p1_wins = 0, p2_wins = 0;

    for (int round = 1; round <= NUM_ROUNDS; round++) {
        int p1_choice;
        int p2_choice;

        // Create threads for player choices
        pthread_t player1Thread;
        pthread_t player2Thread;
        player_data p1data;
        p1data.id=0;
        p1data.p1_choice=&p1_choice;
        p1data.p2_choice=&p2_choice;

        player_data p2data;
        p2data.id=1;
        p2data.p1_choice=&p1_choice;
        p2data.p2_choice=&p2_choice;

        pthread_create(&player1Thread,NULL,player_choice, &p1data);
    pthread_create(&player2Thread,NULL,player_choice, &p2data);

        // Wait for the threads to complete and get their results
        pthread_join(player1Thread,NULL);
        pthread_join(player2Thread,NULL);


        // Display the result of the round
        printf("Round %d:\n", round);
        printf("Player 1: %s\n", choices[(int)p1_choice]);
        printf("Player 2: %s\n", choices[(int)p2_choice]);
        const char *result = determine_winner((int)p1_choice, (int)p2_choice);
        printf("Result: %s\n", result);
        printf("--------------------\n");

        // Update scores
        if (result == "Player 1 wins") p1_wins++;
        else if (result == "Player 2 wins") p2_wins++;

    }

    // Display the final result
    printf("Final Result:\n");
    printf("Player 1 wins: %d\n", p1_wins);
    printf("Player 2 wins: %d\n", p2_wins);
    if (p1_wins > p2_wins) printf("Overall winner: Player 1\n");
    else if (p2_wins > p1_wins) printf("Overall winner: Player 2\n");
    else printf("Overall result: Draw\n");

    return 0;
}
