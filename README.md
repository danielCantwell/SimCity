Team 33
======
SimCity201 Project Repository for CS 201 students
###Individual Contribution Breakdown

####Danny Cantwell
+ Housing Roles and Guis
  * Owner Role
  * Tenant Role
+ Global Money Class
+ Building - Danny Restaurant
+ Integrated Restaurant
  * Added PC Waiter
  * Added bank interaction
  * Added market interaction
+ Setup Panel
  * Creating Individual People with various options
  * Creating Scenarios
  * Changing mode options
+ Junit Testing
  * All the above listed roles

####Jesse Chand
+ Exterior SimCity Guis
  * Person Gui and animation, rotation
  * Labels for each PersonGUI's role
  * Car Gui and animation, rotation
  * Bus Standee Guis
  * Bus Guis and animation, rotation - with defined and efficient routes
  * City Layout including streets, sidewalks, crosswalks, buildings, traffic lights
  * A* pathfinding for pedestrians
  * Building Panels upon mouse click
+ Images for the Gui (custom made, hand-sprited)
  * Person animations (walking, waiting for bus)
  * Houses and other Buildings - interior textures and objects
  * Street, sidewalk, and road textures
  * Traffic lights, cars, and buses
  * Misc. Gui icons (day / time counters, etc.)
+ Sound Effects
  * Non-normative accident events
  * Ambient city sound (background music)
  * Misc. sounds (doors, bells, etc.)

####Brian Chen
+ Assigned and Completed:
 + Base Person and Role Class
 + Junit Testing Person Class

+ Finished:
 + Bulding Wrappers and base Building Class for each building
 + God Class that holds every building and person in the city.
 + Implemented time for the city.
 + Implemented work-shifts for the city (role swapping).
 + Integrated House into exterior gui
 + Integrated House and Person class and gui transition between both.
 + Implemented buildings being closed if not enough employees.
 + Implemented certain buildings being closed on weekends.
 + Implemented a button to manually close Brian Restaurant to show functionality of closing.
 + Keybinds for spawning manager and bank customer.
 + Keybinds for spawning brian restaurant workers and customers.
 + Integrated Brian Restaurant and implemented scenario for it to work.
 + Integrated new features for Brian Rest including PCWaiter.
 + Fully integrated trace panel.
 + Refactor all team's print statements to work with trace panel.
 + Implemented Brian Restaurant working with Market.
 + Implemented closing individual buildings through the setup panel.

####Timothy So
+ Market Roles and Guis
  * Clerk Role
  * Customer Role
    * EASTER EGG: A Customer named Wilczynski will purchase the brain from the extra credit scenario.
    * Customers will purchase and use a car if they do not own one and they have at least $700.
  * Delivery Person Role
    * Assisted with incorperation of all restaurant/market interaction.
    * Created interface for restaurant cooks and cashiers to implement.
  * Manager Role
    * Alerts people to leave when market is done with work.
  * Packer Role
  * Notes:
    * Although people stand on top of each other, it is possible to have any number of clerks, packers, delivery persons, or customers.
+ JUnit Testing
  * Packer Role
  * Clerk Role
+ Fixed issues with screen resolution
  * NOTE: Sufficently large screens may have issues with the gui.
+ Money Class
  * Integrated it into some restaurants.
+ Integration of Restaurant v2.2
  * Integration of restaurant
  * Producer/Consumer Waiter

####Eric Wu
+ Bank Roles and Guis
  * Customer Role
  * Guard Role
  * Manager Role
  * Teller Role
+ Junit Testing
  * All the above listed roles

####The following was done by everyone
+ Attended and contributed to each group meeting
+ Debuging and assisting in other group member's code
+ Worked on the inital integration of the first restaurant

###What's Working
####SimCity Roles
+ the roles themselves are functioning, they can interact with other roles, send messages and do actions properly.

####SimCity Guis
+ Upon DoTravel() call from a Person, a GUI (car or pedestrian) will successfully travel to an address.
+ Upon clicking on a building, its interior is displayed.
+ Buses pick up and drop people off successfully.
+ Vehicles obey traffic laws (crosswalks, lights) (unless they get into accidents).

###How To Run It
1.Setup Panel to Select a Scenario
  * Select a scenario
  
2.Setup Panel to Spawn Individual People
  * Select a Role and create individual people
  * Housing, Vehicle, Morality, Money, Hunger Level, and Name can be changed
  * If bank or market role is selected, choose building number as well
  * Click "Create Person"
  * If "Create Wanderer" is clicked, creates a person without a job

3.Layout of Windows
  * City grid is resizable. 
  * Hover on a building to see what it is. Click it to view it.

4.[VERY IMPORTANT] Showing Images
  * Currently images do not draw without a tiny bit of extra setup. This is a currently unresolved issue that many students are still facing. In order for images to work, find the 'images' folder under the 'simcity201' folder. Drag it into Eclipse, under the top folder. It should show up next to 'src'. Right click 'images' in Eclipse and select 'Build Path > Use as Source Folder'. Refresh and run SimCityGui. It should work.
  * If the above instructions do not work at all, go into Gui.java under 'exterior.gui' and change SHOW_RECT to TRUE. Run SimCityGui and press CTRL+C to enter compatibility mode. It will now draw placeholder rectangles instead of the graphics. 
  * This will also affect imported sound effects and fonts. If you are getting an error with sound effects, the sounds might not be compatible with your system. An error will be thrown but it is harmless. 
