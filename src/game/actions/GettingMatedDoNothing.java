package game.actions;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * GettingMatedDoNothing allows HungryDino (dinos) to take action to do nothing when they are being mated
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see Action
 */
public class GettingMatedDoNothing extends Action {

    /**
     * this method creates instance of GettingMatedDoNothing
     */
    public GettingMatedDoNothing() {
    }

    /**
     * This method tells that actor which is a dino(HungryDino) is getting mated
     * @param actor The actor(HungryDino) performing the action.
     * @param map The map the actor is on.
     * @return String which tells that dino(HungryDino) is getting mated
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return menuDescription(actor);
    }

    /**
     * @param actor The actor performing the action.
     * @return String which tells that dino(HungryDino) is getting mated
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " is getting mated.";
    }
}
