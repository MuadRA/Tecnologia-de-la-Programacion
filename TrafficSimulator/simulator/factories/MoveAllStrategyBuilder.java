package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveAllStrategy;

public class MoveAllStrategyBuilder extends Builder<DequeuingStrategy> {

	public MoveAllStrategyBuilder(String type) {
		super(type);
		
	}

	protected DequeuingStrategy createTheInstance(JSONObject data) {
		if(data == null) {
			throw new IllegalArgumentException("El JSON data debe tener un valor valido");}
		else {
			return new MoveAllStrategy();
		}
	}

}
