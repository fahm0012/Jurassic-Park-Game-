package game.grounds;

import edu.monash.fit2099.engine.*;
import game.actions.PurchaseAction;
import game.portableItems.*;
import game.weapon.Laser;

import java.util.HashMap;

/**
 * This method represents the Vending machine present in the game
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see Ground
 */
public class VendingMachine extends Ground {
    /**
     * hashmap to store items of VendingMachine
     */
    private HashMap<Item, Integer> saleItems;

    /**
     * This method creates a instance of VendingMachine class
     */
    public VendingMachine(){
        super('$');
        createAndAddItemsInVendingMachine();


    }

    /**
     * This method adds the item and there price into a vending machine when its new instance is formed
     */
    public void createAndAddItemsInVendingMachine(){

        saleItems = new HashMap<Item,Integer>();
        saleItems.put(new Fruit(), 30);
        saleItems.put(new VegMealKit(), 100);
        saleItems.put(new CarnMealKit(), 500);
        saleItems.put(new StegosaurEgg(), 200);
        saleItems.put(new PterodactylEgg(), 200);
        saleItems.put(new BrachiosaurEgg(), 500);
        saleItems.put(new AllosaurEgg(), 1000);
        saleItems.put(new Laser(),500);



    }

    /**
     * @return the hashmap of vending machine containing all the items which can be sold
     */
    public HashMap<Item, Integer> getSaleItems() {
        return saleItems;
    }

    /**
     * @param actor the Actor to check
     * @return false as actor cant enter the place where a vending machine is placed
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    /**
     * This method have actions which others actors can do on vending machine.In this case they can only purchase
     * from vending machine
     * @param actor the Actor acting
     * @param location the current Location
     * @param direction the direction of the Ground from the Actor
     * @return Actions which can be done by actors on this vending machine
     */
    @Override
    public Actions allowableActions(Actor actor, Location location, String direction){
        return new Actions(new PurchaseAction(this));
    }

}

