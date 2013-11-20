package SimCity;
import javax.swing.JFrame;

import SimCity.Base.God;
import restaurant.gui.RestaurantGui;

public class Main{
	
	

	public static void main(String[] args) {
		God.Get();
	    RestaurantGui gui = new RestaurantGui();
	    gui.setTitle("csci201 Restaurant");
	    gui.setVisible(true);
	    gui.setResizable(false);
	    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
