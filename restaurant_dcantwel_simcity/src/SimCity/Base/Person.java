/**
 * 
 */
package SimCity.Base;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

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
	private String job;
	private enum Vehicle {car, delivery, walk, bus};
	private Vehicle vehicle = Vehicle.walk;
	
	private Gui gui;
	private Semaphore animation = new Semaphore(0, true);
	
	private ArrayList<Role> roles = new ArrayList<Role>();
	
	private ArrayList<String> inventory = new ArrayList<String>();
	
	private enum Morality {good, crook};
	private enum PersonState {walking,
							  goingToSleep,
							  sleeping,
							  idle,
							  working,
							  workOver
	};
	
	
	//Stats
		private int hungerLevel = 5;
		private Money money = 10;
		private Building building = null;
		private Building destination = null;
		private Morality mor = Morality.good;
		private PersonState ps = PersonState.idle;
		
	//Messages
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
		roles.add(r);
		stateChanged();
		return;
	}
	public void msgSleep(){
		ps = PersonState.goingToSleep;
		stateChanged();
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
		
		//ifperson not doing anything
		if (ps == PersonState.idle){
			//check if he has enough money.
			if (money.dollars < 5){
				goToBank();
				return false;
			}
			
			//Check if he is hungry
			if (hungerLevel < 3){
				goToRestaurant();
				return false;
			}
			
		}
	}
	
	
	//Actions
	private void workOver() {
		for (Role r: roles){
			r.workOver();
		}
	}
	private void goToSleep() {
		// TODO Auto-generated method stub
		
	}
	private void goToBank() {
		// TODO Auto-generated method stub
		
	}
	private void goToRestaurant() {
		// TODO Auto-generated method stub
		
	}
	
	//Utility
	
}

