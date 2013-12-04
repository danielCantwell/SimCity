package Bank.interfaces;

import java.util.List;

import Bank.bankCustomerRole;
import Bank.bankGuardRole;
import Bank.bankGuardRole.Entry;
import Bank.gui.bankGuardGui;
import SimCity.Buildings.B_Bank;

public interface Guard {

	public abstract void setBank(B_Bank bank);

	//----------------------------------------------Messages-------------------------------------------------
	public abstract void enterBuilding();

	public abstract void wantEnter(bankCustomerRole newC);

	public abstract void allowSearch(Customer newC,
			List<String> inventory);

	public abstract void workOver();

	//----------------------------------------------Scheduler-------------------------------------------------
	public abstract boolean pickAndExecuteAnAction();

	//-----------------------------------------------Actions-------------------------------------------------
	public abstract void askSearch(Entry c);

	public abstract void Search(Entry c);

	public abstract void enterBank();

	public abstract void leaveBank();

	public abstract void setGui(bankGuardGui gui);

	public abstract bankGuardGui getGui();

	//public abstract void wantEnter(Customer newC);

	public abstract void RobberEnter(Robber robberRole);

}