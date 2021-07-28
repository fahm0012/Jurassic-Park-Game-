package game.portableItems;

import edu.monash.fit2099.engine.Location;
import game.player.Player;
import game.dinosaur.Stegosaur;

/**
 * This class represents the StegosaurEgg
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Egg
 */
public class StegosaurEgg extends Egg{

    /**
     * This methods creates the instance of StegosuarEgg class.
     */
    public StegosaurEgg(){
        super("StegosaurEgg",'0');
        this.setHatchingPeriod(10);

    }
    /**
     * This tick method runs and increments the turns the egg is on Ground and if its equal to hatching period
     * it hatches the Stegosuar Egg and produces a baby Stegosuar.
     * @param currentLocation Current Location on the map
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);

        turnsOnGround++;

        if(turnsOnGround >= this.getHatchingPeriod()){
            if(!currentLocation.containsAnActor()){
                System.out.println(this + " has hatched at (" + currentLocation.x()+","+currentLocation.y()+")");
                hatchEgg(currentLocation);
            }

        }
    }
    /**
     *
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
        currentLocation.addActor(new Stegosaur("Stegosaur",gender,'C',10));

    }

}
