package brianRest.gui;

import javax.swing.*;

import brianRest.BrianCookRole;
import brianRest.BrianTable;
import brianRest.BrianWaiterRole;
import brianRest.interfaces.BrianHost;
import brianRest.interfaces.BrianWaiter;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.gui.Gui;
import SimCity.trace.AlertLog;
import SimCity.trace.AlertTag;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class BrianRestaurantPanel extends JPanel implements ActionListener{
	
	 private final int WINDOWX = 640;
	 private final int WINDOWY = 640;
	    

	public BrianAnimationPanel bap = new BrianAnimationPanel();
	public JPanel info = new JPanel();
	public JPanel leftInfo = new JPanel();
	public JPanel rightInfo = new JPanel();
	public JPanel rightTopInfo = new JPanel();
	
	JLabel leftInfoLabel = new JLabel("Cook Info");
	JLabel food1;
	JLabel food2;
	JLabel food3;
	JLabel food4;
	JLabel rightInfoLabel = new JLabel("Waiter Break Panel");
	JLabel rightInfoLabel2 = new JLabel("Can only break once.");
	JLabel restOpenInfo = new JLabel("##### RESTAURANT CLOSED #####");
	
	JButton dumpInventory = new JButton("Dump Inventory");
	
	public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<People> list = new ArrayList<People>();
	
   public BrianRestaurantPanel(){
	   setupInfo();
	   setupRestPanel();
	   setVisible(true);
   }
   
   private void setupRestPanel(){
	   
	   
	   setSize(WINDOWX, WINDOWY);
	   setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	   bap.setSize(WINDOWX, WINDOWY-100);
	   Dimension dim = new Dimension(WINDOWX, WINDOWY-200);
	   bap.setPreferredSize(dim);
	   bap.setMaximumSize(dim);
	   bap.setMinimumSize(dim);
       add(bap);
       add (info);
	   
   }
   
   private void setupInfo(){
	   info.setSize(WINDOWX, 100);
	   Dimension dim = new Dimension(WINDOWX, 200);
	   info.setPreferredSize(dim);
	   info.setMaximumSize(dim);
	   info.setMinimumSize(dim);
	   info.add(leftInfo);
	   info.add(rightInfo);
	   setupLeftInfo();
	   setupRightInfo();
	   
   }
   
   private void setupLeftInfo(){
	   Dimension dim = new Dimension(200, 200);
	   leftInfo.setPreferredSize(dim);
	   leftInfo.setMaximumSize(dim);
	   leftInfo.setMinimumSize(dim);
	   leftInfo.setLayout(new BoxLayout(leftInfo, BoxLayout.Y_AXIS));
	   
	   leftInfo.add(restOpenInfo);
	   
	   leftInfo.add(leftInfoLabel);
	   
	   
	   food1 = new JLabel("");
	   food2= new JLabel("");
	   food3= new JLabel("");
	   food4= new JLabel("");
	   
	   leftInfo.add(food1);
	   leftInfo.add(food2);
	   leftInfo.add(food3);
	   leftInfo.add(food4);
	   leftInfo.add(dumpInventory);
	   dumpInventory.addActionListener(this);
	   
	   
   }
   
   public void setRestOpenInfo(boolean t){
	   if (t){
		   restOpenInfo.setText("##### RESTAURANT OPEN #####");
	   }
	   else 
		   restOpenInfo.setText("##### RESTAURANT CLOSED #####");
   }
   
   public void updateCookInfo(BrianCookRole cook){
	   food1.setText("Steak: " + cook.foodDictionary.get("Steak").getAmount());
	   food2.setText("Chicken: " + cook.foodDictionary.get("Chicken").getAmount());
	   food3.setText("Salad: " + cook.foodDictionary.get("Salad").getAmount());
	   food4.setText("Pizza: " + cook.foodDictionary.get("Pizza").getAmount());
   }
   
   private void setupRightInfo(){
	   //Resize the right info box here.
	   Dimension dim2 = new Dimension(420, 200);
	   rightInfo.setPreferredSize(dim2);
	   rightInfo.setMaximumSize(dim2);
	   rightInfo.setMinimumSize(dim2);
	   rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.PAGE_AXIS));
	   //rightInfo.setBorder(BorderFactory.createLineBorder(Color.black));
	   
	   Dimension dim = new Dimension(450, 100);
	   pane.setPreferredSize(dim);
	   pane.setMaximumSize(dim);
	   pane.setMinimumSize(dim);
	   
	   Dimension dim3 = new Dimension(420, 50);
	   rightTopInfo.setPreferredSize(dim3);
	   rightTopInfo.setMaximumSize(dim3);
	   rightTopInfo.setMinimumSize(dim3);
	   
	   rightTopInfo.setLayout(new BoxLayout(rightTopInfo, BoxLayout.Y_AXIS));
	   //Labels here.
	   rightInfo.add(rightTopInfo);
	   rightTopInfo.add(rightInfoLabel);
	   rightTopInfo.add(rightInfoLabel2);
	   
	   
	   view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
	   pane.setViewportView(view);
	   
	   
	   
	   rightInfo.add(pane);
   }
   
   public void addWaiter(String name, BrianWaiter newRole){
	   JCheckBox button = new JCheckBox(name);
	   button.addActionListener(this);
	   list.add(new People(button, newRole));
	   view.add(button);   
	   view.validate();
   }
   
   public void removeWaiter(){
	   AlertLog.getInstance().logError(AlertTag.BrianRest, "Problem", "removing");
	   for (People p: list){
				view.remove(p.button);
				list.remove(p.button);
			}
		view.validate();
		view.revalidate();

   }

@Override
public void actionPerformed(ActionEvent arg0) {
	
	if (arg0.getSource() == dumpInventory){
		
		
	}
	
	for (People p: list){
		
		if (p.button == arg0.getSource()){
			if (p.button.isSelected()){
				p.waiter.msgWantABreak();
				p.button.setEnabled(false);
			}
			return;
		}
		
	}
	
}


private class People{
	JCheckBox button;
	BrianWaiter waiter;
	
	private People(JCheckBox b, BrianWaiter bwr){
		button = b;
		waiter = bwr;
	}

}
   
}
