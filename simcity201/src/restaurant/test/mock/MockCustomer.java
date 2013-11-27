package restaurant.test.mock;


import restaurant.Menu;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public Cashier cashier;
        
        public EventLog log = new EventLog();
        
        String name;

        public MockCustomer(String name) {
        	super(name);
        	this.name = name;
        }

        @Override
        public void msgHereIsYourChange(double total) {
        	log.add(new LoggedEvent("Received msgHereIsYourChange from cashier. Change = " + total));
        }

        @Override
        public void msgInDebt(double remaining_cost) {
        	log.add(new LoggedEvent("Received msgInDebt from cashier. Debt = " + remaining_cost));
        }

		@Override
		public void msgNotInDebt() {
			log.add(new LoggedEvent("Received msgNotItDebt from cashier. Debt = 0"));
		}

		@Override
		public void msgFollowMe(Waiter waiter, Menu menu, int tableNumber) {
			log.add(new LoggedEvent("Received msgFollowMe from waiter. Going to table " + tableNumber));
		}

		@Override
		public void msgWhatDoYouWant() {
			log.add(new LoggedEvent("Received msgWhatDoYouWant from watier"));
		}

		@Override
		public void msgOutOfChoice(Menu menu) {
			log.add(new LoggedEvent("Received msgOutOfChoice from waiter"));
		}

		@Override
		public void msgHereIsYourFood(String choice) {
			log.add(new LoggedEvent("Received msgHereIsYourFood from waiter. You got " + choice));
		}

		@Override
		public void msgHereIsYourBill(double dues) {
			log.add(new LoggedEvent("Received msgHereIsYourBill from waiter. You owe " + dues));
		}

		@Override
		public String getCustomerName() {
			return name;
		}

		@Override
		public void msgIsHungry() {
			// TODO Auto-generated method stub
			
		}

}