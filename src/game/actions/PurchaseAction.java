package game.actions;

import edu.monash.fit2099.engine.*;
import game.player.Player;
import game.grounds.VendingMachine;

import java.util.HashMap;
import java.util.Map;

/**
 * PurchaseAction  allows the Player to take action to buy PortableItems from the Vending machine if they have enough EcoPoints
 * to buy the item they choose to buy or they can quit from the vending machine menu
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 07/05/2021
 * @see Action
 */
public class PurchaseAction extends Action {

    /**
     * vending machine from which purchase has to be taken place.
     */
    protected VendingMachine vendmach;
    /**
     * display for the game
     */
    private Display display = new Display();
    /**
     * the name of the item
     */
    private String item;
    /**
     * the number of chosen item
     */
    private int choice;

    /**
     * This method creates the instance of PurchaseAction
     * @param vendmach vending machine from which purchase will take place
     */
    public PurchaseAction(VendingMachine vendmach){this.vendmach = vendmach;}

    /**
     * This method is run inorder to purchase the item from the vending machine
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return String if a item was purchased successfully or it was not possible to purchase it
     */
    @Override
    public String execute(Actor actor, GameMap map){

        HashMap<Item, Integer> saleItems = vendmach.getSaleItems();

        PrintVendingMachine(saleItems);
        choice = ChooseOption(saleItems);


        int count = 1;

        for(Map.Entry<Item, Integer> set : saleItems.entrySet()) {
            if(count == choice && choice != 9){
                if(Player.ecoPoints >= set.getValue()){
                    actor.addItemToInventory(set.getKey());
                    Player.ecoPoints -= set.getValue();
                    item = set.getKey().toString();
                    break;
                }
                else {
                    return "Insufficient Eco Points";
                }
            }
            else if (choice == 9){
                return "Player exited from Vending Machine wihout buying";
            }
            else
                count++;
        }


        return "Player buys "+ item;
    }

    /**
     * This displays the vending machine menu
     * @param saleItems items which are present in vending machine so it can be sold
     */
    public void PrintVendingMachine(HashMap<Item, Integer>saleItems){
        int index = 1;

        String title = String.format("%12s %23s", "ITEM", "ECO POINTS");
        display.println(title);
        display.println("------------------------------------");
        for(Map.Entry<Item, Integer> set : saleItems.entrySet()){
            String a = String.format("%-1d %-20s %s %-4d", index,set.getKey() , " : " , set.getValue());
            display.println(a);
            index++;
        }
        display.println(String.format("%-1d %-20s",index,"Quit"));

        display.println("\nPlayer's EcoPoints: " + Player.ecoPoints);
    }

    /**
     * This method tells us what item was chosen to be bought.
     * @param saleItems the items that are available in vending machine to be bought
     * @return the option which was selected which refers to a item in vending machine
     */
    public int ChooseOption(HashMap<Item, Integer>saleItems){
        boolean validOption = false;
        while(!validOption){
            display.println("Enter option(" + 1 + "-"+saleItems.size()+"): ");
            char c = display.readChar();
            choice = c - '0';
            if(choice < 1 || choice > saleItems.size()+1)
                display.println("Invalid option.");
            else
                validOption = true;
        }
        return  choice;

    }

    /**
     *
     * @param actor The actor performing the action.
     * @return String that actor bought something from vending machine
     */
    @Override
    public String menuDescription(Actor actor){return "Buy from vending machine";}

}
