/**
 * 
 */
package SimCity.Base;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import restaurant.CustomerAgent;
import SimCity.Globals.Money;
import SimCity.gui.Gui;
import agent.Agent;

/**
 * @author Brian Chen
 *
 */
public class Person extends Agent {
	//Data
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
	public enum PersonState {walking,
							  goingToSleep,
							  sleeping,
							  idle,
							  working,
							  workOver
	};
	
	
	//Stats
	public int hungerLevel = 5;
	public int hungerThreshold = 3; 
	public Money money = new Money(10,0);
	public Money moneyThreshold = new Money(9,0);
	public Building building = null;
	public Building destination = null;
	public Morality mor = Morality.good;
	public PersonState ps = PersonState.idle;

		//Getters and setters
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
		public String getHomeType(){ return house;}
		public PersonState getPersonState() { return ps;}
		public Role getMainRole(){ return mainRole;}
		public void setMainRole(String job) { 
			Role newRole;
			try {
				newRole = (Role)Class.forName(job).newInstance();
				roles.add(newRole);
				mainRole = newRole;
				mainRole.active = true;
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
		
	public Person(Gui gui, String mainRole, Vehicle vehicle, Morality morality, Money money, Money moneyThresh, int hunger, int hungerThresh){
		this.gui = gui;
		setMainRole(mainRole);
		this.vehicle = vehicle;
		this.mor = morality;
		this.money = money;
		this.moneyThreshold = moneyThresh;
		this.hungerLevel = hunger;
		this.hungerThreshold = hungerThresh;
	}
	
	public Person(String mainRole, Vehicle vehicle, Morality morality, Money money, Money moneyThresh, int hunger, int hungerThresh){
		setMainRole(mainRole);
		this.vehicle = vehicle;
		this.mor = morality;
		this.money = money;
		this.moneyThreshold = moneyThresh;
		this.hungerLevel = hunger;
		this.hungerThreshold = hungerThresh;
	}
	
	public Person(String mainRole){
		//setMainRole(mainRole);
		God.Get().EnterBuilding(null, this, mainRole);
		this.mainRole = this.roles.get(0);
		this.vehicle = Vehicle.walk;
		this.mor = Morality.good;
		this.money = new Money(5,10);
		this.moneyThreshold = new Money(1, 0);
		this.hungerLevel = 10;
		this.hungerThreshold = 4;
	}
		
	//Messages
	public void msgMainRole(){
		
	}
		
	public void msgCreateRole(Role r){
		//If there already exists a role r. Turn it on.
		for(Role ro: roles){
			if (ro.getClass() == r.getClass()){
				r.active = true;
                r.myPerson = this;
				stateChanged();
				return;
			}		
		}
		//If there does not exist that role r then add r to list.
		r.setActive(true);
        r.myPerson = this;
		roles.add(r);
		stateChanged();
		return;
	}
	public void msgSleep(){
		ps = PersonState.goingToSleep;
		stateChanged();
	}
	
	public void setHomeType(String home) {
		//set housing to null.. becomes homeless if doesn't pay rent for multiple pay periods
		house = "";
	}
	
	public void msgEnterBuilding(Building b){
		building = b;
		stateChanged();
	}
	
	public void msgGoToBuilding(Building b){
		destination = b;
		stateChanged();
	}
	
	public void msgWorkOver(){
		ps = PersonState.workOver;
		stateChanged();
	}
	
	public void msgMorning(){
		ps = PersonState.idle;
		stateChanged();
	}
	
	//Scheduler
	boolean returnPAEAA = false;
	@Override
	protected boolean pickAndExecuteAnAction() {
		//if there is an active Role r in roles then return whatever that PAEAA returns
		for (Role r: roles){
			if (r.active){
				 returnPAEAA =  r.pickAndExecuteAnAction();
			}
		}
		
		if (ps == PersonState.workOver){
			workOver();
			return false;
		}
		
		if (ps == PersonState.goingToSleep){
			goToSleep();
			return false;
		}
		
		if (destination != null){
			goTo(destination);
		}
		
		//ifperson not doing anything
		if (ps == PersonState.idle){
			//check if he has enough money.
			if (money.dollars < 5){
				goToBank();
				return false;
			}
			
			//Check if he is hungry
			if (hungerLevel < hungerThreshold){
				goToRestaurant();
				return false;
			}
		}
		return returnPAEAA;
	}
	
	
	//Actions
	private void workOver() {
		for (Role r: roles){
			r.workOver();
		}
	}
	private void goToSleep() {
		//animation to home.
	}
	private void goToBank() {
		//animation to bank.
	}
	private void goToRestaurant() {
		//animation to restaurant.
	}
	
	//Utility
	public void goTo(Building b){
		createVehicle();
		//Animation for gui stuff here.
		//Call person gui animation. acquire my semaphore.
		if (intent==Intent.work)
			God.Get().EnterBuilding(b, this, mainRole.getClass().toString());
		destination = null;
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

