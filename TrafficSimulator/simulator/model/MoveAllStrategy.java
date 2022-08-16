package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> aux = new ArrayList<Vehicle>();
		if(q.size() == 0) {return null;}
		else {
			int i = 0;
		while( i < q.size()) {
			
			aux.add(q.get(i));
		}
		
		return aux;
		}
	}
	
}
