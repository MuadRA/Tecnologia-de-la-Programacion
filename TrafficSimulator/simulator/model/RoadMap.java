package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	protected List<Junction> listJunctions;
	protected List<Road> listRoads;
	protected List<Vehicle> listVehicles;
	protected Map<String, Junction> mapIdJunctions;
	protected Map<String, Road> mapIdRoad;
	protected Map<String,Vehicle> mapIdVehicle;
	
	RoadMap(){
		listJunctions = new ArrayList<Junction>();
		listRoads = new ArrayList<Road>();
		listVehicles = new ArrayList<Vehicle>();
		mapIdJunctions = new HashMap<String,Junction>();
		mapIdRoad = new HashMap<String, Road>();
		mapIdVehicle = new HashMap<String, Vehicle>();
	}
	
	protected void addJunction (Junction j) {

		if(!mapIdJunctions.containsKey(j._id)) {
			
			listJunctions.add(j);
			mapIdJunctions.put(j._id, j);
		}
		else {
			throw new IllegalArgumentException("El id de este cruce ya existe.");
		}
	}
	
	protected void addRoad (Road r){
		
		if(!mapIdRoad.containsKey(r._id)) {
			if(listJunctions.contains(r.cruceDestino)) {
				if(listJunctions.contains(r.cruceDestino)){
					
					listRoads.add(r);	
					mapIdRoad.put(r._id, r);
				}
				else {
					throw new IllegalArgumentException("El cruce origen de esta carretera es inexistente.");
				}
			}
			else {
				throw new IllegalArgumentException("El cruce destino de esta carretera es inexistente.");
			}
		}
		else {
			throw new IllegalArgumentException("El Id de esta carretera ya existe.");
		}
	}
	
	protected void addVehicle (Vehicle v) {
		if(!mapIdVehicle.containsKey(v._id)) {
			boolean cumple = true, encontrado = false;;
			int i = 0, j = 0;
			while(i < v.itinerary.size()-1 && cumple) {
				j = 0;
				encontrado = false;
				while(j < listRoads.size() && cumple && !encontrado) {
					if(listRoads.get(j).cruceOrigen._id == v.itinerary.get(i)._id && listRoads.get(j).cruceDestino._id == v.itinerary.get(i+1)._id) {
						encontrado = true;
					}
					j++;
				}
				if(!encontrado) {cumple = false;}
				i++;
			}
		}
		else {
			throw new IllegalArgumentException ("El Id del vehiculo ya existe.");
		}
		listVehicles.add(v);
		mapIdVehicle.put(v._id, v);
	}
	
	protected Junction getJunction (String id) {
			return mapIdJunctions.get(id);
	}
	
	protected Road getRoad (String id) {
			return mapIdRoad.get(id);
	}
	
	protected Vehicle getVehicle(String id) {
			return  mapIdVehicle.get(id);
	}
	
	public List<Junction> getJunctions(){
		/*List<Junction> inmutableListJunctions = Collections.unmodifiableList(listJunctions);
		
		return inmutableListJunctions;*/
		return listJunctions;
	}
	
	public List<Road> getRoads(){
		/*List<Road> inmutableListRoads = Collections.unmodifiableList(listRoads);
		
		return inmutableListRoads;*/
		return listRoads;
	}
	
	public List<Vehicle> getVehicles(){
		/*List<Vehicle> inmutableListVehicles = Collections.unmodifiableList(listVehicles);
		
		return inmutableListVehicles;*/
		return listVehicles;
	}
	
	protected void reset() {
		listJunctions.clear();
		listRoads.clear();
		listVehicles.clear();
		mapIdJunctions.clear();
		mapIdRoad.clear();
		mapIdVehicle.clear();
	}
	
	public JSONObject report() {
		JSONObject ja = new JSONObject();
		JSONArray jarray = new JSONArray();
		JSONArray jarray2 = new JSONArray();
		JSONArray jarray3 = new JSONArray();
		
		for(int i = 0; i < listJunctions.size();i++) {
			jarray.put(listJunctions.get(i).report());
		}
		
		ja.put("junctions", jarray);
		
		for(int i = 0; i < listRoads.size();i++) {
			jarray2.put(listRoads.get(i).report());
		}
		
		ja.put("roads", jarray2);
		
		for(int i = 0; i < listVehicles.size();i++) {
			jarray3.put(listVehicles.get(i).report());
		}
		
		ja.put("vehicles", jarray3);
		return ja;
	}
	
}
