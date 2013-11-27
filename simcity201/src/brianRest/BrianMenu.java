package brianRest;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BrianMenu {
	//Map of Food and Price.
	Map<String, Double> menuDictionary = new Hashtable<String, Double>(); 
	private int size = 4;
	
	public BrianMenu(){
		menuDictionary.put("Steak", 15.99);
		menuDictionary.put("Chicken", 10.99);
		menuDictionary.put("Salad", 5.99);
		menuDictionary.put("Pizza", 8.99);
	}
	
	public int getSize(){
		return size;
	}
	
	public String choice(int n){
		String s[] = menuDictionary.keySet().toArray(new String[0]);
		return s[n];
	}
	
	public double getPrice(String food){
		return menuDictionary.get(food);
	}
	
	public boolean HaveEnoughMoneyForAny(double totalMoney){
		String s[] = menuDictionary.keySet().toArray(new String[0]);
		for (String st: s){
			if (menuDictionary.get(st) <= totalMoney){
				return true;
			}
		}
		return false;
	}
	
	public boolean HaveEnoughMoneyForItem(float money, String choice){
		return money >= menuDictionary.get(choice);
	}
	
	public void remove(String s){
		menuDictionary.remove(s);
		size --;
	}
	
}
