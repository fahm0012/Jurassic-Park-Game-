package game;

import java.util.Arrays;
import java.util.List;

import edu.monash.fit2099.engine.*;
import game.dinosaur.Allosaur;
import game.dinosaur.Brachiosaur;
import game.dinosaur.Pterodactyls;
import game.dinosaur.Stegosaur;
import game.grounds.*;
import game.player.Player;
import game.portableItems.Corpse;

/**
 * The main class for the Jurassic World game.
 * @author Fahad and Shafkat
 * @version 1.0.1
 * @since 22/05/2021
 */
public class Application {

	public static void main(String[] args) {
		BGM.Play();

		World world = new World(new Display());

		FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Tree(), new VendingMachine(), new Bush(), new Lake());


		List<String> mapNorth = Arrays.asList(
				"............................................................$...................",
				"................................................................................",
				"...............................$................................................",
				"................................................~...............................",
				".............................................~~~................................",
				"..............................$............~~~~.........................$.......",
				"............................................~~~~................................",
				"..........$...........................+++.....~~~...............................",
				".......................................++++...~~~~..............................",
				"...................................+++++......~~~~..........$...................",
				"...............$............~~~~~~...++++++...~~~~~~............................",
				"..........................~~~~~~~~~...+++.....~~~~~~~...........................",
				".........................~~~~~~~~~...+++.....~~~~~~~~...........................",
				".........................~~~~~~~~~...........~~~~~~...................$.........",
				"............+++...........~~~~~~~~~...$......~~~~~..............................",
				".............+++++........~~................~~~~..........+.....................",
				"...............++..........~~...............~~...........+++++..................",
				".............+++..........~~~.........$....~........++++++++....................",
				"............+++.........~~~......................++++++++.......................",
				"......................~~~~~.....................+++++...........................",
				".....................~~~~...............$........++++....................++.....",
				"....................~~~...........................+++...................++.++...",
				"..........$.........~~~............................++....................++++...",
				".................................$..................+.....................++....",
				".................................................................$..............");

		List<String> map = Arrays.asList(
		"...........................................................$....................",
		".................++..............................++.............................",
		".....#######...................$................................................",
		".....#_$___#.....++........................++....++..........~~~~.......++......",
		".....#_____#.....................................~~..........~~~~.......+.......",
		".....###.###...........++.....$.................~~~..........~~~~~......$.......",
		".................................................~~..........~~~~~~.............",
		"..........$............++.............+++.........~..........~~~~~~~............",
		".......................................++++...................~~~~~.....++......",
		".+++.............++....++..........+++++....................$..~~~.+............",
		".++......~.....$.....................++++++........................+............",
		".+.......~~...........................++++......................................",
		"...........~.........................+++...........~~.............++............",
		"....+...................++........................~~~............++...$.........",
		"...++.......+++........++.............$..........~~~........+....+..............",
		".............+++++......+...................................++..................",
		"...............++........................................+++++..................",
		".............+++......................$.............++++++++....................",
		"............+++.......................................+++.......................",
		"................................................................................",
		"............................~...........$................................++.....",
		"..+........................~~...........+...............................++.++...",
		"..++......$...............~............+++........++.....................++++...",
		"...+...................................++.................................++....",
		".................................................................$..............");

		GameMenu menu = new GameMenu();
		menu.display();


		GameMap gameMap = new GameMap(groundFactory, map );
		world.addGameMap(gameMap);

		GameMap gameMapNorth = new GameMap(groundFactory, mapNorth);
		world.addGameMap(gameMapNorth);

		Actor player = new Player("Player", '@', 100);
		world.addPlayer(player, gameMap.at(41, 6));


		int maxX = gameMap.getXRange().max();
		int maxY = gameMap.getYRange().max();


		for(int x =0; x <= maxX; x++){
			gameMap.at(x,0).addExit(new Exit("to North Map", gameMapNorth.at(x,maxY),"/"));
			gameMapNorth.at(x,maxY).addExit(new Exit("to South Map", gameMap.at(x,0),"/"));

		}

		/*
		adding 2 Pterodactyls
		 */
		gameMap.at(41,8).addActor(new Pterodactyls("Pter1", 'M','A',70));
		gameMap.at(40,11).addActor(new Pterodactyls("Pter2", 'F','A',70));

		/*
		adding 2 Stegosaurus
		 */
		gameMap.at(30, 12).addActor(new Stegosaur("Stegosaur",'M','A',50));
		gameMap.at(32, 12).addActor(new Stegosaur("Stegosaur",'F','A',50));

		/*
		 adding 2 pairs of Brachiosaurs
		 */
		gameMap.at(62, 17).addActor(new Brachiosaur("Brachiosaur",'M','A',100));
		gameMap.at(50, 16).addActor(new Brachiosaur("Brachiosaur",'F','A',100));
		gameMap.at(60, 15).addActor(new Brachiosaur("Brachiosaur",'F','A',100));
		gameMap.at(53, 16).addActor(new Brachiosaur("Brachiosaur",'M','A',100));

		world.run();
	}
}
/*
*
*
*
*
* */