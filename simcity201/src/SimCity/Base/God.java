package SimCity.Base;

import housing.roles.OwnerRole;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import jesseRest.JesseCustomer;
import brianRest.BrianCustomerRole;
import market.MarketCustomerRole;
import market.MarketManagerRole;
import restaurant.DannyCashier;
import restaurant.DannyCook;
import restaurant.DannyCustomer;
import restaurant.DannyHost;
import restaurant.DannyWaiter;
import timRest.TimCashierRole;
import timRest.TimCookRole;
import timRest.TimCustomerRole;
import timRest.TimHostRole;
import timRest.TimWaiterRole;
import Bank.RobberRole;
import Bank.bankCustomerRole;
import Bank.bankManagerRole;
import EricRestaurant.EricCustomer;
import EricRestaurant.EricHost;
import SimCity.Base.Person.TimeState;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Buildings.B_DannyRestaurant;
import SimCity.Buildings.B_EricRestaurant;
import SimCity.Buildings.B_House;
import SimCity.Buildings.B_JesseRestaurant;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_TimRest;
import SimCity.trace.AlertLog;
import SimCity.trace.AlertTag;
import exterior.gui.AnimationPanel;
import exterior.gui.SimCityGui;
/**
 * @author Brian
 *
 */
public class God {

	    private static final God INSTANCE = new God();
	    
	    //Gui stuff accessible by everyone
	    AnimationPanel animationPanel;
	    SimCityGui simGui;
	    
	    public void setAnimationPanel(AnimationPanel anim){ animationPanel = anim;}
	    public AnimationPanel getAnimationPanel(AnimationPanel anim) { return animationPanel;}
	    public void setSimGui(SimCityGui sim){simGui = sim;}
	    public SimCityGui getSimGui(){return simGui;}
	    
	    //List of God thingies
	    public int day = 5;
	    public int hour;
	    public boolean isWeekend = false;
	    Timer hourTimer;
	    public int hourOffset;
	    public ArrayList<Building> buildings = new ArrayList<Building>();
	    public ArrayList<Person> persons = new ArrayList<Person>();
	    
	    public void addPerson(Person p){ 
	    	if (persons.size() > 0){
		    	/*AlertLog.getInstance().logDebug(AlertTag.God, "DEBUG", persons.size() + "");
		    	for (Person per: persons){
		    		if (per.getMainRoleString().equals("Bank.bankManagerRole"))
		    		AlertLog.getInstance().logWarning(AlertTag.God, "Warning", per.getMainRoleString() + ", work at: " + per.getWorkPlace().getID());
		    	}*/
	    	
	    	//If we are dealing with a manager. make sure to check if there is already a manager that exists.
	    	//this is a double fail safe and should not be needed but i place it here anyways as a fail safe.
	    	if (p.mainRole instanceof bankManagerRole 
	    			|| p.mainRole instanceof DannyHost 
	    			|| p.mainRole instanceof MarketManagerRole 
	    			|| p.mainRole instanceof TimHostRole
	    			|| p.getMainRoleString().equals("brianRest.BrianHostRole")
	    			|| p.getMainRoleString().equals("EricRestaurant.EricHost")
	    			|| p.getMainRoleString().equals("jesseRest.JesseHost")) 
	    	{
	
		    	for (Person pe:persons){
		    		if (pe.getMainRoleString().equals(p.getMainRoleString())){
		    			if (pe.getWorkPlace().getID() == p.getWorkPlace().getID())
		    				if (pe.getShift() == p.getShift()){
		    					AlertLog.getInstance().logError(AlertTag.God, "ERROR", "Can only spawn 1 of type: " + p.getMainRoleString());
		    				return;
		    			}
		    		}
		    		
		    	}
	    	}
	    	}
	    	persons.add(p);
	    }
	    
	    public boolean canAddPerson(Person p){
	    	if (p.mainRole instanceof bankManagerRole 
	    			|| p.mainRole instanceof DannyHost 
	    			|| p.mainRole instanceof MarketManagerRole 
	    			|| p.mainRole instanceof TimHostRole
	    			|| p.getMainRoleString().equals("brianRest.BrianHostRole")
	    			|| p.getMainRoleString().equals("EricRestaurant.EricHost")
	    			|| p.getMainRoleString().equals("jesseRest.JesseHost")) 
	    	{
	    		for (Person pe : persons){
	    			if (pe.getMainRoleString().equals(p.getMainRoleString())){
		    			if (pe.getShift() == p.getShift()){
		    				//if (pe.getWorkPlace() == p.getWorkPlace())
			    			//AlertLog.getInstance().logError(AlertTag.God, "ERROR", "Already instantiated a manager of type: " + p.getMainRoleString());
		    				//return false;
		    			}
	    			}
	    		}
	    	}
	    	return true;
	    	
	    }
	    
	    public void removePerson(Person p){ persons.remove(p);}
	    public void addBuilding(Building j){ buildings.add(j);}
	    public void removeBuilding(Building j){ buildings.remove(j);}
	    public Building getBuilding(int id){
			for (Building b: simGui.buildingList){
				if (b.getID() == id){
					return b;
				}
			}
			//System.out.println("Could not find building: " + id);
			AlertLog.getInstance().logError(AlertTag.God, "God", "Line 73: Could not find building" + id);
			
			return null;
	    }
	    public B_House getBHouse(int id) {
	    	for (Building b : simGui.buildingList) {
	    		if (b.getID() == id) {
	    			return (B_House) b;
	    		}
	    	}
	    	return null;
	    }
	    
	    public enum BuildingType{
	    	Bank,
	    	House,
	    	Market,
	    	Restaurant;
	    }
	    
	    public Building findBuildingOfType(BuildingType bt){
	        Random random = new Random();
	        ArrayList<Building> buildings = new ArrayList<Building>();
	    	if (bt == BuildingType.Bank){
	    		//Find a bank
	    		for(Building b : simGui.buildingList){
	    			if (b instanceof B_Bank) buildings.add(b);
	    		}
                if (!buildings.isEmpty())
                {
                    return buildings.get(random.nextInt(buildings.size()));
                }
	    	}else
	    	if (bt == BuildingType.House){
	    		//Find a market
	    		for(Building b : simGui.buildingList){
	    			if (b instanceof B_House) buildings.add(b);
	    		}
	    		AlertLog.getInstance().logWarning(AlertTag.God, "God", "Be careful when using findHouse; Do not use it to find a house to live in.");
                if (!buildings.isEmpty())
                {
                    return buildings.get(random.nextInt(buildings.size()));
                }
	    	}else
            if (bt == BuildingType.Market){
                //Find a market
                for(Building b : simGui.buildingList){
                    if (b instanceof B_Market) buildings.add(b);
                }
                if (!buildings.isEmpty())
                {
                    return buildings.get(random.nextInt(buildings.size()));
                }
            }else
	    	if (bt == BuildingType.Restaurant){
	    		//Find a bank
	    		findRandomRestaurant();
	    	}
	    	return null;
	    }
	    
	    public int getDay(){ return day;}
	    public int getHour(){return hour;}
	    public Person getPerson(int id){
			return persons.get(id);
	    }
	    
	    //Fix this laters
	    public Building findRandomRestaurant(){
	    	int s = (int)(Math.round((Math.random() * 4)));
	    	switch (s){
	    	case 0: return simGui.buildingList.get(6); //Brian
	    	case 1: return simGui.buildingList.get(7); // Jesse
	    	case 2: return simGui.buildingList.get(9); //Danny
	    	case 3: return simGui.buildingList.get(10); //Tim
	    	case 4: return simGui.buildingList.get(11); //Eric
	    	default : return simGui.buildingList.get(6);
	    	}
	    }
	    public Building findRandomRestaurant(String s){
	    	switch (s){
	    	case "Brian": return simGui.buildingList.get(6); //Brian
	    	case "Jesse": return simGui.buildingList.get(7); // Jesse
	    	case "Danny": return simGui.buildingList.get(9); //Danny
	    	case "Tim": return simGui.buildingList.get(10); //Tim
	    	case "Eric": return simGui.buildingList.get(11); //Eric
	    	default : return simGui.buildingList.get(11);
	    	}
	    }
	    
	    private boolean announcedTime = false;
	    
	    private God() {
	        if (INSTANCE != null) {
	            throw new IllegalStateException("Already instantiated");
	        }
	        
	        //set God variables.
	        hour = 1;
	        hourOffset = 10000;
	        //Set the timer for day.
	        hourTimer = new Timer(hourOffset, new ActionListener() {
				   public void actionPerformed(ActionEvent e){
					   if (hour < 24){hour ++; announcedTime = false;} // hour increments everytime this timer fires.
					   
					   if (hour == 4 && !announcedTime){
						   wakeUp();
					   }
					   
					   //SHIFT #1
					   if (hour == 5 && !announcedTime){
						   flushAllPersonActions();
						   managersGoToWork(1);
					   }
					   
					   if (hour == 7 && !announcedTime){
						   restaurantPeopleGoWork(1);
					   }
					   
					   if (hour == 8 && !announcedTime){
						   goToWork(1);
					   }
					   
					   if (hour == 9 && !announcedTime){
						   fakeCustomersGoToWork(1);
					   }
					   if (hour == 11 && !announcedTime){
						   bankInteraction();
					   }
					   if (hour == 12 && !announcedTime){
						   getOffWork(1);
					   }
					   
					   //SHIFT #2
					   if (hour == 13 && !announcedTime){
						   flushAllPersonActions();

						   managersGoToWork(2);
					   }
					   
					   if (hour == 14 && !announcedTime){
						   restaurantPeopleGoWork(2);
					   }
					   
					   if (hour == 15 && !announcedTime){
						   goToWork(2);
					   }
					   
					   if (hour == 16 && !announcedTime){
						   fakeCustomersGoToWork(2);
					   }
					 
					   
					   if (hour == 20 && !announcedTime){
						   getOffWork(2);
					   }
					   
					   if (hour == 22 && !announcedTime){
						   goHome();
	        			}
					   
					   if (hour==24 && !announcedTime){
						   goToSleep();
						   flushAllPersonActions();
					   }
					   
					   if (hour >= 24) { //if the hour is 24 hours, then hours is reset back to zero
						   hour = 0; //and a day is added to the Date.
						   day ++ ;
						   AlertLog.getInstance().logInfo(AlertTag.God, "God", "Day " + day+ " : It is now morning.");
					   }
					   
					   //Collect rent.
					   if (day % 21 == 0){
						   try{
							   for (Person person: persons){
								   if (person.getMainRole() instanceof OwnerRole){
									   OwnerRole or = (OwnerRole) person.getMainRole();
									   or.msgTimeToCollectRent();
								   }
							   }
						   }
						   catch(Exception e1){};
					   }
					   
					   if (day % 7 == 6 || day % 7 == 0 && !banksClosed){
						   //notifyBanksClosed(); //handled by hosts now.
						   isWeekend = true;
					   }
					   else {
						   if (banksClosed)
							  // 	notifyBanksOpen(); //handled by hosts now.
						   isWeekend = false;
					   }
				   }
	        });
	        hourTimer.start();
	    }

	    public static synchronized God Get() {
	        return INSTANCE;
	    }
	    
	    public void wakeUp(){
	    	announcedTime = true;
	    	AlertLog.getInstance().logInfo(AlertTag.God, "God", "Hour " + hour + " : It is now morning.");
	    	for(Person p: persons){
	    		p.msgMorning();
	    	}
	    }
	    
	    public void fakeCustomersGoToWork(int shift){
	    	announcedTime = true;
	    	for (Person p: persons){
	    		if (p.getTimeState() != TimeState.working){
	    			if (p.getMainRoleString().contains("ustomer") || p.getMainRoleString().contains("Robber"))
	    				p.msgGoToWork();
	    		}
	    	}
	    }
	    
	    public void managersGoToWork(int shift){
	    	announcedTime = true;
	    	AlertLog.getInstance().logInfo(AlertTag.God, "God", "Hour " + hour + " : Managers are going to work.");
	    	for (Person p: persons){
	    		if (p.mainRole instanceof bankManagerRole || p.mainRole instanceof DannyHost || p.mainRole instanceof DannyCook || p.mainRole instanceof DannyCashier || p.mainRole instanceof MarketManagerRole || p.mainRole instanceof TimHostRole){
	    			if (!isWeekend) 
	    			if (p.getShift() == shift) 
	    			p.msgGoToWork();	    			

	    		}else
	    		if (p.getMainRoleString().equals("brianRest.BrianHostRole")){
	    			if (p.getShift() == shift)
	    			p.msgGoToWork();
	    		}
	    		else 
	    		if (p.getMainRoleString().equals("EricRestaurant.EricHost")) {
	    			if (p.getShift() == shift)
	    			p.msgGoToWork();
	    		}
	    		else
	    		if (p.getMainRoleString().equals("jesseRest.JesseHost")) {
	    			if (p.getShift() == shift)
	    			p.msgGoToWork();
	    		}
	    	}
	    }
	    
	    public void goHome(){
	    	announcedTime = true;
	    	AlertLog.getInstance().logInfo(AlertTag.God, "God", "Hour " + hour + " : All citizens are required to go home.");
	    	for(Person p: persons){
	    		p.msgGoHome();
	    	}
	    }
	    
	    public void goToSleep(){
	    	announcedTime = true;
	    	AlertLog.getInstance().logInfo(AlertTag.God, "God", "Hour " + hour + " : All citizens are required to sleep.");
	    	for (Person p: persons){
	    		p.msgSleep();
	    	}
	    }
	    
	    public void goToWork(int shift){
	    	announcedTime = true;
	    	AlertLog.getInstance().logInfo(AlertTag.God, "God", "Hour " + hour + " : Employees must go to work.");
	    	for (Person p: persons){
	    		if(p.getTimeState() != TimeState.working)
	    			if (!(p.getMainRole() instanceof DannyCustomer))
    				if (!(p.getMainRole() instanceof BrianCustomerRole))
					if (!(p.getMainRole() instanceof EricCustomer))
					if (!(p.getMainRole() instanceof TimCustomerRole))
					if (!(p.getMainRole() instanceof JesseCustomer))
					if (!(p.getMainRole() instanceof MarketCustomerRole))
					if (!(p.getMainRole() instanceof bankCustomerRole))
					if (!(p.getMainRole() instanceof RobberRole))
						if (p.getShift() == shift)
						p.msgGoToWork();
	    	}
	    }
	    
	    public void restaurantPeopleGoWork(int shift){
	    	announcedTime = false;
	    	AlertLog.getInstance().logInfo(AlertTag.God, "God", "Hour " + hour + " : Restaurant employees must go to work.");
	    	for(Person p: persons){
	    		
	    		if (p.mainRole instanceof DannyHost || p.mainRole instanceof DannyWaiter || p.mainRole instanceof DannyCook || p.mainRole instanceof DannyCashier){
	    			if (p.getShift() == shift)
	    			p.msgGoToWork();
	    		}
	    		else if (p.mainRole instanceof TimHostRole || p.mainRole instanceof TimWaiterRole || p.mainRole instanceof TimCookRole || p.mainRole instanceof TimCashierRole){
	    			if (p.getShift() == shift)
	    			p.msgGoToWork();
                }
	    		else if (p.getMainRoleString().equals("brianRest.BrianWaiterRole") || p.getMainRoleString().equals("brianRest.BrianPCWaiterRole") || p.getMainRoleString().equals("brianRest.BrianCookRole") || p.getMainRoleString().equals("brianRest.BrianCashierRole")){
	    			if (p.getShift() == shift)
	    			p.msgGoToWork();
	    		}
	    		else if (p.getMainRoleString().equals("EricRestaurant.EricWaiter") ||p.getMainRoleString().equals("EricRestaurant.EricPCWaiter")|| p.getMainRoleString().equals("EricRestaurant.EricCook") || p.getMainRoleString().equals("EricRestaurant.EricCashier")) {
	    			if (p.getShift() == shift)
	    			p.msgGoToWork();
	    		}
	    		else if (p.getMainRoleString().equals("jesseRest.JesseWaiter") || p.getMainRoleString().equals("jesseRest.JessePCWaiter")|| p.getMainRoleString().equals("jesseRest.JesseCook") || p.getMainRoleString().equals("jesseRest.JesseCashier")) {
	    			if (p.getShift() == shift)
	    			p.msgGoToWork();
	    		}
	    	}
	    }
//	    
	    public void bankInteraction() {
	    	for(Person p : persons){
	    	if (p.getMainRoleString().equals("EricRestaurant.EricHost")){
	    		B_EricRestaurant rest = (B_EricRestaurant) God.Get().getBuilding(11);
	    		rest.host.setBM();
	    		}
	    	if (p.getMainRoleString().equals("jesseRest.JesseHost")){
	    		B_JesseRestaurant rest = (B_JesseRestaurant) God.Get().getBuilding(7);
	    		rest.host.setBM();
	    		}
	    	if (p.getMainRoleString().equals("brianRest.BrianHostRole")){
	    		B_BrianRestaurant rest = (B_BrianRestaurant) God.Get().getBuilding(6);
	    		rest.hostRole.setBM();
	    		}
	    	if (p.getMainRoleString().equals("restaurant.DannyHost")){
	    		B_DannyRestaurant rest = (B_DannyRestaurant) God.Get().getBuilding(9);
	    		rest.hostRole.setBM();
	    		}
	    	if (p.getMainRoleString().equals("timRest.TimHostRole")){
	    		B_TimRest rest = (B_TimRest) God.Get().getBuilding(10);
	    		rest.hostRole.setBM();
	    		}
	    	}
	    }
//	    
	    public void getOffWork(int shift){
	    	announcedTime = true;
	    	AlertLog.getInstance().logInfo(AlertTag.God, "God", "Hour " + hour + " : Shift ["+ shift+"] is now over.");
	    	for (Person p: persons){
	    		if (p.getShift() == shift)
	    		p.msgWorkOver();
	    	}
	    }
	    
	    public void getOffWork(Building b){
	    	announcedTime = true;
	    	//AlertLog.getInstance().logWarning(AlertTag.God, "USER", "Closing " + b.getTag());
	    	for (Person p: persons){
	    		if (p.getBuilding() == b){
	    		p.msgGoHome();
	    		p.getBuilding().ExitBuilding(p);
	    		}
	    		
	    	}
	    }
	    
	    public boolean banksClosed = false;
	    private void notifyBanksClosed(){
	    	banksClosed = true;
	    	for (Building b: buildings){
	    		if (b.getTag().equals("B_Bank")){
	    			B_Bank bank = (B_Bank)b;
	    			bank.setForceClose(true);
	    		}
	    	}
	    }
	    private void notifyBanksOpen(){
	    	banksClosed = false;
	    	for (Building b: buildings){
	    		if (b.getTag().equals("B_Bank")){
	    			B_Bank bank = (B_Bank)b;
	    			bank.setForceClose(false);
	    		}
	    	}
	    }
	    
	    public void flushBuilding(Building b){
	    	for (Person p: persons){
	    		if (p.getBuilding() == b){
	    			p.mainRole.workOver();
	    			p.msgGoHome();
	    		}
	    	}
	    }
	    
	    private void flushAllPersonActions(){
	    	for (Person p: persons){
	    		p.actions.clear();
	    	}
	    }
	    public int getTime() {
			return hour; 	
	    }
	    
	    public void HardReset(){
	    	for (Person p: persons){
	    		p.stopThread();
	    	}
	    	persons.clear();
	    }
	    
}
