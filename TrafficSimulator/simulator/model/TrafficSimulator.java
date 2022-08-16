package simulator.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;
import simulator.model.Road.compVehicle;

public class TrafficSimulator implements Observable<TrafficSimObserver> {

	private RoadMap mapRoad;
	private SortedArrayList<Event> eventList;
	private int timesimulation;
	private compEvents compE;
	private List<TrafficSimObserver> observerList;
	
	public TrafficSimulator(){
		
		compE = new compEvents();
		mapRoad = new RoadMap();
		eventList = new SortedArrayList<Event>(compE);
		this.timesimulation = 0;
		observerList = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		eventList.add(e);
		notifyEvent(e);
	}
	
	public void advance() {
		int j = 0;
		timesimulation++;
		
		notifyStart();
			
		while(eventList.size() > 0 && eventList.get(0).getTime() == timesimulation) {
			eventList.remove(0).execute(mapRoad);
		}
		
		eventList.sort(compE);
		
		while(j < mapRoad.listJunctions.size()) {
			mapRoad.listJunctions.get(j).advance(timesimulation);
			j++;
		}
		
		j = 0;
		
		while(j < mapRoad.listRoads.size()) {
			mapRoad.listRoads.get(j).advance(timesimulation);
			j++;
		}
		
		notifyEnd();
		
	}
	
	public void reset() {
		
		mapRoad.reset();
		eventList.clear();
		timesimulation = 0;
		notifyReset();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();	
		
		jo.put("time", this.timesimulation);
		
		jo.put("state", mapRoad.report());
		return jo;
	}
	
	class compEvents implements Comparator<Event>{

		public int compare(Event e1, Event e2) {
			if(e1.getTime() > e2.getTime()) {
				return 1;
			}
			else if(e1.getTime() == e2.getTime()) {
				return 0;
			}
			else {return -1;}
		}
		
	}

	public void addObserver(TrafficSimObserver o) {
		observerList.add(o);
		notifyRegister();
	}

	public void removeObserver(TrafficSimObserver o) {
		
	}
	
	public void notifyStart() {
		for(int i = 0; i < observerList.size();i++) {
			observerList.get(i).onAdvanceStart(mapRoad, eventList, timesimulation);
		}
	}
	
	public void notifyEnd() {
		for(int i = 0; i < observerList.size();i++) {
			observerList.get(i).onAdvanceEnd(mapRoad, eventList, timesimulation);
		}
	}
	
	public void notifyEvent(Event e) {
		for(int i = 0; i < observerList.size();i++) {
			observerList.get(i).onEventAdded(mapRoad, eventList, e,timesimulation);
		}
	}
	
	public void notifyReset() {
		for(int i = 0; i < observerList.size();i++) {
			observerList.get(i).onReset(mapRoad, eventList, timesimulation);
		}
	}
	
	public void notifyRegister() {
		for(int i = 0; i < observerList.size();i++) {
			observerList.get(i).onRegister(mapRoad, eventList, timesimulation);
		}
	}
}
