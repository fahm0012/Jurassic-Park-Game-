package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.dinosaur.HungryDino;
import game.dinosaur.Pterodactyls;
import game.grounds.Lake;
import game.portableItems.Corpse;
import game.portableItems.PortableItem;
import game.dinosaur.Allosaur;
import game.portableItems.AllosaurEgg;
import game.grounds.Bush;
import game.grounds.Tree;

/**
 * EatBehaviour is a behaviour of HungryDinos (dinos) to eat when they are next to a food source and if they
 * are hungry
 * @author Fahad and Shafkat
 * @version 1.1.0
 * @since 22/05/2021
 * @see Action
 * @see Behaviour
 */
public class EatBehaviour extends Action implements Behaviour {

    /**
     * Item which is to eaten by the dino
     */
    Item eatenItem;
    /**
     * This tells if pterodactyl drank water from lake
     */
    Boolean pDrank = false;
    /**
     * This tells if pterodactyl ate one or more fish's from lake
     */
    Boolean ateFish = false;
    /**
     * this tells how many fish's were  eaten by pterodactyl
     */
    int numEatenFish;

    /**
     * constructor to create instance of EatBehaviour
     */
    public EatBehaviour(){}

    /**
     * This method tells us what actor(dino) eat what item at what location
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return String displays what Actor(dino) ate what item at what location
     */
    @Override
    public String execute(Actor actor, GameMap map){

        /*
          String for Brachiosaur
         */
        if(actor.getDisplayChar() == 'b')
            return actor + " eats fruit from tree at " + "("+map.locationOf(actor).x()+","+map.locationOf(actor).y()+")";
        /*
          string for Allosaur
         */
        else if(actor.getDisplayChar() == 'a')
            return actor + " eats corpse/egg";

        else if(actor instanceof Pterodactyls){
            if(pDrank)
                return actor + " drinks from a lake. [FL:" + ((HungryDino)actor).getFoodLevel() + ",WL:" + ((HungryDino)actor).getWaterLvl()+"]";
            else if (ateFish)
                return actor + " eats "+ numEatenFish +" fish from a lake. [FL:" + ((HungryDino)actor).getFoodLevel() + ",WL:" + ((HungryDino)actor).getWaterLvl()+"]";
            else
                return actor + " nibbles on "+ eatenItem +" [FL:" + ((HungryDino)actor).getFoodLevel() + ",WL:" + ((HungryDino)actor).getWaterLvl()+"]";

        }

        /*
          String for Stegosaur
         */
        return actor + " eats fruit from ground/bush at " + "("+map.locationOf(actor).x()+","+map.locationOf(actor).y()+")";

    }

    /**
     * Sees what actor(dino) is passed as a parameter and then call the eat action method for that dino so they can
     * eat a item
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return Action done by the actor.No action can also be done.
     */
    @Override
    public Action getAction(Actor actor, GameMap map){

        HungryDino dino = ((HungryDino)(actor));

        Location here = map.locationOf(actor);

        if(actor.getDisplayChar() == 'd'){
            return stegosaurEatAction(here,dino);
        }
        else if(actor.getDisplayChar() == 'b'){
            return brachiosaurEatAction(here, dino);
        }
        else if(actor instanceof Pterodactyls){
            return pterodactylsEatAction(here, dino);
        }
        else if(actor.getDisplayChar() == 'a'){
            return allosaurEatAction(here, dino);
        }

        return null;
    }

    /**
     * This method allows pterodactylsEatAction to eat from the food src
     * @param here Location in the map
     * @param dino HungryDino(Pterodactyls) which has to eat
     * @return Action to eat the food src
     */
    private Action pterodactylsEatAction(Location here, HungryDino dino){
        //System.out.println("(2)");

        if(dino.getWaterLvl() <= dino.getMinWaterLvl() && dino.getFoodLevel() > dino.getMinHitPoints()){
            //System.out.println("(3)");
            if(here.getGround() instanceof Lake && ((Pterodactyls)dino).getIsFlying()) {
                Lake lake = ((Lake)(here.getGround()));

                if(lake.getSipCapacity() > 0) {
                    int level = 30;
                    level = Math.min(level, dino.getMaxWaterLvl() - dino.getWaterLvl());

                    dino.setWaterLvl(dino.getWaterLvl() + level);
                    lake.decSipCapacity(1);
                }
                pDrank = true;
                return this;
            }
        }

        if(dino.getFoodLevel() > dino.getMinHitPoints()+30)
            return null;

        if(here.getGround() instanceof Lake && ((Pterodactyls)dino).getIsFlying()){

            Lake lake = ((Lake)(here.getGround()));

            if(lake.getFish() <= 0)
                return null;

            int max  = Math.min(2,lake.getFish());
            int min = 0;
            int toeat = (int)Math.floor(Math.random()*(max-min+1)+min);

            int level = toeat * 5;

            level = Math.min(level, dino.getMaxFoodLevel()- dino.getFoodLevel());

            dino.setFoodLevel(dino.getFoodLevel() + level);

            lake.decFish(toeat);

            if(lake.getSipCapacity() > 0) {
                level = 30;
                level = Math.min(level, dino.getMaxWaterLvl() - dino.getWaterLvl());

                dino.setWaterLvl(dino.getWaterLvl() + level);
                lake.decSipCapacity(1);
            }
            ateFish = true;
            numEatenFish = toeat;

            return this;
        }

        Item corpse = containsCorpse(here);
        if(corpse != null){
            ((Pterodactyls)dino).setIsFlying(false);
            updateFoodLevel(dino,corpse);

            if(((Corpse)corpse).getHp() <= 0)
                here.removeItem(corpse);
            eatenItem = corpse;
            return this;
        }



        return null;
    }

    /**
     * This is a method where a Allosaur eats corpse,egg or nothing
     * @param here is the Location at which a Allosaur dino is present
     * @param dino is one of the Allosaur dino
     * @return Action taken by the Allosaur dino.No action can also be taken.
     */
    private Action allosaurEatAction(Location here, HungryDino dino){

        /*
          checks if Allosaur is hungry
         */
        if(dino.getFoodLevel() >= dino.getMinHitPoints())
            return null;

        /*
          checks if there is a corpse at the location where the Allosaur dino is present
         */
        Item corpse = containsCorpse(here);
        if(corpse != null){
            updateFoodLevel(dino, corpse);
            here.removeItem(corpse);
            eatenItem = corpse;
            return this;
        }

        /*
          checks if there is a dino egg at the current location so Allosaur can eat it
         */
        else if(containsEgg(here)){
            updateFoodLevel(dino);
            return this;
        }

        return null;

    }

    /**
     * This method checks if there is a egg at current location which can be eaten by Allosaur
     * @param here current Location on the map
     * @return returns true if a egg is present at a current position and removes that egg as its gonna be eaten by
     * Allosaur
     */
    private boolean containsEgg(Location here){
        for(Item item: here.getItems()){
            if(item.getDisplayChar() == '0' && !(item instanceof AllosaurEgg))
                eatenItem = item;
                here.removeItem(item);
            return true;
        }
        return false;
    }

    /**
     * This method checks if there is a corpse at current location which can be eaten by Allosaur
     * @param here current Location on the map
     * @return Item which is a corpse if its present at the current location.As this corpse will be eaten by Allosaur
     */
    private Item containsCorpse(Location here){
        for(Item item: here.getItems()){
            if(item.getDisplayChar() == '%')
                return item;
        }
        return null;
    }

    /**
     * This is a method where a Stegoaur eat fruit from bush or fruit from ground
     * @param here current location on the map
     * @param dino is one of the Stegosaur dino
     * @return Action taken by the Stegosaur to eat the Fruit from ground or bush.No action can also take place if no
     *         fruit available on ground or bush at the current location
     */
    private Action stegosaurEatAction(Location here, HungryDino dino){

        /*
          checks if Stegosaur dino is hungry
         */
        if(dino.getFoodLevel() >= dino.getMinHitPoints())
            return null;

        /*
          checks if current location contains fruit on ground so it can be eaten by the Stegosaurs dino
         */
        if(containsFruit(here)){
            updateFoodLevel(dino);
            return this;
        }

        /*
          check if the location is of a bush and it contains fruit on it so it can be eaten by the Stegosaurs dino
         */
        else if(here.getGround() instanceof Bush){
            Bush bush = ((Bush)(here.getGround()));

            if(!bush.hasFruits()){ return null; }

            updateFoodLevel(dino);
            bush.removeFromFruitList();
            return this;
        }
        return null;
    }

    /**
     * This is a method where a Brachiosaur eat fruit from Tree.
     * @param here current location on the map
     * @param dino is one of the Brachiosaur dino
     * @return Action taken by the Brachiosaur to eat the Fruit from Tree.No action can also take place if no
     *        fruit available on Tree at the current location
     */
    private Action brachiosaurEatAction(Location here, HungryDino dino){
        /*
          checks if Brachiosaur is hungry
         */
        if(dino.getFoodLevel() >= dino.getMinHitPoints())
            return null;

        /*
          checks if current location have a tree and it has fruit on it.Brachiosaur can eat multiple fruit on this tree
         */
        if(here.getGround() instanceof Tree){
            Tree tree = ((Tree)(here.getGround()));

            if(!tree.hasFruits()){return null;}

            while(tree.hasFruits() && (dino.getFoodLevel() != dino.getMaxFoodLevel())){
                updateFoodLevel(dino);
                tree.removeFromFruitList();

            }
            return this;
        }
        return null;
    }

    /**
     * This method updates the food level for dinos(Brachiosaur,Stegosaur) when they eat fruit.
     * @param dino it can be any dino of type(Brachiosaur,Stegosaur)
     */
    private void updateFoodLevel(HungryDino dino){
        int levelInc;
        if(dino.getDisplayChar() == 'd')
            levelInc = 10;
        else if(dino.getDisplayChar() == 'b')
            levelInc = 5;
        else
            levelInc = 10;

        int addLevel = Math.min(levelInc, dino.getMaxFoodLevel()-dino.getFoodLevel());
        dino.setFoodLevel(dino.getFoodLevel() + addLevel);
    }

    /**
     * Increments the food level of Allosaur dino according to the type of corpse
     * @param dino is one of Allosaur dino
     * @param corpse the corpse which is to be eaten by Allosaur
     */
    private void updateFoodLevel(HungryDino dino, Item corpse){

        if(dino instanceof Pterodactyls){
            ((Corpse)corpse).decHP(10);
            int level = Math.min(10, dino.getMaxFoodLevel()- dino.getFoodLevel());
            dino.setFoodLevel(dino.getFoodLevel() + level);
        }

        else {
            if (((PortableItem) (corpse)).getName().contains("Brachiosaur"))
                dino.setFoodLevel(dino.getMaxFoodLevel());
            else if(((PortableItem) (corpse)).getName().contains("Pter")){
                int level = Math.min(30, dino.getMaxFoodLevel()- dino.getFoodLevel());
                dino.setFoodLevel(dino.getFoodLevel() + level);
            }
            else {
                int addLevel = Math.min(50, dino.getMaxFoodLevel() - dino.getFoodLevel());
                dino.setFoodLevel(dino.getFoodLevel() + addLevel);
            }
        }
    }

    /**
     * This method tells if there is a fruit at the current location
     * @param here current location on the map
     * @return true if there is a fruit at the current location
     */
    private boolean containsFruit(Location here){
        for(Item item: here.getItems()){
            if(item.getDisplayChar() == 'o')
                here.removeItem(item);
                return true;
        }
        return false;
    }

    /**
     * @param actor The actor performing the action.
     * @return String telling that actor(dinos) ate fruit or corpse or egg
     */
    @Override
    public String menuDescription(Actor actor) {
        if (actor instanceof Allosaur) {
            return "Eat Corpse/Egg";
        }
        else
            return "Eat fruit";
    }

}
