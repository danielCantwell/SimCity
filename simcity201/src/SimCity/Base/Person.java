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
	private String house;
	private Role mainRole;
	private enum Vehicle {car, delivery, walk, bus};
	private Vehicle vehicle = Vehicle.walk;
	
	private Gui gui;
	private Semaphore animation = new Semaphore(0, true);
	
	private ArrayList<Role> roles = new ArrayList<Role>();
	
	private ArrayList<String> inventory = new ArrayList<String>();
	
	public enum Morality {good, crook};
	public enum PersonState {walking,
							  goingToSleep,
							  sleeping,
							  idle,
							  working,
							  workOver
	};
	
	
	//Stats
		private int hungerLevel = 5;
		private int hungerThreshold = 3; 
		private Money money = new Money(10,0);
		private Money moneyThreshold = new Money(9,0);
		private Building building = null;
		private Building destination = null;
		private Morality mor = Morality.good;
		private PersonState ps = PersonState.idle;

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
			} catch(Exception e){
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
		
	//Messages
	public void msgMainRole(){
		
	}
		
	public void msgCreateRole(Role r){
		//If there already exists a role r. Turn it on.
		for(Role ro: roles){
			if (ro.getClass() == r.getClass()){
				r.active = true;
				stateChanged();
				return;
			}		
		}
		//If there does not exist that role r then add r to list.
		r.setActive(true);
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
		//Call person gui animation.
		God.Get().EnterBuilding(b, this);
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

