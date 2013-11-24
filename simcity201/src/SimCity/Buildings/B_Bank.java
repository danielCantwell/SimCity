package SimCity.Buildings;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import Bank.bankGuardRole;
import Bank.bankManagerRole;
import Bank.bankManagerRole.Teller;
import Bank.tellerRole;
import SimCity.Base.Building;
/**
 * @author Brian
 *
 */
public class B_Bank extends Building{
	
	bankManagerRole bankManager;
	bankGuardRole bankGuard;
	
	List<tellerRole> tellers = new ArrayList<tellerRole>();
	
	public bankManagerRole getBankManager(){return bankManager;}
	public bankGuardRole getBankGuard(){return bankGuard;}
	public List<tellerRole> getTellers(){return tellers;}
	
	public void setBankManager(bankManagerRole bmr){bankManager = bmr;}
	public void setBankGuard(bankGuardRole bgr){bankGuard = bgr;}
	public void addTeller(tellerRole t){ tellers.add(t);}
	public tellerRole getTeller(int id){ return tellers.get(id);}
	
	

	public B_Bank(int id, JPanel jp) {
		super(id, jp);
		// TODO Auto-generated constructor stub
	}
	
	public B_Bank(int id){
		
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