
Team 33
======
SimCity201 Project Repository for CS 201 students
###Individual Contribution Breakdown

####Danny Cantwell
+ Housing Roles and Guis
  * Owner Role
  * Tenant Role
+ Global Money Class
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
+ Base Person and Role Class
+ Bulding Wrappers and base Building Class for each building
+ God Class that holds everthing in the city
+ Junit Testing
  * Person Class
  * God Class

####Tim So
+ Market Roles and Guis
  * Clerk Role
  * Customer Role
  * Delivery Person Role
  * Manager Role
  * Packer Role

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

####SimCity Person Interaction
+ Comments go here

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
+ Comments go here

####SimCity Interactions
+ Comments go here

####SimCity GUI
+ Buses and bus stops
+ Custom graphics for Cars and Buses
+ Custom graphics for interiors
+ Additional panel for instantiating and customizing Persons during runtime
+ Sounds and 3D stuff as a nice-to-have