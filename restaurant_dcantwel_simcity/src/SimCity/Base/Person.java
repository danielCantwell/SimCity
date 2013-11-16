/**
 * 
 */
package SimCity.Base;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

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
		private Money money;
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
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
}

