package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.actions.DrinkAction;
import game.actions.FeedDinoAction;
import game.actions.LayEggAction;
import game.behaviours.*;
import game.behaviours.MoveToFoodSource;
import game.player.Player;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents the Allosaur dino
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see HungryDino
 */
public class Allosaur extends HungryDino{

    /**
     * List of behaviours a Allosaur can do.
     */
    private List<Behaviour> actionFactories = new ArrayList<Behaviour>();

    /**
     * This methods creates the instance of Allosaur class
     * @param name  name of the Allosaur dino.
     * @param gender gender of the Allosaur dino.
     */
    public Allosaur(String name, char gender) {
        this.name = name;
        this.gender = gender;
        displayChar = 'a';
        growthState = 'C';
        hitPoints = 20;
        maxHitPoints = 100;
        minHitPoints = 90;
        age = 0;
        ageToBecomeAdult = 50;
        eatingType = 'C';
        incubationPeriodTurns = 20;
        turnsBeingUnconscious = 0;
        turnsUnconThirst = 0;
        turnsToBecomeCorpse = 20;
        waterLvl = 60;
        minWaterLvl = 40;
        maxWaterLvl = 100;
        mating = false;


        actionFactories.add(new EatBehaviour());
        actionFactories.add(new MoveToFoodSource());
        actionFactories.add(new MoveToMateBehaviour());
        actionFactories.add(new WanderBehaviour());


    }

    /**
     * This methods tells what actions can be done on Allosaur by other actors.
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return List of Actions which can be done by other actors on Allosaur.
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {

        Actions list = super.getAllowableActions(otherActor,direction,map);
        list.add(new FeedDinoAction(this,direction));

        // checks that actor is not a Player as a Player cant mate with a dino
        if(!(otherActor instanceof Player)){
            list.add(new MateBehaviour(this));
        };

        return list;
    }

    /**
     * This method runs at each turn and in this method Allosaur makes decision to do a specific action
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return Action chosen by Allosaur at each turn
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {

        // update the health of the Allosaur dino
        updateHealthStats(map);

        // check if Allosaur dino is not conscious
        if(!isConscious()){
            displayChar = 'u';
            return new DoNothingAction();
        }
        else
            displayChar = 'a';

        //checks if the Allosaur dino has egg
        if(hasEgg){
            //display.println("turnsE:"+turnsHasEgg);
            if(turnsHasEgg == incubationPeriodTurns){
                return new LayEggAction();
            }
            else
                turnsHasEgg++;
        }

       // display.println("Health: " + hitPoints);

        for(Action action: actions){
            if(!map.contains(this))
                break;
            if(action.menuDescription(this) == "EatPter"){
                Action eat = ((Behaviour)action).getAction(this,map);
                if(eat != null){
                    return eat;
                }
            }
        }


        // checks if in the actions list if there is a action to Attack Steagasours
        for(Action action: actions){
            if(!map.contains(this))
                break;
            if(action.menuDescription(this) == "AttackSteg"){
                Action attack = ((Behaviour)action).getAction(this,map);
                if(attack != null){
                    return attack;
                }
            }
        }

        for(Action action: actions){
            if(!map.contains(this))
                break;
            if(action instanceof DrinkAction){
                if(waterLvl < minWaterLvl){
                    return action;
                }
            }
        }

        //actionFactories.add(0, new DrinkBehaviour(actions));
        actionFactories.add(0, new MateDriver(actions));

        // checking the behaviour in our actionFactories list
        for (Behaviour factory : actionFactories) {
            if(!map.contains(this))
                break;
            Action action = factory.getAction(this, map);
            if(action != null){
                //System.out.println(name + " :"+factory);
                //System.out.println("IM CALLED:" + action.menuDescription(this));
                cleanse(actionFactories);
                return action;
            }
        }
        cleanse(actionFactories);
        return new DoNothingAction();
    }

    /**
     * This methods removes the MateDriver from actionfactories for Allosaur after mating
     * @param actionFactories List of behaviours ALlosaur can do
     */
    private void cleanse(List<Behaviour> actionFactories){
        actionFactories.removeIf(factory -> factory instanceof MateDriver);
    }
}
