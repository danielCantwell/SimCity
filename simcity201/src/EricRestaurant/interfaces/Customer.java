package EricRestaurant.interfaces;

import EricRestaurant.EricCashier;
import EricRestaurant.EricHost;
import EricRestaurant.EricWaiter;
import EricRestaurant.gui.CustomerGui;

public interface Customer {

	/* (non-Javadoc)
	 * @see restaurant.Cashier#setHost(restaurant.HostAgent)
	 */
	public abstract void setHost(EricHost host);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#setCash(restaurant.CashierAgent)
	 */
	public abstract void setCash(EricCashier cash);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#setWaiter(restaurant.WaiterAgent)
	 */
	public abstract void setWaiter(EricWaiter waiter);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getCustomerName()
	 */
	public abstract String getCustomerName();

	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Cashier#gotHungry()
	 */
	public abstract void gotHungry();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgSitAtTable(restaurant.WaiterAgent, int)
	 */
	public abstract void msgSitAtTable(EricWaiter w, int table);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgAnimationFinishedGoToSeat()
	 */
	public abstract void msgAnimationFinishedGoToSeat();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#whatOrder()
	 */
	public abstract void whatOrder();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#checkToCust(restaurant.CashierAgent)
	 */
	public abstract void checkToCust(EricCashier cs);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#foodReceived()
	 */
	public abstract void foodReceived();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#giveChange(double)
	 */
	public abstract void giveChange(double change);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#bumChange(double)
	 */
	public abstract void bumChange(double change);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgAnimationFinishedLeaveRestaurant()
	 */
	public abstract void msgAnimationFinishedLeaveRestaurant();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#leaveTable()
	 */
	public abstract void leaveTable();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getName()
	 */
	public abstract String getName();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getHungerLevel()
	 */
	public abstract int getHungerLevel();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#setHungerLevel(int)
	 */
	public abstract void setHungerLevel(int hungerLevel);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#toString()
	 */
	public abstract String toString();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#setGui(restaurant.gui.CustomerGui)
	 */
	public abstract void setGui(CustomerGui g);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getGui()
	 */
	public abstract CustomerGui getGui();

}