package game.portableItems;

import edu.monash.fit2099.engine.Location;
import game.dinosaur.Pterodactyls;
import game.dinosaur.Stegosaur;
import game.grounds.Tree;
import game.player.Player;

/**
 * This class represents the PterodactylEgg
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see Egg
 */
public class PterodactylEgg extends Egg {

    /**
     * This method forms a new instance of PterodactylEgg class
     */
    public PterodactylEgg() {
        super("PterodactylEgg", '0');
//        this.setHatchingPeriod(10);
        this.setHatchingPeriod(10);
    }

    /**
     * This method checks if PterodactylEgg is on Tree then increments the turnsOnGround as it can only hatch
     * on a tree and it hatches the PterodactylEgg when it reaches the HatchingPeriod and if there is no actor
     * at the same location as the egg
     * @param currentLocation Current Location on the map
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);

        if(currentLocation.getGround() instanceof Tree){
            turnsOnGround++;

            if(turnsOnGround >= this.getHatchingPeriod()){
                if(!currentLocation.containsAnActor()){
                    System.out.println(this + " has hatched at (" + currentLocation.x()+","+currentLocation.y()+")");
                    hatchEgg(currentLocation);
                }

            }

        }
    }
    /**
     *This method hatches the egg and Produces Pterodactyls  dino child and removes the egg from ground and
     * increments players ecopoints.
     * @param currentLocation its the current Location where a egg is placed
     */
    @Override
    public void hatchEgg(Location currentLocation) {
        char gender;

        if (Math.random() <= 0.5)
            gender = 'M';
        else {
            gender = 'F';
        }
        Player.ecoPointsInc("sh");
        currentLocation.removeItem(this);
        currentLocation.addActor(new Pterodactyls("Pterodactyl",gender,'C',20));

    }
}
