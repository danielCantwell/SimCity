package brianRest.gui;

import javax.swing.*;

import brianRest.BrianTable;
import brianRest.BrianWaiterRole;
import brianRest.interfaces.BrianHost;
import brianRest.interfaces.BrianWaiter;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.gui.Gui;

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
	
	JLabel leftInfoLabel = new JLabel("Left info");
	JLabel rightInfoLabel = new JLabel("Waiter Break Panel");
	JLabel rightInfoLabel2 = new JLabel("Can only break once.");
	
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
	   leftInfo.add(leftInfoLabel);
	   
   }
   
   private void setupRightInfo(){
	   //Resize the right info box here.
	   Dimension dim2 = new Dimension(400, 200);
	   rightInfo.setPreferredSize(dim2);
	   rightInfo.setMaximumSize(dim2);
	   rightInfo.setMinimumSize(dim2);
	   rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.Y_AXIS));
	   
	   //Labels here.
	   rightInfo.add(rightInfoLabel);
	   rightInfo.add(rightInfoLabel2);
	   
	   
	   view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
	   pane.setViewportView(view);
	   
	   
	   Dimension dim = new Dimension(200, 100);
	   pane.setPreferredSize(dim);
	   pane.setMaximumSize(dim);
	   pane.setMinimumSize(dim);
	   rightInfo.add(pane);
   }
   
   public void addWaiter(String name, BrianWaiter newRole){
	   JCheckBox button = new JCheckBox(name);
	   button.addActionListener(this);
	   list.add(new People(button, newRole));
	   view.add(button);   
	   view.validate();
   }
   
   public void removeWaiter(BrianWaiter role){
	   for (People p: list){
			
			if (role == p.waiter){
				view.remove(p.button);
				list.remove(p.button);
				view.validate();
				return;
			}
			
		}
   }

@Override
public void actionPerformed(ActionEvent arg0) {
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
