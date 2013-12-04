package SimCity.Buildings;

import javax.swing.JPanel;

import timRest.TimHostRole;
import timRest.TimCookRole;
import timRest.TimCustomerRole;
import timRest.TimCashierRole;
import timRest.TimWaiterRole;
import timRest.gui.TimAnimationPanel;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
/**
 * @author Timothy So
 *
 */
public class B_TimRest extends Building{
	
	private TimHostRole hostRole;
    public TimAnimationPanel panel;

    public void setManager(TimHostRole h){
        hostRole = h;
    }
    public TimHostRole getManager(){
        return hostRole;
    }
	
	public B_TimRest(int id, JPanel jp) {
		super(id, jp);
		// TODO Auto-generated constructor stub
	}
	
	public B_TimRest(int id){
		
	}

	public B_TimRest(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_Market";
		setOpen(areAllNeededRolesFilled());
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
		return (hostRole != null && hostRole.isRestaurantReady());
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
            /*if (newRole instanceof TimHostRole)
            {
                TimHostRole marketRole = (TimHostRole) person.mainRole;
                hostRole = marketRole;
                panel.manager = person;
                panel.addGui(marketRole.getGui());
            }
            if (newRole instanceof MarketClerkRole)
            {
                MarketClerkRole marketRole = (MarketClerkRole) person.mainRole;
                marketRole.setManager(hostRole);
                hostRole.addClerk(marketRole);
                panel.addGui(marketRole.getGui());
            }
            if (newRole instanceof MarketPackerRole)
            {
                MarketPackerRole marketRole = (MarketPackerRole) person.mainRole;
                marketRole.setManager(hostRole);
                hostRole.addPacker(marketRole);
                panel.addGui(marketRole.getGui());
            }
            if (newRole instanceof MarketCustomerRole)
            {
                MarketCustomerRole marketRole = null;
                for (Role r : person.roles)
                {
                    if (r instanceof MarketCustomerRole)
                    {
                        marketRole = (MarketCustomerRole) r;
                        marketRole.setManager(hostRole);
                        panel.addGui(marketRole.getGui());
                    }
                }
            }*/
            person.msgEnterBuilding(this);
            setOpen(areAllNeededRolesFilled());
        } catch(Exception e){
            e.printStackTrace();
            System.out.println ("God: no class found");
        }
    }

	@Override
	public void ExitBuilding(Person person) {
        for (int i = 0; i < person.roles.size(); i++)
        {
            /*if (person.roles.get(i) instanceof TimHostRole)
            {
                TimHostRole cRole = (TimHostRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof MarketClerkRole)
            {
                MarketClerkRole cRole = (MarketClerkRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof MarketPackerRole)
            {
                MarketPackerRole cRole = (MarketPackerRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof MarketCustomerRole)
            {
                MarketCustomerRole cRole = (MarketCustomerRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }*/
        }
	}
	

}