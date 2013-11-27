package SimCity.Buildings;

import javax.swing.JPanel;

import market.MarketCustomerRole;
import market.MarketManagerRole;
import market.gui.MarketAnimationPanel;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
/**
 * @author Brian
 *
 */
public class B_Market extends Building{
	
	private MarketManagerRole managerRole;
    public MarketAnimationPanel panel;

    public void setManager(MarketManagerRole m){
        managerRole = m;
    }
    public MarketManagerRole getManager(){
        return managerRole;
    }
	
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
		return managerRole.isRestaurantReady();
	}

	@Override
	protected void fillNeededRoles(Person p, Role r) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void EnterBuilding(Person person, String job){
        Role newRole = null;
        try {
            newRole = (Role)Class.forName(job).newInstance();
            newRole.setActive(true);
            newRole.setPerson(person);
            person.msgCreateRole(newRole, true);
            fillNeededRoles(person, newRole);
            if (newRole instanceof MarketCustomerRole)
            {
                MarketCustomerRole marketRole = (MarketCustomerRole) newRole;
                marketRole.setManager(managerRole);
                panel.addGui(marketRole.getGui());
            }
            person.msgEnterBuilding(this);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println ("God: no class found");
        }
    }

	@Override
	public void ExitBuilding(Person person) {
		// TODO Auto-generated method stub
		
	}
	

}