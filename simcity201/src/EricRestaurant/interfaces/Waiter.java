package EricRestaurant.interfaces;

import EricRestaurant.EricCashier;
import EricRestaurant.EricCustomer;
import EricRestaurant.EricHost;
import EricRestaurant.EricWaiter;
import EricRestaurant.EricWaiter.myCustomer;
import EricRestaurant.gui.HostGui;
import SimCity.Globals.Money;

public interface Waiter {

	public abstract void msgWaiter(String c);

	public abstract void restock(String c);

	public abstract void setCashier(EricCashier c);

	public abstract void setHost(EricHost h);

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract void HostCalled(EricCustomer cust, int table, int num);

	public abstract void takeMyOrder(Customer cust);

	public abstract void hereIsOrder(Cashier cust, String choice);

	public abstract void reorder(myCustomer cx);

	public abstract void atTheCook();

	public abstract void atTheCust();

	public abstract void orderDone(String choice);

	public abstract void msgAtTable();

	public abstract void atCook(String choice);

	public abstract void waiterGotCheck(Money p, Customer c, EricCashier cs);

	public abstract void LeavingTable(Customer c);

	public abstract void deliver(myCustomer c);

	public abstract void bringToCust(myCustomer c);

	public abstract void alertHost(myCustomer c);

	public abstract void askBreak();

	public abstract void offBreak();

	public abstract void addWaiter();

	public abstract void setGui(HostGui gui);

	public abstract HostGui getGui();

}