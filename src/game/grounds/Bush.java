package game.grounds;

import edu.monash.fit2099.engine.Location;
import game.dinosaur.Brachiosaur;

/**
 * This class represents  the Bush which produced on the Ground
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Plant
 */
public class Bush extends  Plant{

    /**
     * This methods creates the instance of Bush class.Passes 1% probability for a bush to make fruit
     */
    public Bush(){
        super("Bush",0.1);
        displayChar = '`';
    }

    /**
     * This methods runs at each turn for every bush and calls makeFruit method to create fruits.Also checks
     * if a Brachiosaur dino crushed the bush
     * @param location The location of the bush in the map
     */
    @Override
    public void tick(Location location) {
        super.tick(location);

        //To make fruit for bush
        makeFruit();

        // checks if the bush is crushed by Brachiosaur
        if(location.containsAnActor()){
            if(location.getActor() instanceof Brachiosaur){
                if(Math.random() <= 0.5)
                    location.setGround(new Dirt());
            }
        }
    }

}
