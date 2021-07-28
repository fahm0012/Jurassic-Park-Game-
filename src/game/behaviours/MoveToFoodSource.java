package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.dinosaur.Brachiosaur;
import game.dinosaur.HungryDino;
import game.dinosaur.Pterodactyls;
import game.dinosaur.Stegosaur;
import game.grounds.Lake;
import game.player.Player;
import game.portableItems.*;
import game.grounds.Bush;
import game.grounds.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows the HungryDinos to move towards the closet food source when they are hungry
 * @author Fahad and Shafkat
 * @version 1.1.0
 * @since 22/05/2021
 * @see Behaviour
 */
public class MoveToFoodSource implements Behaviour{
    /**
     * This method checks what type of dino is acting and then directs that dino towards the closet food source near it.
     * For Allosaurs dino it prioritises corpses then egg and then deciding to move toward alive Stegosaurs to attack
     * and eat it
     * For Pterodactyl priority to eat nearest corpse or fish from lake if possible otherwise eat the closet egg
     * if available to eat
     * @param actor the Actor(dino) acting
     * @param map the GameMap containing the Actor(dino)
     * @return Action to be taken by the Actor(dino) to move toward food source.No action can also be return if no food source
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        /*
          maximum x position of map
          maximum y position of map
         */
        int maxX = map.getXRange().max();
        int maxY = map.getYRange().max();
        /*
          stores coordinates of closest food source
         */
        ArrayList<Integer> closetSrcXYCor = new ArrayList<Integer>();

        /*
          checks that actor is not instance of Player
         */
        if (!(actor instanceof Player)){
            /*
              checks if actor is in instance of Stegosaur
             */
            if (actor instanceof Stegosaur){
                return stegasoursGetAction(actor,map);
            }
            /*
              checks if actor is in instance of Brachiosaur
             */
            else if (actor instanceof Brachiosaur) {
                return brachiosaurGetAction(actor,map);

            }
            else if (actor instanceof Pterodactyls){
                return PterodactylsGetAction(actor,map);

            }
            else {
                return allosaurGetAction(actor,map);
            }
        }
        return null;
    }

    /**
     * This method is to move Pterodactyls dino to the closet food source available which can be eaten by Pterodactyls
     * under the current state of Pterodactyls.If Pterodactyls finds no corps in the close range and if it cant
     * eat the fishes because it cant fly then it eats the egg which is within its 5 steps
     * @param actor this is a Pterodactyls dino
     * @param map   GameMap of the game
     * @return Action that should be taken by Pterodactyls to move towards the food source
     */
    private Action PterodactylsGetAction(Actor actor, GameMap map) {
        /*
          maximum x position of map
          maximum y position of map
         */
        int maxX = map.getXRange().max();
        int maxY = map.getYRange().max();

        /*
          stores coordinates for closet eating source
         */
        ArrayList<Integer>  eatingSrc = new ArrayList<Integer>();

        /*
          stores coordinates for a closet egg
         */
        ArrayList<Integer> eggLoc = new ArrayList<Integer>();

        /*
          downcast actor into HungryDino
         */
        Pterodactyls dino = ((Pterodactyls)(actor));

        /*
         check if Pterodactyls is hungry
         */
        if(dino.getFoodLevel() >= dino.getMinHitPoints())
            return null;

        for (int x = map.getXRange().min();x <= maxX;x++) {
            for (int y = map.getYRange().min(); y <= maxY; y++) {

                if (!(map.at(x,y).containsAnActor())) {
                    /*
                      get items present at current x and y
                     */
                    List<Item> itemsList = map.at(x, y).getItems();

                    /*
                      steps to reach the item
                     */
                    int steps = distance(map.locationOf(actor), map.at(x, y));

                    /*
                        checks if there is a lake at the current location and if the Pterodactyls can fly to that lake eat that fish and it will reach that lake within its life
                     */
                    if (map.at(x, y).getGround() instanceof Lake && (((Lake) map.at(x, y).getGround()).getFish()) > 0 && dino.isFlying() && (steps < (dino.getTurnsLimitToFly() - dino.getNumOfTrnFlying())) && (steps + 1 < dino.getFoodLevel())) {
                        setBestNearestFoodSrc(eatingSrc, map, actor, x, y);
                    }

                    /*
                      iterate through all items present at current location
                     */
                    for (Item item : itemsList) {
                        if ((item.getDisplayChar() == '%') && (steps < dino.getFoodLevel())) {
                            /*
                              sets cordinates of the closet corpse
                             */
                            setBestNearestFoodSrc(eatingSrc, map, actor, x, y);

                        }
                        /*
                          check if there is nearby egg withing 5 steps and it wont get unconscious while moving towards that egg and checks that its not eating the egg of its own type
                        */
                        else if (item instanceof Egg && (steps + ((Egg) item).turnsOnGround < ((Egg) item).getHatchingPeriod() && (steps < dino.getFoodLevel())) && steps < 5 && !(item instanceof PterodactylEgg)) {
                            /*
                              sets cordinates of the closet eggs
                             */
                            setBestNearestFoodSrc(eggLoc, map, actor, x, y);
                        }
                    }
                }
            }
        }
        if (eatingSrc.size() != 0){
            return moveActorToFoodSource(actor,map,eatingSrc.get(0),eatingSrc.get(1));
        }
        else if(eggLoc.size() != 0){
            return moveActorToFoodSource(actor,map,eggLoc.get(0),eggLoc.get(1));

        }
        else {
            return null;
        }

    }


    /**
     * This method is for Allosaurs eating the Brachiosaur corpse if its hungry and if its not avialable eat
     *  allosaurs or stegosaurs corpse and if thats not avilable eat egg and even if thats not possible attack a
     *  alive stegosaurs.This is done if these sources are in a particluar range
     * @param actor this is a Allosaur dino
     * @param map GameMap of the game
     * @return Action that should be taken by Allosaur to move towards the food source
     */
    public Action allosaurGetAction(Actor actor,GameMap map){
        /*
          maximum x position of map
          maximum y position of map
         */
        int maxX = map.getXRange().max();
        int maxY = map.getYRange().max();

        /*
           stores coordinates for Alive Pterodactyls which is on the ground near the Allosaur
         */
        ArrayList<Integer> alivePterodactylsLoc = new ArrayList<Integer>();

        /*
          stores coordinates for closet bracheasours corpse
         */
        ArrayList<Integer> bracheasoursCorpseLoc = new ArrayList<Integer>();
        /*
          stores coordinates for closet allosaur corpse or stegosaurs corpse
         */
        ArrayList<Integer> otherCorpseLoc = new ArrayList<Integer>();
        /*
          stores coordinates for a closet alive stegosaurs
         */
        ArrayList<Integer> aliveStegoLoc = new ArrayList<Integer>();
        /*
          stores coordinates for a closet egg
         */
        ArrayList<Integer> eggLoc = new ArrayList<Integer>();

        /*
          downcast actor into HungryDino
         */
        HungryDino dino = ((HungryDino)(actor));

        if (dino.getFoodLevel() >= dino.getMinHitPoints()){
            return null;
        }

        /*
          iterate through whole map
         */
        for (int x = map.getXRange().min();x <= maxX;x++) {
            for (int y = map.getYRange().min(); y <= maxY; y++) {

                if(!(map.at(x,y).containsAnActor())) {
                    /*
                      get items present at current x and y
                     */
                    List<Item> itemsList = map.at(x, y).getItems();
                    /*
                      steps to reach the item
                     */
                    int steps = distance(map.locationOf(actor), map.at(x, y));
                    /*
                      iterate through all items present at current location
                     */
                    for (Item item : itemsList) {
                        if ((item.getDisplayChar() == '%') && (((PortableItem) item).getName().contains("Brachiosaur")) && (distance(map.locationOf(actor), map.at(x, y)) <= 15)
                                && (steps + ((PortableItem) item).getCounter() < 40) && (steps < dino.getFoodLevel()) && (dino.getFoodLevel() < 50)) {
                            /*
                              sets cordinates of the closet Brachiosaur corpse
                             */
                            setBestNearestFoodSrc(bracheasoursCorpseLoc, map, actor, x, y);
                        }
                        /*
                          if stegosaurs or allosaurs corpse present within 10 steps and current Allosaurs is hungry and it wont die until it reach there then store coordinates of that corpse in otherCorpseLoc
                         */
                        else if ((item.getDisplayChar() == '%') && (distance(map.locationOf(actor), map.at(x, y)) <= 10
                                && (steps + ((PortableItem) item).getCounter() < 20) && (steps < dino.getFoodLevel())) && (dino.getFoodLevel() < 50)) {
                            /*
                              sets cordinates of the closet stegosaurs or allosaurs corpse
                             */
                            setBestNearestFoodSrc(otherCorpseLoc, map, actor, x, y);
                        }
                       /*
                          check if there is nearby egg withing 5 steps and its possible for Allosaurs to reach that egg with becoming unconscious
                        */
                        else if (item instanceof Egg && (steps + ((Egg) item).turnsOnGround < ((Egg) item).getHatchingPeriod() && (steps < dino.getFoodLevel())) && steps < 11 && !(item instanceof AllosaurEgg)) {
                            /*
                              sets cordinates of the closet eggs
                             */
                            setBestNearestFoodSrc(eggLoc, map, actor, x, y);
                        }
                    }
                    /*
                        Is it possible to find a alive stegasours near Allosaurs so it can attack
                     */
                    if ((map.at(x, y).containsAnActor() && map.at(x, y).getActor() instanceof Pterodactyls)
                            && !(((Pterodactyls) (map.at(x, y).getActor())).isFlying()) && !(map.at(x, y).getGround() instanceof Tree)
                            && (steps < dino.getFoodLevel()) && (steps <= 5) && (dino.getFoodLevel() < 50)) {
                        System.out.println(!(((Pterodactyls) (map.at(x, y).getActor())).isFlying()));
                        System.out.println(!(map.at(x, y).getGround() instanceof Tree));
                        setBestNearestFoodSrc(alivePterodactylsLoc, map, actor, x, y);
                    } else if (map.at(x, y).containsAnActor() && map.at(x, y).getActor() instanceof Stegosaur
                            && (steps < dino.getFoodLevel())
                            && !(((Stegosaur) (map.at(x, y).getActor())).isWoundedState())) {
                        setBestNearestFoodSrc(aliveStegoLoc, map, actor, x, y);
                    }
                }
            }

        }
        /*
          Priority to eat is set
          1) Bracheasours Corpse
          2) Stegosaurs or Allosaurs Corpse
          3) Egg
          4) Attack Stegosaurs to eat it
         */

        if (bracheasoursCorpseLoc.size() != 0){
            return moveActorToFoodSource(actor,map,bracheasoursCorpseLoc.get(0),bracheasoursCorpseLoc.get(1));
        }
        else if (otherCorpseLoc.size() != 0){
            return moveActorToFoodSource(actor,map,otherCorpseLoc.get(0),otherCorpseLoc.get(1));
        }
        else if (alivePterodactylsLoc.size() != 0){
            return moveActorToFoodSource(actor,map,alivePterodactylsLoc.get(0),alivePterodactylsLoc.get(1));
        }
        else if(eggLoc.size() != 0){
            return moveActorToFoodSource(actor,map,eggLoc.get(0),eggLoc.get(1));

        }
        else if(aliveStegoLoc.size() != 0){
            return moveActorToFoodSource(actor,map,aliveStegoLoc.get(0),aliveStegoLoc.get(1));

        }
        else {
            return null;
        }
    }

    /**
     * This method is for stegasours eating the closet fruit near it which is either on ground or on bush
     * @param actor this is a Stegasours
     * @param map GameMap of the game
     * @return Action that should be taken by Stegasours to move towards this food source
     */
    public Action stegasoursGetAction(Actor actor,GameMap map){
         /*
          maximum x position of map
          maximum y position of map
         */
        int maxX = map.getXRange().max();
        int maxY = map.getYRange().max();
        /*
          closet food source coordinates
         */
        ArrayList<Integer> closetSrcXYCor = new ArrayList<Integer>();
        /*
          downcasting Actor to HungryDino
         */
        HungryDino dino = ((HungryDino)(actor));

        /*
         check if Stegasours is hungry
         */
        if(dino.getFoodLevel() >= dino.getMinHitPoints())
            return null;
        /*
          iterate through whole map
         */
        for (int x = map.getXRange().min();x <= maxX;x++){
            for (int y = map.getYRange().min();y <= maxY;y++){

                if(!(map.at(x,y).containsAnActor())) {

                    /*
                      steps to reach the item
                     */
                    int steps = distance(map.locationOf(actor), map.at(x, y));

                    /*
                     check if at this location there is a bush which have fruit on it
                     */
                    if (map.at(x, y).getGround() instanceof Bush && ((Bush) map.at(x, y).getGround()).hasFruits() && steps < dino.getFoodLevel()) {
                        /*
                         set coordinates of closet food source
                         */
                        setBestNearestFoodSrc(closetSrcXYCor, map, actor, x, y);
                    } else {
                        /*
                        list of items at current location
                         */
                        List<Item> itemsList = map.at(x, y).getItems();
                        /*
                          go through the list of items at this location
                         */
                        for (Item item : itemsList) {
                            /*
                              if its a fruit and it is near the dino then get it
                             */
                            if ((item instanceof Fruit) && ((((Fruit) item).fallenDay() + steps) < 15) && steps < dino.getFoodLevel()) {
                                /*
                                    set coordinates of closet food source
                                */
                                setBestNearestFoodSrc(closetSrcXYCor, map, actor, x, y);
                            }
                        }
                    }
                }
            }
        }
        if (closetSrcXYCor.size() != 0){
            return moveActorToFoodSource(actor,map,closetSrcXYCor.get(0),closetSrcXYCor.get(1));
        }
        else {
            return null;
        }
    }

    /**
     * This method is for brachiosaur eating fruit from tree which is closet to it.Otherwise it doesnt eat anything
     * @param actor the dino which is hungry
     * @param map GamMap of the game
     * @return Action to move towards food source for  brachiosaur if its close enough otherwis eno action.
     */
    public Action brachiosaurGetAction(Actor actor,GameMap map){
        /*
          maximum x position of map
          maximum y position of map
         */
        int maxX = map.getXRange().max();
        int maxY = map.getYRange().max();
        /*
          closet food source coordinates
         */
        ArrayList<Integer> closetSrcXYCor = new ArrayList<Integer>();
         /*
          downcasting Actor to HungryDino
         */
        HungryDino dino = ((HungryDino)(actor));
        /*
         check if Brachiosaur is hungry
         */
        if(dino.getFoodLevel() >= dino.getMinHitPoints())
            return null;
        /*
          iterate through whole map
         */
        for (int x = map.getXRange().min();x <= maxX;x++) {
            for (int y = map.getYRange().min(); y <= maxY; y++) {

                if(!(map.at(x,y).containsAnActor())) {

                     /*
                      steps to reach the item
                     */
                    int steps = distance(map.locationOf(actor), map.at(x, y));

                    /*
                      check if at current location there is a tree with fruits
                     */
                    if (map.at(x, y).getGround() instanceof Tree && ((Tree) map.at(x, y).getGround()).hasFruits() && steps < dino.getFoodLevel()) {
                        setBestNearestFoodSrc(closetSrcXYCor, map, actor, x, y);
                    }
                }
            }
        }
        if (closetSrcXYCor.size() != 0){
            return moveActorToFoodSource(actor,map,closetSrcXYCor.get(0),closetSrcXYCor.get(1));
        }
        else {
            return null;
        }

    }

    /**
     * This method moves the Actor(dino) one step closer to food source
     * @param actor the current dino which is hungry
     * @param map GameMap of the game
     * @param x   x coordinate of the map containing food src
     * @param y   y coordinate of the map containing food src
     * @return Action that need to be taken to move towards food source
     */
    public Action moveActorToFoodSource(Actor actor, GameMap map, int x,int y){
        Location here = map.locationOf(actor);
        Location there = map.at(x,y);
        Action returnAction = null;

        int currentDistance = distance(here, there);
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                int newDistance = distance(destination, there);
                if (newDistance < currentDistance) {
                    returnAction = new MoveActorAction(destination, exit.getName());
                }
            }
        }
        return returnAction ;

    }

    /**
     * This methods helps us to choose what food src is closet to the actor.
     * @param closetSrcXYCor coordinates of old food source store in this list
     * @param map GameMap of the game
     * @param actor Actor which is hungry
     * @param x x coordinate which contains a  food source
     * @param y y coordinate which contains a  food source
     */
    public void setBestNearestFoodSrc( ArrayList<Integer> closetSrcXYCor,GameMap map,Actor actor,int x,int y){
        if (closetSrcXYCor.size() != 0){
            if (distance(map.locationOf(actor),map.at(x,y)) <
                    distance(map.locationOf(actor),map.at(closetSrcXYCor.get(0),closetSrcXYCor.get(1)))){
                closetSrcXYCor.set(0,x);
                closetSrcXYCor.set(1,y);
            }

        }
        else {
            closetSrcXYCor.add(x);
            closetSrcXYCor.add(y);
        }
    }

    /**
     * This method tells us how many steps (actor or item or ground) at location a is far from (actor or item or ground)
     * at location b.
     * @param a location of one (actor or item or ground)
     * @param b location of one (actor or item or ground)
     * @return the number of steps b is far from a
     */
    private int distance(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

}
