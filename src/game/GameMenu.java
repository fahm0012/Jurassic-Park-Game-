package game;

import java.util.Scanner;
import edu.monash.fit2099.engine.Display;
import game.actions.QuitAction;
import game.player.Player;
import game.portableItems.Egg;

/**
 * This class represents the GameMenu console shown to the Player
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 */
public class GameMenu {
    /**
     * display toc show on the console
     */
    Display display = new Display();
    /**
     * choice chosen by the Player
     */
    private int choice;

    /**
     * This method prints the menu for the player and checks what option player choose to play
     */
    public void display(){
        PrintMenu();
        choice = ChooseOption();

        if(choice == 1){
            ChallengeMode();
        }
        else if(choice == 3){
            display.println("Goodbye.");
            System.exit(0);
        }
    }

    /**
     * This method is a display for the challenge mode of the game
     */
    private void ChallengeMode(){
        Scanner input = new Scanner(System.in);
        boolean valid = false;

        while(!valid) {
            display.println("Enter EcoPoints to win: ");
            int ecoPoints = input.nextInt();
            display.println("Enter turns: ");
            int turns = input.nextInt();

            if (ecoPoints < 0 || turns <=0){
                display.println("Invalid, pick value >0");
            }
            else{
                valid = true;

                Player.ecoPoints = 0;
                Player.pointsToWin = ecoPoints;
                Player.turnsToWin = turns;
                Player.gameMode = 1;

            }
        }

    }


    /**
     * The menu displayed to player at the start of the game
     */
    private void PrintMenu(){

        display.println("------------------------------------------------");
        String title = String.format("%34s", "JURASSIC PARK GAME MENU");
        display.println(title);
        display.println("------------------------------------------------");
        display.println("Select a game mode.\n");
        display.println("1. Challenge Mode");
        display.println("2. Sandbox");
        display.println("3. Quit");
    }

    /**
     * @return the option chosen by player in a int type
     */
    private int ChooseOption(){
        boolean validOption = false;
        while(!validOption){
            display.println("Enter option: ");
            char c = display.readChar();
            choice = c - '0';
            if(choice < 1 || choice > 3)
                display.println("Invalid option.");
            else
                validOption = true;
        }
        return  choice;
    }
}
