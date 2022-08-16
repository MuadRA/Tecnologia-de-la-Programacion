package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
	
	private Factory<LightSwitchingStrategy> lightFactory;
	private Factory<DequeuingStrategy> dequeueFactory;
	private int time;
	private String id;
	private int x;
	private int y;
	
	public NewJunctionEventBuilder(String type,Factory<LightSwitchingStrategy> lightFactory,Factory<DequeuingStrategy> dequeueFactory ) {
		super(type);
		this.lightFactory = lightFactory;
		this.dequeueFactory = dequeueFactory;
	}

	protected Event createTheInstance(JSONObject data) {
		time = data.getInt("time");
		id = data.getString("id");
		x = data.getJSONArray("coor").getInt(0);
		y = data.getJSONArray("coor").getInt(1);
		return new NewJunctionEvent(time,id,lightFactory.createInstance(data.getJSONObject("ls_strategy")),dequeueFactory.createInstance(data.getJSONObject("dq_strategy")),x,y);
	}
	
	
}
