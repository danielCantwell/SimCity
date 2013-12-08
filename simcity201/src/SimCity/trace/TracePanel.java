package SimCity.trace;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/**
 * The class for managing and displaying the message traces from the factory/simcity. To be used as an 
 * {@link AlertListener} with {@link AlertLog}.  By default, ALL LEVELS AN NO TAGS ARE ENABLED!  <br><br>
 * 
 * When printing, the {@link AlertLevel} AND the {@link AlertTag} must be enabled to see a message.  This means that 
 * if only ERROR and INFO levels are enabled, and someone with a tag BANK_TELLER 
 * (presumably tagged on all of the messages any bank tellers send) sends an ERROR, a WARNING, and an INFO message,
 * then the only ones that will appear are the ERROR and INFO.  You would have to re-enable the WARNING AlertLevel
 * (which filters and updates the trace panel) to see those messages., even if the tag for BANK_TELLER were enabled 
 * the whole time.  Similarly, if WARNING Level is enabled, but PERSON Tag messages are being hidden, then if a 
 * WARNING message comes through with a tag of PERSON, it won't display because person messages aren't visible. <br><br>
 * 
 * If you wish to customize that behavior, then look at the {@link TracePanel#isAlertVisible(Alert)} method.
 * 
 * @author Keith DeRuiter
 * @version 2.0
 * 
 * @see AlertLog
 * @see AlertLevel
 * @see AlertTag
 */
public class TracePanel extends JScrollPane implements AlertListener {

	private static final long serialVersionUID = 5643932617391465416L;
	private JTextPane traceTextPane;

	private List<Alert> newAlerts = Collections.synchronizedList(new ArrayList<Alert>());
	private Set<AlertLevel> visibleLevels = Collections.synchronizedSet(EnumSet.allOf(AlertLevel.class));
	private Set<AlertTag> visibleTags = Collections.synchronizedSet(EnumSet.noneOf(AlertTag.class));

	Dimension size;
	Style errorStyle;
	Style warningStyle;
	Style infoStyle;
	Style defaultStyle;

	public TracePanel() {
		super();
		this.setBorder(new BevelBorder(EtchedBorder.LOWERED));
		this.size = new Dimension(500, 100);
		traceTextPane = new JTextPane();
		traceTextPane.setEditable(false);
		traceTextPane.setPreferredSize(size);
		// this.add(traceTextPane);
		this.setViewportView(traceTextPane);

		this.setPreferredSize(size);
		this.setEnabled(true);

		//Set up the styled doc and styles to use for printing
		StyledDocument styledDoc = traceTextPane.getStyledDocument();

		// Create a style object and then set the style attributes
		errorStyle = styledDoc.addStyle("ErrorStyle", null);
		warningStyle = styledDoc.addStyle("WarningStyle", null);
		infoStyle = styledDoc.addStyle("InfoStyle", null);
		defaultStyle = styledDoc.addStyle("DefaultStyle", null);

		// Error
		StyleConstants.setForeground(errorStyle, Color.red);

		// Warning
		StyleConstants.setForeground(warningStyle, Color.yellow);
		//TODO: This looks ugly and is only so we could actually read the text... change it!
		//And maybe change font color or panel background color or something...
		StyleConstants.setBackground(warningStyle, Color.black);

		// Info
		StyleConstants.setForeground(infoStyle, Color.blue);

		// Default
		StyleConstants.setForeground(defaultStyle, Color.black);


		//Set the default visible levels, redundant with the EnumSet declaration above
		visibleLevels.add(AlertLevel.MESSAGE);
		visibleLevels.add(AlertLevel.ERROR);
		visibleLevels.add(AlertLevel.WARNING);
		visibleLevels.add(AlertLevel.INFO);
		visibleLevels.add(AlertLevel.DEBUG);
		
		//Sets up the TracePanel to automatically be tied with the AlertLog.
		AlertLog.getInstance().addAlertListener(this);
	}

	
	/**
	 * Determines if a given alert should be displayed based on all of the currently enabled {@link AlertLevel}
	 * and {@link AlertTag} parameters.
	 * @param alert The alert to check.
	 * @return true if the alert should be visible, false otherwise.
	 * 
	 * @see {@link #showAlertsWithLevel(AlertLevel)}
	 * @see {@link #hideAlertsWithLevel(AlertLevel)}
	 * @see {@link #showAlertsWithTag(AlertTag)}
	 * @see {@link #hideAlertsWithTag(AlertTag)}
	 */
	private boolean isAlertVisible(Alert alert) {
		if(visibleLevels.contains(alert.level) && visibleTags.contains(alert.tag)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Updates the trace panel to include all new messages from the log, 
	 */
	private void updateTracePanel() {
		synchronized (newAlerts) {
			for (Alert alert : newAlerts) {
				try {
					if(!isAlertVisible(alert)) {	//If not visible, skip this alert in the loop
						continue;
					}

					//Pick a style to print with
					Style styleToPrint = null;
					switch (alert.level) {
					case ERROR:
						styleToPrint = errorStyle;
						break;
					case WARNING:
						styleToPrint = warningStyle;
						break;
					case INFO:
						styleToPrint = infoStyle;
						break;
					default:
						styleToPrint = defaultStyle;
						break;
					}

					//Insert the alert into the panel's document
					int endPosition = traceTextPane.getDocument().getEndPosition().getOffset();
					traceTextPane.getStyledDocument().insertString(endPosition, alert.toString() + "\n", styleToPrint);
					
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			newAlerts.clear();	//We've dealt with the new alerts, so remove them form the new alerts list.
		}
	}

	/**
	 * Filters the trace panel according to Level and Tag and only displays those which have been enabled.
	 */
	private void filterTracePanel() {
		try {
			traceTextPane.getStyledDocument().remove(0, traceTextPane.getStyledDocument().getLength());	//Removes the whole document
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		List<Alert> alerts = AlertLog.getInstance().getAlerts();	//Get all the alerts from the log
		Collections.sort(alerts);									//Sort them (they end up sorted by timestamp)
		for(Alert alert:alerts) {
			if(visibleTags.contains(alert.tag) && visibleLevels.contains(alert.level)) {
				newAlerts.add(alert);
				//System.out.println("Adding Alert: " + alert.name + alert.level + alert.tag);
			}
		}

		updateTracePanel();	//update the panel to now reflect the correct alerts
	}

	/**
	 * Makes Alerts with a given {@link AlertLevel} show up in the trace panel.
	 * @param level The level whose alerts you do want to see.
	 */
	public void showAlertsWithLevel(AlertLevel level) {
		this.visibleLevels.add(level);
		filterTracePanel();
	}
	/**
	 * Makes Alerts with a given {@link AlertLevel} not show up in the trace panel.
	 * @param level The level whose alerts you don't want to see.
	 */
	public void hideAlertsWithLevel(AlertLevel level) {
		this.visibleLevels.remove(level);
		filterTracePanel();
	}

	/**
	 * Makes Alerts with a given {@link AlertTag} show up in the trace panel.
	 * @param tag The tag whose alerts you do want to see.
	 */
	public void showAlertsWithTag(AlertTag tag) {
		this.visibleTags.add(tag);
		filterTracePanel();
	}

	/**
	 * Makes Alerts with a given {@link AlertTag} not show up in the trace panel.
	 * @param tag The tag whose alerts you don't want to see.
	 */
	public void hideAlertsWithTag(AlertTag tag) {
		this.visibleTags.remove(tag);
		filterTracePanel();
	}
	
	/**
	 * Enables Alerts to be displayed for all Tag values in {@link AlertTag}.
	 * Convenience method.
	 */
	public void showAlertsForAllTags() {
		visibleTags = Collections.synchronizedSet(EnumSet.allOf(AlertTag.class));
	}
	
	/**
	 * Enables Alerts to be displayed for all Level values in {@link AlertLevel}.
	 * Convenience method.
	 */
	public void showAlertsForAllLevels() {
		visibleLevels = Collections.synchronizedSet(EnumSet.allOf(AlertLevel.class));
	}

	/**
	 * Adds a new {@link Alert} to the trace panel.
	 * @param alert The alert that is being added.
	 */
	public void addNewAlert(Alert alert) {
		//This should make it only scroll down if we are already at the bottom.  Like a scroll lock kinda thing.
		boolean scrollDown = (this.getVerticalScrollBar().getValue() + this
				.getVerticalScrollBar().getVisibleAmount()) == this
				.getVerticalScrollBar().getMaximum();

		//add the new alert and update the panel to show it
		newAlerts.add(alert);
		updateTracePanel();

		//JScrollBar bar = this.getVerticalScrollBar();
		// bar.setValue(bar.getMaximum() + bar.getVisibleAmount());

		//Should snap the trace panel to the bottom when a new thing is added (makes it so you don't have to scroll down manually as new stuff gets added in)
		if (scrollDown) {
			Document d = this.traceTextPane.getDocument();
			this.traceTextPane.select(d.getLength(), d.getLength());
			// this.getVerticalScrollBar().setValue(
			// this.getVerticalScrollBar().getMaximum()
			// + this.getVerticalScrollBar().getVisibleAmount());
		}
	}

	/** {@inheritDoc} */
	@Override
	public void alertOccurred(Alert alert) {
		addNewAlert(alert);
	}

}
