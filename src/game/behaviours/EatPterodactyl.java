package game.behaviours;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import game.dinosaur.HungryDino;
import game.dinosaur.Pterodactyls;

/**
 * EatPterodactyl is a behaviour for Allosaur  to eat Pterodactyl
 * are hungry
 * @author Fahad and Shafkat
 * @version 1.1.0
 * @since 22/05/2021
 * @see Action
 * @see Behaviour
 */
public class EatPterodactyl extends Action implements Behaviour {

    /**
     * Pterodactyls that has to be eaten by Allosaur
     */
    private Pterodactyls target;

    /**
     * This method creates a instance of EatPterodactyl class
     * @param target Pterodactyls that has to be eaten
     */
    public EatPterodactyl(Pterodactyls target){this.target = target;}

    /**
     * @param actor The actor(Allosaur) performing the action.
     * @param map The map the actor(Allosaur) is on.
     * @return String that tells Allosaur ate the whole Pterodactyl
     */
    @Override
    public String execute(Actor actor, GameMap map){
        return actor +" eats " + target + " whole.";
    }

    /**
     *
     * @param actor the Actor(Allosaur) acting
     * @param map the GameMap containing the Actor
     * @return the Action taken by Allosaur to eat Pterodactyl
     */
    @Override
    public Action getAction(Actor actor, GameMap map){
        HungryDino dino;
        if(actor instanceof HungryDino){
            dino = (HungryDino)actor;
        }
        else
            return null;


        int level = Math.min(30, dino.getMaxWaterLvl() - dino.getFoodLevel());

        dino.setFoodLevel(dino.getFoodLevel()+level);
        map.removeActor(target);

        return this;



    }

    /**
     * This method provides with description that Pterodactyl was eaten
     * @param actor The actor performing the action.
     * @return String telling Pterodactyl was eaten
     */
    @Override
    public String menuDescription(Actor actor){ return "EatPter";}
}
