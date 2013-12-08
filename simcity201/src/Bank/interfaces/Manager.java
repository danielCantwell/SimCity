package Bank.interfaces;

import java.util.HashMap;
import java.util.Map;

import SimCity.Globals.Money;
import Bank.bankCustomerRole;
import Bank.bankManagerRole;
import Bank.bankManagerRole.Teller;
import Bank.tellerRole;

public interface Manager {

	public abstract Guard getGuard();

	public abstract void setGuard(Guard bg);

	public static final int NCounters = 2;
	public Map<Integer, Money> ManagerAccs = new HashMap<Integer, Money>();

	//----------------------------------------------Messages-------------------------------------------------
	public abstract void newTeller(Bank.interfaces.Teller teller);

	public abstract void newClient(Customer bc);

	//----------------------------------------------Scheduler-------------------------------------------------
	public abstract boolean pickAndExecuteAnAction();

	public abstract void callTeller(Customer c, Teller t);

	public abstract void workOver();

	public abstract void tellerReady(tellerRole tellerRole);

	public abstract void giveMap(Map<Integer, Money> bankAccs);

}