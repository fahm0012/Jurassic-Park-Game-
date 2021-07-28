package game.behaviours;

import edu.monash.fit2099.engine.*;
import game.dinosaur.HungryDino;
import game.dinosaur.Pterodactyls;
import game.grounds.Tree;
import game.player.Player;

import java.util.ArrayList;
/**
 * This class allows HungryDino(dino) to move to towards a potential partner in order to mate.
 * @author Fahad and Shafkat
 * @version 1.1.0
 * @since 22/05/2021
 * @see Behaviour
 */
public class MoveToMateBehaviour implements Behaviour{
    /**
     * This method finds closet Actor(dino) for our current Actor(dino) which can mate with this current Actor(dino)
     * @param actor the Actor(dino) acting
     * @param map the GameMap containing the Actor
     * @return Action to be taken by actor(dino) to move toward other Actor(dino) so it can mate.It may return null
     * if actor(dino) cant mate or a suitable actor(dino) isn't available to mate
     */
    @Override
    public Action getAction(Actor actor, GameMap map) {


        if (!(actor instanceof Player)) {
            int maxX = map.getXRange().max();
            int maxY = map.getYRange().max();
            ArrayList<Integer> closetMate = new ArrayList<Integer>();
            for (int x = map.getXRange().min();x <= maxX;x++) {
                for (int y = map.getYRange().min(); y <= maxY; y++) {
                    if (map.at(x,y).containsAnActor()){
                        int steps = distance(map.locationOf(actor),map.at(x,y));
                        Actor target = map.at(x,y).getActor();
                        if( target instanceof HungryDino && isReady(actor)&& isReady(target) && sameSpecies(actor,target) && oppositeSex(actor,target)){

                            if(actor instanceof Pterodactyls &&  (((HungryDino) target).getFoodLevel()- (steps+1) >= ((HungryDino) target).getMinHitPoints()) && (((HungryDino) target).getFoodLevel()- (steps+1) >= ((HungryDino) target).getMinWaterLvl()))
                            {
                                pterodactylOnTreeAndAdjacentTree(closetMate,actor,map,target);
                            }
                            else if (steps <= 8) {
                                setBestNearestMatingPartner(closetMate, map, actor, x, y);
                            }

                        }
                    }
                }
            }
            if (closetMate.size() != 0){
                return moveActorToMatingPartner(actor,map,closetMate.get(0),closetMate.get(1));
            }
            else {
                return null;
            }

        }
        return null;

    }


    /**
     * This method checks if the dino can mate as it doesnt have a egg already,has a food level to mate and is an Adult
     * dino
     * @param actor the dino acting
     * @return true if dino can mate
     */
    private boolean isReady(Actor actor){

        int level;

        if (actor.getDisplayChar() == 'd')
            level = 50;
        else if (actor.getDisplayChar() == 'b')
            level = 70;
        else
            level = 50;

        return (((HungryDino)actor).getFoodLevel() >= level && ((HungryDino)actor).getGrowthState() == 'A' && !(((HungryDino)actor).getHasEgg()));

    }
    /**
     * This methods checks if both dinos are of same species
     * @param actor the dino acting
     * @param target the dino with which mating will take place
     * @return true if both dinos are same species
     */
    private boolean sameSpecies(Actor actor, Actor target){
        return actor.getDisplayChar() == target.getDisplayChar();
    }

    /**
     * This method checks if both dinos are of opposite sex
     * @param actor the dino acting
     * @param target the dino with which mating will take place
     * @return true if both dinos are of opposite sex
     */
    private boolean oppositeSex(Actor actor, Actor target){
        return (((HungryDino) actor).getGender() != ((HungryDino) target).getGender());
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

    /**
     * This methods sets a close mating partner coordinates in closetMate list.
     * @param closetMate cordinates of the closet mates so far
     * @param map GameMap of the game
     * @param actor actor which is mating
     * @param x x cordinate of new mating partner
     * @param y y cordinate of new mating partner
     */
    public void setBestNearestMatingPartner( ArrayList<Integer> closetMate,GameMap map,Actor actor,int x,int y){
        if (closetMate.size() != 0){
            if (distance(map.locationOf(actor),map.at(x,y)) <
                    distance(map.locationOf(actor),map.at(closetMate.get(0),closetMate.get(1)))){
                closetMate.set(0,x);
                closetMate.set(1,y);
            }

        }
        else {
            closetMate.add(x);
            closetMate.add(y);
        }
    }

    /**
     * This method returns a action which should be taken to move current actor(dino) one step close the the mate partner
     * @param actor the dino acting
     * @param map GameMap of the game
     * @param x  x-coordinate of the location of mate partner for current dino
     * @param y  y-coordinate of the location of mate partner for current dino
     * @return Action which should be taken to move current actor(dino) one step close the the mate partner
     */
    public Action moveActorToMatingPartner(Actor actor, GameMap map, int x, int y){
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

    /**
     * This method move Pterodactyls dinos to the closet tree where it can mate
     * @param closetMate ArrayList containing the coordinates of the closet target which is found till this point
     * @param actor  it is a Pterodactyls which needs to mate
     * @param map    GameMap of the game
     * @param target the partner with which  Pterodactyls wants to mate
     */
    private void pterodactylOnTreeAndAdjacentTree(ArrayList<Integer> closetMate,Actor actor,GameMap map,Actor target){
        if (map.locationOf(target).getGround() instanceof Tree){
            int x = map.locationOf(target).x();
            int y = map.locationOf(target).y();
            int finalX = -1;
            int finalY = -1;
            if (x != map.getXRange().min() && map.at(x-1,y).getGround() instanceof Tree && !(map.at(x-1,y).getActor() instanceof Pterodactyls)){
                if (finalX != -1 && finalY != -1){
                    if ( distance(map.at(x-1,y),map.locationOf(actor)) < distance(map.at(finalX,finalY),map.locationOf(actor))){
                        finalX = x-1;
                        finalY = y;
                    }

                }
                else {
                    finalX = x-1;
                    finalY = y;
                }
            }
            if (x != map.getXRange().max() && map.at(x+1,y).getGround() instanceof Tree && !(map.at(x+1,y).getActor() instanceof Pterodactyls)){
                if (finalX != -1 && finalY != -1){
                    if ( distance(map.at(x+1,y),map.locationOf(actor)) < distance(map.at(finalX,finalY),map.locationOf(actor))){
                        finalX = x+1;
                        finalY = y;
                    }

                }
                else {
                    finalX = x+1;
                    finalY = y;
                }
            }
            if (y != map.getYRange().max() && map.at(x,y+1).getGround() instanceof Tree && !(map.at(x,y+1).getActor() instanceof Pterodactyls)){
                if (finalX != -1 && finalY != -1){
                    if ( distance(map.at(x,y+1),map.locationOf(actor)) < distance(map.at(finalX,finalY),map.locationOf(actor))){
                        finalX = x;
                        finalY = y+1;
                    }

                }
                else {
                    finalX = x;
                    finalY = y+1;
                }
            }
            if (y != map.getYRange().min() && map.at(x,y-1).getGround() instanceof Tree && !(map.at(x,y-1).getActor() instanceof Pterodactyls)){
                if (finalX != -1 && finalY != -1){
                    if ( distance(map.at(x,y-1),map.locationOf(actor)) < distance(map.at(finalX,finalY),map.locationOf(actor))){
                        finalX = x;
                        finalY = y-1;
                    }

                }
                else {
                    finalX = x;
                    finalY = y-1;
                }
            }

            if (finalX != -1 && finalY != -1){
                setBestNearestMatingPartner(closetMate, map, actor, finalX, finalY);
            }
        }
    }


}
