/**
 * 
 */
package SimCity.Base;

import housing.gui.HousingAnimation;
import housing.roles.TenantRole;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;
import javax.swing.Timer;

import market.MarketDeliveryPersonRole;
import exterior.gui.AnimationPanel;
import exterior.gui.CarGui;
import exterior.gui.Gui;
import exterior.gui.PersonGui;
import restaurant.*;
import timRest.TimCashierRole;
import timRest.TimCookRole;
import timRest.TimCustomerRole;
import timRest.TimHostRole;
import timRest.TimWaiterRole;
import SimCity.Base.God.BuildingType;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Buildings.B_DannyRestaurant;
import SimCity.Buildings.B_EricRestaurant;
import SimCity.Buildings.B_House;
import SimCity.Buildings.B_JesseRestaurant;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_TimRest;
import SimCity.Globals.Money;
import SimCity.trace.AlertLog;
import SimCity.trace.AlertTag;
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
	public String mainRoleString;
	public Building workPlace = null;
	public enum Intent {customer, work};
	public enum Vehicle {car, delivery, walk, bus};
	public Vehicle vehicle = Vehicle.walk;
	public int shift = 1;
	public int getShift(){return shift;}
	public Gui gui;
	public Semaphore animation = new Semaphore(0, true);
	public AnimationPanel animPanel;
	public ArrayList<Role> roles = new ArrayList<Role>();
	
	public List<GroceryList> groceryList = new ArrayList<GroceryList>();
	
	public ArrayList<String> inventory = new ArrayList<String>();
	
	public enum Morality {good, crook};
	public enum TimeState {
								none,
								msgMorning,
								morning,
								msgGoToWork,
								working,
								msgWorkOver,
								workOver,
								msgGoHome,
								goingHome,
								msgGoToSleep,
								sleeping;
							};
	
	//A GoAction is simply a way to figure out where the person wants to go.				
	public enum GoAction {
		goHome,
		goDannyRestaurant,
		goMarket,
		goBank,
		goBrianRestaurant,
		goEricRestaurant, 
		goJesseRestaurant,
		goTimRestaurant
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
	public int accNum = -1;

		//Getters and setters
		public String getMainRoleString(){return mainRoleString;}
		public TimeState getTimeState(){return timeState;}
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
		public Building getWorkPlace(){return workPlace;}
		public String getName(){return name;};
		public void setWorkPlace(Building work){workPlace = work;}
		public void setMainRoleString(String job){mainRoleString = job;}
		public void setMainRole(String job) { 
			
			//Temporary fix to factory with classes that need parameters
			if (job.equals("brianRest.BrianHostRole")) return;
			else if (job.equals("brianRest.BrianCookRole")) return;
			else if (job.equals("brianRest.BrianCashierRole"))return;
			else if (job.equals("brianRest.BrianCustomerRole")) {return;}
			else if (job.equals("brianRest.BrianWaiterRole")){return;}
			else if (job.equals("brianRest.BrianPCWaiterRole")){return;}
			else if (job.equals("EricRestaurant.EricHost"))return;
			else if (job.equals("EricRestaurant.EricCook"))return;
			else if (job.equals("EricRestaurant.EricCashier"))return;
			else if (job.equals("EricRestaurant.EricCustomer"))return;
			else if (job.equals("EricRestaurant.EricWaiter"))return;
			else if (job.equals("EricRestaurant.EricPCWaiter"))return;
			else if (job.equals("jesseRest.JesseHost"))return;
			else if (job.equals("jesseRest.JesseWaiter"))return;
			else if (job.equals("jesseRest.JessePCWaiter"))return;
			else if (job.equals("jesseRest.JesseCustomer"))return;
			else if (job.equals("jesseRest.JesseCashier"))return;
			else if (job.equals("jesseRest.JesseCook"))return;

			
			Role newRole;
			try {
				newRole = (Role)Class.forName(job).newInstance();
				newRole.setPerson(this);
				newRole.setActive(false);
				roles.add(newRole);
				mainRole = newRole;
				mainRole.myPerson = this;
			} catch(Exception e){
			}
		}
		public void resetActiveRoles(){
			for (Role r : roles){
				r.setActive(false);
			}
		}
		
	//USE THIS CONSTRUCTOR.
	public Person(String name, Gui gui, String mainRole, Vehicle vehicle, Morality morality, Money money, Money moneyThresh, int hunger, int hungerThresh, String houseType, B_House house, Building workplace, int shift){
		this.gui = gui;
		this.shift = shift;
		setMainRole(mainRole);
		mainRoleString = mainRole;
		this.vehicle = vehicle;
		this.mor = morality;
		this.money = money;
		this.moneyThreshold = moneyThresh;
		this.hungerLevel = hunger;
		this.hungerThreshold = hungerThresh;
		this.house = houseType; 
		this.name = name;
		if (gui != null){
			Random rand = new Random();
			int spawnPoint = rand.nextInt(16);
			this.building = God.Get().getBuilding(spawnPoint);
		}
		myHouse = house;
		this.workPlace = workplace;
		setUpHungerTimer();
	}
	
	//Test constructor. DO NOT FKING USE UNLESS FOR TESTING!!!
	public Person(String mainRole){
		this.name = "TestPerson";
		setMainRole(mainRole);
		mainRoleString = mainRole;
		this.vehicle = Vehicle.walk;
		this.mor = Morality.good;
		this.money = new Money(100,10);
		this.accNum = 1;
		this.moneyThreshold = new Money(1, 0);
		this.hungerLevel = 10;
		this.hungerThreshold = 4;
		myHouse = new B_House(new JPanel());
		house = "Apartment";
        God.Get().addPerson(this);
	}
//Messages ---------------------------------------
	
	private void addAction(Action a){actions.addLast(a);; stateChanged();}
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
		//building = null;
	}
	
	//No idea if this works. Scary as shit.
	//Call this function to go to queue to go to a building. The person will only go to a building if he is in the exterior world.
	//If the person is in the interior world, the person will continue to do whatever he is currently doing.
	public void msgGoToBuilding(Building b, Intent i){
		if (b == null) return;
		//AlertLog.getInstance().logError(AlertTag., name, message);
		if (b instanceof B_Bank){ addAction(new Action(GoAction.goBank, i));}
		else if (b instanceof B_House){ addAction(new Action(GoAction.goHome, i));}
		else if (b instanceof B_DannyRestaurant){ addAction(new Action(GoAction.goDannyRestaurant, i));}
		else if (b instanceof B_Market){ addAction(new Action(GoAction.goMarket, i));}
		else if (b instanceof B_BrianRestaurant){addAction(new Action(GoAction.goBrianRestaurant, i));}
		else if (b instanceof B_EricRestaurant){addAction(new Action(GoAction.goEricRestaurant, i));}
		else if (b instanceof B_JesseRestaurant){addAction(new Action(GoAction.goJesseRestaurant, i));}
		else if (b instanceof B_TimRest){ addAction(new Action(GoAction.goTimRestaurant, i));}
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
	
	public void msgGoToWork(){
		actions.clear();
		timeState = TimeState.msgGoToWork;
		stateChanged();
	}
	
	public void msgGoHome(){
		timeState = TimeState.msgGoHome;
		stateChanged();
	}
	
	public void msgMorning(){
		timeState = TimeState.msgMorning;
		stateChanged();
	}
	
	//Scheduler
	boolean returnPAEAA = false;

	@Override
	public boolean pickAndExecuteAnAction() {
		
		//Handle time of day first thing!!
		
		//Morning call must go first BECAUSE tenant role must be overridden if the GOD class messages the person.
		if (timeState == TimeState.msgMorning){
			isMorning();
			return false;
		}
		
		if (timeState == TimeState.msgGoToWork){
			isWorkTime();
			return false;
		}
		
		//Work over must go before the active roles to override whatever active role is currently going.
		if (timeState == TimeState.msgWorkOver){
			workOver();
			return false;
		}
		
		if (timeState == TimeState.msgGoHome){
			goHome();
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
			return false; //might be true
		}
		
		//if the person leaves a building and has nothing to do.
		if (timeState == TimeState.none && actions.size() == 0){
			//check if he has enough money.
			if (money.dollars < moneyThreshold.dollars){
				msgGoToBuilding(God.Get().findBuildingOfType(BuildingType.Bank), Intent.customer);
				
				//goTo(new Action(GoAction.goBank, Intent.customer));
				return true;
			}
			
			//Check if he is hungry
			if (hungerLevel < hungerThreshold){
				msgGoToBuilding(God.Get().findBuildingOfType(BuildingType.Restaurant), Intent.customer);
				return true;
			}
			goHome();
			
		}
		
		return returnPAEAA;
	}
	
	
	//Actions
	private void workOver() {
		timeState = TimeState.none;
		for (Role r: roles){
			if (r.getActive())
				r.workOver();
		}	
	}
	
	private void goHome(){
		timeState = TimeState.none;
		//If go home is called, you should delete everything in your actions list for the day. We force the people to go
		//home to go to sleep.
		actions.clear();
		msgGoToBuilding(myHouse, Intent.customer);
	}
	
	private void isWorkTime(){
		timeState = TimeState.working;
		for (Role r: roles){
			if (r.getActive()){
				if (r instanceof TenantRole){
					TenantRole tr = (TenantRole)r;
					tr.msgGoToWork();
					return;
				}
			}
		}
		msgGoToBuilding(getWorkPlace(), Intent.work);
		return;
	}
	
	private void isMorning(){
		timeState = TimeState.none;
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
		Building b = null;
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
		if (action.getGoAction() == GoAction.goMarket && action.intent == Intent.customer){
			//Go to your market.
			b = God.Get().findBuildingOfType(BuildingType.Market);
			Do("going to market");
		}else
        if (action.getGoAction() == GoAction.goMarket && action.intent == Intent.work){
            //Go to your market.
            b = this.workPlace;
            Do("going to market");
        }else
		if (action.getGoAction() == GoAction.goDannyRestaurant && action.intent == Intent.customer){
			//Go to restaurant
			b = God.Get().getBuilding(9);
			Do("Going to restaurant");
		}else
		if (action.getGoAction() == GoAction.goDannyRestaurant && action.intent == Intent.work){
			//Put all restaurant roles here.
			if (mainRole instanceof DannyWaiter || mainRole instanceof DannyPCWaiter || mainRole instanceof DannyHost || mainRole instanceof DannyCook || mainRole instanceof DannyCashier){
				b = God.Get().getBuilding(9);
				Do("working at restaurant");
			}
			if (mainRole instanceof DannyCustomer)
			{
			    b = God.Get().getBuilding(9);
                Do("Going to eat at Danny restaurant");
			}
            if (mainRole instanceof MarketDeliveryPersonRole)
            {
                b = God.Get().getBuilding(9);
                Do("Going to deliver to restaurant.");
            }
		}
		else
        if (action.getGoAction() == GoAction.goTimRestaurant && action.intent == Intent.customer){
            //Go to restaurant
            b = God.Get().getBuilding(10);
            Do("Going to restaurant");
        }else
        if (action.getGoAction() == GoAction.goTimRestaurant && action.getIntent() == Intent.work){
            //Put all restaurant roles here.
            if (mainRole instanceof TimHostRole || mainRole instanceof TimWaiterRole || mainRole instanceof TimCookRole || mainRole instanceof TimCashierRole){
                b = God.Get().getBuilding(10);
                Do("working at restaurant");
            }
            if (mainRole instanceof TimCustomerRole)
            {
                b = God.Get().getBuilding(9);
                Do("Going to eat at Tim restaurant");
            }
            if (mainRole instanceof MarketDeliveryPersonRole)
            {
                b = God.Get().getBuilding(10);
                Do("Goint to deliver to restaurant.");
            }
        }
		else
		if (action.getGoAction() == GoAction.goBrianRestaurant && action.intent == Intent.customer){
			b = God.Get().getBuilding(6);
			Do("Going to Brian Restaurant");
		}
		else
		if (action.getGoAction() == GoAction.goBrianRestaurant && action.intent == Intent.work){
			//Put all restaurant roles here
			b = God.Get().getBuilding(6);
			Do("Working at Brian Restaurant");
		}
		else 
		if (action.getGoAction() == GoAction.goEricRestaurant && action.intent == Intent.work) {
			b = God.Get().getBuilding(11);
			Do("Working at Eric Restaurant");
		}
		else 
		if (action.getGoAction() == GoAction.goEricRestaurant && action.intent == Intent.customer) {
			b = God.Get().getBuilding(11);
			Do("Going to Eric Restaurant");
		}
		else 
		if (action.getGoAction() == GoAction.goJesseRestaurant && action.intent == Intent.customer) {
			b = God.Get().getBuilding(7);
			Do("Going to Jesse Restaurant");
		}
		else 
			if (action.getGoAction() == GoAction.goJesseRestaurant && action.intent == Intent.work) {
				b = God.Get().getBuilding(7);
				Do("Working at Jesse Restaurant");
			}
		else b = God.Get().getBuilding(11);
		
		//Animation for gui stuff here.
		//############################# Animate to the  building here. ##########################################
		
		
		if (b == null){
			AlertLog.getInstance().logError(AlertTag.PERSON, name, "Error no building found");
			b = God.Get().getBuilding(0);
		}
		
		//Call person gui animation. acquire my semaphore.
		destination = b;
		
		//AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Going from :" + building.getTag() + " to " +b.getTag() + ".");
		
		if (gui instanceof PersonGui) {
			if (vehicle != Vehicle.bus) {
				((PersonGui) gui).DoTravel(building.id, b.id);
			} else {
				animPanel.enterBus(this);
			}
		} else if (gui instanceof CarGui) {
			((CarGui) gui).DoTravel(building.id, b.id);
		}
		
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (action.intent == Intent.work){
			destination = null;
			building = b;
			//b.EnterBuilding(this, mainRole.getClass().toString());
			b.EnterBuilding(this, mainRoleString);
		}
		else if (action.intent == Intent.customer)
			{
				destination = null;
				building = b;
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
	
	public void loseCar(Person other) {
		// Pay a 250 dollar fine.
		setMoney(getMoney().subtract(250, 0));
		other.setMoney(other.getMoney().add(250, 0));
		
		if (gui instanceof CarGui) {
			Random rand = new Random();
			int newTransportation = rand.nextInt(10);
			
			if (newTransportation < 5) {
				vehicle = Vehicle.walk;
				AlertLog.getInstance().logWarning(AlertTag.God, "God (GUI)", "A person (" + this + ") lost their car in an accident and will walk henceforth. Paid $250 fee to victim (" + other + ").");
			} else {
				vehicle = Vehicle.bus;
				AlertLog.getInstance().logWarning(AlertTag.God, "God (GUI)", "A person (" + this + ") lost their car in an accident and will take the bus henceforth. Paid $250 fee to victim (" + other + ").");
			}
			gui = animPanel.getNewGui(this);
		}
	}
	
	Timer hungerTimer;
	int hungerOffset = 6000;
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
	
	public void setAnimPanel(AnimationPanel animPanel) {
		this.animPanel = animPanel;
	}
	
	public class GroceryList {
		public String item;
		public int amount;
		
		public GroceryList(String i, int a) {
			item = i;
			amount = a;
		}
	}
	
	//DEBUG
    public void testMarket()
    {
        addActionToFront(new Action(GoAction.goMarket, Intent.customer));
    }
    
    public void testTim()
    {
        addActionToFront(new Action(GoAction.goTimRestaurant, Intent.customer));
    }
}

