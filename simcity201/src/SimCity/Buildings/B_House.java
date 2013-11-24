package SimCity.Buildings;

import housing.roles.OwnerRole;

import javax.swing.JPanel;

import SimCity.Base.Building;
/**
 * @author Brian
 *
 */
public class B_House extends Building{

	OwnerRole owner;
	
	public B_House(int id, JPanel jp) {
		super(id, jp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getManagerString() {
		return "House.OwnerRole";
	}

	@Override
	public String getCustomerString() {
		return "House.TenantRole";
	}

}