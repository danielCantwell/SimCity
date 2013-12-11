package Bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import restaurant.DannyHost;
import timRest.TimHostRole;
import brianRest.BrianHostRole;
import jesseRest.JesseHost;
import SimCity.Globals.*;
import SimCity.trace.AlertTag;
import SimCity.Base.*;
import SimCity.Base.Person.Intent;
import SimCity.Buildings.B_Bank;
import Bank.gui.*;
import Bank.interfaces.Customer;
import Bank.interfaces.Manager;
import Bank.interfaces.Robber;
import Bank.interfaces.Teller;
import EricRestaurant.EricHost;

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

	List<Rest> rest = Collections.synchronizedList(new ArrayList<Rest>());

	public class Rest {
		int acc;
		Money m;
		EricHost eh;
		JesseHost jh;
		BrianHostRole bh;
		DannyHost dh;
		TimHostRole th;
	}
	Money initial = new Money(500,0);
	public enum state{ none, ready, atCounter, added, noAcc, yesAcc, setUp, robbed, askedService, called, inTrans, withdraw, deposit, leaving };
	private state s = state.none;


	//-----------------------------------------------Messages-------------------------------------------------
	@Override
	public void enterBuilding() {
		s = state.ready;
		B_Bank bank = (B_Bank)myPerson.getBuilding();
		bank.addTeller(this);
		manager = bank.getBankManager();
		if(!(manager == null)){
		Do(AlertTag.BANK,"Teller: I am a teller: "+this);
		manager.newTeller(this);
		bankGui bankgui = (bankGui)myPerson.building.getPanel();
		bankgui.addGui(gui);
		bankgui.repaint();
		gui.setText("Teller");
		stateChanged();
		} else bank.ExitBuilding(myPerson);
	}
	@Override
	public void managerMap(Map<Integer, Money> managerAccs){
		bankAccs.putAll(managerAccs);
		System.out.println("bankAccs and managerAccs tranfered" +bankAccs.get(1000));
	}
	
	@Override
	public void tellerAssigned(Customer c, int accNum) {
		Client cl = new Client();
		cl.cust = c;
		cl.accountNum = accNum;
		cl.s = state.added;
		clients.add(cl);
		Do(AlertTag.BANK,"Teller: New customer assigned to me :"+this+".."+clients.size());
		stateChanged();
	}
	
	@Override
	public void tellerAssigned(Customer c) {
		Client cl = new Client();
		cl.cust = c;
		cl.accountNum = c.getAccNum();
		cl.s = state.added;
		clients.add(cl);
		Do(AlertTag.BANK,"Teller: New customer assigned to me :"+this+".."+clients.size());
		stateChanged();
	}
	
	@Override
	public void foundTeller(Money money, Customer cust) {
		synchronized(clients) {
			for (Client c : clients) {
				if (c.cust == cust) {
					//c.accountNum = accNum;
					c.money = money;
//					Do(AlertTag.BANK,"Inside PAE scheduler0: "+c+" Client size: "+clients.size());

					if(bankAccs.get(c.accountNum) != null) {
						c.s = state.yesAcc;
					} 
					else {
						c.s = state.noAcc;
					}
					Do(AlertTag.BANK,"Teller: Customer has come to me accNum: "+c.accountNum+" "+c.s+" cash: "+c.money.getDollar());
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
		Do(AlertTag.BANK,"Teller: Robber came and is taking money");
		stateChanged();
	}
	@Override
	public void requestWithdraw(int acc, Money money) {
		synchronized(clients) {
			for (Client c : clients) {
				if (c.accountNum == acc) {
					c.s = state.withdraw;
					c.editmoney = money;
					Do(AlertTag.BANK,"Teller: Customer requested "+c.s+": $"+c.editmoney.getDollar());
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
					Do(AlertTag.BANK,"Teller: Customer requested "+c.s+": $"+c.editmoney.getDollar());
//					Do(AlertTag.BANK,"Before PAE scheduler: "+c+" Client size: "+clients.size()+" Client money: "+c.money.getDollar());
					stateChanged();
				}	
			}
		}
	}

	public void restMoney(int acc, Money m, EricHost host) {
		Rest r = new Rest();
		r.acc = acc;
		r.m = m;
		r.eh = host;
		rest.add(r);
		stateChanged();
	}
	public void restMoney(int acc, Money m, JesseHost host) {
		Rest r = new Rest();
		r.acc = acc;
		r.m = m;
		r.jh = host;
		rest.add(r);
		stateChanged();
	}
	public void restMoney(int acc, Money m, BrianHostRole host) {
		Rest r = new Rest();
		r.acc = acc;
		r.m = m;
		r.bh = host;
		rest.add(r);
		stateChanged();
	}
	public void restMoney(int acc, Money m, DannyHost host) {
		Rest r = new Rest();
		r.acc = acc;
		r.m = m;
		r.dh = host;
		rest.add(r);
		stateChanged();
	}
	public void restMoney(int acc, Money m, TimHostRole host) {
		Rest r = new Rest();
		r.acc = acc;
		r.m = m;
		r.th = host;
		rest.add(r);
		stateChanged();
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
		//Do(AlertTag.BANK,".");
		if(s == state.ready) {
			goToCounter();
			return true;
		}
		//Do(AlertTag.BANK,"..");
		if(s == state.leaving) {
			leaveBank();
			return true;
		}
		//Do(AlertTag.BANK,"...");

		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.robbed) {
					robbery(c);
					return true;
				}
			}
		}
		//Do(AlertTag.BANK,"....");

		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.added) {
					callClient(c);
					return true;
				}
			}
		}
		//Do(AlertTag.BANK,".....");

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
		//Do(AlertTag.BANK,"......");

		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.withdraw) {
					withdrawDone(c);
					return true;
				}
				else if (c.s == state.deposit) {
//					Do(AlertTag.BANK,"Inside PAE scheduler2: "+c+" Client size: "+clients.size()+" Client money: "+c.money.getDollar());
					depositDone(c);
					return true;
				}
			}
		}

		synchronized(rest) {
			for(Rest r : rest) {
				transaction(r);
				return true;
			}
		}
		//System.out.println(".......");
		return false;
	}

	//-----------------------------------------------Actions-------------------------------------------------
	public void callClient(Client c) {
		c.cust.tellerCalled(this, c.accountNum);
		c.s = state.called;
	}

	public void askService(Client c){
		c.s = state.askedService;
		c.cust.whatService();
	}

	public void accSetUp(Client c) {
		Do(AlertTag.BANK,"Teller: no existing account, creating..."+" Client money: "+c.money.getDollar());
		bankAccs.put(c.accountNum, initial);
		c.s = state.yesAcc;
	}

	public void robbery(Client c) {
		c.money.add(20, 0);
		c.r.doneRobbing(c.money);
		clients.remove(c);
	}



	public void withdrawDone(Client c) {
		if( ! (c.editmoney.isGreaterThan(bankAccs.get(c.accountNum)))) {
			c.money.add(c.editmoney);
			Money temp = bankAccs.get(c.accountNum);
			bankAccs.put(c.accountNum, temp.subtract(c.editmoney));
			c.cust.transactionComplete(c.money);
			Do(AlertTag.BANK,"Customer current total: $"+c.money.dollars);
			clients.remove(c);
			manager.tellerReady(this);
		}
		else Do(AlertTag.BANK,"Teller: Insufficient funds");
	}

	public void depositDone(Client c) {
		c.s = state.inTrans;
		c.money.subtract(c.editmoney);
		bankAccs.put(c.accountNum, bankAccs.get(c.accountNum).add(c.editmoney));
		c.cust.transactionComplete(c.money);
		clients.remove(c);
		manager.tellerReady(this);

	}

	public void transaction(Rest r) {
		if (r.m.isGreaterThan(300, 0)){
			Money temp = r.m.subtract(300, 0);
			Money temp2 = bankAccs.get(r.acc);
			temp.add(temp2);
			bankAccs.put(r.acc,temp);

			r.m = new Money(300,0);
			if (r.acc == 1000) { System.out.println("Eric Restaurant Bank Account #"+r.acc+" Amount : $ "+bankAccs.get(r.acc).getDollar()); r.eh.transDone(r.m); }
			if (r.acc == 2000) { System.out.println("Jesse Restaurant Bank Account #"+r.acc+" Amount : $ "+bankAccs.get(r.acc).getDollar()); r.jh.transDone(r.m); }
			if (r.acc == 3000) { System.out.println("Brian Restaurant Bank Account #"+r.acc+" Amount : $ "+bankAccs.get(r.acc).getDollar()); r.bh.transDone(r.m); }
			if (r.acc == 4000) { System.out.println("Danny Restaurant Bank Account #"+r.acc+" Amount : $ "+bankAccs.get(r.acc).getDollar()); r.dh.transDone(r.m); }	
			if (r.acc == 5000) { System.out.println("Tim Restaurant Bank Account #"+r.acc+" Amount : $ "+bankAccs.get(r.acc).getDollar()); r.th.transDone(r.m); }		
		}
		else {
			bankAccs.put(r.acc, bankAccs.get(r.acc).subtract(100, 0));
			r.m.add(100, 0);
			if (r.acc == 1000) r.eh.transDone(r.m);
			if (r.acc == 2000) r.jh.transDone(r.m);
			if (r.acc == 3000) r.bh.transDone(r.m);
			if (r.acc == 4000) r.dh.transDone(r.m);
			if (r.acc == 5000) r.th.transDone(r.m);

		}
		rest.remove(r);
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
		if(manager == null) {
			System.out.println("Teller: leaving Bank because there is no manager");
			myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		}
		else {
		myPerson.getMoney().add(50, 0);
		manager.giveMap(bankAccs);
		gui.doLeaveBank();
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		bankGui bg = (bankGui)myPerson.building.getPanel();
		bg.removeGui(gui);
		myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		exitBuilding(myPerson);
		}
	}

	@Override
	public void setGui(tellerGui gui) {
		this.gui = gui;
	}

	@Override
	public tellerGui getGui() { 
		return gui;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "BTlr";
	}

}
