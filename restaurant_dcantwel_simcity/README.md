##Restaurant Project Repository

###Student Information
  + Name: Daniel Cantwell
  + USC Email: dcantwel@usc.edu
  + USC ID: 7069640115
  + Lecture: T/Th 11:00-12:30
  + Lab: Wed 10:00-12:00
  
###Compiling Instructions
  + Use eclipse
  + Add JUnit 3 to Java Build Path
  + Right click RestaurantGui.java
  + Choose Run as -> Java Application
  + or
  + Right click project
  + Choose Run as -> JUnit Test
  
###Notes
  + The way my markets work, the cashier does not need to know if the two bills are a single order or separate orders, so the two scenarios look the same to the cashier
  + Give both waiters and customers unique names
  + Name customers Steak / Chicken / Pizza / Salad to force them to order this, whether they have the money or not
  + Name customers Flake to have them order anything whether they have money or not
  + All markets start with the same amount of food.  In the output console, it will say cook's inventory and when markets run out of food
  + Markets start with 8 steaks, 2 chickens, 6 pizzas, 10 salad. To test market out of food, take advantage of the low chicken inventory
  + Waiter going on break : check box will be selected and disabled while requesting break, it will enable and stay selected if host accepts the break request, and it will be enabled and unselect if break request is denied

###Resources
  + [Restaurant v1](http://www-scf.usc.edu/~csci201/readings/restaurant-v1.html)
  + [Agent Roadmap](http://www-scf.usc.edu/~csci201/readings/agent-roadmap.html)
