package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{
	protected String id;
	protected int maxSpeed;
	protected int contClass;
	protected List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
	}

	void execute(RoadMap map) {
		List<Junction> it2 = new ArrayList<Junction>();
		Vehicle v;
		for(int i = 0; i < itinerary.size();i++) {
			it2.add(map.getJunction(itinerary.get(i)));
		}
		
		v = new Vehicle(id,maxSpeed,contClass,it2);
		map.addVehicle(v);
		v.moveToNextRoad();
	}
	
	public String toString() {
		return "New Vehicle '"+id+"'";
	}

}
