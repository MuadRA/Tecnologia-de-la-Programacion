package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{
	private int time;
	private List<Pair<String,Weather>> ws;
	private String mostrar;
	
	public SetWeatherEventBuilder(String type) {
		super(type);
		ws = new ArrayList<Pair<String,Weather>>();
	}

	protected Event createTheInstance(JSONObject data) {
		if(data == null) {
			throw new IllegalArgumentException("El JSON data debe tener un valor valido");
		}
		else {
			Pair<String,Weather> aux;
			String auxRoad,auxWeather;
			Weather auxW;
			String mostrar = null;
			this.time = data.getInt("time");
			int longitud = data.getJSONArray("info").length();
			for(int i = 0; i < longitud;i++) {
				auxRoad = data.getJSONArray("info").getJSONObject(i).getString("road");
				auxWeather = data.getJSONArray("info").getJSONObject(i).getString("weather");
				auxW = Weather.valueOf(auxWeather);
				aux = new Pair<String,Weather>(auxRoad, auxW);
				ws.add(aux);
				mostrar = mostrar + "(" + aux.getFirst() + "," + aux.getSecond() + ")";
				
				if(i != (longitud-1)) {
					mostrar = mostrar + ", ";
				}
			}
			
			return new SetWeatherEvent(this.time,this.ws);
		}
	}
	
}
