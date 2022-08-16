package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;
import simulator.view.MapComponent;

public class Controller {
	TrafficSimulator sim;
	Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory){
	this.sim = sim;
	this.eventsFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		
		for(int i = 0; i < jo.getJSONArray("events").length();i++) {
			Event e = eventsFactory.createInstance(jo.getJSONArray("events").getJSONObject(i));
			
			if(e != null) {
				sim.addEvent(e);
			}
		}
	}
	
	public void run(int n, OutputStream out) throws IOException {
		JSONObject output = new JSONObject();
		JSONArray state = new JSONArray();
		
		for(int i = 0; i < n;i++) {
		sim.advance();
		state.put(sim.report());
		}
		
		output.put("states", state);
		String rep = output.toString(3);
		System.out.println(rep);
		
		if(out != null) {
		out.write(rep.getBytes());
		out.close();
		}
	}
	
	public void reset() {
		sim.reset();
	}

	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		sim.addEvent(e);
	}
}
