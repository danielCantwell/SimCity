package SimCity.Buildings;

import javax.swing.JPanel;

import market.MarketDeliveryPersonRole;
import timRest.TimHostRole;
import timRest.TimCookRole;
import timRest.TimCustomerRole;
import timRest.TimCashierRole;
import timRest.TimWaiterRole;
import timRest.gui.TimAnimationPanel;
import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.God.BuildingType;
import SimCity.Base.Person;
import SimCity.Base.Person.Intent;
import SimCity.Base.Role;
import SimCity.Globals.Money;
/**
 * @author Timothy So
 *
 */
public class B_TimRest extends Building{
	Money TRestMoney = new Money(700,0);
	public int TAccNum = 5000;
	
	public TimHostRole hostRole;
    public TimAnimationPanel panel;

    public void setManager(TimHostRole h){
        hostRole = h;
    }
    public TimHostRole getManager(){
        return hostRole;
    }
	
	public B_TimRest(int id, JPanel jp) {
		super(id, jp);
	}
	
	public B_TimRest(int id){
		
	}

	public B_TimRest(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_Restaurant";
		setOpen(areAllNeededRolesFilled());
	}
	
	@Override
	public String getManagerString() {
		return "timRest.TimHostRole";
	}

	@Override
	public String getCustomerString() {
		return "timRest.TimCustomerRole";
	}

	@Override
	public boolean areAllNeededRolesFilled() {
		return (hostRole != null && hostRole.isRestaurantReady());
	}

	@Override
	protected void fillNeededRoles(Person p, Role r) {
		
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
            if (newRole instanceof TimHostRole)
            {
                TimHostRole restaurantRole = (TimHostRole) person.mainRole;
                hostRole = restaurantRole;
                hostRole.setAcc(TAccNum);
                hostRole.setMoney(TRestMoney);
                panel.setHost(restaurantRole);
                panel.addGui(restaurantRole.getGui());
                person.msgEnterBuilding(this);
            }
            else if (hostRole != null)
            {
                if (newRole instanceof TimCookRole)
                {
                    TimCookRole restaurantRole = (TimCookRole) person.mainRole;
                    hostRole.setCook(restaurantRole);
                    //restaurantRole.setHost(hostRole);
                    //hostRole.addClerk(restaurantRole);
                    restaurantRole.setHost(hostRole);
                    B_Market m = (B_Market)God.Get().findBuildingOfType(BuildingType.Market);
                    panel.addGui(restaurantRole.getGui());
                    restaurantRole.addItemToInventory("Steak", 4, 2000);
                    restaurantRole.addItemToInventory("Chicken", 4, 3000);
                    restaurantRole.addItemToInventory("Salad", 4, 1000);
                    restaurantRole.addItemToInventory("Pizza", 4, 2000);
                    restaurantRole.addMarket(m.getManager());
                }
                if (newRole instanceof TimCashierRole)
                {
                    TimCashierRole restaurantRole = (TimCashierRole) person.mainRole;
                    hostRole.setCashier(restaurantRole);
                    //restaurantRole.setHost(hostRole);
                    //hostRole.addClerk(restaurantRole);
                    //panel.addGui(restaurantRole.getGui());
                    restaurantRole.setMoney(hostRole.getMoney());
                    restaurantRole.addItemToMenu("Steak", new Money(15, 99));
                    restaurantRole.addItemToMenu("Chicken", new Money(10, 99));
                    restaurantRole.addItemToMenu("Salad", new Money(5, 99));
                    restaurantRole.addItemToMenu("Pizza", new Money(7, 99));
                }
                if (newRole instanceof TimWaiterRole)
                {
                    TimWaiterRole restaurantRole = (TimWaiterRole) person.mainRole;
                    restaurantRole.setHost(hostRole);
                    hostRole.addWaiter(restaurantRole);
                    restaurantRole.addItemToMenu("Steak", new Money(15, 99));
                    restaurantRole.addItemToMenu("Chicken", new Money(10, 99));
                    restaurantRole.addItemToMenu("Salad", new Money(5, 99));
                    restaurantRole.addItemToMenu("Pizza", new Money(7, 99));
                    panel.addGui(restaurantRole.getGui());
                }
                if (newRole instanceof MarketDeliveryPersonRole)
                {
                    MarketDeliveryPersonRole restaurantRole = (MarketDeliveryPersonRole) person.mainRole;
                    if (getOpen())
                    {
                        restaurantRole.msgGuiArrivedAtDestination();
                    }
                    else
                    {
                        for (int i = 0; i < restaurantRole.orders.size(); i++)
                        {
                            if (restaurantRole.orders.get(i).getId() == getID())
                            {
                                restaurantRole.orders.get(i).setPending();
                                restaurantRole.msgGuiRestaurantClosed();
                            }
                        }
                    }
                }
                if (getOpen() && newRole instanceof TimCustomerRole)
                {
                    TimCustomerRole restaurantRole = null;
                    for (Role r : person.roles)
                    {
                        if (r instanceof TimCustomerRole)
                        {
                            restaurantRole = (TimCustomerRole) r;
                            restaurantRole.setHost(hostRole);
                            panel.addGui(restaurantRole.getGui());
                            restaurantRole.gotHungry();
                        }
                    }
                }
                person.msgEnterBuilding(this);
            }
            setOpen(areAllNeededRolesFilled());
            panel.repaint();
        } catch(Exception e){
            //e.printStackTrace();
            int goTo = (int)(person.getBuilding().getID() *2 / 3 - 1);
			if (goTo > God.Get().buildings.size() - 1){
				goTo = 0;
			}
			person.msgGoToBuilding(God.Get().getBuilding(goTo), Intent.work);
			person.msgExitBuilding();
            System.out.println ("God: no class found");
        }
    }

	@Override
	public void ExitBuilding(Person person) {
        for (int i = 0; i < person.roles.size(); i++)
        {
            if (person.roles.get(i) instanceof TimHostRole)
            {
                TimHostRole cRole = (TimHostRole)person.roles.get(i);
            	TRestMoney = cRole.getMoney();
                cRole.setActive(false);
                ((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof TimCookRole)
            {
                TimCookRole cRole = (TimCookRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof TimCashierRole)
            {
                TimCashierRole cRole = (TimCashierRole)person.roles.get(i);
                cRole.setActive(false);
                //((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof TimWaiterRole)
            {
                TimWaiterRole cRole = (TimWaiterRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof MarketDeliveryPersonRole)
            {
                MarketDeliveryPersonRole cRole = (MarketDeliveryPersonRole)person.roles.get(i);
                cRole.setActive(false);
                //((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
            if (person.roles.get(i) instanceof TimCustomerRole)
            {
                TimCustomerRole cRole = (TimCustomerRole)person.roles.get(i);
                cRole.setActive(false);
                ((B_TimRest) person.building).panel.removeGui(cRole.getGui());
                person.msgExitBuilding();
            }
        }
	}
	@Override
	public Role getManagerRole() {
		return hostRole;
	}
	

}