import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import org.newdawn.slick.Image;

/* SWEN20003 Object Oriented Software Development
 * ParseFile Class
 * Author: Nihal Mirpuri <nmirpuri>
 */

/**
 * Reads the input file, and creates items and units based on the input
 * Returns the stats of this file upon request
 * @author Nihal Mirpuri
 */
public class ParseFile {
	private static BufferedReader br;

	/** 
	 * Reads the file input, and extracts known objects from file to initialize in world
	 * @param world The world in which the game is running
	 * @param filepath The path to the file that will be used to find data
	 */
	public static void readFile(World world, String filepath) {
			try{
			// First try to open the file
			FileInputStream fileStream = new FileInputStream(filepath);
			DataInputStream dataStream = new DataInputStream(fileStream);
			br = new BufferedReader(new InputStreamReader(dataStream));
			
			// Creating variables that will be used to parse each line
			String line;
			StringTokenizer token;
			//TODO: Make the file compatible with the vector class (instead of array)
			Vector list = new Vector();
			String[] words;
			int counter = 0;
			Stats stats = new Stats();
			
			// Read each line that is input until file reaches EOF or a set number of blank lines in a row
			while(true) {
				// Read the first line, and checks if it's null. If it is, increase counter
				if((line = br.readLine()).isEmpty()) {
					counter++;
						// If counter reaches a certain threshold, break the loop (considered EOF)
					if(counter > 5)
						break;
				} else {
					//TODO: Fix this code to work with data.txt instead of units.txt and items.txt
					//TODO: Ignore commented out lines
					
					// Reset the counter, since a non-blank line has been found
					counter = 0;
					
					// Create a token for the first word
					token = new StringTokenizer(line, " ");
				
					words = new String[6];
					
					// Read up to the first 7 words on each line
					for(int i = 0; i < 7; i++) {
						if(token.hasMoreTokens()) {
							words[i] = token.nextToken(" 	,");
						}
					}
					
					// Make the first word all lower case
					words[0] = words[0].toLowerCase();
					
					// Ignore commented out lines
					if(words[0].startsWith("#"))
						continue;
					
					// Read the general unit data
					//TODO: Remove the Stats class
					if(!words[0].isEmpty() && words[1].endsWith(".png")) {
						stats.addStats(words);
					}
					
					// Check if coordinates are defined
					if(isNumeric(words[1]) && isNumeric(words[2])) {
						//TODO Code for adding a new object goes here
						
						// Checks if a player is already defined
						if(words[0].equals("player")) {
							if(world.getPlayer() != null) {
								System.err.println("Error: More than one player specified");
								return;
							}
						}
						
					}
					
					// Create the player object, if not already, from the file
					/**if(words[0].equals("player")) {
						if(world.getPlayer() != null) {
							System.err.println("Error: More than one player specified");
							return;
						} else {
							Image sprite = new Image(Game.ASSETS_PATH + "/units/" + stats.getImage("player"));
							world.setPlayer(new Player(300, stats.getFirepower("player"), 
									stats.getShield("player"), stats.getShield("player"), 
									stats.getSpeed("player"), stats.getDamage("player"),
									Double.parseDouble(words[1]),
									Double.parseDouble(words[2]), sprite, true, true));
						}
					}*/

					// Create the enemies from file
					/**if(words[0].equals("fighter") && isNumeric(words[1]) && isNumeric(words[2])) {
						Image sprite = new Image(Game.ASSETS_PATH + "/units/" + stats.getImage(words[0]));
						world.addFighter(new Fighter(stats.getShield(words[0]), stats.getShield(words[0]), 
								stats.getSpeed(words[0]), stats.getDamage(words[0]), Double.parseDouble(words[1]), 
								Double.parseDouble(words[2]), sprite, true, false));
					}
					
					if(words[0].equals("asteroid") && isNumeric(words[1]) && isNumeric(words[2])) {
						Image sprite = new Image(Game.ASSETS_PATH + "/units/" + stats.getImage(words[0]));
						world.addAsteroid(new Asteroid(stats.getShield(words[0]), stats.getShield(words[0]), 
								stats.getSpeed(words[0]), stats.getDamage(words[0]), Double.parseDouble(words[1]), 
								Double.parseDouble(words[2]), sprite, true, false));
					}
					
					if(words[0].equals("drone") && isNumeric(words[1]) && isNumeric(words[2])) {
						Image sprite = new Image(Game.ASSETS_PATH + "/units/" + stats.getImage(words[0]));
						world.addDrone(new Drone(stats.getShield(words[0]), stats.getShield(words[0]), 
								stats.getSpeed(words[0]), stats.getDamage(words[0]), Double.parseDouble(words[1]), 
								Double.parseDouble(words[2]), sprite, true, false));
					}
					
					if(words[0].equals("boss") && isNumeric(words[1]) && isNumeric(words[2])) {
						Image sprite = new Image(Game.ASSETS_PATH + "/units/" + stats.getImage(words[0]));
						world.addBoss(new Boss(stats.getShield(words[0]), stats.getShield(words[0]), 
								stats.getSpeed(words[0]), stats.getDamage(words[0]), Double.parseDouble(words[1]), 
								Double.parseDouble(words[2]), sprite, true, false));
					} */
					
					// Create items from file
					if(words[0].equals("repair") && isNumeric(words[1]) && isNumeric(words[2])) {
						Image sprite = new Image(Game.ASSETS_PATH + "/items/" + words[0] + ".png");
						world.addRepair(new Repair(0, false, Double.parseDouble(words[1]), 
								Double.parseDouble(words[2]), sprite, true, true));
					}
					
					if(words[0].equals("firepower") && isNumeric(words[1]) && isNumeric(words[2])) {
						Image sprite = new Image(Game.ASSETS_PATH + "/items/" + words[0] + ".png");
						world.addFirepower(new Firepower(0, false, Double.parseDouble(words[1]), 
								Double.parseDouble(words[2]), sprite, true, true));
					}
					
					if(words[0].equals("shield") && isNumeric(words[1]) && isNumeric(words[2])) {
						Image sprite = new Image(Game.ASSETS_PATH + "/items/" + words[0] + ".png");
						world.addShield(new Shield(0, false, Double.parseDouble(words[1]), 
								Double.parseDouble(words[2]), sprite, true, true));
					}
					
				}
				
			}
			br.close();
		} catch (Exception e) {
			// Print the error if encountered
			System.err.println("Error: " +e.getMessage());
		}
	}

	/** 
	 * Code to detect if string is numeric
	 * @param str String to detect if numeric
	 * @return Returns if the string is numeric or not
	 */
	public static boolean isNumeric(String str) {
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);
		} catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
