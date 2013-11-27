package restaurant;

import restaurant.interfaces.Customer;

public class Table {
	Customer occupiedBy;
	int tableNumber;
	int guiPosX;
	int guiPosY;

	Table(int tableNumber) {
		this.tableNumber = tableNumber;
		guiPosX = 0;
		guiPosY = 0;
	}
	
	Table(int tableNumber, int posX, int posY) {
		this.tableNumber = tableNumber;
		guiPosX = posX;
		guiPosY = posY;
	}

	void setOccupant(Customer cust) {
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

	Customer getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
}
