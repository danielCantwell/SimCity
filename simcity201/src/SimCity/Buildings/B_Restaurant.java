package SimCity.Buildings;

import javax.swing.JPanel;

import SimCity.Base.Building;
/**
 * @author Brian
 *
 */
public class B_Restaurant extends Building{

	public B_Restaurant(int id, JPanel jp) {
		super(id, jp);
		// TODO Auto-generated constructor stub
	}
	
	public B_Restaurant(JPanel jp, int xCoord, int yCoord){
		super();
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_Restaurant";
	}

	@Override
	public String getManagerString() {
		return null;
	}

	@Override
	public String getCustomerString() {
		// TODO Auto-generated method stub
		return null;
	}



}