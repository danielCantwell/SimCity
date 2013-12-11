package SimCity.Buildings;

import javax.swing.JPanel;

import market.MarketClerkRole;
import market.MarketCustomerRole;
import market.MarketDeliveryPersonRole;
import market.MarketManagerRole;
import market.MarketPackerRole;
import market.gui.MarketAnimationPanel;
import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.God.BuildingType;
import SimCity.Base.Person.Intent;
import SimCity.Base.Role;
import SimCity.trace.AlertLog;
import SimCity.trace.AlertTag;
/**
 * @author Brian
 *         Timothy So
 *
 */
public class B_Market extends Building{
	
	private MarketManagerRole managerRole;
    public MarketAnimationPanel panel;
    

    private boolean isOpen = true;

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
		return (isOpen && managerRole != null && managerRole.isRestaurantReady());
	}

	@Override
	protected void fillNeededRoles(Person p, Role r) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void EnterBuilding(Person person, String job){
        Role newRole = null;
        try {
        	if (forceClose){
				person.msgGoHome();
				return;
			}
        	
        	if (job.equals("usto")){
				person.msgGoToBuilding(God.Get().findBuildingOfType(BuildingType.Restaurant), Intent.work);
					return;
			}
        	
            newRole = (Role)Class.forName(job).newInstance();
            newRole.setActive(true);
            newRole.setPerson(person);
            person.msgCreateRole(newRole, true);
            fillNeededRoles(person, newRole);
            if (newRole instanceof MarketManagerRole)
            {
                MarketManagerRole marketRole = (MarketManagerRole) person.mainRole;
                managerRole = marketRole;
                panel.manager = person;
                panel.addGui(marketRole.getGui());
                
                isOpen = true;
                
                AlertLog.getInstance().logInfo(AlertTag.Market, person.getName(), "Manager entering Market.");
                person.msgEnterBuilding(this);
                panel.repaint();
                setOpen(areAllNeededRolesFilled());
            }
            else if (managerRole != null)
            {
                if (newRole instanceof MarketClerkRole)
                {
                    MarketClerkRole marketRole = (MarketClerkRole) person.mainRole;
                    if (marketRole.manager == null)
                    {
                        marketRole.setManager(managerRole);
                        managerRole.addClerk(marketRole);
                    }
                    panel.addGui(marketRole.getGui());
                }
                else if (newRole instanceof MarketPackerRole)
                {
                    MarketPackerRole marketRole = (MarketPackerRole) person.mainRole;
                    if (marketRole.manager == null)
                    {
                        marketRole.setManager(managerRole);
                        managerRole.addPacker(marketRole);
                    }
                    panel.addGui(marketRole.getGui());
                }
                else if (newRole instanceof MarketDeliveryPersonRole)
                {
                    MarketDeliveryPersonRole marketRole = (MarketDeliveryPersonRole) person.mainRole;
                    if (marketRole.manager == null)
                    {
                        marketRole.setManager(managerRole);
                        managerRole.addDeliveryPerson(marketRole);
                        marketRole.setHomeMarket(this);
                    }
                    panel.addGui(marketRole.getGui());
                    marketRole.msgGuiArrivedAtMarket();
                }
                else if (newRole instanceof MarketCustomerRole)
                {
                    if (getOpen())
                    {
                        MarketCustomerRole marketRole = null;
                        for (Role r : person.roles)
                        {
                            if (r instanceof MarketCustomerRole)
                            {
                                marketRole = (MarketCustomerRole) r;
                                marketRole.setManager(managerRole);
                                panel.addGui(marketRole.getGui());
                            }
                        }
                    }
                    else
                    {
                        AlertLog.getInstance().logWarning(AlertTag.Market, person.getName(), "Market Closed.");
                        return;
                    }
                }
                else
                {
                    AlertLog.getInstance().logError(AlertTag.Market, person.getName(), "Wrong class entering market.");
                    return;
                }
                AlertLog.getInstance().logInfo(AlertTag.Market, person.getName(), "Entering Market.");
                person.msgEnterBuilding(this);
                panel.repaint();
            }
            else
            {
                AlertLog.getInstance().logError(AlertTag.Market, person.getName(), "MarketManager = null");
            }
            setOpen(areAllNeededRolesFilled());
            if (getOpen())
            {
                AlertLog.getInstance().logInfo(AlertTag.Market, person.getName(), "Market Open.");
            }
            else
            {
                AlertLog.getInstance().logInfo(AlertTag.Market, person.getName(), "Market Close.");
            }
        } catch(Exception e){
           // e.printStackTrace();
           
			person.msgGoToBuilding(God.Get().findBuildingOfType(BuildingType.Restaurant), Intent.work);
			ExitBuilding(person);

            //System.out.println ("God: no class found");
        }
    }

	@Override
	public void ExitBuilding(Person person) {
        for (int i = 0; i < person.roles.size(); i++)
        {
            if (person.roles.get(i) instanceof MarketManagerRole)
            {
                MarketManagerRole cRole = (MarketManagerRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_Market) person.building).panel.removeGui(cRole.getGui());
                if (managerRole == cRole)
                {
                    isOpen = false;
                }
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof MarketClerkRole)
            {
                MarketClerkRole cRole = (MarketClerkRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_Market) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof MarketPackerRole)
            {
                MarketPackerRole cRole = (MarketPackerRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_Market) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof MarketDeliveryPersonRole)
            {
                MarketDeliveryPersonRole cRole = (MarketDeliveryPersonRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_Market) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof MarketCustomerRole)
            {
                MarketCustomerRole cRole = (MarketCustomerRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_Market) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
        }
	}
	@Override
	public Role getManagerRole() {
		// TODO Auto-generated method stub
		return managerRole;
	}
	

}