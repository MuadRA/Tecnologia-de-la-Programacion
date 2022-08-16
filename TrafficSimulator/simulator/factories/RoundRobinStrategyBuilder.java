package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public RoundRobinStrategyBuilder(String type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		if(data == null) {
			throw new IllegalArgumentException("El JSON data debe tener un valor valido");}
		else {
			if(!data.has("timeslot")) {
				return new RoundRobinStrategy(1);}
			
			else{
				int aux = data.getInt("timeslot");
				return new RoundRobinStrategy(aux);}
		}
	}

}
