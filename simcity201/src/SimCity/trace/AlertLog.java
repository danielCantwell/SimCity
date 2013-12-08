package SimCity.trace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * This is a class designed to make it easier to send alerts and control them in
 * a specific way without everyone climbing over each other to get print
 * statements out there. It does this by requiring every message to have a tag
 * associated with it. The default are ERROR, WARNING, or INFO, but you can add
 * any you want. The nice part is that you can control which tags actually get
 * printed to the console and which simply get put on to a list.
 * 
 * @author Keith DeRuiter
 * @version 2.0
 * 
 * @see Alert
 * @see AlertListener
 * @see AlertLevel
 */
public class AlertLog {

	/** Holds the single AlertLog instance. */
	private static AlertLog instance = new AlertLog();
	
	/** List of all {@link AlertListeners} that should be notified when an alert occurs. */
	private List<AlertListener> registeredAlertListeners;

	/**
	 * The list of {@link AlertLevel} that will print to the System.out/err console when added to the Log.
	 * Defaults to every level printing.
	 */
	private Set<AlertLevel> printedAlertLevels = Collections.synchronizedSet(EnumSet.allOf(AlertLevel.class));
	
	/** The list of all the Alerts. */
	private List<Alert> alerts = Collections.synchronizedList(new ArrayList<Alert>());
	

	/**
	 * Private constructor to make AlertLog a singleton, creates a list to hold all alert listeners.
	 * Also sets the default printed types to be errors, warnings, and messages.
	 */
	private AlertLog() {
//		this.printedAlertLevels.add(AlertLevel.ERROR);
//		this.printedAlertLevels.add(AlertLevel.WARNING);
		this.registeredAlertListeners = Collections.synchronizedList(new ArrayList<AlertListener>());
	}
	
	/**
	 * Gets the one instance of the AlertLog singleton.
	 * @return The one singleton instance of AlertLog.
	 */
	public static AlertLog getInstance() {
		return AlertLog.instance;
	}

	/**
	 * Logs an error using {@link #sendAlert}.
	 * @param tag the {@link AlertTag} saying what group type this alert is tagged as.
	 * @param name The name of the thing sending this alert.
	 * @param message The message of this alert.
	 */
	public void logError(AlertTag tag, String name, String message) {
		this.sendAlert(AlertLevel.ERROR, tag, name, message);
	}
	
	/**
	 * Logs a warning using {@link #sendAlert}.
	 * @param tag the {@link AlertTag} saying what group type this alert is tagged as.
	 * @param name The name of the thing sending this alert.
	 * @param message The message of this alert.
	 */
	public void logWarning(AlertTag tag, String name, String message) {
		this.sendAlert(AlertLevel.WARNING, tag, name, message);
	}
	
	/**
	 * Logs info using {@link #sendAlert}.
	 * @param tag the {@link AlertTag} saying what group type this alert is tagged as.
	 * @param name The name of the thing sending this alert.
	 * @param message The message of this alert.
	 */
	public void logInfo(AlertTag tag, String name, String message) {
		this.sendAlert(AlertLevel.INFO, tag, name, message);
	}
	
	/**
	 * Logs a message using {@link #sendAlert}.
	 * @param tag the {@link AlertTag} saying what group type this alert is tagged as.
	 * @param name The name of the thing sending this alert.
	 * @param message The message of this alert.
	 */
	public void logMessage(AlertTag tag, String name, String message) {
		this.sendAlert(AlertLevel.MESSAGE, tag, name, message);
	}
	
	/**
	 * Logs a debug alert using {@link #sendAlert}.
	 * @param tag the {@link AlertTag} saying what group type this alert is tagged as.
	 * @param name The name of the thing sending this alert.
	 * @param message The message of this alert.
	 */
	public void logDebug(AlertTag tag, String name, String message) {
		this.sendAlert(AlertLevel.DEBUG, tag, name, message);
	}
	
	
	/**
	 * Send an alert to the log.
	 * 
	 * @param level The {@link AlertLevel} to log this message with.
	 * @param tag the {@link AlertTag} saying what group type this alert is tagged as.
	 * @param name The name of the thing sending this alert 
	 * @param message The message to be logged.
	 */
	public void sendAlert(AlertLevel level, AlertTag tag, String name, String message) {		
//		StackTraceElement[] s = new RuntimeException().getStackTrace();
//		String className = s[1].getClassName();

//		String senderClassName = new Throwable().getStackTrace()[1].getClassName();

		Date date = new Date();	//Timestamp

		//Make the alert.  Also prints to std out/err.
		Alert alert = new Alert(level, tag, name, message, date);
		if (this.printedAlertLevels.contains(level)) {
			if (level == AlertLevel.ERROR) {
				System.err.println(alert);
			} else {
				System.out.println(alert);
			}
		}
		this.alerts.add(alert);
		
		//Notify all people who have told me they want to listen for alerts.
		for(AlertListener al:this.registeredAlertListeners) {
			al.alertOccurred(alert);
		}
	}

	/**
	 * Enable an AlertLevel to the list of types that are printed to the console when an alert is made.
	 * If it is already on the list nothing will change.  <br><br>
	 * NOTE: This only controls printing to the
	 * standard console at the time an alert is generated.
	 * 
	 * @param type
	 */
	public void enableAlertLevel(AlertLevel type) {
		this.printedAlertLevels.add(type);
	}

	/**
	 * Disable an AlertType from the list of types that are printed to the console when an alert is made.
	 * If it is not on the list nothing will change.  <br><br>
	 * NOTE: This only controls printing to the
	 * standard System.out/System.err console at the time an alert is generated.
	 * 
	 * @param type
	 */
	public void disableAlertLevel(AlertLevel type) {
		this.printedAlertLevels.remove(type);
	}

	/**
	 * Get a copy of the list of all the alerts the AlertLog has collected.
	 * 
	 * @return The list of all {@link Alert}s. 
	 */
	public List<Alert> getAlerts() {
		return new ArrayList<Alert>(this.alerts);
	}
	
	/**
	 * Registers someone to be notified when an alert is added.
	 * @param alertListener the listener to register.
	 */
	public void addAlertListener(AlertListener alertListener) {
		if(registeredAlertListeners.contains(alertListener)) {
			return;
		}
		registeredAlertListeners.add(alertListener);
	}
}
