package restaurant.interfaces;


public interface Market {

	String name = "";

	//########## Messages  ###############
	public abstract void msgINeedFood(String choice, int amount, Cook c);
	
	public void msgPayMarket(double money);

}