package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject {
	
	protected Junction cruceOrigen;
	protected Junction cruceDestino;
	protected int longitud;	//Medido en metros.
	protected int maxSpeed;
	protected int limitSpeed;
	protected int limitContamination;
	protected Weather weatherConditions;
	protected int totalContamination;
	private List<Vehicle> listVehicle;
	private compVehicle comp;
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather){ //Crear clase para juntar todas las excepciones.
		super(id);

		
		if( maxSpeed < 0) {
			throw new IllegalArgumentException ("Es necesario introducir una velocidad máxima positiva.");
		}
		else if(contLimit < 0) {
			throw new IllegalArgumentException ("Es necesario introducir una contaminacion limite positiva.");
		}
		else if(length < 0) {
			throw new IllegalArgumentException ("Es necesario introducir una longitud positiva.");
		}
		else if( srcJunc == null || destJunc == null ) {
			throw new IllegalArgumentException ("No puede haber un cruce sin valor.");
		}
		else if(weather == null){
			throw new IllegalArgumentException ("Weather no puede ser null.");
		}else {
			
			cruceOrigen = srcJunc;
		
			cruceDestino = destJunc;
		
			this.maxSpeed = maxSpeed;
			
			this.limitSpeed = maxSpeed;
			
			limitContamination = contLimit;
		
			longitud = length;
		
			weatherConditions = weather;
			
			comp = new compVehicle();
			
			listVehicle = new SortedArrayList<Vehicle>(comp);
			
			srcJunc.addOutGoingRoad(this);
			
			destJunc.addIncommingRoad(this);
		}
	}
	
	protected void enter(Vehicle v) {
		
		if(v.ubication != 0) {
			throw new IllegalArgumentException ("La ubicacion inicial del vehiculo debe de ser 0."); 
		}
		else if(v.actSpeed != 0) {
			throw new IllegalArgumentException ("La velocidad actual debe ser 0.");
		}
		else {
		listVehicle.add(v);
		}
	}
	
	protected void exit(Vehicle v) {
		listVehicle.remove(v);
	}
	
	protected  void setWeather(Weather w) {
		
		if(w==null) {
			throw new IllegalArgumentException("Weather debe tener un valor valido.");
		}
		else {
			weatherConditions = w;
		}
	}
	
	protected void addContamination(int c) {
		
		if( c < 0) {
			throw new IllegalArgumentException("La contaminacion total debe ser un valor positivo.");
		}
		else {
			totalContamination += c;
		}
	}
	
	protected abstract void reduceTotalContamination(); 
	
	protected abstract void updateSpeedLimit();
	
	protected abstract int calculateVehicleSpeed(Vehicle v);
	
	protected void advance(int time){ 
		int vel, i=0;
		reduceTotalContamination();
		
		updateSpeedLimit();
		
		
		while( i < listVehicle.size()) {
			
			vel = calculateVehicleSpeed(listVehicle.get(i));
			
			listVehicle.get(i).setSpeed(vel);
			
			listVehicle.get(i).advance(time);
			i++;
			
		}
		
		listVehicle.sort(comp);
		
	}
	
	public Junction getSrc() {
		return cruceOrigen;
	}
	
	public Junction getDest() {
		return cruceDestino;
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray jarray = new JSONArray();
		
		jo.put("id", this._id);
		jo.put("speedlimit", this.limitSpeed);
		jo.put("weather", this.weatherConditions);
		jo.put("co2", this.totalContamination);
		for(int i = 0; i < listVehicle.size();i++) {
			jarray.put(listVehicle.get(i)._id);
		}
		jo.put("vehicles", jarray);
		
		return jo;
	}
	
	class compVehicle implements Comparator<Vehicle>{

		public int compare(Vehicle v1, Vehicle v2) {
			if(v1.ubication < v2.ubication) {
				return 1;
			}
			else if(v1.ubication == v2.ubication) {
				return 0;
			}
			else {
				return -1;
			}
		}
		
	}

	public int getTotalCO2() {
		return totalContamination;
	}

	public int getCO2Limit() {
		return limitContamination;
	}

	public Weather getWeatherConditions() {
		return weatherConditions;
	}

	public int getLength() {
		return longitud;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getLimitSpeed() {
		return limitSpeed;
	}

	public List<Vehicle> getListV(){
		List<Vehicle> inmutableListVehicles = Collections.unmodifiableList(listVehicle);
		return inmutableListVehicles;
	}

}
