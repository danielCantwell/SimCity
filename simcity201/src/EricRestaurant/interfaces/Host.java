package EricRestaurant.interfaces;

import java.util.Collection;
import java.util.List;

import EricRestaurant.EricCashier;
import EricRestaurant.EricCook;
import EricRestaurant.EricCustomer;
import EricRestaurant.EricWaiter;
import EricRestaurant.gui.HostGui;
import SimCity.Globals.Money;

public interface Host {

	public abstract void setWaiter(EricRestaurant.interfaces.Waiter w);

	public abstract void setCook(EricCook ck);

	public abstract void setCashier(EricCashier cash);

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract List getWaitingCustomers();

	public abstract Collection getTables();


	public abstract void msgLeavingTable(Customer cust,
			EricRestaurant.interfaces.Waiter w);

	public abstract void newWaiter(EricWaiter wait);

	public abstract void canIBreak(EricRestaurant.interfaces.Waiter w);

	public abstract void backFromBreak(EricRestaurant.interfaces.Waiter w);

	public abstract void setGui(HostGui gui);

	public abstract HostGui getGui();

	public abstract void workOver();

	public abstract void msgIWantFood(EricCustomer cust, Money c);

}