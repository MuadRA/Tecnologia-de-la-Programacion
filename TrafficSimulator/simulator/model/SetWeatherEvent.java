package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	protected List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws == null) {throw new IllegalArgumentException("La lista weather debe tener un valor valido");}
		else {this.ws = ws;}
	}

	void execute(RoadMap map) {
		for(int i = 0; i < ws.size();i++) {
		if(map.getRoad(ws.get(i).getFirst()) == null) {
			throw new IllegalArgumentException("La carretera no existe en el mapa de carreteras");}
		else{
			map.getRoad(ws.get(i).getFirst()).setWeather(ws.get(i).getSecond());}	
		}
	}

	public String toString() {
		String mostrar = "";
		for(int i = 0; i < ws.size();i++) {
			mostrar = mostrar + "(" + ws.get(i).getFirst() + "," + ws.get(i).getSecond() + ")";
		
			if(i != (ws.size()-1)) {
			mostrar = mostrar + ", ";
			}
		}
		
		return "Change Weather: ["+ mostrar + "]";
	}
}
