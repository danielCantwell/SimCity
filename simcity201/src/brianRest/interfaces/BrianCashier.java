package brianRest.interfaces;


public interface BrianCashier {

	//########## Messages  ###############
	public abstract void msgHereIsCheck(String choice, BrianCustomer c,
			BrianWaiter wa);

	public abstract void msgHeresIsMyMoney(BrianCustomer c, double totalMoney);

}