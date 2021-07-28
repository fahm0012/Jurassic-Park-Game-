package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.dinosaur.HungryDino;
import game.dinosaur.Pterodactyls;
import game.grounds.Tree;
import game.portableItems.AllosaurEgg;
import game.portableItems.BrachiosaurEgg;
import game.portableItems.PterodactylEgg;
import game.portableItems.StegosaurEgg;

/**
 * This class allows the HungryDino (dinos) to mate if its possible
 * @author Fahad and Shafkat
 * @version 1.1.0
 * @since 22/05/2021
 * @see Action
 * @see Behaviour
 */
public class MateBehaviour extends Action implements Behaviour{

    /**
     * The target dino with which the current dino mates
     */
    private HungryDino target;

    /**
     * This method creates a instance of MateBehaviour
     * @param target dino with which the current dino mates
     */
    public MateBehaviour(HungryDino target){ this.target = target;
    }

    /**
     * This method tells what actor mates with which actor
     * @param actor The dino performing the action.
     * @param map The map the dino is on.
     * @return String which tells us that what actor mates with which actor
     */
    @Override
    public String execute(Actor actor, GameMap map){


        return actor + " mates with " + target;

    }

    /**
     * This method checks if its possible for our current dino to mate with it target actor.
     * @param actor the dino acting
     * @param map the GameMap containing the dino
     * @return Action if current dino can mate with target dino otherwise returns null.
     */
    @Override
    public Action getAction(Actor actor, GameMap map){

        if( !isReady(actor) || !sameSpecies(actor,target) || !isReady(target) || !oppositeSex((HungryDino)actor,target)) {
            return null;
        }
        else{
            //target.mating = true;
            //Pter not in contiguous trees.
            if(actor instanceof Pterodactyls && ( !(map.locationOf(actor).getGround() instanceof Tree) || !(map.locationOf(target).getGround() instanceof Tree) ) )
                return null;

            mate(((HungryDino)actor),target);
            return this;
        }

    }

    /**
     * This method gives description what action took place.Over here Mating took place
     * @param actor The dino performing the action.
     * @return String that indicates that dinos Mate
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Mate";
    }

    /**
     * This method checks if the dino can mate
     * @param actor the dino acting
     * @return true if dino can mate
     */
    private boolean isReady(Actor actor){

        int level;

        if (actor.getDisplayChar() == 'd' || actor instanceof Pterodactyls)
            level = 50;
        else if (actor.getDisplayChar() == 'b')
            level = 70;
        else
            level = 50;

        return (((HungryDino)actor).getFoodLevel() >= level && ((HungryDino)actor).getGrowthState() == 'A' && !(((HungryDino)actor).getHasEgg()));

    }

    /**
     * This methods checks if both dinos are of same species
     * @param actor the dino acting
     * @param target the dino with which mating will take place
     * @return true if both dinos are same species
     */
    private boolean sameSpecies(Actor actor, Actor target){
        return actor.getDisplayChar() == target.getDisplayChar();
    }

    /**
     * This method checks if both dinos are of opposite sex
     * @param actor the dino acting
     * @param target the dino with which mating will take place
     * @return true if both dinos are of opposite sex
     */
    private boolean oppositeSex(HungryDino actor, HungryDino target){
        return (actor.getGender() != target.getGender());
    }

    /**
     * This method causes mating between to dinos and puts the egg into the female dino.
     * @param actor the dino acting
     * @param target the dino with which mating will take place
     */
    private void mate(HungryDino actor, HungryDino target){

        HungryDino male, female;

        if (actor.getGender() == 'M'){
            male = actor;
            female = target;
        }
        else{
            male = target;
            female = actor;
        }

        if(actor == male) {
            female.setMating(true);
        }
        else {
            male.setMating(true);
        }


        if(actor.getDisplayChar() == 'd'){
            StegosaurEgg egg = new StegosaurEgg();
            female.addItemToInventory(egg);
        }
        else if(actor.getDisplayChar() == 'b'){
            BrachiosaurEgg egg = new BrachiosaurEgg();
            female.addItemToInventory(egg);
        }
        else if(actor instanceof Pterodactyls){
            PterodactylEgg egg = new PterodactylEgg();
            female.addItemToInventory(egg);
        }
        else {
            AllosaurEgg egg = new AllosaurEgg();
            female.addItemToInventory(egg);
        }
        female.setHasEgg();
    }
}
