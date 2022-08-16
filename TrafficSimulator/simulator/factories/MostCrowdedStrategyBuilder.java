package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public MostCrowdedStrategyBuilder(String type) {
		super(type);
		
	}

	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		if(data == null) {
			throw new IllegalArgumentException("El JSON data debe tener un valor valido");}
		else {
			if(!data.has("timeslot")) {
				return new MostCrowdedStrategy(1);}
			
			else{
				int aux = data.getInt("timeslot");
				return new MostCrowdedStrategy(aux);}
		}
	}

}
