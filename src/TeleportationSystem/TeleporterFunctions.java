package TeleportationSystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * @author      Jorvon Carter carterjorvon@gmail.com>
 */
public class TeleporterFunctions {
	
	/**
	 * Adds one or two cities to the teleportation network.
	 *
	 * The method assumes that two cities are entered as a
	 * "city-adjacent-city" tuple. The city and its neighbor
	 * are both added.
	 * 
	 * @param network 	an undirected hash table graph of cities 
	 * @param cityPair  "city-adjacent-city tuple
	 * @return         	nothing
	 */
	public static void addCityToGraph(HashMap<String, City> network, String[] cityPair) {
		if(!network.containsKey(cityPair[0])) {
			City city = new City(cityPair[0]);
			network.put(cityPair[0], city);
		}
		if(!network.containsKey(cityPair[1])) {
			City city = new City(cityPair[1]);
			network.put(cityPair[1], city);
		}
		
		network.get(cityPair[0]).setAdjacentCity(cityPair[1]); 
		network.get(cityPair[1]).setAdjacentCity(cityPair[0]);
	}
	
	/**
	 * Finds the cities within a jump distance of an origin city.
	 * 
	 * @param jumps 		the jump distance from the origin
	 * @param originCity	the jump origin city
	 * @param network		an undirected hash table graph of cities
	 * @return         		a set of all the cities within the jump distance
	 */
	public static Set<String> citiesWithinDistance(int jumps, String originCity, HashMap<String, City> network) {
		Set<String> citiesWithinDistance = new HashSet<String>();
		citiesWithinDistance.addAll(((City) network.get(originCity)).getAdjacentCities());
		jumps--;
		while(jumps > 0) {
			
			for(Object city : citiesWithinDistance.toArray()) {
				citiesWithinDistance.addAll(network.get(city).getAdjacentCities());
			}
			jumps--;
		}
		citiesWithinDistance.removeIf(city -> city.equals(originCity));
		return citiesWithinDistance;
	}
	
	/**
	 * Determines whether it's possible to teleport between
	 * an origin and a destination city.
	 *
	 * The method uses a breadth-first search to find any
	 * path between the origin and destination city.
	 * 
	 * @param originCity		the teleportation origin city 
	 * @param destinationCity  	the teleportation final destination city 
	 * @param network			an undirected hash table graph of cities
	 * @return         			true if there is a path, otherwise false
	 */
	public static Boolean possibleTeleport(String originCity, String destinationCity, HashMap<String, City> network) {
		
		Queue<String> queue = new LinkedList<String>();
		Set<String> visited = new HashSet<String>();
		queue.add(originCity);
		while(!queue.isEmpty()){
			if (queue.peek().equals(destinationCity)){
				return true;
			}
			Set<String> list = network.get(queue.remove()).getAdjacentCities();
			for (String x : list){
				if (!visited.contains(x)){
					queue.add(x);
					visited.add(x);
				}
			}
			list.clear();
		}
		return false;
	}
	
	/**
	 * A depth-first search utility algorithm.
	 *
	 * The method uses a modified depth-first search for finding a cycle 
	 * from the origin city. Rather than start at the origin city,
	 * the modified DFS starts at on of the cities adjacent to the origin
	 * and finds a successful loop if it finds a path to a different 
	 * city adjacent to the origin. 
	 * 
	 * @param originCity	the teleportation origin city 
	 * @param start  		DFS start location 
	 * @param network		an undirected hash table graph of cities
	 * @return         		true if there is a loop, otherwise false
	 */
	public static Boolean DFS(String originCity, String start, HashMap<String, City> network) {
		
		Stack<String> stack = new Stack<String>();
		Set<String> visited = new HashSet<String>();
		Set<String> originCityAdjacent = new HashSet<String>();
		originCityAdjacent.addAll(network.get(originCity).getAdjacentCities());
		
		stack.add(start);
		visited.add(start);
		while(!stack.isEmpty()) {
			if(originCityAdjacent.contains(stack.peek()) && !stack.peek().equals(start) && !stack.peek().equals(originCity)) {
				return true;
			}
					
			for(String s : network.get(stack.pop()).getAdjacentCities()) {
				if(!visited.contains(s)) {
					stack.add(s);
					visited.add(s);
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Finds a loop containing the specified city.
	 *
	 * The method uses a modified DFS algorithm for finding a cycle.
	 * A DFS search is attempted at each city adjacent to the origin until
	 * and if a path to the origin is found.
	 * 
	 * @param originCity	the teleportation origin city 
	 * @param network		an undirected hash table graph of cities
	 * @return         		true if there is a loop, otherwise false
	 */
	public static Boolean possibleLoop(String originCity, HashMap<String, City> network) {
		
		Set<String> toCheck = new HashSet<String>();
		toCheck.addAll(network.get(originCity).getAdjacentCities());
		
		for(String city : toCheck) {
			if(DFS(originCity, city, network)) {
				return true;
			}
		}
		
		return false;
	}
}
