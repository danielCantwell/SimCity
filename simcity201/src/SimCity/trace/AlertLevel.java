package SimCity.trace;

/**
 * Alert levels to be used with AlertLog. Read comments below for more
 * information.
 * 
 * @author Keith DeRuiter
 * @version 2.0
 * 
 */
public enum AlertLevel {
	
	/** 
	 * Error is meant to be used if you want to alert someone
	 * about something they might be doing wrong. Of course,
	 * limiting its use is a good idea. Errors are printed
	 * in red.
	 */
	ERROR,
	
	/** 
	 * Warnings are similar to errors in that you want to
	 * alert someone else, but it isn't quite as important
	 * as an error. Warnings are printed normally.
	 */
	WARNING,
	
	/**
	 * Info is just excessive information someone might
	 * want to know some day. It is generally off by default.
	 */
	INFO,

	/**
	 * Most alerts will have this level.  This should be the normal 
	 * logging level for trace panel or console messages.
	 */
	MESSAGE,

	/**
	 * This level could be used to print specific debug information.
	 */
	DEBUG
}
