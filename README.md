Team 33
======
SimCity201 Project Repository for CS 201 students
###Individual Contribution Breakdown

####Danny Cantwell
+ Housing Roles and Guis
  * Owner Role
  * Tenant Role
+ Global Money Class
+ Setup Panel
  * Creating Individual People with various options
  * Creating Scenarios
  * Changing mode options
+ Junit Testing
  * All the above listed roles

####Jesse Chand
+ Exterior SimCity Guis
  * Person Gui and animation, rotation
  * Car Gui and animation, rotation
  * City Layout including streets, sidewalks, crosswalks, buildings
  * A* pathfinding for pedestrians
  * Building Panels upon mouse click
+ Images for the Gui (custom made, hand-sprited)
  * Person animation
  * Houses and other Buildings
  * Street, sidewalk, and road textures

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
  * Delivery Person Role
  * Manager Role
  * Packer Role
+ JUnit Testing
  * Packer Role
  * Clerk Role

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

###How To Run It
1.Keyboard Shortcuts to spawn people
  * CNTL - N : Spawns a bank customer
  * CNTL - M : Spawns a bank Manager
  * CNTL - T : Spawns a bank Teller
  * CNTL - G : Spawns a bank Guard
  * CNTL - C : Toggles compatibility mode (draws solid rectangles instead of images). Use this if the simulation is lagging or your images are broken.

2.Layout of Windows
  * City grid is resizable. 
  * Hover on a building to see what it is. Click it to view it.

3.[VERY IMPORTANT] Showing Images
  * Currently images do not draw without a tiny bit of extra setup. This is a currently unresolved issue that many students are still facing. In order for images to work, find the 'images' folder under the 'simcity201' folder. Drag it into Eclipse, under the top folder. It should show up next to 'src'. Right click 'images' in Eclipse and select 'Build Path > Use as Source Folder'. Refresh and run SimCityGui. It should work.
  * If the above instructions do not work at all, go into Gui.java under 'exterior.gui' and change SHOW_RECT to TRUE. Run SimCityGui and press CTRL+C to enter compatibility mode. It will now draw placeholder rectangles instead of the graphics. 

###What's Missing/left for V2
####SimCity Roles
+ All Restaurants need to be integrated and have working functionality.
+ Market.DeliveryPerson
  * Since restaurants and entering buildings was implemented late in the process, the normative scenario for the delivery person was difficult to implement. The interaction is designed, but could use a revision before more work is begun on it. That will probably occur when the restaurants are halfway implemented.

####SimCity Interactions
+ Integrate buildings with outer city.
+ Make sure that people can walk in and out of buildings without trouble.
+ Market Restaurant interaction.
+ Bank is in the simulation, however it broke when trying to integrating. We are working to fix this bug.
+ Market/Restaurant
  * The delivery person is key to getting the goods from the market to the restaurant, so this interaction will be implemented when the delivery person is fully implemented.
+ Market/Bank
  * Also, since most of the time was spent getting the customer interactions completed for the city, we didn't have quite enough time to have the market deposit and withdraw funds. However, this should be a quick task and should be implemented soon after the customer interactions are completed.
+ Market Non-norms
  * A lot of the time was spent getting the normative scenarios finished. However, the market was designed with non-normative scenarios in mind, so implementing them will not require extensive overhauling of the code.


####SimCity GUI
+ Buses and bus stops
+ Custom graphics for Cars and Buses
+ Custom graphics for interiors
+ Additional panel for instantiating and customizing Persons during runtime
+ Sounds and 3D stuff as a nice-to-have