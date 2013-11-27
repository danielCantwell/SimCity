package EricRestaurant.gui;


import java.awt.*;

import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Waiter;

public class WaiterGui implements Gui {

    private Waiter agent = null;

    private int xPos = 20, yPos = 40;//default waiter position
    private int xDestination = 20, yDestination = 40;//default start position

    public static final int xTable = 200;
    public static final int yTable = 250;

    public WaiterGui(Waiter agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + 20) & (yDestination == yTable - 20)) {
           agent.msgAtTable();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Customer customer) {
        xDestination = xTable + 20;
        yDestination = yTable - 20;
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
