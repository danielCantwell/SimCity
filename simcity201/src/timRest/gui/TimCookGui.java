package timRest.gui;


import timRest.TimCookRole;

import java.awt.*;
import java.util.ArrayList;

import SimCity.gui.Gui;

/**
 * 	@author Timothy So
 *  Displays cook and his grills.
 */

public class TimCookGui implements Gui {

    private TimCookRole agent = null;

    private String grillIcon = "";
    
    private String[] servingIcons = {"", "", ""};
    
    private int xPos = 500, yPos = 250;//default cook position

    public TimCookGui(TimCookRole agent) {
        this.agent = agent;
    }

    public void updatePosition()
    {
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillRect(xPos, yPos, 20, 20);
        
        // plating
        g.setColor(Color.ORANGE);
        g.fillRect(xPos-20, yPos-20, 20, 60);
        g.fillRect(xPos-20, yPos-40, 60, 20);
        g.fillRect(xPos-20, yPos+40, 60, 20);
        
        // cooking
        g.setColor(Color.BLACK);
        g.fillRect(xPos+20, yPos-20, 20, 60);
        
        g.setColor(Color.GRAY);
        g.drawString(grillIcon, xPos+35, yPos);
        
        g.setColor(Color.BLACK);
        for (int i = 0; i < servingIcons.length; i++)
        {
        	g.drawString(servingIcons[i], xPos - 10, yPos + 20*(i-1)-5);
        }
    }

    public boolean isPresent() {
        return true;
    }

	public void setGrill(String choice) {
		if (choice.equals("Steak"))
		{
			grillIcon = "ST";
		}
		else if (choice.equals("Chicken"))
		{
			grillIcon = "CH";
		}
		else if (choice.equals("Salad"))
		{
			grillIcon = "SA";
		}
		else if (choice.equals("Pizza"))
		{
			grillIcon = "PZ";
		}
		else
		{
			grillIcon = "";
		}
	}

	public void addServingArea(int table, String choice) {
		String text = new String();
		if (choice.equals("Steak"))
		{
			text = "ST";
		}
		else if (choice.equals("Chicken"))
		{
			text = "CH";
		}
		else if (choice.equals("Salad"))
		{
			text = "SA";
		}
		else if (choice.equals("Pizza"))
		{
			text = "PZ";
		}
		else
		{
			text = "??";
		}
		servingIcons[table] = text;
	}

	public void removeServingArea(int table)
	{
		servingIcons[table] = "";
	}

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void restart()
    {
        // TODO Auto-generated method stub
        
    }
}
