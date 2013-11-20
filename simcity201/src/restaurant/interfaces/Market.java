/**
 * 
 */
package restaurant.interfaces;

import java.util.Map;

import restaurant.CookAgent;
import restaurant.MarketAgent.Order;

/**
 * @author Daniel
 *
 */
public interface Market {
	
	public abstract void msgOrderFood(CookAgent cook, Map<String, Integer> lowFood);
	
	public abstract void msgOrderReady(Order order);
	
	public abstract void msgPayment(double moneyOwed);
}
