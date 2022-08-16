package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {
	private int time;
	private String id;
	private int maxSpeed;
	private int clase;
	private List<String> itin;
	public NewVehicleEventBuilder(String type) {
		super(type);
		this.itin = new ArrayList<String>();
	}

	protected Event createTheInstance(JSONObject data) {
		if(data == null) {throw new IllegalArgumentException("El JSON data debe tener un valor valido");}
		else {
			itin = new ArrayList<String>();
			this.time = data.getInt("time");
			this.id = data.getString("id");
			this.maxSpeed = data.getInt("maxspeed");
			this.clase = data.getInt("class");
			for(int i = 0; i < data.getJSONArray("itinerary").length();i++) {
				itin.add(data.getJSONArray("itinerary").getString(i));
			}
			return new NewVehicleEvent(this.time,this.id,this.maxSpeed,this.clase,this.itin);
		}
	}


}
