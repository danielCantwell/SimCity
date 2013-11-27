package restaurant.interfaces;

import restaurant.CookAgent;

public interface Market {

	public abstract void msgWantMoreFood(CookAgent cook, String choice, int amount);
	public abstract void msgHereIsPayment(double price);

	public abstract String getName();
}