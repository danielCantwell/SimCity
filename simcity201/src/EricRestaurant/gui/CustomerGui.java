package EricRestaurant.gui;

import java.awt.*;

import EricRestaurant.EricHost;
import EricRestaurant.interfaces.Cashier;
import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Host;

public class CustomerGui implements Gui{
	private Host host;
	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos = -20, yPos = -20;
	private int xDestination = 20, yDestination = 20;
	
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable = 200;
	public static final int yTable = 250;
	static int count=0;
	String displayText = "";

	public CustomerGui(Customer c, EricHost h){ //HostAgent m) {
		agent = c;
		
		//maitreD = m;
		host = h;
		this.gui = gui;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=5;
		else if (xPos > xDestination)
			xPos-=5;

		if (yPos < yDestination)
			yPos+=5;
		else if (yPos > yDestination)
			yPos-=5;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				//gui.setCustomerEnabled(agent);
				isHungry = false;
				
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				return;
				
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		if (displayText.trim().length() >0)
			g.drawString(displayText, (xPos-10), (yPos-3));
	}

	public void setText(String string) {
		displayText = string;
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int sn) {//later you will map seatnumber to table coordinates.
		sn = sn -1;
		xDestination = xTable+sn*100;
		yDestination = yTable;
		command = Command.GoToSeat;
	}


	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void custWait() {
		xDestination = 20;
		yDestination = 20;
	}
}
