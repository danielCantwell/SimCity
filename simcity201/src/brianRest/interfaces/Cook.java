package restaurant.interfaces;


public interface Cook {

	//########## Messages  ###############
	public abstract void msgHeresAnOrder(String o, Waiter w,
			int tableNumber);

	public abstract void msgFillOrder(String choice, int amount, boolean filled);

}