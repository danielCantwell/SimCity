package SimCity.Base;

import housing.roles.OwnerRole;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import market.MarketCustomerRole;
import market.MarketManagerRole;
import restaurant.DannyCashier;
import restaurant.DannyCook;
import restaurant.DannyCustomer;
import restaurant.DannyHost;
import restaurant.DannyWaiter;
import timRest.TimCashierRole;
import timRest.TimCookRole;
import timRest.TimHostRole;
import timRest.TimWaiterRole;
import Bank.bankCustomerRole;
import Bank.bankManagerRole;
import SimCity.Base.Person.TimeState;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_House;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_DannyRestaurant;
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
	    public int day;
	    public int hour;
	    public boolean isWeekend = false;
	    Timer hourTimer;
	    public int hourOffset;
	    public ArrayList<Building> buildings = new ArrayList<Building>();
	    public ArrayList<Person> persons = new ArrayList<Person>();
	    
	    public void addPerson(Person p){ persons.add(p);}
	    public void removePerson(Person p){ persons.remove(p);}
	    public void addBuilding(Building j){ buildings.add(j);}
	    public void removeBuilding(Building j){ buildings.remove(j);}
	    public Building getBuilding(int id){
			for (Building b: simGui.buildingList){
				if (b.getID() == id){
					return b;
				}
			}
			System.out.println("Could not find building: " + id);
			//throw new Exception();
			
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
	    	if (bt == BuildingType.Bank){
	    		//Find a bank
	    		for(Building b : buildings){
	    			if (b instanceof B_Bank) return b;
	    		}
	    	}else
	    	if (bt == BuildingType.Market){
	    		//Find a bank
	    		for(Building b : buildings){
	    			if (b instanceof B_Market) return b;
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
	    	int r = (int)(Math.round((Math.random() * 4)));
	    	switch (r){
	    	case 0: return simGui.buildingList.get(6);
	    	case 1: return simGui.buildingList.get(7);
	    	case 2: return simGui.buildingList.get(9);
	    	case 3: return simGui.buildingList.get(10);
	    	case 4: return simGui.buildingList.get(11);
	    	default : return simGui.buildingList.get(6);
	    	}
	    }
	    
	    private boolean announcedTime = false;
	    
	    private God() {
	        if (INSTANCE != null) {
	            throw new IllegalStateException("Already instantiated");
	        }
	        //System.out.println("God Created");
	        //set God variables.
	        hour = 3;
	        hourOffset = 10000;
	        //Set the timer for day.
	        hourTimer = new Timer(hourOffset, new ActionListener() {
				   public void actionPerformed(ActionEvent e){
					   if (hour < 24){hour ++; announcedTime = false;} // hour increments everytime this timer fires.
					   
					   if (hour == 4 && !announcedTime){
						   
						   wakeUp();
					   }
					   
					   if (hour == 5 && !announcedTime){
						   flushAllPersonActions();
						   managersGoToWork();
					   }
					   
					   if (hour == 8 && !announcedTime){
						   restaurantPeopleGoWork();
					   }
					   
					   if (hour == 10 && !announcedTime){
						   goToWork();
					   }
					   
					   if (hour == 13 && !announcedTime){
						   fakeCustomersGoToWork();
					   }
					   
					   
					   if (hour == 18 && !announcedTime){
						   getOffWork();
					   }
					   
					   if (hour == 20 && !announcedTime){
						   goHome();
	        			}
					   
					   if (hour==24 && !announcedTime){
						   goToSleep();
						   flushAllPersonActions();
					   }
					   
					   if (hour >= 24) { //if the hour is 24 hours, then hours is reset back to zero
						   hour = 0; //and a day is added to the Date.
						   day ++ ;
						   //System.out.println ("Day: "+ day);
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
					   
					   if (day % 6 == 0 || day % 7 == 0 && !banksClosed){
						   //notifyBanksClosed();
						   isWeekend = true;
					   }
					   else {
						   if (banksClosed)
							   	//notifyBanksOpen();
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
	    	System.out.println ("Morning");
	    	for(Person p: persons){
	    		p.msgMorning();
	    	}
	    }
	    
	    public void fakeCustomersGoToWork(){
	    	announcedTime = true;
	    	for (Person p: persons){
	    		if (p.getTimeState() != TimeState.working){
	    			p.msgGoToWork();
	    		}
	    	}
	    }
	    
	    public void managersGoToWork(){
	    	announcedTime = true;
	    	System.out.println("managers going to work");
	    	for (Person p: persons){
	    		if (p.mainRole instanceof bankManagerRole || p.mainRole instanceof DannyHost || p.mainRole instanceof MarketManagerRole || p.mainRole instanceof TimHostRole){
	    			p.msgGoToWork();
	    		}else
	    		if (p.getMainRoleString().equals("brianRest.BrianHostRole")){
	    			p.msgGoToWork();
	    		}
	    		else 
	    		if (p.getMainRoleString().equals("EricRestaurant.EricHost")) {
	    			p.msgGoToWork();
	    		}
	    		else
	    		if (p.getMainRoleString().equals("jesseRest.JesseHost")) {
	    			p.msgGoToWork();
	    		}
	    	}
	    }
	    
	    public void goHome(){
	    	announcedTime = true;
	    	System.out.println("EVERYONE GO HOME");
	    	for(Person p: persons){
	    		p.msgGoHome();
	    	}
	    }
	    
	    public void goToSleep(){
	    	announcedTime = true;
	    	System.out.println("go to sleep now.");
	    	for (Person p: persons){
	    		p.msgSleep();
	    	}
	    }
	    
	    public void goToWork(){
	    	announcedTime = true;
	    	System.out.println("go to work now!");
	    	for (Person p: persons){
	    		if(p.getTimeState() != TimeState.working)
	    		if (!(p.mainRole instanceof bankCustomerRole) && !(p.mainRole instanceof DannyCustomer) && !(p.mainRole instanceof MarketCustomerRole)){
	    			p.msgGoToWork();
	    		}
	    	}
	    }
	    
	    public void restaurantPeopleGoWork(){
	    	announcedTime = false;
	    	System.out.println("Restaurant Work Time");
	    	for(Person p: persons){
	    		if (p.mainRole instanceof DannyHost || p.mainRole instanceof DannyWaiter || p.mainRole instanceof DannyCook || p.mainRole instanceof DannyCashier){
	    			p.msgGoToWork();
	    		}
	    		else if (p.mainRole instanceof TimHostRole || p.mainRole instanceof TimWaiterRole || p.mainRole instanceof TimCookRole || p.mainRole instanceof TimCashierRole){
                    p.msgGoToWork();
                }
	    		else if (p.getMainRoleString().equals("brianRest.BrianWaiterRole") || p.getMainRoleString().equals("brianRest.BrianCookRole") || p.getMainRoleString().equals("brianRest.BrianCashierRole")){
	    			p.msgGoToWork();
	    		}
	    		else if (p.getMainRoleString().equals("EricRestaurant.EricWaiter") || p.getMainRoleString().equals("EricRestaurant.EricCook") || p.getMainRoleString().equals("EricRestaurant.EricCashier")) {
	    			p.msgGoToWork();
	    		}
	    		else if (p.getMainRoleString().equals("jesseRest.JesseWaiter") || p.getMainRoleString().equals("jesseRest.JesseCook") || p.getMainRoleString().equals("jesseRest.JesseCashier")) {
	    			p.msgGoToWork();
	    		}
	    	}
	    }
	    
	    public void getOffWork(){
	    	announcedTime = true;
	    	System.out.println("its time to go off work");
	    	for (Person p: persons){
	    		p.msgWorkOver();
	    	}
	    }
	    
	    public boolean banksClosed = false;
	    private void notifyBanksClosed(){
	    	banksClosed = true;
	    	for (Building b: buildings){
	    		if (b.getTag().equals("B_Bank")){
	    			B_Bank bank = (B_Bank)b;
	    			bank.setOpen(false);
	    		}
	    	}
	    }
	    private void notifyBanksOpen(){
	    	banksClosed = false;
	    	for (Building b: buildings){
	    		if (b.getTag().equals("B_Bank")){
	    			B_Bank bank = (B_Bank)b;
	    			bank.setOpen(true);
	    		}
	    	}
	    }
	    
	    private void flushAllPersonActions(){
	    	for (Person p: persons){
	    		p.actions.clear();
	    	}
	    }
	    
}
