package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.dinosaur.HungryDino;
import game.dinosaur.Stegosaur;


/**
 * AttackStegosaurBehaviour allows the Allosaurs to attack Stegosaur
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Action
 * @see Behaviour
 */
public class AttackStegosaurBehaviour extends Action implements Behaviour {

    /**
     * The target dino to be Attacked which is Stegosaur
     */
    private Stegosaur target;

    /**
     * constructor for AttackStegosaurBehaviour class.Sets the target
     * @param target Steogsaur that is being attacked
     */
    public AttackStegosaurBehaviour(Stegosaur target){this.target = target;}

    /**
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return the String which gives the description of the event of attack.
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        return actor + " attacks " + target;
    }

    /**
     *
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return Action in which it  Allosaur attacks the Stegosaur if certain conditions are met.
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {

        /*
          if our target is already been attacked by a Allosaur and the attack was done in less than 20 turns then
          this target will not be attacked again
         */
        if(target.isWoundedState()){return null;}
        int damage = 20;
        int levelInc;

        /*
          down casting the Actor instance into HungaryDIno
         */
        HungryDino dino = ((HungryDino) actor);

        /*
          decrements the health od target
         */
        target.hurt(damage);

        /*
          check if our target is not conscious if its not then it died of attack
         */
        if (!target.isConscious()) {
            target.dinoDeath(map);
        }

        /*
          check if a baby ALlosaurs attacked or a child attacked the Stegosaurs
         */
        if(dino.getGrowthState() == 'A'){
            levelInc = 20;
        }
        else
            levelInc = 10;

        /*
          increment the foodlevel of the Allosaurs dino which attacked the Stegosaurs
         */
        int addLevel = Math.min(levelInc, dino.getMaxFoodLevel()- dino.getFoodLevel());
        dino.setFoodLevel(dino.getFoodLevel()+addLevel);

        /*
          seting the target Stegosaurs dino wound state to be true
         */
        target.setWoundedState(true);
        return this;
    }

    /**
     * Gives description of what happened.
     * @param actor The actor performing the action.
     * @return String which tells that a
     */
    @Override
    public String menuDescription(Actor actor) { return "AttackSteg"; }

}
