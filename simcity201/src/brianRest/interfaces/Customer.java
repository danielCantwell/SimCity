package restaurant.interfaces;

import restaurant.Menu;
import restaurant.gui.CustomerGui;

public interface Customer {

	public abstract void msgIsHungry();

	public abstract void msgFollowMe(Menu m);

	public abstract void msgFullHouse();

	//Get a message from customer GUI when we reach the table to handle animation. Once we reach the table set Customer State to seated.
	public abstract void msgWhatWouldYouLike();

	public abstract void msgHeresYourOrder(String order);

	public abstract void msgOutOfFood(Menu m);

	//Paying
	public abstract void msgHereIsTotal(double totalCost2);

	public abstract void msgHeresYourChange(double d);

	public abstract void msgDie();

	public abstract void DoGoToDeadLocation();

	public abstract CustomerGui getGui();

}