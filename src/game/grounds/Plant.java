package game.grounds;

import edu.monash.fit2099.engine.Ground;
import game.portableItems.Fruit;
import game.actions.PickFruitFromPlantAction;
import game.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a abstract class for Bush and Tree as they are plants
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Ground
 */
public abstract class Plant extends Ground {
    /**
     * type of plant Bush or Tree
     */
    private String plantType;
    /**
     * The probability of making fruit for the plant
     */
    private double makeFruitPorbability;
    /**
     * list of fruits produced by the plant
     */
    private List<Fruit> fruitList = new ArrayList<Fruit>();

    /**
     * This method forms a instance of Plant class.Its a abstract class so its not possible.It uses parent class
     * Ground constructor for its child classes
     * @param plantType
     * @param makeFruitPorbability
     */
    public Plant(String plantType,double makeFruitPorbability){
        super('P');

        this.makeFruitPorbability = makeFruitPorbability;
        this.plantType = plantType;
    }

    /**
     * This method allows player to get fruit from plant.
     * @return Action of type PickFruitFromPlantAction
     */
    public PickFruitFromPlantAction getFruit(){
        return new PickFruitFromPlantAction(this);
    }

    /**
     * @return the type of plant
     */
    public String getPlantType(){return plantType;}

    /**
     * @return the list of fruits produced by the plant
     */
    public List<Fruit> getFruitList() {
        return Collections.unmodifiableList(fruitList);
    }

    /**
     * This method adds a new fruit produced into fruit list for that plant
     * @param fruit new fruit produced by the plant
     */
    public void addToFruitList(Fruit fruit) { fruitList.add(fruit); }

    /**
     * This method removes the fruit from the list as its picked by the player
     */
    public void removeFromFruitList() {
        if(hasFruits()){fruitList.remove(0);}
    }

    /**
     * This method tells if the plant have fruits on it
     * @return true if there is a fruit on the plant
     */
    public boolean hasFruits(){
        if (fruitList.isEmpty())
            return false;
        else
            return true;
    }

    /**
     * This method makes fruit for the plant according to the probability for that plant to make fruit
     * and it fruit is grown by tree then it increments ecoPoints for the player as a riped fruit is formed
     * @return true if a fruit is formed by the plant.
     */
    public boolean makeFruit(){

        if(Math.random() <= makeFruitPorbability){
            if(this instanceof Tree){
                Player.ecoPointsInc("rf");
            }
            addToFruitList(new Fruit());
            return true;
        }
        else
            return false;
    }

}
