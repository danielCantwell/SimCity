package brianRest;

import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import agent.Agent;


import brianRest.gui.BrianAnimationPanel;
import brianRest.gui.WaiterGui;
import brianRest.interfaces.BrianCashier;
import brianRest.interfaces.BrianCook;
import brianRest.interfaces.BrianCustomer;
import brianRest.interfaces.BrianHost;
import brianRest.interfaces.BrianWaiter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

public abstract class BrianAbstractWaiter extends Role implements BrianWaiter {
	
	protected abstract void GiveOrderToCook(MyCustomer mc, boolean displayText);
	
	protected enum MyCustomerState {waiting, seated, readyToOrder, ordering, reordering, ordered, orderCooking, orderReady, eating, doneEating,
		gotCheck, wantCheck, waitingCheck, paying, dead, rotting;}
	
	protected enum WaiterState {none, wantABreak, askedBreak, goingOnBreak, onBreak};

	
	protected class MyCustomer {
		BrianCustomer customer;
		   BrianTable table;
		   String choice;
		   double totalCost;
		   MyCustomerState state = MyCustomerState.waiting;
		   
		   public MyCustomer(BrianCustomer c, BrianTable t) {
				customer = c;
				table = t;
				totalCost = 0;
			}
	}
	
}