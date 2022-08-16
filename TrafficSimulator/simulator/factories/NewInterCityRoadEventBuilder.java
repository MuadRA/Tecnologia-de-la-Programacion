package simulator.factories;

import simulator.model.Event;
import simulator.model.Weather;
import simulator.model.NewInterCityRoadEvent;


public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder{

	public NewInterCityRoadEventBuilder(String type) {
		super(type);
		
	}

	
	protected Event createTheRoad(int time2, String id2, String src2, String dest2, int length2, int co2limit2,
			int maxspeed2, Weather aux2) {
		
		return new NewInterCityRoadEvent(time2,id2,src2,dest2,length2,co2limit2,maxspeed2,aux2);
	}


}
