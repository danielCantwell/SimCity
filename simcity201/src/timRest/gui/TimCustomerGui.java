package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

import java.awt.*;
import java.util.concurrent.Semaphore;

public class CustomerGui implements Gui{

	private final int SPEED = 5;
	
	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;
	
	Semaphore positionSemaphore = new Semaphore(1, true);

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToWait, GoToSeat, LeaveRestaurant, GoToCashier};
	private Command command=Command.noCommand;

	public CustomerGui(CustomerAgent c, RestaurantGui gui)
	{
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
		this.gui = gui;
	}

	public void updatePosition() {
		try {
			positionSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (xPos < xDestination)
			xPos+= SPEED;
		else if (xPos > xDestination)
			xPos-= SPEED;

		if (yPos < yDestination)
			yPos+= SPEED;
		else if (yPos > yDestination)
			yPos-= SPEED;
		
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToWait)
			{
				agent.msgAnimationFinishedGoToWait();
			}
			else if (command==Command.GoToSeat)
			{
				agent.msgAnimationFinishedGoToSeat();
			}
			else if (command==Command.LeaveRestaurant)
			{
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			else if (command == Command.GoToCashier)
			{
				agent.msgAnimationFinishedGoToCashier();
			}
			command=Command.noCommand;
		}
		positionSemaphore.release();
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
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
	
	public void DoGoToWaiting(Point seatPos) {
		try {
			positionSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xDestination = seatPos.x;
		yDestination = seatPos.y;
		command = Command.GoToWait;
		positionSemaphore.release();
	}

	public void DoGoToSeat(Point tablePosition) {
		try {
			positionSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xDestination = tablePosition.x;
		yDestination = tablePosition.y;
		command = Command.GoToSeat;
		positionSemaphore.release();
	}
	
	public void DoFollowWaiter(WaiterAgent waiter)
	{
		Point pos = waiter.getPos();
		xDestination = pos.x;
		yDestination = pos.y;
	}

	public void DoExitRestaurant() {
		try {
			positionSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
		positionSemaphore.release();
	}


	public void DoGoToCashier() {
		try {
			positionSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xDestination = -20;
		yDestination = 200;
		command = Command.GoToCashier;
		positionSemaphore.release();
	}
}
