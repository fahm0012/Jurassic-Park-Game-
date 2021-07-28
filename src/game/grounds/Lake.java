package game.grounds;

import edu.monash.fit2099.engine.*;
import game.actions.DrinkAction;
import game.dinosaur.HungryDino;
import game.dinosaur.Pterodactyls;

/**
 * This class represents  the Lake which is present on the Ground
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see Ground
 */
public class Lake extends Ground {

    /**
     * The sips present in the lake for dinos to drink
     */
    private int sipCapacity;

    /**
     * turns are incremented in each turn as it might rain after every 10 turns of the Lake
     */
    private int turnCounter;

    /**
     * probability for a rain to occur
     */
    private double rainProbability;

    /**
     * maximum number of fishes a lake can have.
     */
    private int fishCapacity;

    /**
     * number of fish's in the lake
     */
    private int fish;

    /**
     * probability of fish to produce a new fish
     */
    private double fishBirthProbability;

    /**
     * number of turns after which it might rain
     */
    private int turnsToRain;

    /**
     * min probability at which a water can be added to the lake when it rain
     */
    private double min = 0.1;
    /**
     * max probability at which a water can be added to the lake when it rain
     */
    private double max = 0.6;


    /**
     * This method creates a instance of Lake
     */
    public Lake(){
        super('~');
        sipCapacity = 25;
        rainProbability = 0.2;
        fishCapacity = 25;
        fish = 5;
        fishBirthProbability = 0.6;
        turnCounter = 1;
    }

    /**
     * @return current sipCapacity of the lake
     */
    public int getSipCapacity() {
        return sipCapacity;
    }

    /**
     * @param sipCapacity decrease the sipCapacity when a dino drinks from the lake
     */
    public void decSipCapacity(int sipCapacity) {
        this.sipCapacity -= sipCapacity;
    }

    /**
     * @return number of fish's in the lake
     */
    public int getFish() {
        return fish;
    }

    /**
     * This method decreases fish's from the lake when a dino eats them
     * @param fish number of fish's eaten by the dino
     */
    public void decFish(int fish) {
        this.fish -= fish;
    }

    /**
     * This method only allows Pterodactyls to enter lake if they are flying and other actors cant enter the lake
     * @param actor the actor which is trying to enter lake
     * @return
     */
    @Override
    public  boolean canActorEnter(Actor actor){
        if (actor instanceof Pterodactyls && ((Pterodactyls) actor).isFlying())
            return true;

        else
            return false;
    }

    /**
     * Actions which can be done on the lake by other actors
     * @param actor the Actor acting
     * @param location the current Location
     * @param direction the direction of the Ground from the Actor
     * @return Action taken by the other actor on the lake
     */
    @Override
    public Actions allowableActions(Actor actor, Location location, String direction){
        Actions list = super.allowableActions(actor,location,direction);
        if(actor instanceof HungryDino) {
            list.add(new DrinkAction(location));
        }
        return list;
    }

    /**
     * This is run for lake at each turn so it controls what does lake do in each turn
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location){
        super.tick(location);

        //System.out.println("fish: " + fish + " sips: " + sipCapacity);

        //fish birth
        if(Math.random() <= fishBirthProbability) {
            if(fish < fishCapacity)
                fish += 1;
        }

        //rain
        if(turnCounter % 10 == 0) {
            if (Math.random() < rainProbability) {
                double rainfall = min + Math.random() * ((max - min) + 1);
                int add = (int)(Math.floor((20 * rainfall)));
                sipCapacity += add;
                //System.out.println("rain " + sipCapacity);

                int maxX = location.map().getXRange().max();
                int maxY = location.map().getYRange().max();

                for (int x = location.map().getXRange().min();x < maxX;x++){
                    for (int y = location.map().getYRange().min();y < maxY;y++){
                        if(location.map().at(x,y).containsAnActor()){
                            if(location.map().at(x,y).getActor() instanceof HungryDino){
                                HungryDino dino = (HungryDino)location.map().at(x,y).getActor();
                                if(!(dino.isConsciousThirst()) && dino.getWaterLvl() <= 0)
                                {
                                    dino.incWaterLvl(10);
                                    //System.out.println("now: " + dino.getWaterLvl());
                                }
                            }
                        }
                    }

                }


            }
        }

        turnCounter++;

    }



}
