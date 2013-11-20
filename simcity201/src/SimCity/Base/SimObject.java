package SimCity.Base;
/**
 * @author Brian Chen
 *
 */
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
