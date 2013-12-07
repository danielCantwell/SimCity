package Bank.interfaces;

import Bank.gui.bankCustomerGui;
import SimCity.Globals.Money;

public interface Customer {

	public abstract void setGuard(Guard bg);

	public abstract void setMoney(Money m);

	public abstract void setAccNum(int a);

	//-----------------------------------------------Messages------------------------------------------------
	public abstract void enterBuilding();

	public abstract void requestSearch();

	public abstract void yesEnter();

	public abstract void noEnter();

	public abstract void tellerCalled(Teller t);

	public abstract void whatService();

	public abstract void transactionComplete(Money m);

	public abstract void workOver();

	//-----------------------------------------------Scheduler-------------------------------------------------
	public abstract boolean pickAndExecuteAnAction();

	//-----------------------------------------------Actions-------------------------------------------------
	public abstract void openDoor();

	public abstract void giveInv();

	public abstract void findTeller();

	public abstract void chooseService();

	public abstract void leaveBank();

	public abstract void setGui(bankCustomerGui gui);

	public abstract bankCustomerGui getGui();

	public abstract void doneMotion();

}