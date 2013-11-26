/**
 * 
 */
package SimCity.Base;

import housing.roles.OwnerRole;
import housing.roles.TenantRole;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

import exterior.gui.PersonGui;
import restaurant.DannyCustomer;
import sun.misc.Queue;
import Bank.bankCustomerRole;
import SimCity.Base.God.BuildingType;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_House;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_Restaurant;
import SimCity.Globals.Money;
import SimCity.gui.Gui;
import agent.Agent;
/**
 * @author Brian
 *
 */
public class Person extends Agent {
	//Data
	public String name;
	public B_House myHouse;
	public String house;
	public Role mainRole;
	public enum Intent {customer, work};
	public Intent intent = Intent.customer; //when the person enters a building, is he a customer or going to work.
	public enum Vehicle {car, delivery, walk, bus};
	public Vehicle vehicle = Vehicle.walk;
	
	public PersonGui gui;
	public Semaphore animation = new Semaphore(0, true);
	
	public ArrayList<Role> roles = new ArrayList<Role>();
	
	public ArrayList<String> inventory = new ArrayList<String>();
	
	public enum Morality {good, crook};
	public enum TimeState {
								none,
								msgMorning,
								morning,
								msgWorkOver,
								workOver,
								msgGoToSleep,
								sleeping;
							};
	
	//A GoAction is simply a way to figure out where the person wants to go.				
	public enum GoAction {
		goHome,
		goRestaurant,
		goMarket,
		goBank,
		goSleep;
	}
	
	//This is an inner class that holds a GoAction and an Intent.
	//How to use? The goAction tells you where to go and the intent tells you why you are going.
	public class Action{
		GoAction goAction = GoAction.goHome;
		Intent intent = Intent.customer;
		
		public Action(GoAction g, Intent i){
			goAction = g;
			intent = i;
		}
		public GoAction getGoAction(){return goAction;}
		public Intent getIntent(){return intent;}
	}
	
	public LinkedList <Action> actions = new LinkedList<Action>();
	
	
	//Stats
	public int hungerLevel = 5;
	public int hungerThreshold = 3; 
	public Money money = new Money(10,0);
	public Money moneyThreshold = new Money(9,0);
	public Building building = null;
	public Building destination = null;
	public Morality mor = Morality.good;
	public TimeState timeState = TimeState.none;
	private int accNum;

		//Getters and setters
		public void setHouse(B_House house){myHouse = house;}
		public B_House getHouse(){return myHouse;} 
		public int getHungerLevel(){return hungerLevel;}
		public void setHungerLevel(int s){hungerLevel = s;}
		public void addHungerLevel(int s){hungerLevel += s;}
		public int getHungerThreshold(){return hungerThreshold;}
		public Money getMoney(){return money;}
		public Money getMoneyThreshold() {return moneyThreshold;}
		public void setMoney(int dollars, int cents){money.dollars = dollars; money.cents = cents;}
		public void setMoney(Money newMoney){money = newMoney;}
		public void setMoney(double newMoney){money.dollars = (int)newMoney; money.cents = (int)((newMoney - (int)newMoney)*100);}
		public int getAccNum(){return accNum;}
		public Building getBuilding(){return building;}
		public Building getDestination(){return destination;}
		public Morality getMorality(){return mor; }
		public void setHomeType(String home) {house = "";}
		public String getHomeType(){ return house;}
		public TimeState getPersonState() { return timeState;}
		public Role getMainRole(){ return mainRole;}
		public void setMainRole(String job) { 
			Role newRole;
			try {
				newRole = (Role)Class.forName(job).newInstance();
				newRole.setPerson(this);
				newRole.setActive(false);
				roles.add(newRole);
				mainRole = newRole;
				mainRole.myPerson = this;
			} catch(Exception e){
				e.printStackTrace();
				System.out.println ("no class found");
			}
		}
		public void resetActiveRoles(){
			for (Role r : roles){
				r.setActive(false);
			}
		}
		public void setIntent(Intent i){intent = i;}
		public Intent getIntent(){return intent;}
		
	//use this constructor
	public Person(String name, PersonGui gui, String mainRole, Vehicle vehicle, Morality morality, Money money, Money moneyThresh, int hunger, int hungerThresh, String houseType, B_House house){
		this.gui = gui;
		setMainRole(mainRole);
		this.vehicle = vehicle;
		this.mor = morality;
		this.money = money;
		this.moneyThreshold = moneyThresh;
		this.hungerLevel = hunger;
		this.hungerThreshold = hungerThresh;
		this.house = houseType; 
		this.name = name;
		this.building = God.Get().buildings.get(1);
		myHouse = house;
	}
	
	public Person(String name, String mainRole, Vehicle vehicle, Morality morality, Money money, Money moneyThresh, int hunger, int hungerThresh){
		setMainRole(mainRole);
		this.vehicle = vehicle;
		this.mor = morality;
		this.money = money;
		this.moneyThreshold = moneyThresh;
		this.hungerLevel = hunger;
		this.hungerThreshold = hungerThresh;
		house = "apartment";
		this.name = name;
	}
	
	public Person(String mainRole){
		this.name = "Brian";
		setMainRole(mainRole);
		this.vehicle = Vehicle.walk;
		this.mor = Morality.good;
		this.money = new Money(100,10);
		this.accNum = 1;
		this.moneyThreshold = new Money(1, 0);
		this.hungerLevel = 10;
		this.hungerThreshold = 4;
		house = "apartment";
	}
//Messages ---------------------------------------
	
	private void addAction(Action a){actions.add(a); stateChanged();}
	public void addActionToFront(Action a){actions.addFirst(a); stateChanged();}
	
	public void msgCreateRole(Role r, boolean enterBuilding){
		//If there already exists a role r. Turn it on.
		for(Role ro: roles){
			if (ro.getClass().equals(r.getClass())){
				ro.setActive(true);
				ro.enterBuilding();
				return;
			}		
		}
		//If there does not exist that role r then add r to list.
		r.setActive(true);
        r.myPerson = this;
		roles.add(r);
		if (enterBuilding){
			r.enterBuilding();
		}
		stateChanged();
		return;
	}
	
//These messages must be called when a person enters or exits a building.
	public void msgEnterBuilding(Building b){
		building = b;
	}
	
	public void msgExitBuilding(){
		building = null;
	}
	
	//No idea if this works. Scary as shit.
	//Call this function to go to queue to go to a building. The person will only go to a building if he is in the exterior world.
	//If the person is in the interior world, the person will continue to do whatever he is currently doing.
	public void msgGoToBuilding(Building b, Intent i){
		Do("msgGoToBuilding "+ b.toString());
		if (b instanceof B_Bank){ addAction(new Action(GoAction.goBank, i));}
		else if (b instanceof B_House){ addAction(new Action(GoAction.goHome, i));}
		else if (b instanceof B_Restaurant){ addAction(new Action(GoAction.goRestaurant, i));}
		else if (b instanceof B_Market){ addAction(new Action(GoAction.goMarket, i));}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see SimCity.Base.PersonInterface#msgSleep()
	 */
	public void msgSleep(){
		timeState = TimeState.msgGoToSleep;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see SimCity.Base.PersonInterface#msgWorkOver()
	 */
	public void msgWorkOver(){
		timeState = TimeState.msgWorkOver;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see SimCity.Base.PersonInterface#msgMorning()
	 */
	public void msgMorning(){
		timeState = TimeState.msgMorning;
		stateChanged();
	}
	
	//Scheduler
	boolean returnPAEAA = false;
	/* (non-Javadoc)
	 * @see SimCity.Base.PersonInterface#pickAndExecuteAnAction()
	 */
	@Override
	public boolean pickAndExecuteAnAction() {
		
		//Handle time of day first thing!!
		
		//Morning call must go first BECAUSE tenant role must be overridden if the GOD class messages the person.
		if (timeState == TimeState.msgMorning){
			isMorning();
			return false;
		}
		
		//Work over must go before the active roles to override whatever active role is currently going.
		if (timeState == TimeState.msgWorkOver){
			workOver();
			return false;
		}
		
		//Going to sleep must override the tenant role which is why it has to be before any roles are called.
		if (timeState == TimeState.msgGoToSleep){
			goToSleep();
			return false;
		}

		//if there is an active Role r in roles then return whatever that PAEAA returns
		for (Role r: roles){
			if (r.getActive()){
				//I need to do this active role shit to supress the hunger decremation while at work.
				//Hunger level WILL decrease if the person is in a house.
				if (r instanceof TenantRole) hasActiveRole = false;
					else hasActiveRole = true;
				System.out.println("hi");
				 returnPAEAA =  r.pickAndExecuteAnAction();
				 //This means that only one role will activate.
				 return returnPAEAA;
			}
		}
			
		//Destination comes after the roles because when a person leaves a building, the person's destination is set.
		//But the destination will ONLY come into play after the role has been turned off.
		//This means that the role has to leave a building and turn off before the person can go anywhere.
		//ALSO the role must set the person's intent before leaving the workplace.
		//pop the first thing off the action
		System.out.println("holy shit");
		if (actions.size() > 0){	
			System.out.println("popping");
			goTo(actions.pop());
			return false; //might be true
		}
		
		//if the person leaves a building and has nothing to do.
		if (timeState == TimeState.none && actions.size() == 0){
			//check if he has enough money.
			if (money.dollars < 5){
				goTo(new Action(GoAction.goBank, Intent.customer));
				return false;
			}
			
			//Check if he is hungry
			if (hungerLevel < hungerThreshold){
				goTo(new Action(GoAction.goRestaurant, Intent.customer));
				return false;
			}
			
			goTo(new Action(GoAction.goHome, Intent.customer));
		}
		return returnPAEAA;
	}
	
	
	//Actions
	private void workOver() {
		for (Role r: roles){
			if (r.getActive())
				r.workOver();
		}
	}
	private void isMorning(){
		for (Role r: roles){
			if (r.getActive()){
				if (r instanceof TenantRole){
					TenantRole tr = (TenantRole)r;
					tr.msgMorning();
					timeState = TimeState.none;
					return;
				}
			}
		}
		timeState = TimeState.none;
	}
	private void goToSleep(){
		timeState = TimeState.sleeping;
		for (Role r: roles){
			if (r.getActive()){
				if (r instanceof TenantRole){
					TenantRole tr = (TenantRole)r;
					tr.msgSleeping();
					return;
				}
			}
		}
		timeState = TimeState.none;
	}
	
	//Utility
	private void goTo(Action action){
		Building b = myHouse;
		createVehicle();
		
		//Handling which action
		if (action.getGoAction() == GoAction.goBank){
			//Choose a bank to go.
			b = God.Get().findBuildingOfType(BuildingType.Bank);
			Do("Going to bank");
		}else
		if (action.getGoAction() == GoAction.goHome){
			//Go to your home.
			b = myHouse;
			Do("Going home");
		}else
		if (action.getGoAction() == GoAction.goMarket){
			//Go to your market.
			b = God.Get().findBuildingOfType(BuildingType.Market);
			Do("going to market");
		}else
		if (action.getGoAction() == GoAction.goRestaurant){
			//Go to restaurant
			b = God.Get().findBuildingOfType(BuildingType.Restaurant);
			Do("Going to restaurant");
		}
		else b = null;
		
		//Animation for gui stuff here.
		//############################# Animate to the  building here. ##########################################
		
		System.out.println("I should be walking to the building here using my animation");
		
		if (b == null){
			System.out.println ("Person: error no building found");
			return;
		}
		
		//Call person gui animation. acquire my semaphore.
		System.out.println("Going from :" + building + " to " + b + ".");
		gui.DoTravel(building.id, b.id);
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (action.intent == Intent.work)
			b.EnterBuilding(this, mainRole.getClass().toString());
		else if (action.intent == Intent.customer)
			{
				destination = null;
				b.EnterBuilding(this, b.getCustomerString());
			}
	}
	
	void acquire(){
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void release(){
		animation.release();
	}
	
	void createVehicle(){
		if (vehicle == Vehicle.bus) return;
		if (vehicle == Vehicle.car)return;
		if (vehicle == Vehicle.walk);
		//changes person gui's image. based on vehicle.
		
	}
	
	Timer hungerTimer;
	int hungerOffset = 10000;
	boolean hasActiveRole = false;
	void setUpHungerTimer(){
		 hungerTimer = new Timer(hungerOffset, new ActionListener() {
		 public void actionPerformed(ActionEvent e){
			 if (hasActiveRole) return;
			   if(hungerLevel > 0){
				   hungerLevel --;
			   }
			   if (hungerLevel <= 0){
				   hungerLevel = 0;
			   }
			   //Implement dying here if we have time.
		 }
      });
      hungerTimer.start();
	}
}

