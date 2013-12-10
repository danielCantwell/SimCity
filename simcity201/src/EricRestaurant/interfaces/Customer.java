package EricRestaurant.interfaces;

import EricRestaurant.EricAbstractWaiter;
import EricRestaurant.EricCashier;
import EricRestaurant.EricHost;
import EricRestaurant.EricWaiter;
import EricRestaurant.gui.CustomerGui;
import SimCity.Globals.Money;

public interface Customer {

	public abstract void setHost(EricHost host);

	public abstract void setCash(EricCashier cash);

	public abstract void setWaiter(EricAbstractWaiter waiter);

	public abstract String getCustomerName();

	public abstract void gotHungry();

	public abstract void msgSitAtTable(EricAbstractWaiter w, int table);

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void whatOrder();

	public abstract void checkToCust(EricCashier cs);

	public abstract void foodReceived();

	public abstract void giveChange(Money change);

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void leaveTable();

	public abstract String getName();

	public abstract int getHungerLevel();

	public abstract void setHungerLevel(int hungerLevel);

	public abstract String toString();

	public abstract void setGui(CustomerGui g);

	public abstract CustomerGui getGui();

}