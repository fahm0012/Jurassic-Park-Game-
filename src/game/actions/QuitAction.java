package game.actions;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import game.Application;
import game.Music;

/**
 * QuitAction class allows the player to take action to quit the game.It provides with the option to quit and
 * also ask users to restart the game after they quit the game
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see Action
 */
public class QuitAction extends Action {
    /**
     * This is a varaible of type Display so we can give option for player to quit the game
     */
    Display display = new Display();

    /**
     * This method creates a instance of QuitAction
     */
    public QuitAction(){}


    /**
     * This methods ask users if it want to restart the game and if they choose No (N) then quits the game and
     * says bye to them
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return String where if they say No(N) they dont want to restart the game it returns Goodbye message to users
     *          otherwise returns empty string
     */
    @Override
    public String execute(Actor actor, GameMap map){
        display.println("Restart game? [Y/N]");
        char choice = display.readChar();

        if(choice == 'N' || choice == 'n'){
            display.println("Goodbye.");
            System.exit(0);
        }
        else
            Music.stop = true;
            Application.main(new String[]{""});

        return "";
    }

    /**
     * gives description that Player quited the game
     * @param actor The actor performing the action.
     * @return String gives description that Player quited the game
     */
    @Override
    public String menuDescription(Actor actor){return "Quit game";}
}
