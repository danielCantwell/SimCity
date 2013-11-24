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
public class B_Market extends Building{
	
	public B_Market(int id, JPanel jp) {
		super(id, jp);
		// TODO Auto-generated constructor stub
	}
	
	public B_Market(int id){
		
	}

	@Override
	public String getManagerString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomerString() {
		// TODO Auto-generated method stub
		return null;
	}
	

}