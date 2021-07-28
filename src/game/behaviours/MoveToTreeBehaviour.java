package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.dinosaur.HungryDino;
import game.grounds.Tree;
import game.player.Player;

import java.util.ArrayList;

/**
 * This class allows Pterodactyls to move to towards a tree.
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see Behaviour
 */
public class MoveToTreeBehaviour implements Behaviour{

    /**
     * This method allows the actor to move towards the tree if possible
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return Action to move toward the tree if possible
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {
        /*
          checks that actor is not instance of Player
         */
        if (!(actor instanceof Player) && !(map.locationOf(actor).getGround() instanceof Tree)){

            /*
          maximum x position of map
          maximum y position of map
         */
            int maxX = map.getXRange().max();
            int maxY = map.getYRange().max();
        /*
          closet Tree  coordinates
         */
            ArrayList<Integer> closetSrcXYCor = new ArrayList<Integer>();
         /*
          downcasting Actor to HungryDino
         */
            HungryDino dino = ((HungryDino)(actor));
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
                        if (map.at(x, y).getGround() instanceof Tree && steps < dino.getFoodLevel() && steps < dino.getWaterLvl() && (map.at(x, y).containsAnActor())) {
                            setBestNearestFoodSrc(closetSrcXYCor, map, actor, x, y);
                        }
                    }
                }
            }
            if (closetSrcXYCor.size() != 0){
                return moveToTree(actor,map,closetSrcXYCor.get(0),closetSrcXYCor.get(1));
            }
            else {
                return null;
            }

        }
        return null;
    }



    /**
     * This methods helps us to choose what tree is closet to the actor.
     * @param closetSrcXYCor coordinates of old tree stored in this list which is closet to the actor
     * @param map GameMap of the game
     * @param actor Actor which wants to move towards the tree
     * @param x x coordinate which contains current tree x coordinate
     * @param y y coordinate which contains current tree y coordinate
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
     * This method tells us how many steps is the actor away from the tree
     * at location b.
     * @param a location of one (actor or item or ground)
     * @param b location of one (actor or item or ground)
     * @return the number of steps b is far from a
     */
    private int distance(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }


    /**
     * This method moves the Actor(dino) one step closer to the tree
     * @param actor Actor which wants to move towards the tree
     * @param map  map of the game at which actor is present
     * @param x x coordinate which contains current tree x coordinate
     * @param y y coordinate which contains current tree y coordinate
     * @return Action to move towards the tree
     */
    public Action moveToTree(Actor actor, GameMap map, int x,int y){
        Location here = map.locationOf(actor);
        Location there = map.at(x,y);

        int currentDistance = distance(here, there);
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                int newDistance = distance(destination, there);
                if (newDistance < currentDistance) {
                    return new MoveActorAction(destination, exit.getName());
                }
            }
        }
        return null;
    }

}
