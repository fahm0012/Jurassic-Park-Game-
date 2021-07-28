package game.dinosaur;

import edu.monash.fit2099.engine.*;
import game.actions.*;
import game.behaviours.*;
import game.grounds.Tree;
import game.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a Pterodactyls dino
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see HungryDino
 */
public class Pterodactyls extends HungryDino{

    /**
     * List of behaviours a Pterodactyl can do.
     */
    private List<Behaviour> actionFactories = new ArrayList<Behaviour>();

    /**
     * tells if Pterodactyl is flying
     */
    private boolean isFlying;

    /**
     * tells for how long Pterodactyl is flying for
     */
    private int numOfTrnFlying;

    /**
     * tells after how many turns the Pterodactyls gets tired and stops flying
     */
    private int turnsLimitToFly;


    /**
     * @return true if Pterodactyl is flying otherwise false
     */
    public boolean getIsFlying(){return isFlying;}

    /**
     * @param tf set to true when a Pterodactyl flies and set to false when it stops flying
     */
    public void setIsFlying(boolean tf){isFlying = tf;}

    /**
     * This method creates a instance of Pterodactyls
     * @param name of the Pterodactyl
     * @param gender of the Pterodactyl
     * @param growthState Tells if its a Adult or Child Pterodactyl
     * @param hitPoints Current Food Level of Pterodactyl
     */
    public Pterodactyls(String name, char gender, char growthState, int hitPoints){
        this.name = name;
        this.gender = gender;
        this.growthState = growthState;
        this.hitPoints = hitPoints;
        displayChar = 'p';
        maxHitPoints = 100;
        minHitPoints = 90;
        ageToBecomeAdult = 30;
        eatingType = 'C';
//        incubationPeriodTurns = 10;
        incubationPeriodTurns = 10;
        turnsBeingUnconscious = 0;
        turnsUnconThirst = 0;
        turnsToBecomeCorpse = 20;
//        waterLvl = 30;
        waterLvl = 60;
        maxWaterLvl = 100;
        minWaterLvl = 40;
        numOfTrnFlying = 1;
        isFlying = true;
        turnsLimitToFly = 30;
        mating = false;

        if (growthState == 'A')
            age = 30;
        else
            age = 0;

        actionFactories.add(new MoveToMateBehaviour());
        actionFactories.add(new EatBehaviour());
        actionFactories.add(new MoveToFoodSource());
        actionFactories.add(new MoveToWaterSource());
//        actionFactories.add(new WanderBehaviour());
    }

    /**
     * The action other actors can do on Pterodactyl
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return Action which the other actor do on Pterodactyl
     */
    @Override
    public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions list = super.getAllowableActions(otherActor,direction,map);

        list.add(new FeedDinoAction(this,direction));

        if(!(otherActor instanceof Player))
            list.add(new MateBehaviour(this));

        if(otherActor instanceof Allosaur)
            list.add(new EatPterodactyl(this));

        return list;
    }

    /**
     * The method handles the Action taken by Pterodactyl at each turn in the game
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return Action taken by the  Pterodactyl at the current turn of the game
     */
    @Override
    public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {

        updateHealthStats(map);

        //display.println(this+ " FL:" + hitPoints + ",WL:"+ waterLvl);

        if(!isConscious() || !isConsciousThirst()){
            displayChar = 'u';
            return new DoNothingAction();
        }
//        else {
//            displayChar = 'p';
//        }

        //If allosaur is near, move to a tree


        if( map.locationOf(this).getGround() instanceof Tree ){
            numOfTrnFlying = 1;
            isFlying = true;
        }

        if ( numOfTrnFlying >= turnsLimitToFly ){
            isFlying = false;
            //if ((hitPoints > minHitPoints) && (waterLvl > minWaterLvl) && !(map.locationOf(this).getGround() instanceof Tree)){

            actionFactories.add(0,new MoveToTreeBehaviour());
            //}
        }
        else
            isFlying = true;


        if (isFlying){
            if(!(map.locationOf(this).getGround() instanceof Tree)) {
                numOfTrnFlying++;
            }
            displayChar = 'P';
        }
        else
            displayChar = 'p';


        if ((hitPoints > 70) && (waterLvl > minWaterLvl) && !(map.locationOf(this).getGround() instanceof Tree)){
            actionFactories.add(1,new MoveToTreeBehaviour());
        }

//        if (!isFlying && hitPoints < minHitPoints){
//            actionFactories.add(0,new MoveToTreeBehaviour());
//        }

        if (!(map.locationOf(this).getGround() instanceof Tree) ){
            actionFactories.add(new WanderBehaviour());
        }

        //display.println("isFlying: " + isFlying + " turnsFlying:" + numOfTrnFlying);
        //display.println("HE: " + hasEgg);
        //display.println("turns has Egg: " + turnsHasEgg);

        if(hasEgg){
            //display.println("turnsE:"+turnsHasEgg);
            if(turnsHasEgg >= incubationPeriodTurns){
                if( map.locationOf(this).getGround() instanceof Tree){
                    return new LayEggAction();
                }
                else {
                    actionFactories.add(0,new MoveToTreeBehaviour());
                }
            }
            else
                turnsHasEgg++;
        }


        for(Action action: actions){
            if(action instanceof DrinkAction){
                if(waterLvl < minWaterLvl){
                    return action;
                }
            }
        }

//        for(Action action: actions){
//            if(action.menuDescription(this) == "Mate"){
//                Action mate = ((Behaviour)action).getAction(this,map);
//                if(mate != null){
//                    return mate;
//                }
//                else if(mating){
//                    mating = false;
//                    return new GettingMatedDoNothing();
//                }
//            }
//        }



        //actionFactories.add(0,new DrinkBehaviour(actions));
        actionFactories.add(0, new MateDriver(actions));

        //display.println(this +" "+actionFactories);

        for (Behaviour factory : actionFactories) {
            if(!map.contains(this))
                break;
            Action action = factory.getAction(this, map);
            if(action != null){
                //System.out.println(name + " :"+factory);
                cleanse(actionFactories);
                //System.out.println("IM CALLED:" + action.menuDescription(this));
                return action;
            }
        }

        cleanse(actionFactories);
        return new DoNothingAction();
    }

    /**
     * @return the List of Behaviours
     */
    public List<Behaviour> getActionFactories() {
        return actionFactories;
    }

    /**
     * @return true if Pterodactyl is flying otherwise false
     */
    public boolean isFlying() {
        return isFlying;
    }

    /**
     * @param flying set isFlying to false when Pterodactyl isnt flying.Set to true when its flying
     */
    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    /**
     * @return number of turns Pterodactyl is flying for
     */
    public int getNumOfTrnFlying() {
        return numOfTrnFlying;
    }

    /**
     * @param numOfTrnFlying sets  current number of turns for which Pterodactyl is flying for
     */
    public void setNumOfTrnFlying(int numOfTrnFlying) {
        this.numOfTrnFlying = numOfTrnFlying;
    }

    /**
     * @return number of turns after which Pterodactyl gets tired of flying and stops flying
     */
    public int getTurnsLimitToFly() {
        return turnsLimitToFly;
    }

    /**
     * This method cleans the behaviours like(WanderBehaviour,MoveToTreeBehaviour,MateDriver) in list of behaviours of Pterodactyl
     * @param actionFactories the list of behaviours which Pterodactyl can do
     */
    private void cleanse(List<Behaviour> actionFactories){
        actionFactories.removeIf(factory -> factory instanceof WanderBehaviour || factory instanceof MoveToTreeBehaviour || factory instanceof MateDriver);
    }


}
