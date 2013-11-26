/**
 * 
 */
package restaurant.interfaces;

import java.util.Map;

import restaurant.DannyCook;
import restaurant.MarketAgent.Order;

/**
 * @author Daniel
 *
 */
public interface Market {
	
	public abstract void msgOrderFood(DannyCook cook, Map<String, Integer> lowFood);
	
	public abstract void msgOrderReady(Order order);
	
	public abstract void msgPayment(double moneyOwed);
}
