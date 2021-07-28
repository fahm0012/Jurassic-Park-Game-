package game.dinosaur;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import game.actions.DrinkAction;
import game.actions.FeedDinoAction;
import game.actions.LayEggAction;
import game.behaviours.*;
import game.behaviours.MoveToFoodSource;
import game.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents Brachiosaur dino
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see HungryDino
 */
public class Brachiosaur extends HungryDino {


    /**
     * List of behaviours a Brachiosaur can do.
     */
    private List<Behaviour> actionFactories = new ArrayList<Behaviour>();

    /**
     * This methods creates the instance of Brachiosaur class.
     * @param name(String)  name of the Brachiosaur dino.
     * @param gender(char)  name of the Brachiosaur dino.
     * @param growthState(char) Tells if its a Adult or Child Brachiosaur
     * @param hitPoints(int) FoodLevel of Brachiosaur
     */
    public Brachiosaur(String name, char gender, char growthState, int hitPoints) {
        this.name = name;
        this.gender = gender;
        this.growthState = growthState;
        this.hitPoints = hitPoints;
        displayChar = 'b';
        maxHitPoints = 160;
        minHitPoints = 140;
        if (growthState == 'A')
            age = 50;
        else
            age = 0;
        ageToBecomeAdult = 50;
        eatingType = 'H';
        incubationPeriodTurns = 30;
        turnsBeingUnconscious = 0;
        turnsUnconThirst = 0;
        turnsToBecomeCorpse = 15;
        waterLvl = 60;
        minWaterLvl = 40;
        maxWaterLvl = 200;
        mating = false;

        actionFactories.add(new EatBehaviour());
        actionFactories.add(new MoveToWaterSource());
        actionFactories.add(new MoveToMateBehaviour());
        actionFactories.add(new MoveToFoodSource());
        actionFactories.add(new WanderBehaviour());
    }

    /**
     * This methods tells what actions can be done on Brachiosaur by other actors.
     * @param otherActor(Actor) the Actor that might be performing attack
     * @param direction(String)  String representing the direction of the other Actor
     * @param map(GamMap)        current GameMap
     * @return List of Actions which can be done by other actors on Brachiosaur.
     */
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions list = super.getAllowableActions(otherActor,direction,map);
        list.add(new FeedDinoAction(this,direction));
        if(!(otherActor instanceof Player)){
            list.add(new MateBehaviour(this));
        }

        return list;
    }

    /**
     * This method allows Brachiosaur to make decision on every turn to do specific behaviour.
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return Action chosen by Brachiosaur at each turn
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {

        //display.println("FL: " + hitPoints + ",WL: " + waterLvl);

        // update the health of the Brachiosaur dino
        updateHealthStats(map);

        // check if Brachiosaur dino is not conscious
        if(!isConscious()){
            displayChar = 'u';
            return new DoNothingAction();
        }
        else
            displayChar = 'b';

        //checks if the Brachiosaur dino has egg
        if(hasEgg){
            //display.println("turnsE:"+turnsHasEgg);
            if(turnsHasEgg == incubationPeriodTurns){
                return new LayEggAction();
            }
            else
                turnsHasEgg++;
        }

        for(Action action: actions){
            if(action instanceof DrinkAction){
                if(!map.contains(this))
                    break;
                if(waterLvl < minWaterLvl){
                    return action;
                }
            }
        }

        //display.println(name +" Egg: "+ hasEgg);
        //display.println("Health: "+hitPoints);
        //display.println("Age: " + age +"," + "Status: " + growthState);

        //actionFactories.add(0, new DrinkBehaviour(actions));
        actionFactories.add(0, new MateDriver(actions));


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
     * This methods removes the MateDriver from actionfactories for Brachiosaur after mating
     * @param actionFactories List of behaviours Brachiosaur can do
     */
    private void cleanse(List<Behaviour> actionFactories){
        actionFactories.removeIf(factory -> factory instanceof MateDriver);
    }
}




