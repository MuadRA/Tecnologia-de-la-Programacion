package simulator.model;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	protected List<Road> listRoadE;
	protected Map<Junction, Road> mapRoadS;
	protected List<List<Vehicle>> listColas; // mirar inicializacion
	protected int indexGreenTrafficLight;
	protected int lastTrafficLightChange;
	protected LightSwitchingStrategy lightStrategy;
	protected DequeuingStrategy extractStrategy;
	private int x, y;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor){
		
		super(id);
		
		if(lsStrategy==null || dqStrategy==null) {
			throw new IllegalArgumentException ("Las estrategias deben tener un valor valido.");
		}else if(( xCoor < 0 ) || ( yCoor < 0 )){
			throw new IllegalArgumentException ("Las coordenadas, deben de ser positivas.");
		}else {
			
			lightStrategy = lsStrategy;
			
			extractStrategy = dqStrategy;
			
			x = xCoor;
			
			y = yCoor;
			
			this.listRoadE = new ArrayList<Road>();
			
			indexGreenTrafficLight = -1;
			
			lastTrafficLightChange = 0;
			
			listColas = new ArrayList<List<Vehicle>>();
			
			mapRoadS = new HashMap<Junction, Road>();
		}
	}
		
		public int getY() {
		return y;
	}

		public int getX() {
		return x;
	}

		protected void addIncommingRoad(Road r) {
			
			if(r.cruceDestino != this) {
				throw new IllegalArgumentException ("La carretera debe ser entrante a este cruce.");
			}
			else {
			List<Vehicle> aux = new ArrayList<Vehicle>();
			listRoadE.add(r);
			listColas.add(aux);
			}
		}
		
		protected void addOutGoingRoad(Road r) {
			if(r.cruceOrigen != this) {
				throw new IllegalArgumentException("La carretera debe ser saliente a este cruce ");
			}
			else {
				mapRoadS.put(r.cruceDestino, r);
			}
		}
		
		protected void enter(Vehicle v) {
			int i = 0;
			while(i < listRoadE.size()) {
				if(v.road._id == listRoadE.get(i)._id) {
					listColas.get(i).add(v);
				}
				i++;
			}
			
		}
		
		protected Road roadTo(Junction j) {
				return mapRoadS.get(j);
		}
		
		protected void advance(int time) {
			int lastGreenTrafficLight = indexGreenTrafficLight;
			if(indexGreenTrafficLight == -1) {
				indexGreenTrafficLight = lightStrategy.chooseNextGreen(listRoadE, listColas, indexGreenTrafficLight, lastTrafficLightChange, time);
				lastTrafficLightChange = time;
			}
			else {
				List<Vehicle> aux = extractStrategy.dequeue(listColas.get(indexGreenTrafficLight));
				if(aux != null) {
					for(int i = 0; i < aux.size();i++) {
						listColas.get(indexGreenTrafficLight).get(i).moveToNextRoad();
						listColas.get(indexGreenTrafficLight).remove(i);
					}
				}
				indexGreenTrafficLight = lightStrategy.chooseNextGreen(listRoadE, listColas, indexGreenTrafficLight, lastTrafficLightChange, time);
				if(lastGreenTrafficLight != indexGreenTrafficLight) {
					lastTrafficLightChange = time;}
			}
		}
		
		public JSONObject report() {
			JSONObject ja = new JSONObject();
			JSONArray jarray = new JSONArray();
			String s;
			ja.put("id", this._id);
			
			if(indexGreenTrafficLight == -1) {ja.put("green", "none");}
			else {ja.put("green", listRoadE.get(indexGreenTrafficLight)._id);}
			
			for(int i = 0; i < listColas.size();i++) {
				JSONObject ja2 = new JSONObject();
				JSONArray jarray2 = new JSONArray();
				ja2.put("road", listRoadE.get(i)._id);
				
				for(int j = 0; j < listColas.get(i).size();j++) {
				s = listColas.get(i).get(j)._id;
				jarray2.put(s);
				}
				
				ja2.put("vehicles", jarray2);
				jarray.put(ja2);
			}
			
			
			ja.put("queues", jarray);
			return ja;
		}

		public int getGreenLightIndex() {
			return indexGreenTrafficLight;
		}

		public List<Road> getInRoads() {
			return listRoadE;
		}
		
		public String getColas() {
			String s = "";
			for(int i = 0; i < listRoadE.size();i++) {
				s = s + listRoadE.get(i).getId() + ":[";
				for(int j = 0; j < listColas.get(i).size();j++) {
					s = s + listColas.get(i).get(j).getId();
					if(j != (listColas.get(i).size()-1)) {
						s = s + ", ";
					}
				}
				s = s + "] ";
			}
			return s;
		}
		
	}
	
	
	


