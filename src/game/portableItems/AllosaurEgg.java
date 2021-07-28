package game.portableItems;

import edu.monash.fit2099.engine.Location;
import game.player.Player;
import game.dinosaur.Allosaur;

/**
 * This class represents the AllosaurEgg
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Egg
 */
public class AllosaurEgg extends Egg {
    /**
     * This methods creates the instance of AllosaurEgg class.
     */
    public AllosaurEgg(){
        super("AllosaurEgg", '0');
        this.setHatchingPeriod(50);

    }

    /**
     * This tick method runs and increments the turns the egg is on Ground and if its equal to hatching period
     * it hatches the Allosaur Egg and produces a baby Allosaur.
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

        /*
         Takes a 50% probability to produce Male or Female Allosaur baby dino
         */
        if (Math.random() <= 0.5)
            gender = 'M';
        else {
            gender = 'F';
        }

        /*
          increments ecoPoints of Player as Allosaur egg hatching
          */
        Player.ecoPointsInc("ah");

        /*
          adding the baby Allosaur to the game map.
         */
        currentLocation.addActor(new Allosaur("Allosaur", gender));

        /*
         removing the egg from game map as egg has hatched into a baby Allosaur
         */
        currentLocation.removeItem(this);

    }
}
