/**
 * 
 */
package restaurant.interfaces;

/**
 * @author Daniel
 *
 */
public interface Waiter {
	
	public abstract void msgReadyToOrder(Customer customer);
	
	public abstract void msgHereIsMyOrder(String choice, Customer customer);
	
	public abstract void msgHereIsBill(Customer customer, double cost);
	
	public abstract void msgDoneAndPaying(Customer customer);
	
	public abstract void msgCantAffordAnythingElse(Customer customer);

	public abstract void msgOrderReady(String choice, int table);


}
