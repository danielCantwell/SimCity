package Bank.interfaces;

import SimCity.Globals.Money;

public interface Robber {

	public abstract void setGuard(Guard bg);

	public abstract void setMoney(Money m);

	public abstract void setAccNum(int a);

	//----------------------------------------------Messages-------------------------------------------------
	public abstract void enterBuilding();

	public abstract void yesEnter();

	public abstract void tellerCalled(Teller t);

	public abstract void doneRobbing(Money m);

	//----------------------------------------------Scheduler-------------------------------------------------

	//----------------------------------------------Actions-------------------------------------------------
	public abstract void openDoor();

	public abstract void findTeller();

	public abstract void leaveBank();

	public abstract void workOver();

}