package TeleportationSystem;

import java.util.HashSet;
import java.util.Set;

/**
 * @author      Jorvon Carter carterjorvon@gmail.com>
 */
public class City {
	String name;
	Set<String> adjacentCities;
	
	City(String name){
		this.name = name;
		this.adjacentCities = new HashSet<String>();
	}
	
	String getName(){
		return name;
	}
	
	Set<String> getAdjacentCities(){
		return adjacentCities;
	}
	
	void setAdjacentCity(String city){
		this.adjacentCities.add(city);
	}
}
