package brianRest.interfaces;


public interface BrianCook {

	//########## Messages  ###############
	public abstract void msgHeresAnOrder(String o, BrianWaiter w,
			int tableNumber);

	public abstract void msgFillOrder(String choice, int amount, boolean filled);

}