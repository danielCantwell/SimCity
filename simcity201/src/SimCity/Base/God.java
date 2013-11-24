package SimCity.Base;

import housing.roles.OwnerRole;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_Restaurant;
import exterior.gui.AnimationPanel;
/**
 * @author Brian
 *
 */
public class God {

	    private static final God INSTANCE = new God();
	    
	    //Gui stuff accessible by everyone
	    AnimationPanel animationPanel;
	    
	    public void setAnimationPanel(AnimationPanel anim){ animationPanel = anim;}
	    public AnimationPanel getAnimationPanel(AnimationPanel anim) { return animationPanel;}
	    
	    //List of God thingies
	    int day;
	    int hour;
	    boolean isWeekend = false;
	    Timer hourTimer;
	    int hourOffset;
	    ArrayList<Building> buildings = new ArrayList<Building>();
	    ArrayList<Person> persons = new ArrayList<Person>();
	    
	    public void addPerson(Person p){ persons.add(p);}
	    public void removePerson(Person p){ persons.remove(p);}
	    public void addBuilding(Building j){ buildings.add(j);}
	    public void removeBuilding(Building j){ buildings.remove(j);}
	    public Building getBuilding(int id){
			return buildings.get(id);
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
	    public void assignID(SimObject s){
	    	int newID = 0;
	    	while(true){
				try {
					persons.get(newID);
					newID ++;
				}
				catch(Exception e){
					s.id = newID;
					break;
				}
	    	}
	    }
	    
	    public Building findRandomRestaurant(){
	    	while (true){
		    	Random rndnum = new Random (5);
		    	int random = rndnum.nextInt();
		    	if (buildings.get(random) instanceof B_Restaurant){
		    		return buildings.get(random);
		    	}
	    	}
	    }
	    
	    private God() {
	        if (INSTANCE != null) {
	            throw new IllegalStateException("Already instantiated");
	        }
	        System.out.println("God Created");
	        //set God variables.
	        hour = 0;
	        hourOffset = 1000;
	        //Set the timer for day.
	        hourTimer = new Timer(hourOffset, new ActionListener() {
				   public void actionPerformed(ActionEvent e){
					   if (hour < 24){hour ++;} // hour increments everytime this timer fires.
					   
					   if (hour == 6){
						   wakeUp();
					   }
					   
					   if (hour == 5){
						   getOffWork();
					   }
					   
					   if (hour >= 24) { //if the hour is 24 hours, then hours is reset back to zero
						   hour = 0; //and a day is added to the Date.
						   day ++ ;
						   System.out.println ("Day: "+ day);
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
					   
					   if (day % 6 == 0 || day % 7 == 0){
						   //notifyBankClosed();
						   isWeekend = true;
					   }
					   else {
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
	    	for(Person p: persons){
	    		p.msgMorning();
	    	}
	    }
	    
	    public void getOffWork(){
	    	for (Person p: persons){
	    		p.msgWorkOver();
	    	}
	    }
	    
	    
	    //BUILDING MEDIATOR STUFF
	    public void EnterBuilding(Building building, Person person, String job){
	    		Role newRole;
				try {
					newRole = (Role)Class.forName(job).newInstance();
					newRole.setActive(true);
					newRole.setPerson(person);
					person.msgCreateRole(newRole, true);
				} catch(Exception e){
					e.printStackTrace();
					System.out.println ("God: no class found");
				}
				person.msgEnterBuilding(building);
	    }
	    
	    public void ExitBuilding(Person person){
	    	person.resetActiveRoles();
	    	person.msgExitBuilding();
	    }
	    
}
