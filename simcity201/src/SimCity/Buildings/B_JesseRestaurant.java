package SimCity.Buildings;
import javax.swing.JPanel;

import jesseRest.*;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Globals.Money;
/***
 * 
 * @author Eric
 *
 */
public class B_JesseRestaurant extends Building{
	public int B_JesseAccNum = 2000;
	public Money JRestMoney = new Money(700,0);
	public JesseHost host = new JesseHost("JHost");
	public JesseCashier cashier = new JesseCashier("JCashier");
	public JesseCook cook = new JesseCook("JCook");
	int numWaiter = 0;
	
	public boolean hostFilled = false, cashierFilled = false, cookFilled = false;

	
	public B_JesseRestaurant(int id, JPanel jp) {
		super(id, jp);
		// TODO Auto-generated constructor stub
	}
	
	public B_JesseRestaurant(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_JesseRestaurant";
	}
	
	public void EnterBuilding(Person person, String role) {
		Role newRole = null;
		try {
			if(role.equals("jesseRest.JesseHost")) { 
				newRole = host;
				host.setMoney(JRestMoney);
				host.setAcc(B_JesseAccNum);
				hostFilled = true;
				setOpen(areAllNeededRolesFilled());
				}
			else if(role.equals("jesseRest.JesseWaiter")) {
				if(hostFilled) {
				newRole = new JesseWaiter("JWaiter");
				numWaiter++;
				host.addWaiter((JesseWaiter) newRole);
				((JesseWaiter) newRole).setCook(cook);
				((JesseWaiter) newRole).setCashier(cashier);
				((JesseWaiter) newRole).setHost(host);
				setOpen(areAllNeededRolesFilled());
				} else { ExitBuilding(person); person.msgGoHome(); return; }
			}
			else if(role.equals("jesseRest.JesseCook")) { 
				if(hostFilled) {
				newRole = cook;
				cookFilled = true;
				jesseRest.gui.AnimationPanel ap = (jesseRest.gui.AnimationPanel)person.building.getPanel();
				((JesseCook) newRole).setAnimationPanel(ap);
				setOpen(areAllNeededRolesFilled());
				} else { ExitBuilding(person); person.msgGoHome(); return; }
			}
			else if(role.equals("jesseRest.JesseCashier")) {
				if(hostFilled) {
				newRole = cashier;
				cashier.setMoney(host.getMoney());
				cashierFilled = true;
				setOpen(areAllNeededRolesFilled());
				} else { ExitBuilding(person); person.msgGoHome(); return; }
			}
			else if(role.equals("jesseRest.JesseCustomer")) {
				if(areAllNeededRolesFilled()) {
				newRole = new JesseCustomer("JCustomer");
				System.out.println("JCustomer Made");
				}
				else {
					setOpen(false);
					System.out.println("Eric Restaurant is closed, leaving...");
					ExitBuilding(person);person.msgGoHome(); return;
				}
			}
			newRole.setActive(true);
			newRole.setPerson(person);
			person.msgCreateRole(newRole, true);
			fillNeededRoles(person, newRole);
			person.msgEnterBuilding(this);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println ("Building: no class found");
			System.out.println("Checking is newRole actually exists : "+newRole);
		}
	}
	


	@Override
	public boolean areAllNeededRolesFilled() {
		System.out.println("Jesse Restaurant Roles Filled? Host: "+hostFilled+"  Cook: "+cookFilled+"  Cashier: "+cashierFilled+"  Waiters: "+numWaiter);
		return hostFilled && cashierFilled && cookFilled && numWaiter > 0;
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

	@Override
	protected void fillNeededRoles(Person p, Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ExitBuilding(Person person) {
		System.out.println(person.getMainRoleString().toString());
		if(person.getMainRoleString().equals("jesseRest.JesseHost")){
			JRestMoney = host.getMoney();
			hostFilled = false;
		}
		if(person.getMainRoleString().equals("jesseRest.JesseWaiter")) numWaiter--;
		if(person.getMainRoleString().equals("jesseRest.JesseCashier")) cashierFilled = false;
		if(person.getMainRoleString().equals("jesseRest.JesseCook")) cookFilled = false;
		person.resetActiveRoles();
    	person.msgExitBuilding();		

	}

	@Override
	public Role getManagerRole() {
		// TODO Auto-generated method stub
		return host;
	}

}
