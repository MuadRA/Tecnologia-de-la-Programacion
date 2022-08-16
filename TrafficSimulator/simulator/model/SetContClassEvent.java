package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event{
	protected List<Pair<String,Integer>> cs;
	
	public SetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		if(cs == null) {throw new IllegalArgumentException("La lista contClass debe tener un valor valido");}
		else {this.cs = cs;}
	}

	void execute(RoadMap map) {
		for(int i = 0; i < cs.size();i++) {
			if(map.getVehicle(cs.get(i).getFirst()) == null) {
				throw new IllegalArgumentException("El Vehiculo no existe en el mapa de carreteras");}
			else {
				map.getVehicle(cs.get(i).getFirst()).setContaminationClass(cs.get(i).getSecond());
			}
		}
		
	}
	
	public String toString() {
		String mostrar = "";
		for(int i = 0; i < cs.size();i++) {
			mostrar = mostrar + "(" + cs.get(i).getFirst() + "," + cs.get(i).getSecond() + ")";
		
			if(i != (cs.size()-1)) {
			mostrar = mostrar + ", ";
			}
		}
		
		return "Change CO2 class: ["+ mostrar + "]";
	}
}
