package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {
	private int time;
	private List<Pair<String,Integer>> cc;
	private String mostrar;
	
	public SetContClassEventBuilder(String type) {
		super(type);
		cc = new ArrayList<Pair<String,Integer>>();
	}

	protected Event createTheInstance(JSONObject data) {
		if(data == null) {
			throw new IllegalArgumentException("El JSON data debe tener un valor valido");
		}
		else {
			Pair<String, Integer> aux;
			String auxVeh;
			int auxClass;
			mostrar = null;
			int longitud = data.getJSONArray("info").length();
			this.time = data.getInt("time");
			
			for(int i = 0; i < longitud;i++) {
				auxVeh = data.getJSONArray("info").getJSONObject(i).getString("vehicle");
				auxClass = data.getJSONArray("info").getJSONObject(i).getInt("class");
				aux = new Pair<String,Integer>(auxVeh, auxClass);
				cc.add(aux);
				mostrar = mostrar + "(" + aux.getFirst() + "," + aux.getSecond() + ")";
				
				if(i != (longitud-1)) {
					mostrar = mostrar + ", ";
				}
			}
			
			return new SetContClassEvent(this.time,this.cc);
		}
	}
	
}
