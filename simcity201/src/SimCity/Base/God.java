package SimCity.Base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Timer;

/**
 * @author Brian Chen
 *
 */
public class God {

	    private static final God INSTANCE = new God();
	    
	    //List of God thingies
	    int day;
	    int hour;
	    Timer hourTimer;
	    int hourOffset;
	    HashMap<Integer, SimObject> simObjects; //may remove bc redundant
	    ArrayList<Building> buildings = new ArrayList<Building>();
	    ArrayList<Person> persons = new ArrayList<Person>();
	    
	    
	    public int getDay(){ return day;}
	    public int getHour(){return hour;}
	    public SimObject Find(int id){
			return simObjects.get(id);
	    }
	    public void assignID(SimObject s){
	    	int newID = 0;
	    	while(true){
				try {
					simObjects.get(newID);
					newID ++;
				}
				catch(Exception e){
					s.id = newID;
					break;
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
	        simObjects = new HashMap<Integer, SimObject>();
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
				   }
	        });
	        hourTimer.start();
	    }

	    public static synchronized God Get() {
	        return INSTANCE;
	    }
	    
	    public void wakeUp(){
	    	
	    }
	    
	    public void getOffWork(){
	    	
	    }
	    
	    
	    //BUILDING MEDIATOR STUFF
	    public void EnterBuilding(Building building, Person person){
	    	building.canAdd(person.getJob());
	    }
	    
}
