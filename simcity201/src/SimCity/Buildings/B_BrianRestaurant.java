package SimCity.Buildings;

import javax.swing.JPanel;

import com.sun.xml.internal.org.jvnet.fastinfoset.RestrictedAlphabet;

import market.MarketDeliveryPersonRole;
import brianRest.BrianCashierRole;
import brianRest.BrianCookRole;
import brianRest.BrianCustomerRole;
import brianRest.BrianHostRole;
import brianRest.BrianPCWaiterRole;
import brianRest.BrianWaiterRole;
import brianRest.OrderStand;
import brianRest.gui.BrianAnimationPanel;
import brianRest.gui.BrianRestaurantPanel;
import brianRest.interfaces.BrianCashier;
import brianRest.interfaces.BrianHost;
import restaurant.DannyCashier;
import restaurant.DannyCustomer;
import restaurant.DannyHost;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Globals.Money;
/**
 * @author Brian
 *
 */
public class B_BrianRestaurant extends Building{
	public int B_BrianAccNum = 3000;
	Money BRestMoney = new Money(700,0);
	public BrianHostRole hostRole = new BrianHostRole("Host");
	public BrianCashierRole cashierRole = new BrianCashierRole("Cashier");
	public BrianCookRole cookRole = new BrianCookRole("Cook");
	public int numberOfWaiters = 0;
	
	public OrderStand orderstand;
	public OrderStand getOrderStand(){return orderstand;}
	
	public boolean hostFilled = false, cashierFilled = false, cookFilled = false;

	public B_BrianRestaurant(int id, JPanel jp) {
		super(id, jp);
		
	}
	
	public B_BrianRestaurant(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		BrianRestaurantPanel brp = (BrianRestaurantPanel)jp;
		BrianAnimationPanel bap = (BrianAnimationPanel)brp.bap;
		bap.setRestaurant(this);
		x = xCoord;
		y = yCoord;
		tag = "B_Restaurant";
		orderstand = new OrderStand(this, cookRole);
	}

	@Override
	public String getManagerString() {
		return "brianRest.BrianCustomerRole";
	}
	
	public BrianCashierRole getCashier(){return cashierRole;}

	@Override
	public String getCustomerString() {
		// TODO Auto-generated method stub
		return "brianRest.BrianCustomerRole";
	}

	@Override
	public boolean areAllNeededRolesFilled() {
		// TODO Auto-generated method stub
		return hostFilled && cashierFilled && cookFilled && numberOfWaiters > 0;
	}

	@Override
	protected void fillNeededRoles(Person p, Role r) {
			//Should be handled by the roles themselves right now.
	}

	@Override
	public void ExitBuilding(Person person) {
		if(person.getMainRoleString().equals("brianRest.BrianHostRole")){
			BRestMoney = hostRole.getMoney();
		}
		person.resetActiveRoles();
    	person.msgExitBuilding();
	}
	
	@Override
	public void EnterBuilding(Person person, String job){
		Role newRole = null;
		try {
			if (job.equals("brianRest.BrianCustomerRole")) {
				newRole = new BrianCustomerRole("Customer");  
				System.out.println("#cust created");}
			else if (job.equals("brianRest.BrianHostRole")) {
				newRole = hostRole; 
				hostRole.setMoney(BRestMoney);
				hostRole.setAccNum(B_BrianAccNum);
				hostFilled = true;
				setOpen(areAllNeededRolesFilled());}
			else if (job.equals("brianRest.BrianWaiterRole")){ 
				numberOfWaiters++;
				newRole = new BrianWaiterRole("Waiter", hostRole, cookRole, cashierRole, numberOfWaiters);
				newRole.setPerson(person);
				hostRole.addWaiter((BrianWaiterRole)newRole);
				setOpen(areAllNeededRolesFilled());}
			else if (job.equals("brianRest.BrianCookRole")) { 
				newRole = cookRole; 
				cookFilled = true;
				setOpen(areAllNeededRolesFilled());}
			else if (job.equals("brianRest.BrianCashierRole")) { 
				newRole = cashierRole; 
				cashierRole.setMoney(hostRole.getMoney());
				cashierFilled = true;
				setOpen(areAllNeededRolesFilled());}
			else  if (job.equals("brianRest.BrianPCWaiterRole")){ 
				numberOfWaiters++;
				newRole = new BrianPCWaiterRole("PCWaiter", hostRole, cookRole, cashierRole, numberOfWaiters);
				newRole.setPerson(person);
				hostRole.addWaiter((BrianPCWaiterRole)newRole);
				setOpen(areAllNeededRolesFilled());}
			else if (person.mainRole instanceof MarketDeliveryPersonRole)
            {
                MarketDeliveryPersonRole restaurantRole = (MarketDeliveryPersonRole) person.mainRole;
                restaurantRole.setActive(true);
                restaurantRole.setPerson(person);
                restaurantRole.msgGuiArrivedAtDestination();
                person.msgEnterBuilding(this);
                return;
            }
			newRole.setActive(true);
			newRole.setPerson(person);
			person.msgCreateRole(newRole, true);
			person.msgEnterBuilding(this);
		} catch(Exception e){
			e.printStackTrace();
			System.out.println ("Building: no class found");
		}
}

	@Override
	public Role getManagerRole() {
		// TODO Auto-generated method stub
		return hostRole;
	}



}