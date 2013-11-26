package SimCity.Buildings;

import javax.swing.JPanel;

import market.MarketManagerRole;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
/**
 * @author Brian
 *
 */
public class B_Market extends Building{
	
	private MarketManagerRole managerRole;
	
	public B_Market(int id, JPanel jp) {
		super(id, jp);
		// TODO Auto-generated constructor stub
	}
	
	public B_Market(int id){
		
	}

	public B_Market(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_Market";
	}
	
	public void setManager(MarketManagerRole m){
		managerRole = m;
	}
	public MarketManagerRole getManager(){
		return managerRole;
	}
	
	@Override
	public String getManagerString() {
		return "market.MarketManagerRole";
	}

	@Override
	public String getCustomerString() {
		return "market.MarketCustomerRole";
	}

	@Override
	public boolean areAllNeededRolesFilled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void fillNeededRoles(Person p, Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ExitBuilding(Person person) {
		// TODO Auto-generated method stub
		
	}
	

}