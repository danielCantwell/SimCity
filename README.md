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
  * Car Gui and animation, rotation
  * City Layout including streets, sidewalks, crosswalks, buildings
  * A* pathfinding for pedestrians
  * Building Panels upon mouse click
+ Images for the Gui (custom made, hand-sprited)
  * Person animation
  * Houses and other Buildings
  * Street, sidewalk, and road textures
  * Traffic lights, cars, and buses

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