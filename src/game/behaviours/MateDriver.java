package game.behaviours;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.actions.GettingMatedDoNothing;
import game.dinosaur.HungryDino;

/**
 * This a driver which controls the mating of the HungryDino(dinos).
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see  Behaviour
 */
public class MateDriver implements Behaviour {

    /**
     * this is the actions taken by the Actor(HungryDinos) during mating
     */
    private Actions actions;

    /**
     * This method creates instance of MateDriver
     * @param actions done by the actor while mating
     */
    public MateDriver(Actions actions){this.actions = actions;}

    /**
     *This methods checks if actor is a HungryDino and allows them to mate if possible and controls the mating process
     * of dinos
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return Action that should be taken for Mating process
     */
    @Override
    public Action getAction(Actor actor, GameMap map){

        HungryDino dino;

        if(actor instanceof HungryDino)
            dino = (HungryDino)actor;
        else
            return null;

        if(dino.getMating()){
            dino.setMating(false);
            return new GettingMatedDoNothing();
        }

        for(Action action: actions){
            if(action.menuDescription(actor) == "Mate"){
                Action mate = ((Behaviour)action).getAction(actor,map);
                if(mate != null){
                    return mate;
                }
                else if(dino.getMating()){
                    dino.setMating(false);
                    return new GettingMatedDoNothing();
                }
            }
        }


        return null;

    }
}
