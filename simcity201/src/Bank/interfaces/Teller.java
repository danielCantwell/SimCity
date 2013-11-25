package Bank.interfaces;

import Bank.bankCustomerRole;
import Bank.tellerRole;
import Bank.gui.tellerGui;
import Bank.tellerRole.Client;
import SimCity.Globals.Money;

public interface Teller {

	//-----------------------------------------------Messages-------------------------------------------------
	public abstract void enterBuilding();

	public abstract void tellerAssigned(bankCustomerRole c);

	public abstract void foundTeller(int accNum, Money money,
			bankCustomerRole cust);

	public abstract void requestWithdraw(int acc, Money money);

	public abstract void requestDeposit(int acc, Money money);

	public abstract void workOver();

	//-----------------------------------------------Scheduler-------------------------------------------------
	public abstract boolean pickAndExecuteAnAction();

	//-----------------------------------------------Actions-------------------------------------------------
	public abstract void callClient(Client c);

	public abstract void askService(Client c);

	public abstract void accSetUp(Client c);

	public abstract void withdrawDone(Client c);

	public abstract void depositDone(Client c);

	public abstract void goToCounter();

	public abstract void leaveBank();

	public abstract void setGui(tellerGui gui);

	public abstract tellerGui getGui();

}