//package restaurant.gui;
//
//
//import restaurant.CookAgent;
//
//import java.awt.*;
//import java.util.ArrayList;
//
///**
// * 	@author Timothy So
// *  Displays cook and his grills.
// */
//
//public class CookGui implements Gui {
//
//    private CookAgent agent = null;
//
//    private String grillIcon = "";
//    
//    private String[] servingIcons = {"", "", ""};
//    
//    private int xPos = 300, yPos = 440;//default waiter position
//
//    public CookGui(CookAgent agent) {
//        this.agent = agent;
//    }
//
//    public void updatePosition()
//    {
//    }
//
//    public void draw(Graphics2D g) {
//        g.setColor(Color.GRAY);
//        g.fillRect(xPos, yPos, 20, 20);
//        
//        // plating
//        g.setColor(Color.ORANGE);
//        g.fillRect(xPos-20, yPos-20, 60, 20);
//        g.fillRect(xPos-40, yPos-20, 20, 60);
//        g.fillRect(xPos+40, yPos-20, 20, 60);
//        
//        // cooking
//        g.setColor(Color.BLACK);
//        g.fillRect(xPos-20, yPos+20, 60, 20);
//        
//        g.setColor(Color.GRAY);
//        g.drawString(grillIcon, xPos, yPos+35);
//        
//        g.setColor(Color.BLACK);
//        for (int i = 0; i < servingIcons.length; i++)
//        {
//        	g.drawString(servingIcons[i], xPos + 20*(i-1), yPos - 5);
//        }
//    }
//
//    public boolean isPresent() {
//        return true;
//    }
//
//	public void setGrill(String choice) {
//		if (choice.equals("Steak"))
//		{
//			grillIcon = "ST";
//		}
//		else if (choice.equals("Chicken"))
//		{
//			grillIcon = "CH";
//		}
//		else if (choice.equals("Salad"))
//		{
//			grillIcon = "SA";
//		}
//		else if (choice.equals("Pizza"))
//		{
//			grillIcon = "PZ";
//		}
//		else
//		{
//			grillIcon = "";
//		}
//	}
//
//	public void addServingArea(int table, String choice) {
//		String text = new String();
//		if (choice.equals("Steak"))
//		{
//			text = "ST";
//		}
//		else if (choice.equals("Chicken"))
//		{
//			text = "CH";
//		}
//		else if (choice.equals("Salad"))
//		{
//			text = "SA";
//		}
//		else if (choice.equals("Pizza"))
//		{
//			text = "PZ";
//		}
//		else
//		{
//			text = "??";
//		}
//		servingIcons[table] = text;
//	}
//
//	public void removeServingArea(int table)
//	{
//		servingIcons[table] = "";
//	}
//}
