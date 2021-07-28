package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.dinosaur.HungryDino;
import game.grounds.Lake;
import game.player.Player;

import java.util.ArrayList;


/**
 * This class allows HungryDinos(dino) to move to towards a water source.
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see Behaviour
 */
public class MoveToWaterSource implements Behaviour{


    /**
     * @param actor the Actor(dino) acting
     * @param map the GameMap containing the Actor(dino)
     * @return Action to be taken by the Actor(dino) to move toward water source.No action can also be return if no water source
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
         returning action
         */
        Action returnAction;

        /*
          stores coordinates of closest water source
         */
         ArrayList<Integer> closetSrcXYCor = new ArrayList<Integer>();

        /*
          checks that actor is not instance of Player
         */
        if (!(actor instanceof Player)){
        /*
          downcasting Actor to HungryDino
         */
            HungryDino dino = ((HungryDino)(actor));

            /*
              check if the dino is thirsty
            */
            if(dino.getWaterLvl() >= dino.getMinWaterLvl())
                return null;
            else {
                /*
                  iterate through whole map
                */
                for (int x = map.getXRange().min();x <= maxX;x++){
                    for (int y = map.getYRange().min();y <= maxY;y++){

                        if(!(map.at(x,y).containsAnActor())) {
                            if (map.at(x, y).getGround() instanceof Lake && !(map.at(x, y).containsAnActor())) {
                                setBestNearestFoodSrc(closetSrcXYCor, map, actor, x, y);
                            }
                        }
                    }
                }

            }

        }
        if (closetSrcXYCor.size() != 0){
            returnAction = moveActorToWaterSource(actor,map,closetSrcXYCor.get(0),closetSrcXYCor.get(1));
        }
        else {
            returnAction = null;
        }
        return returnAction;
    }

    /**
     * This method moves the Actor(dino) one step closer to water source
     * @param actor the current dino which is thirsty
     * @param map GameMap of the game
     * @param x  x coordinate of the map containing water src
     * @param y  y coordinate of the map containing water src
     * @return Action that should be taken to move towards water src
     */
    public Action moveActorToWaterSource(Actor actor, GameMap map, int x, int y){
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
     * This methods helps us to choose what water src is closet to the actor.
     * @param closetSrcXYCor coordinates of old water source store in this list
     * @param map GameMap of the game
     * @param actor Actor which is thirsty
     * @param x x coordinate which contains a water source
     * @param y y coordinate which contains a water source
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
