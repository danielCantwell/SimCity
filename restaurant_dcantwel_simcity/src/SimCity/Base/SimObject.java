package SimCity.Base;

public class SimObject {
	int id;
	
	public SimObject(){
		id = 0;
		God.Get().assignID(this);
	}
	
	public int getID(){	
		return id;
	}
}
