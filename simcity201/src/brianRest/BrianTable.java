package brianRest;

import brianRest.interfaces.BrianCustomer;
import restaurant.interfaces.Customer;

public class BrianTable {
	BrianCustomer occupiedBy;
	int tableNumber;
	int guiPosX;
	int guiPosY;

	BrianTable(int tableNumber) {
		this.tableNumber = tableNumber;
		guiPosX = 0;
		guiPosY = 0;
	}
	
	BrianTable(int tableNumber, int posX, int posY) {
		this.tableNumber = tableNumber;
		guiPosX = posX;
		guiPosY = posY;
	}

	void setOccupant(BrianCustomer cust) {
		occupiedBy = cust;
	}
	
	public int getTableNumber(){
		return tableNumber;
	
	}
	public int getPosX(){
		return guiPosX;
	}
	
	public int getPosY(){
		return guiPosY;
	
	}

	void setUnoccupied() {
		occupiedBy = null;
	}

	BrianCustomer getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
}
