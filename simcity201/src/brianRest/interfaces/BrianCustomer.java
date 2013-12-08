package brianRest.interfaces;

import brianRest.BrianAbstractWaiter;
import brianRest.BrianMenu;
import brianRest.BrianPCWaiterRole;
import brianRest.BrianWaiterRole;
import restaurant.Menu;
import restaurant.gui.CustomerGui;

public interface BrianCustomer {

	public abstract void msgIsHungry();

	public abstract void msgFullHouse();

	//Get a message from customer GUI when we reach the table to handle animation. Once we reach the table set Customer State to seated.
	public abstract void msgWhatWouldYouLike();

	public abstract void msgHeresYourOrder(String order);

	//Paying
	public abstract void msgHereIsTotal(double totalCost2);

	public abstract void msgHeresYourChange(double d);

	public abstract void msgDie();

	public abstract void DoGoToDeadLocation();

	public abstract brianRest.gui.CustomerGui getGui();

	public abstract void msgOutOfFood(BrianMenu m);

	public abstract void msgFollowMe(BrianMenu m);

	public abstract void setWaiter(BrianWaiter brianPCWaiterRole);

}