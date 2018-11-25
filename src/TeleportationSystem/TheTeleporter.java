package TeleportationSystem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * @author      Jorvon Carter carterjorvon@gmail.com>
 */
public class TheTeleporter {
	
	public static void main(String[] args) {
		
		/**
		 * The city network.
		 */
		HashMap<String, City> network = new HashMap<String, City>();

		/**
		 * Load input text file.
		 */
		Scanner inputFile;
		try {
			inputFile = new Scanner(new FileReader("input.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Inupt file could not be read.");
			e.printStackTrace();
			return;
		}
		
		/**
		 * Read the input text file line-by-line. The format of the 
		 * line determines how the program will respond.
		 */	
		MainLoop:
		while(inputFile.hasNextLine()) {
			String inputLine = inputFile.nextLine();
				
			/**
			 * Add a city to the network.
			 */
			if (inputLine.contains("-")) {
				String[] cityPair = inputLine.split("-");
				Arrays.stream(cityPair).map(String::trim).toArray(unused -> cityPair);
				TeleporterFunctions.addCityToGraph(network, cityPair);
				continue MainLoop;
			}
			
			/**
			 * Find cities within jump distance.
			 */
			if(inputLine.contains("jumps") && inputLine.matches("([\\w\\s]+)\\d([\\w\\s]+)")) {	
				for(String city : network.keySet()) {
					if(inputLine.contains(city)) {
						int jumpsInt = Integer.parseInt(inputLine.replaceAll("[\\D]", ""));
						Set<String> citiesWithinDistance = TeleporterFunctions.citiesWithinDistance(jumpsInt, city, network);
						System.out.println(String.format("cities from %s in %s jumps: %s", 
								city, jumpsInt, citiesWithinDistance));
						continue MainLoop;
					}
				}
				System.out.println("Jump origin city not found.");
			}
			
			/**
			 * Determine whether teleportation is possible between two cities.
			 */
			if(inputLine.matches("([\\w\\s]+)(\\s)from(\\s)([\\w\\s]+)(\\s)to(\\s)([\\w\\s]+)")) {
				ArrayList<String> originAndDestination = new ArrayList<String>();
				for(String city : network.keySet()) {
					if (inputLine.contains(city)) {
						originAndDestination.add(city);
					}
				}
				if(originAndDestination.size() == 2 && !originAndDestination.get(0).equals(originAndDestination.get(1))) {
					System.out.println(String.format("Can I teleport from %s to %s: %s", 
							originAndDestination.get(0), originAndDestination.get(1), 
							TeleporterFunctions.possibleTeleport(originAndDestination.get(0), originAndDestination.get(1), network)));
					originAndDestination.clear();
					continue MainLoop;
				}
				else {
					System.out.println("Origin-destination city pair not found.");
					originAndDestination.clear();
				}
			}
			
			/**
			 * Determine whether loop is possible from a city.
			 */
			if(inputLine.contains("loop")) {
				for(String city : network.keySet()) {
					if(inputLine.contains(city)) {
						System.out.println(String.format("loop possible from %s: %s", city, TeleporterFunctions.possibleLoop(city, network)));
						continue MainLoop;
					}
				}
				System.out.println("loop origin city not found");
			}
		}	
		inputFile.close();
	}
}
