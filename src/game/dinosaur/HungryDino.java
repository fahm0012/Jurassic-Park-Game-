package game.dinosaur;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import game.behaviours.Behaviour;
import game.portableItems.Corpse;
import game.portableItems.PortableItem;

import java.util.List;


/**
 * This is a abstract class for all the dinos.
 * @author Fahad and Shafkat
 * @version 1.1.0
 * @since 22/05/2021
 * @see Actor
 */
public abstract class HungryDino extends Actor {

    /**
     * List of behaviours a HungryDino can do.
     */
    private List<Behaviour> actionFactories;

    /**
     * gender of the dino
     */
    protected char gender;

    /**
     *  represents the  nutrition of dino (Herbivore or Carnivore)
     */
    protected char eatingType;

    /**
     * shows if the dino is a adult = 'A' or a child = 'C'
     */
    protected char growthState='A';

    /**
     * age of the dino
     */
    protected int age;

    /**
     * Food level after which a  dino becomes hungry
     */
    protected int minHitPoints;

    /**
     * tells if dino has become a corpse
     */
    protected boolean corpseState=false;

    /**
     * number of turns after which  dino dies if it was unconscious  for these number of days
     */
    protected int turnsBeingUnconscious=0;

    /**
     * tells if a dino has egg
     */
    protected boolean hasEgg=false;

    /**
     * number of turns for which dino is carrying the egg
     */
    protected int turnsHasEgg=0;

    /**
     * the days for which the egg remains in the female  dino until it lays the egg
     */
    protected int incubationPeriodTurns;

    /**
     * age for baby dino to become Adult
     */
    protected int ageToBecomeAdult;

    /**
     * number of turns after which  dino dies if it was unconscious  for these number of days
     */
    protected int turnsToBecomeCorpse;

    /**
     * status if a dino is mating
     */
    protected boolean mating;

    /**
     * the current water level of dino
     */
    protected int waterLvl;

    /**
     * the maximum capacity for water level
     */
    protected int maxWaterLvl;
    /**
     * the water level at which it gets thirsty
     */
    protected int minWaterLvl;
    /**
     * turns after which die due to thirst
     */
    protected int turnsUnconThirst;


    /**
     * @return minWaterLvl at which dino becomes thirsty
     */
    public int getMinWaterLvl() {
        return minWaterLvl;
    }

    /**
     * @return the maximum capacity for water level
     */
    public int getMaxWaterLvl() {
        return maxWaterLvl;
    }

    /**
     * @return the current water level of dino
     */
    public int getWaterLvl() {
        return waterLvl;
    }

    /**
     * This method changes the current water level of the dino
     * @param waterLvl which needs to be set as new current water level for dino
     */
    public void setWaterLvl(int waterLvl) {
        this.waterLvl = waterLvl;
    }

    /**
     * @return true if dino is mating otherwise false
     */
    public boolean getMating(){return mating;}

    /**
     * @param mating sets mating to true if dino is mating otherwise false
     */
    public void setMating(boolean mating) {
        this.mating = mating;
    }

    /**
     *  this is a constructor for HungryDino but its a abstract class a instance of Hungary Dino cant be formed.
     *  Calls Parent class Actor in the constructor
     */
    public HungryDino(){
        super("Dino",'D',100);
    }

    /**
     * @return gender of the dino
     */
    public char getGender() { return gender; }


    /**
     * @return the growth State of dino(Adult='A' or Child='C')
     */
    public char getGrowthState() { return growthState; }

    /**
     * @return the age of the dino
     */
    public int getAge() {
        return age;
    }

    /**
     * @return current food level of dino
     */
    public int getFoodLevel(){return hitPoints;
    }

    /**
     * @return maximum food level of the dino
     */
    public int getMaxFoodLevel(){return maxHitPoints;}

    /**
     * @return food level at which dino becomes hungry
     */
    public int getMinHitPoints() {
        return minHitPoints;
    }

    /**
     * This method changes the food level of dino
     * @param level the change that needs to be made in dinos food level.
     */
    public void setFoodLevel(int level){hitPoints = level;}

    /**
     * @return if dino is Herbivore = 'H' or a Carnivore = 'C'
     */
    public char getEatingType() {
        return eatingType;
    }

    /**
     * @return tells if the dino has a egg
     */
    public boolean getHasEgg(){return hasEgg;}

    /**
     * This method works when a dino gets pregnant and then it sets the hasEgg to true
     */
    public void setHasEgg() {
        hasEgg = true;
    }

    /**
     * This method increase the water level for the dino
     * @param waterLvl the amount by which water level has to be increased
     */
    public void incWaterLvl(int waterLvl) {
        this.waterLvl += waterLvl;
    }

    /**
     * tells if the dino is unconscious due to its thirst
     * @return True if dino unconscious due to its thirst otherwise False
     */
    public boolean isConsciousThirst(){
        return waterLvl>0;
    }

    /**
     * This method updates the food level(health) of the dino
     * @param map the gameMap at which dinos are present
     */
    protected void updateHealthStats(GameMap map){

        if(!isConsciousThirst()){
            turnsUnconThirst++;
            if(turnsUnconThirst > 15)
                dinoDeath(map);
        }
        else{
            turnsUnconThirst = 0;

            if(waterLvl > 0){
                waterLvl--;
            }
            if(waterLvl <= minWaterLvl){
                if(!map.contains(this)) {
                    String loc = "(" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ")" + " is getting thirsty!";
                    System.out.println(this + " at " + loc);
                }
            }

        }
        if(!isConscious()){
            turnsBeingUnconscious++;
            if(turnsBeingUnconscious >= turnsToBecomeCorpse) {
                dinoDeath(map);
            }
        }
        else{
            turnsBeingUnconscious = 0;

            hitPoints--;

            /*
              if current food level is less or equal to the food level at which dino gets hungry print message
              that dino at this location is hungry
             */
            if(hitPoints <= minHitPoints){
                if(!map.contains(this)) {
                    String loc = "(" + map.locationOf(this).x() + "," + map.locationOf(this).y() + ")" + " is getting hungry!";
                    System.out.println(this + " at " + loc);
                }
            }

            /*
              increments age of dino if its a child so it can convert into a adult after specific days
             */
            if (growthState == 'C'){
                age++;
            }
            /*
              converts child dino into adult when it reach specific age
             */
            if (age >= ageToBecomeAdult && growthState == 'C'){
                growthState = 'A';
            }
        }

    }

    /**
     * This method converts a dead dino into corpse and removes that dino from map and adds a corpse of that dino on map
     * @param map Game map on which dinos are present
     */
    public void dinoDeath(GameMap map){
        //PortableItem corpse = new PortableItem("dead "+ this, '%');
        int hp;
        if(this instanceof Stegosaur || this instanceof Allosaur)
            hp = 50;
        else if(this instanceof Pterodactyls)
            hp = 30;
        else
            hp=100;
        Corpse corpse = new Corpse("dead "+ this, '%',hp);
        if(map.contains(this)) {
            map.locationOf(this).addItem(corpse);
            map.removeActor(this);
        }
    }

    /**
     * Tells if dino is carrying  a egg
     * @param hasEgg  returns true if dino is carying a egg
     */
    public void setHasEgg(boolean hasEgg) {
        this.hasEgg = hasEgg;
    }

    /**
     * This sets the turns for which dino is carrying the egg
     * @param turnsHasEgg  current turns for which dino is carrying the egg
     */
    public void setTurnsHasEgg(int turnsHasEgg) {
        this.turnsHasEgg = turnsHasEgg;
    }
}
