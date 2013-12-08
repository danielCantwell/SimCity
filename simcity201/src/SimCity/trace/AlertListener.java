package SimCity.trace;
/**
 * An interface for being notified when an {@link Alert} occurs.
 * @author Keith DeRuiter
 *
 */
public interface AlertListener {
	/**
	 * Called when an alert occurs.
	 * @param alert The alert that happened.
	 */
	public void alertOccurred(Alert alert);
}
