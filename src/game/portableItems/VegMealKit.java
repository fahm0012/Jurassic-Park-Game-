package game.portableItems;

/**
 * This class represents the Vegetarian meal kit which can be purchased from vending machine
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see PortableItem
 */
public class VegMealKit extends PortableItem {
    /**
     * if true tells us that it can be eaten by Vegetarians(herbivores)
     */
    private boolean vegetarian;

    /**
     * This method creates instance of VegMealKit class
     */
    public VegMealKit(){
        super("Vegetarian Meal Kit", 'V');
        vegetarian = true;
    }

    /**
     * @return true as VegMealKit can be eaten by Vegetarians(herbivores)
     */
    public boolean getVegetarian(){
        return  vegetarian;
    }
}
