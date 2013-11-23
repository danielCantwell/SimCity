package SimCity.Base;

import housing.roles.OwnerRole;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import restaurant.gui.DannyRestaurantAnimationPanel;
import exterior.gui.AnimationPanel;

/**
 * @author Brian Chen
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
	    ArrayList<JPanel> buildings = new ArrayList<JPanel>();
	    ArrayList<Person> persons = new ArrayList<Person>();
	    
	    public void addPerson(Person p){ persons.add(p);}
	    public void removePerson(Person p){ persons.remove(p);}
	    public void addBuilding(JPanel j){ buildings.add(j);}
	    public void removeBuildign(JPanel j){ buildings.remove(j);}
	    
	    
	    public int getDay(){ return day;}
	    public int getHour(){return hour;}
	    public Person Find(int id){
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
	    
	    public JPanel findRandomRestaurant(){
	    	while (true){
		    	Random rndnum = new Random (5);
		    	int random = rndnum.nextInt();
		    	if (buildings.get(random) instanceof DannyRestaurantAnimationPanel){
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
					person.msgCreateRole(newRole);
				} catch(Exception e){
					System.out.println ("no class found");
				}
	    }
	    
	    public void ExitBuilding(Person person){
	    	person.resetActiveRoles();
	    }
	    
}
