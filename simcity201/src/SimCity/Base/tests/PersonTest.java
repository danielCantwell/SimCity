/**
 * 
 */
package SimCity.Base.tests;

import exterior.gui.SimCityGui;
import SimCity.Base.God;
import SimCity.Base.God.BuildingType;
import SimCity.Base.Person;
import SimCity.Base.Person.Action;
import SimCity.Base.Person.GoAction;
import SimCity.Base.Person.Intent;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.TimeState;
import SimCity.Base.Person.Vehicle;
import SimCity.Buildings.B_Bank;
import SimCity.Buildings.B_House;
import SimCity.Globals.Money;
import housing.roles.OwnerRole;
import housing.test.mock.MockTenant;
import junit.framework.TestCase;

/**
 * @author Brian
 *
 */
public class PersonTest extends TestCase {
	
	OwnerRole owner;
	Person person;
	MockTenant mtOne;
	MockTenant mtTwo;
	
	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();
		God.Get();
		SimCityGui scg = new SimCityGui();
		God.Get().setSimGui(scg);
		person = new Person("Briiiannn",null, "Bank.bankCustomerRole", Vehicle.walk, Morality.good, new Money(60,3), new Money(10, 0), 10, 3, "house", (B_House)God.Get().getBuilding(1), God.Get().getBuilding(3), 1);
		person.building = person.getHouse();
	}
	
	public void set(){
		try {
			//setUp();
		} catch (Exception e) {
			System.out.println("setUp failed");
			e.printStackTrace();
		}
	}
	
	public void test_CheckConstructor() {
		//set();
		assertTrue("Name should be Briiiannn", person.name.equals("Briiiannn"));
		//Test main role
		assertTrue("Main role should be Bank.bankCustomerRole class: ", person.mainRole instanceof Bank.bankCustomerRole);
		//Test vehicle
		assertTrue("Vehicle should be walk", person.vehicle == Vehicle.walk);
		//Test Morality
		assertTrue("Morality is good" , person.mor == Morality.good);
		//Test current money
		assertTrue("Dollar amount in person shoudld be 60.03", person.money.dollars == 60 && person.money.cents == 3);
		//Test money threshold.
		assertTrue("Money threshold should be $10", person.moneyThreshold.dollars == 10 && person.moneyThreshold.cents == 0);
		//Test hunger level
		assertTrue("Hunger level should be 10, which is not hungry", person.hungerLevel == 10);
		assertTrue("Hunger threshold should be 3, thats when hes hunry", person.hungerThreshold == 3);
		assertTrue("Person's house should be initialized to string house", person.house.equals("house"));
		assertTrue("Person's inventory should be initialized to have nothing in it. ", person.inventory.size() == 0);
	}
	
	public void test_PersonEntersBank(){
		//set();
		System.out.println(person.roles.toString());
		//Lets set up a god class so that the person can enter the bank. First we need to create a bank!
		God.Get();
		//B_Bank bank= new B_Bank(2); simcity gui should auto spawn our buildings now.
		//God.Get().addBuilding(bank); //I'm making a bank.
		//assertTrue("God should have a bank in its list.", God.Get().buildings.size() == 1 && God.Get().buildings.get(0) instanceof B_Bank);
		
		//Ok now the sim gui is suppose to spawn a bank at building #2
		assertTrue("The bank's ID should be 0 and the bank's tag should be B_Bank", God.Get().getBuilding(2) instanceof B_Bank);
		
		//Cannot implement the below test because we changed the way GOD functions.
		//assertTrue ("Can god find the bank using the findbyid function?", God.Get().getBuilding(2) == God.Get().buildings.get(0));
		
		//At this point the bank should be pretty much set up and ready to go. Let's add the person to the bank.
		person.msgGoToBuilding(God.Get().getBuilding(2), Intent.customer);
		//One roles should be in your roles list.
		assertTrue("There should be one role in the roles list", person.roles.size() == 1);
		//I will force the role to be active so that I do not need to animate to the actual building.
		assertTrue("That role should be inactive", person.roles.get(0).getActive() == false);
		
		
		//Ok now we need to check if the action was put in the person's actionlist.
		assertTrue("Does the person's action list contain one item?" , person.actions.size() == 1);
		assertTrue("Is that action of type goBank", person.actions.get(0).getGoAction() == GoAction.goBank);
		assertTrue("Does that goBank action have an intent of customer??", person.actions.get(0).getIntent() == Intent.customer);
		
		//Now what if we added a second action but it is work this time!
		person.msgGoToBuilding(God.Get().getBuilding(2), Intent.work);
		
		
		
		//I have to release the semaphore manually because I do not have a person gui to do so.
		
		
		
		assertTrue("Are there now 2 things in the action list of the person?", person.actions.size() == 2);
		assertTrue("Are they both of type bank?", person.actions.get(0).getGoAction() == GoAction.goBank && person.actions.get(1).getGoAction() == GoAction.goBank);
		assertTrue("Is the first action's intent customer", person.actions.get(0).getIntent() == Intent.customer);
		assertTrue("Is the second action's intent work?", person.actions.get(1).getIntent() == Intent.work);
		
		person.animation.release();
		
		//Now lets try the scheduler see if anything breaks
		person.pickAndExecuteAnAction();
		

		
		//There should now be only one thing in the actions list.
		System.out.println(person.actions.size());
		assertTrue("After PAEAA, the action list should be of size 1.", person.actions.size() == 1);
		assertTrue("That action should have a GoAction of goBank and an intent of work.", person.actions.get(0).getGoAction() == GoAction.goBank && person.actions.get(0).getIntent() == Intent.work);
		
		assertTrue("There should be one role that is not active in the person because it has not been processed yet..", person.roles.get(0).getActive() == false);
	}
	
	public void testPersonMoneyLevel(){
		//set();
		System.out.println(person.roles.toString());
		
		//Manually set the person's money level down low to 2, which is below the money threshold.
		person.money = new Money(1,0);
		
		//Make sure that the Money level is actually less than the Money threshold.
		assertTrue ("Money level is less than the Money threshold.", person.moneyThreshold.isGreaterThan(person.money));
		
		//I am forced to manually release the animation because i do not have a gui on the person for the sake of tests.
		person.animation.release();
		
		assertTrue("The person's timestate shoudl be none.", person.timeState == TimeState.none);
		
		
		assertTrue("The person's pick and execute returned true because his list of actions is now populated.", person.pickAndExecuteAnAction());
		
		
		assertTrue ("The person should now have an action in his list to go to the bank to get money.", person.actions.size() == 1);
		//He should be going to the bank because he needs money right?
		assertTrue("Let us check that money action. The action's goAction should be to a bank", person.actions.get(0).getGoAction() == GoAction.goBank);
		//Let me make sure he is going as a customer.
		assertTrue("That GoAction should be as a customer", person.actions.get(0).getIntent() == Intent.customer);
		
		//The rest of the bank testing is a separate test.

	}
	
	public void testPersonHungryLevel(){
		
		//manually force the hunger level to below the threshold which is currently set to 3.
		person.hungerLevel = 2;
		
		//make sure that the hunger level is actually below the hunger threshold.
		assertTrue ("Money level is less than the Money threshold.", person.hungerLevel < person.getHungerThreshold());
		
		//I am forced to manually release the animation because i do not have a gui on the person for the sake of tests.
		person.animation.release();
		
		//Again the person's time state must be none in order for him to actually do things on his own.
		assertTrue("The person's timestate shoudl be none.", person.timeState == TimeState.none);
		
		//After running the pick and execute, there should be a new action in the person's list of actions
		assertTrue("The person's pick and execute returned true because his list of actions is now populated.", person.pickAndExecuteAnAction());
		
		
		assertTrue ("The person should now have an action in his list to go to the bank to get money.", person.actions.size() == 1);
		//He should be going to the bank because he needs money right?
		assertTrue("Let us check that money action. The action's goAction should be to a bank", person.actions.get(0).getGoAction() == GoAction.goBank);
		//Let me make sure he is going as a customer.
		assertTrue("That GoAction should be as a customer", person.actions.get(0).getIntent() == Intent.customer);
		
		

	}
	
}


















