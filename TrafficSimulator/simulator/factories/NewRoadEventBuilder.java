package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event>{
	private int time;
	private String id;
	private String src;
	private String dest;
	private int length;
	private int co2limit;
	private int maxspeed;
	private String weather;
	private Weather aux;
	NewRoadEventBuilder(String type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	protected abstract Event createTheRoad(int time2, String id2, String src2, String dest2, int length2, int co2limit2, int maxspeed2, Weather aux2);
	
	protected Event createTheInstance(JSONObject data) {
		if(data == null) {throw new IllegalArgumentException("El JSON data debe tener un valor valido");}
		else{
			time = data.getInt("time");
			id = data.getString("id");
			src = data.getString("src");
			dest = data.getString("dest");
			length = data.getInt("length");
			co2limit = data.getInt("co2limit");
			maxspeed = data.getInt("maxspeed");
			weather = data.getString("weather");
			aux = Weather.valueOf(weather);
			
			return createTheRoad(time,id,src,dest,length,co2limit,maxspeed,aux);
		}
	}

	

}
