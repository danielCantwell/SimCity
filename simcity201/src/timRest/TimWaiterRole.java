package timRest;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Semaphore;

import SimCity.Base.Role;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import agent.Agent;
import timRest.gui.TimAnimationPanel;
import timRest.gui.TimCookGui;
import timRest.gui.TimWaiterGui;
import timRest.interfaces.TimCashier;
import timRest.interfaces.TimCustomer;
import timRest.interfaces.TimWaiter;
import timRest.interfaces.TimCustomer;

public class TimWaiterRole extends TimAbstractWaiterRole{

	// actions
	
	protected void dropOffOrder()
	{
		int tableNumber = pendingOrders.keys().nextElement();
		cook.msgHereIsAnOrder(this, pendingOrders.get(tableNumber), tableNumber);
		pendingOrders.remove(tableNumber);
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "TWtr";
	}

}
