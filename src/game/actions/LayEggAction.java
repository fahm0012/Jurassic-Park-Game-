package game.actions;

import edu.monash.fit2099.engine.*;
import game.dinosaur.HungryDino;

/**
 * LayEggAction is responsible for HungryDino(dino) to take a action to lay egg on the Ground if its possible
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Action
 */
public class LayEggAction extends Action {
    /**
     * creates a instance of LayEggAction
     */
    public LayEggAction(){}

    /**
     * This methods tells us what dino laid egg at what position
     * @param actor The actor(dino) performing the action.
     * @param map The map the actor(dino) is on.
     * @return String which tells what dino laid egg at what position
     */
    @Override
    public String execute(Actor actor, GameMap map){

        HungryDino dino = ((HungryDino)(actor));


        Actions dropActions = new Actions();
        for (Item item : dino.getInventory())
            dropActions.add(item.getDropAction());
        for (Action drop : dropActions)
            drop.execute(dino, map);


        dino.setHasEgg(false);
        dino.setTurnsHasEgg(0);


        return actor + " lays egg at " +"("+ map.locationOf(dino).x() + "," +map.locationOf(dino).y() + ")" ;

    }

    /**
     * @param actor The actor(dino) performing the action.
     * @return String tells us that egg has been laid.
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Lay";
    }
}
