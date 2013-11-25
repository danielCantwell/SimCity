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
	
	public void setOwner(OwnerRole o){
		owner = o;
	}
	public OwnerRole getOwner(){
		return owner;
	}
	
	public B_House(int id, JPanel jp) {
		super(id, jp);
		tag = "B_House";
	}
	
	public B_House(JPanel jp){
		
	}
	
	public B_House(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_House";
	}

	@Override
	public String getManagerString() {
		// TODO Auto-generated method stub
		return "housing.roles.OwnerRole";
	}

	@Override
	public String getCustomerString() {
		// TODO Auto-generated method stub
		return "housing.roles.TenantRole";
	}

	

}