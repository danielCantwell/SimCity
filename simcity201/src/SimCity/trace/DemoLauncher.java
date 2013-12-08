package SimCity.trace;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A quick demo of some squares moving around and logging when they change color zones.  You can also
 * click the mouse in either color to generate a different kind of message. <br><br>
 * 
 * TUTORIAL: You can search through this file for the word "TUTORIAL" to see relevant pieces of 
 * code with instructions on how to use the {@link AlertLog} and {@link TracePanel}.
 * 
 * @author Keith DeRuiter
 *
 */
@SuppressWarnings("serial")
public class DemoLauncher extends JFrame {

	AnimPanel animationPanel;
	ControlPanel controlPanel;
	
	//============================ TUTORIAL ==========================================
	//You declare a TracePanel just like any other variable, and it extends JPanel, so you use
	//it in the same way.
	TracePanel tracePanel;
	//================================================================================


	public DemoLauncher() {
		this.animationPanel = new AnimPanel();
		this.tracePanel = new TracePanel();
		this.controlPanel = new ControlPanel(tracePanel);
		tracePanel.setPreferredSize(new Dimension(800, 300));
		
		this.setLayout(new BorderLayout());
		this.add(animationPanel, BorderLayout.CENTER);
		this.add(controlPanel, BorderLayout.EAST);
		this.add(tracePanel, BorderLayout.SOUTH);
	}

	public void start() {
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				animationPanel.update();		//Moves demo shapes around				
				repaint();
			}
		}, 0, 10);
		
		//============================ TUTORIAL ==========================================
		//We have to tell the trace panel what kinds of messages to display.  Here we say to display
		//normal MESSAGEs tagged with the PERSON tag (the type I use in this demo).  You can also 
		//hide messages of certain Levels, or messages tagged a certain way (eg. Don't show anything
		//that says it is from a MARKET_EMPLOYEE).  Here we decide to hide debug messages and things
		//tagged as AlertTag.BUS_STOP
		tracePanel.showAlertsWithLevel(AlertLevel.ERROR);		//THESE PRINT RED, WARNINGS PRINT YELLOW on a black background... :/
		tracePanel.showAlertsWithLevel(AlertLevel.INFO);		//THESE PRINT BLUE
		tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);		//THESE SHOULD BE THE MOST COMMON AND PRINT BLACK
		
		tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
		
		tracePanel.showAlertsWithTag(AlertTag.PERSON);
		tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
		
		tracePanel.hideAlertsWithTag(AlertTag.BUS_STOP);
		//
		//You will have to add your own AlertTag types to the AlertTag enum for your project.
		//There are two helper methods that enable all AlertLevels and all AlertTags that you can use
		//if you don't want to manually enable them all.  IF NOTHING APPEARS, CHECK THAT YOU HAVE 
		//THE RIGHT LEVELS AND TAGS TURNED ON.  That will likely be the most common problem.
		//================================================================================
	
		//============================ TUTORIAL ==========================================
		//We need to let the log know that the trace panel wants to listen for alert messages.
		//This is how the trace panel will be automatically told when anyone logs a message, and 
		//will store or display it as appropriate.  Note how we are accessing the AlertLog: using
		//the fact that it is a globally accessible singleton (there is only ever ONE log, and 
		//everybody accesses the same one).
		AlertLog.getInstance().addAlertListener(tracePanel);
		//================================================================================		
	}

	
	//CLASS TO DISPLAY THIS SIMPLE ANIMATION
	private class AnimPanel extends JPanel implements MouseListener {

		//Just used to hold the thing i'm moving around
		private class Thing extends Point { 
			String name;
			int dx = 1;
			boolean inRed = true;
			
			public Thing(String name, int yPos) {
				this.name = name;
				this.y = yPos;
			}

			public void move() {
				if(this.x > 500) {	//Go left
					dx = -2;
				} else if(this.x < 200) {	//Go right
					dx = 2;
				}
				this.x += dx;
			}
		}

		ArrayList<Thing> things = new ArrayList<>();

		Rectangle redArea;
		Rectangle blueArea;

		public AnimPanel() {
			things.add(new Thing("Thing One", 150));
			things.add(new Thing("Thing Two", 300));
			things.get(0).x = 350;
			
			//everything stays 0 b/c window isn't visible yet...
			redArea = new Rectangle(0, 0, this.getWidth()/2, this.getHeight());
			blueArea = new Rectangle(this.getWidth()/2, 0, this.getWidth(), this.getHeight());
			
			this.addMouseListener(this);
		}

		public void update() {
			//Big hack to keep rect values current
			redArea = new Rectangle(0, 0, this.getWidth()/2, this.getHeight());
			blueArea = new Rectangle(this.getWidth()/2, 0, this.getWidth(), this.getHeight());
			
			for(Thing t : things) {
				t.move();
				if(redArea.contains(t) && !t.inRed) {
					t.inRed = true;
					
					//============================ TUTORIAL ==========================================
					//THIS IS HOW YOU SEND THINGS TO THE ALERT LOG, WHICH THE TRACE PANEL READS
					//Note that we are using the logInfo() method to specify that this is a normal MESSAGE,
					//as opposed to an INFO message, or an ERROR/WARNING.  We send the name of the "person"
					//as well, and that will be printed out automatically.
					AlertLog.getInstance().logMessage(AlertTag.PERSON, t.name, "I am in the RED ZONE!");
					//================================================================================
				}
				if(blueArea.contains(t) && t.inRed) {
					t.inRed = false;
					
					//============================ TUTORIAL ==========================================
					//THIS IS HOW YOU SEND THINGS TO THE ALERT LOG, WHICH THE TRACE PANEL READS
					//Note that we are using the logInfo() method to specify that this is a normal MESSAGE,
					//as opposed to an INFO message, or an ERROR/WARNING.  We send the name of the "person"
					//as well, and that will be printed out automatically.
					AlertLog.getInstance().logMessage(AlertTag.PERSON, t.name, "I am in the blue zone...");					
					//================================================================================
				}
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
						
			g2d.setColor(Color.RED);
			g2d.fillRect(redArea.x, redArea.y, this.getWidth(), this.getHeight());
			g2d.setColor(Color.BLUE);
			g2d.fillRect(blueArea.x, blueArea.y, this.getWidth(), this.getHeight());
			
			g2d.setColor(Color.GREEN);
			for(Thing thing : things) {
				g2d.fillRect(thing.x, thing.y, 20, 20);
			}
		}


		@Override
		public void mousePressed(MouseEvent e) {
			if(redArea.contains(e.getPoint())) {
				//============================ TUTORIAL ==========================================
				//THIS IS HOW YOU SEND THINGS TO THE ALERT LOG, WHICH THE TRACE PANEL READS
				//Note that we are using the logMessage() method to specify that this is an INFO message,
				//as opposed to a normal message, or an ERROR/WARNING.  Here we are telling it to log with the 
				//tag: BANK_CUSTOMER from a sender called "Mouse".
				AlertLog.getInstance().logInfo(AlertTag.BANK_CUSTOMER, "Mouse", "You clicked the RED ZONE!");
				//================================================================================
			}
			if(blueArea.contains(e.getPoint())) {
				//============================ TUTORIAL ==========================================
				//Note that we are using the logMessage() method to specify that this is an ERROR message,
				//as opposed to INFO, or WARNING, etc.  Here we are telling it to log with the 
				//tag: BANK_CUSTOMER from a sender called "Mouse".
				//Notice the different methods to log different Levels of messages.  There are methods to 
				//logError, logWarning, logInfo, logMessage, and logDebug that you should use. 
				AlertLog.getInstance().logError(AlertTag.BANK_CUSTOMER, "Mouse", "You clicked the blue zone");
				//================================================================================
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}

	
	//CONTROL PANEL CLASS
	public class ControlPanel extends JPanel {
		TracePanel tp;	//Hack so I can easily call showAlertsWithLevel for this demo.
		
		JButton enableMessagesButton;		//You could (and probably should) substitute a JToggleButton to replace both
		JButton disableMessagesButton;		//of these, but I split it into enable and disable for clarity in the demo.
		JButton enableErrorButton;		
		JButton disableErrorButton;		
		JButton enableBankCustTagButton;		//You could (and probably should) substitute a JToggleButton to replace both
		JButton disableBankCustTagButton;		//of these, but I split it into enable and disable for clarity in the demo.
		
		public ControlPanel(final TracePanel tracePanel) {
			this.tp = tracePanel;
			enableMessagesButton = new JButton("Show Level: MESSAGE");
			disableMessagesButton = new JButton("Hide Level: MESSAGE");
			enableErrorButton = new JButton("Show Level: ERROR");
			disableErrorButton = new JButton("Hide Level: ERROR");
			enableBankCustTagButton = new JButton("Show Tag: BANK_CUSTOMER");
			disableBankCustTagButton = new JButton("Hide Tag: BANK_CUSTOMER");
			
			
			enableMessagesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This is how you make messages with a certain Level (normal MESSAGE here) show up in the trace panel.
					tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
					//================================================================================
				}
			});
			disableMessagesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This is how you make messages with a certain Level not show up in the trace panel.
					tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
					//================================================================================
				}
			});
			enableErrorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This is how you make messages with a level of ERROR show up in the trace panel.
					tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
					//================================================================================
				}
			});
			disableErrorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This is how you make messages with a level of ERROR not show up in the trace panel.
					tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
					//================================================================================
				}
			});
			enableBankCustTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This works the same way as AlertLevels, only you're using tags instead.
					//In this demo, I generate message with tag BANK_CUSTOMER when you click in the 
					//AnimationPanel somewhere.
					tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
					//================================================================================
				}
			});
			disableBankCustTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//============================ TUTORIAL ==========================================
					//This works the same way as AlertLevels, only you're using tags instead.
					tracePanel.hideAlertsWithTag(AlertTag.BANK_CUSTOMER);
					//================================================================================
				}
			});
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(enableMessagesButton);
			this.add(disableMessagesButton);
			this.add(enableErrorButton);
			this.add(disableErrorButton);
			this.add(enableBankCustTagButton);
			this.add(disableBankCustTagButton);
			this.setMinimumSize(new Dimension(50, 600));
		}
	}
	
	
	/**
	 * Launch the demo.
	 * @param args
	 */
	public static void main(String[] args) {
		DemoLauncher dl = new DemoLauncher();
		dl.setTitle("Trace Panel Demo");
		dl.setDefaultCloseOperation(EXIT_ON_CLOSE);
		dl.setPreferredSize(new Dimension(900, 700));
		dl.setMinimumSize(new Dimension(900, 700));
		dl.setResizable(false);
		dl.setVisible(true);

		dl.start();
	}

}
