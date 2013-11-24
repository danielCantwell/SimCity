/**
 * 
 */
package SimCity.Base;

import housing.roles.TenantRole;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import restaurant.CustomerAgent;
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
	
	public Gui gui;
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
	
	public enum Action{
		goHome,
		goRestaurant,
		goMarket,
		goBank,
		goSleep;
		
		Intent intent = Intent.customer;
		public Action setIntent(Intent i) { intent = i; return this;}
		public Intent getIntent(){return intent;}
	}
	
	LinkedList <Action> actions = new LinkedList<Action>();
	
	
	//Stats
	public int hungerLevel = 5;
	public int hungerThreshold = 3; 
	public Money money = new Money(10,0);
	public Money moneyThreshold = new Money(9,0);
	public Building building = null;
	public Building destination = null;
	public Morality mor = Morality.good;
	public TimeState timeState = TimeState.none;

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
				roles.add(newRole);
				mainRole = newRole;
				
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
		
		
	public Person(String name, Gui gui, String mainRole, Vehicle vehicle, Morality morality, Money money, Money moneyThresh, int hunger, int hungerThresh, String houseType){
		this.gui = gui;
		setMainRole(mainRole);
		this.vehicle = vehicle;
		this.mor = morality;
		this.money = money;
		this.moneyThreshold = moneyThresh;
		this.hungerLevel = hunger;
		this.hungerThreshold = hungerThresh;
		house = houseType; 
		this.name = name;
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
		//setMainRole(mainRole);
		//God.Get().EnterBuilding(null, this, mainRole);
		this.name = "Brian";
		setMainRole(mainRole);
		this.vehicle = Vehicle.walk;
		this.mor = Morality.good;
		this.money = new Money(5,10);
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
	
	public void msgGoToBuilding(Building b, Intent i){
		if (b instanceof B_Bank){ addAction(Action.goBank.setIntent(i));}
		else if (b instanceof B_House){ addAction(Action.goHome.setIntent(i));}
		else if (b instanceof B_Restaurant){ addAction(Action.goRestaurant.setIntent(i));}
		else if (b instanceof B_Market){ addAction(Action.goMarket.setIntent(i));}
		stateChanged();
	}
	
	public void msgSleep(){
		timeState = TimeState.msgGoToSleep;
		stateChanged();
	}
	
	public void msgWorkOver(){
		timeState = TimeState.msgWorkOver;
		stateChanged();
	}
	
	public void msgMorning(){
		timeState = TimeState.msgMorning;
		stateChanged();
	}
	
	//Scheduler
	boolean returnPAEAA = false;
	@Override
	protected boolean pickAndExecuteAnAction() {
		
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
		if (actions.size() > 0){	
			goTo(actions.pop());
			return true;
		}
		
		//if the person leaves a building and has nothing to do.
		if (timeState == TimeState.none){
			//check if he has enough money.
			if (money.dollars < 5){
				goTo(Action.goBank.setIntent(Intent.customer));
				return false;
			}
			
			//Check if he is hungry
			if (hungerLevel < hungerThreshold){
				goTo(Action.goRestaurant.setIntent(Intent.customer));
				return false;
			}
			
			goTo(Action.goHome.setIntent(Intent.customer));
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
					//tr.msgMorning();
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
					//tr.msgSleeping();
					return;
				}
			}
		}
		timeState = TimeState.none;
	}
	
	//Utility
	public void goTo(Action action){
		Building b = myHouse;
		createVehicle();
		//Handling which action
		if (action == Action.goBank){
			//Choose a bank to go.
			b = God.Get().findBuildingOfType(BuildingType.Bank);
			Do("Going to bank");
		}else
		if (action == Action.goHome){
			//Go to your home.
			b = myHouse;
			Do("Going home");
		}else
		if (action == Action.goMarket){
			//Go to your market.
			b = God.Get().findBuildingOfType(BuildingType.Market);
			Do("going to market");
		}else
		if (action == Action.goRestaurant){
			//Go to restaurant
			b = God.Get().findBuildingOfType(BuildingType.Restaurant);
			Do("Going to restaurant");
		}
		
		//Animation for gui stuff here.
		
		if (b == null){
			System.out.println ("Person: error no building found");
		}
		
		//Call person gui animation. acquire my semaphore.
		if (action.intent == Intent.work)
			God.Get().EnterBuilding(b, this, mainRole.getClass().toString());
		else if (action.intent == Intent.customer)
			{
				destination = null;
				God.Get().EnterBuilding(b, this, b.getCustomerString());
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
		//changes person gui's image. based on vehicle.
	}
	
	
}

