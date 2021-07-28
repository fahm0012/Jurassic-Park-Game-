package game.portableItems;

import game.portableItems.PortableItem;

/**
 * This class represents the Carnivore meal kit which can be purchased from vending machine
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see PortableItem
 */
public class CarnMealKit extends PortableItem{
    /**
     * tells if it can be eaten by Herbivores or Carnivores.
     */
    private boolean vegetarian;

    /**
     * This methods creates the instance of CarnMealKit class
     */
    public CarnMealKit(){
        super("Carnival Meal Kit",'C');
    }

    /**
     * @return False as this meal kit cant be eaten by Herbivores
     */
    public boolean getVegetarian(){
        return  vegetarian;
    }

}
