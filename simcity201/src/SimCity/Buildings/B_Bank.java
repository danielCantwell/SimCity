package SimCity.Buildings;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import Bank.bankManagerRole.Teller;
import Bank.interfaces.Guard;
import Bank.interfaces.Manager;
import Bank.tellerRole;
import SimCity.Base.Building;
/**
 * @author Brian
 *
 */
public class B_Bank extends Building{
	
	Manager bankManager;
	Guard bankGuard;
	
	List<tellerRole> tellers = new ArrayList<tellerRole>();
	
	public Manager getBankManager(){return bankManager;}
	public Guard getBankGuard(){return bankGuard;}
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
	public B_Bank(JPanel jp, int xCoord, int yCoord){
		super();
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

	

}