package game.actions;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import game.dinosaur.Brachiosaur;
import game.dinosaur.HungryDino;
import game.dinosaur.Pterodactyls;
import game.grounds.Lake;

/**
 * DrinkAction class is responsible for HungryDinos (dinos) to make action to drink water from the lake if possible.
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 * @see Action
 */
public class DrinkAction extends Action{

    /**
     * Location of the lake
     */
    private Location lakeLocation;

    /**
     * This method forms the instance of DrinkAction
     * @param lakeLocation location of the lake
     */
    public DrinkAction(Location lakeLocation){
        this.lakeLocation = lakeLocation;
    }

    /**
     * This method allows dinos to drink water if its possible
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return String which tells dino drank from lake if its current water level is less than minimum water level of
     *         dino and if there are sips in the lake and if the actor is instance of HungryDino class
     */
    @Override
    public String execute(Actor actor, GameMap map){

        if(!(actor instanceof HungryDino)){return "";}

        HungryDino dino = ((HungryDino)(actor));

        if(dino.getWaterLvl() > dino.getMinWaterLvl()){return "";}

        Lake lake = (Lake)lakeLocation.getGround();

        if(lake.getSipCapacity() <= 0){return "";}

        if(dino instanceof Brachiosaur){
            /*
              checks how many sips does Brachiosaur needs to fill its self to full capacity
             */
            int toDrink = Math.min(80, dino.getMaxWaterLvl()-dino.getWaterLvl());

            /*
               check how many sips does brachiosaur drink from lake as it can only drink max 3 sips
             */
            int decSips = Math.min(3, (int)Math.ceil(toDrink / 30));

            lake.decSipCapacity(decSips);
            dino.incWaterLvl(toDrink);


        }
        else {
            /*
             * other dinos like Allosaur,Stegosaur and Pterodactyl can only drink one sip at a turn so
             * decrease one sip in lake
             */
            lake.decSipCapacity(1);

            int level = Math.min(30, dino.getMaxWaterLvl() - dino.getWaterLvl());

            /*
              increase the dinos water level
             */
            dino.incWaterLvl(level);


        }
        return dino + " drinks from the lake..";

    }

    /**
     * @param actor The actor performing the action.
     * @return that the actor drinks water
     */
    @Override
    public String menuDescription(Actor actor){return "drink";}
}
