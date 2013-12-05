package Bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import SimCity.Globals.*;
import SimCity.Base.*;
import SimCity.Buildings.B_Bank;
import Bank.gui.*;
import Bank.interfaces.Customer;
import Bank.interfaces.Manager;
import Bank.interfaces.Robber;
import Bank.interfaces.Teller;

/**
 * Bank Teller Role
 * @author Eric
 */

public class tellerRole extends Role implements Teller {
	//-----------------------------------------------Data-------------------------------------------------
	Manager manager;
	private Semaphore moving = new Semaphore(0,true);
	private tellerGui gui = new tellerGui(this);
	public Map<Integer, Money> bankAccs = new HashMap<Integer, Money>();
	public List<Client> clients = Collections.synchronizedList(new ArrayList<Client>());
	public class Client {
		int accountNum;
		Money money;
		Money editmoney;
		state s;
		Customer cust;
		Robber r;
	}
	Money empty = new Money(0,0);
	public enum state{ none, ready, atCounter, added, noAcc, yesAcc, setUp, robbed, askedService, called, inTrans, withdraw, deposit, leaving };
	private state s = state.none;


	//-----------------------------------------------Messages-------------------------------------------------
	@Override
	public void enterBuilding() {
		s = state.ready;
		System.out.println("Teller: I am a teller: "+this);
		B_Bank bank = (B_Bank)myPerson.getBuilding();
		manager = bank.getBankManager();
		manager.newTeller(this);
		bankGui bankgui = (bankGui)myPerson.building.getPanel();
		bankgui.addGui(gui);
		gui.setText("Teller");
		stateChanged();
	}

	@Override
	public void tellerAssigned(Customer c) {
		Client cl = new Client();
		cl.cust = c;
		cl.s = state.added;
		clients.add(cl);
		System.out.println("Teller: New customer assigned to me :"+this+".."+clients.size());
		stateChanged();
	}

	@Override
	public void foundTeller(int accNum, Money money, Customer cust) {
		synchronized(clients) {
			for (Client c : clients) {
				if (c.cust == cust) {
					c.accountNum = accNum;
					c.money = money;
//					System.out.println("Inside PAE scheduler0: "+c+" Client size: "+clients.size());

					if(bankAccs.get(c.accountNum) != null) {
						c.s = state.yesAcc;
					} 
					else {
						c.s = state.noAcc;
					}
					System.out.println("Teller: Customer has come to me accNum: "+accNum+" "+c.s+" cash: "+c.money.getDollar());
					stateChanged();
				}
			}
		}
	}

	public void RobTeller(int accNum, Money money, Robber r) {
		Client c = new Client();
		c.accountNum = accNum;
		c.money = money;
		c.r = r;
		c.s = state.robbed;
		clients.add(c);
		System.out.println("Teller: Robber came and is taking money");
		stateChanged();
	}
	@Override
	public void requestWithdraw(int acc, Money money) {
		synchronized(clients) {
			for (Client c : clients) {
				if (c.accountNum == acc) {
					c.s = state.withdraw;
					c.editmoney = money;
					System.out.println("Teller: Customer requested "+c.s+": $"+c.editmoney.getDollar());
					stateChanged();
				}
			}
		}
	}

	@Override
	public void requestDeposit(int acc, Money money) {
		synchronized(clients) {
			for (Client c : clients) {
				if (c.accountNum == acc) {
					c.s = state.deposit;
					c.editmoney = money;
					System.out.println("Teller: Customer requested "+c.s+": $"+c.editmoney.getDollar());
					System.out.println("Before PAE scheduler: "+c+" Client size: "+clients.size()+" Client money: "+c.money.getDollar());
					stateChanged();
				}	
			}
		}
	}

	@Override
	public void workOver() {
		s = state.leaving;
	}

	public void doneMotion() {
		moving.release();
	}

	//-----------------------------------------------Scheduler-------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		//System.out.println(".");
		if(s == state.ready) {
			goToCounter();
			return true;
		}
		//System.out.println("..");
		if(s == state.leaving) {
			leaveBank();
			return true;
		}
		//System.out.println("...");

		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.robbed) {
					robbery(c);
					return true;
				}
			}
		}
		//System.out.println("....");

		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.added) {
					callClient(c);
					return true;
				}
			}
		}
		//System.out.println(".....");

		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.yesAcc) {
					askService(c);
					return true;
				}
			}
		}
			synchronized(clients) {
				for (Client c : clients) {
					if(c.s == state.noAcc) {
						accSetUp(c);
						return true;
					}
				}
			}
		//System.out.println("......");

		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.withdraw) {
					withdrawDone(c);
					return true;
				}
				else if (c.s == state.deposit) {
					System.out.println("Inside PAE scheduler2: "+c+" Client size: "+clients.size()+" Client money: "+c.money.getDollar());
					depositDone(c);
					return true;
				}
			}
		}
		//System.out.println(".......");
		return false;
	}

	//-----------------------------------------------Actions-------------------------------------------------
	public void callClient(Client c) {
		c.cust.tellerCalled(this);
		c.s = state.called;
	}

	public void askService(Client c){
		c.s = state.askedService;
		c.cust.whatService();
	}

	public void accSetUp(Client c) {
		System.out.println("Teller: no existing account, creating..."+" Client money: "+c.money.getDollar());
		bankAccs.put(c.accountNum, empty);
		c.s = state.yesAcc;
	}

	public void robbery(Client c) {
		c.money = c.money.add(20, 0);
		c.r.doneRobbing(c.money);
		clients.remove(c);
	}



	public void withdrawDone(Client c) {
		if( ! (c.editmoney.isGreaterThan(bankAccs.get(c.accountNum)))) {
			c.money = c.money.add(c.editmoney);
			Money temp = bankAccs.get(c.accountNum);
			bankAccs.put(c.accountNum, temp.subtract(c.editmoney));
			c.cust.transactionComplete(c.money);
			System.out.println("Customer current total: $"+c.money.dollars);
			clients.remove(c);
			manager.tellerReady(this);
		}
		else System.out.println("Teller: Insufficient funds");
	}

	public void depositDone(Client c) {
		c.s = state.inTrans;
		c.money = c.money.subtract(c.editmoney);
		//System.out.println("###"+c.money.getDollar());
		Money temp = bankAccs.get(c.accountNum);
		bankAccs.put(c.accountNum, temp.add(c.editmoney));
		c.cust.transactionComplete(c.money);
		System.out.println("Customer current total: $"+c.money.dollars);
		//System.out.println(bankAccs.get(1).getDollar());
		clients.remove(c);
		manager.tellerReady(this);

	}

	@Override
	public void goToCounter() {
		gui.doGoToCounter(1);
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		s = state.atCounter;
	}

	@Override
	public void leaveBank() {
		gui.doLeaveBank();
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		exitBuilding(myPerson);
	}

	@Override
	public void setGui(tellerGui gui) {
		this.gui = gui;
	}

	@Override
	public tellerGui getGui() { 
		return gui;
	}

}
