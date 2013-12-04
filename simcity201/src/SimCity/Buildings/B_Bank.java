package SimCity.Buildings;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import Bank.bankGuardRole;
import Bank.bankManagerRole;
import Bank.bankManagerRole.Teller;
import Bank.interfaces.Guard;
import Bank.interfaces.Manager;
import Bank.tellerRole;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
/**
 * @author Brian
 *
 */
public class B_Bank extends Building{
	
	Manager bankManager;
	Guard bankGuard;
	
	Person manager = null;
	Person guard = null;
	
	List<tellerRole> tellers = new ArrayList<tellerRole>();
	
	public Manager getBankManager(){return bankManager;}
	public Guard getBankGuard(){return bankGuard;}
	public Person getGuard() {return guard;}
	public List<tellerRole> getTellers(){return tellers;}
	
	public void setBankManager(Manager bmr){bankManager = bmr;}
	public void setBankGuard(Guard bgr){bankGuard = bgr;}
	public void addTeller(tellerRole t){ tellers.add(t);}
	public Bank.interfaces.Teller getTeller(int id){ return tellers.get(id);}
	
	public B_Bank(int id){
		super(id);
		tag = "B_Bank";
	}

	public B_Bank(int id, JPanel jp) {
		super(id, jp);
		tag = "B_Bank";
	}
	public B_Bank(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_Bank";
	}


	@Override
	public String getManagerString() {
		return "Bank.bankManagerRole";
	}
	@Override
	public String getCustomerString() {
		return "Bank.bankCustomerRole";
	}
	@Override
	public boolean areAllNeededRolesFilled() {
		//System.out.println("manager: " + manager + " guard: " + bankGuard);
		return manager != null  && guard != null;
	}
	@Override
	public void ExitBuilding(Person person) {
		if (person == bankGuard) manager =null;
		else if (person == bankManager) guard = null;
    	person.resetActiveRoles();
    	person.msgExitBuilding();
	}
	@Override
	protected void fillNeededRoles(Person p, Role r) {
		if (r instanceof bankManagerRole){
			manager = r.myPerson;
			bankManager = (bankManagerRole)r;
		}
		else if (r instanceof bankGuardRole){
			guard = r.myPerson;
			bankGuard = (bankGuardRole)r;
			System.out.println("This is guard: " + r);
		}
		
	}

}