package brianRest.interfaces;


public interface BrianMarket {

	String name = "";

	//########## Messages  ###############
	public abstract void msgINeedFood(String choice, int amount, BrianCook c);
	
	public void msgPayMarket(double money);

}