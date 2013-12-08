package SimCity.trace;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class that AlertLog uses which contains all of the relevant information for a single alert.
 * 
 * @author Keith DeRuiter
 * @version 2.0
 */
public class Alert implements Comparable<Alert> {
	/** Used to filter alerts by {@link AlertLevel} */
	public final AlertLevel level;
	
	public final AlertTag tag;
	
	/** Displayed in the message */
	public final String name;
	
	/** The message of this alert */
	public final String message;
	
	/** Timestamp of this alert */
	public final Date date;

	public Alert(AlertLevel level, AlertTag tag, String sender, String message, Date date) {
		this.level = level;
		this.tag = tag;
		this.name = sender;
		this.message = message;
		this.date = date;
	}

	public String toString() {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		if(this.level == AlertLevel.MESSAGE) {
			return format.format(this.date) + " | (" + this.name + ") " + this.message;
		}
		return format.format(this.date) + " | [" + this.level + "] : (" + this.name + ") " + this.message;
	}

	@Override
	public int compareTo(Alert a) {
		if(!(a instanceof Alert)) {
			throw new ClassCastException("Object being compared to is not an instance of Alert!");
		}
		Alert otherAlert = (Alert) a;
		return this.date.compareTo(otherAlert.date);
	}

}
