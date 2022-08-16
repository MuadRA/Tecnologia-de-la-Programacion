package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	protected int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot){
		
		this.timeSlot = timeSlot;
	}
	
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		int i = 0,tam=0, h=0;
		int aux = 0;
		
		if(roads.isEmpty()) {
			return -1;
		}
		else if(currGreen == -1) {
			
			while( i < qs.size()) {
				if( qs.get(i).size() > tam) {
					currGreen = i;
					tam = qs.get(i).size();
				}
				
				i++;
			}
			return currGreen;
		}
		else if( (currTime - lastSwitchingTime) < timeSlot) {
			return currGreen;
		}
		else {
			h = (currGreen+1)%qs.size();
			for(i = 0; i < qs.size();i++) {
				if(aux < qs.get(h).size()) {
					aux = qs.get(h).size();
					currGreen = h;
				}
				h++;
				if(h == qs.size()) {
					h = 0;
				}
			}
			return currGreen;
		}
	}

}
