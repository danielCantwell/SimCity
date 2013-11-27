/**
 * 
 */
package restaurant.interfaces;

import restaurant.Menu;

/**
 * @author Daniel
 *
 */
public interface Customer {
    /**
     * @param total change (if any) due to the customer
     *
     * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
     */
    public abstract void msgHereIsYourChange(double change);


    /**
     * @param remaining_cost how much money is owed
     * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
     */
    public abstract void msgInDebt(double debt);
    
    public abstract void msgNotInDebt();

    public abstract void msgFollowMe(Waiter waiter, Menu menu, int tableNumber);
    
    public abstract void msgWhatDoYouWant();
    
    public abstract void msgOutOfChoice(Menu menu);
    
    public abstract void msgHereIsYourFood(String choice);
    
    public abstract void msgHereIsYourBill(double dues);

	public abstract String getCustomerName();


	public abstract void msgIsHungry();

}
