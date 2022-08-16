package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject{
	
	protected List<Junction> itinerary;
	private int numJunc;
	private int maxSpeed;
	protected int actSpeed;
	private VehicleStatus statusV;
	protected Road road;	///
	protected int ubication;
	protected int gradeContamination;
	private int totalContamination;
	private int totalDistance;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary){
		
		super(id);
		
		if(maxSpeed < 0) {
			throw new IllegalArgumentException ("La velocidad maxima debe tener un valor positivo");
		}
		else if(contClass < 0 || contClass >10) {
			throw new IllegalArgumentException ("contClas debe tener un valor valido, entre 0 y 10 (ambos inclusive)");
		}
		else if(itinerary.size()<2){
			throw new IllegalArgumentException ("La longitud del itinerario debe ser al menos 2");
		}
		else {
			this.maxSpeed = maxSpeed;
			
			this.gradeContamination = contClass;
			
			Collections.unmodifiableList(new ArrayList<>(itinerary));
			
			this.itinerary = itinerary;
			
			this.road = null;
			
			this.statusV = VehicleStatus.PENDING;
			
			this.ubication = 0;
			
			this.totalDistance = 0;
			
			this.numJunc = 0;
		}
		
	}
	
	protected void setSpeed(int s) {
		if(road.longitud == this.ubication) {
			actSpeed = 0;
		}
		else {
			if(s < 0) {
				throw new IllegalArgumentException ("La velocidad debe ser positiva.");
			}
			else if (s < maxSpeed){
				actSpeed = s;
			}
			else {
				actSpeed = maxSpeed;
			}
		}
	}
	
	
	protected void setContaminationClass(int c) {
		
		if(c<=10 && c>=0) {
			gradeContamination = c;
		}
		else {
			throw new IllegalArgumentException ("La contaminacion del vehiculo debe ser un valor entre 0-10 (ambos inclusive).");
		}
		
	}
	
	protected void advance(int time) {
		 int antUbication=ubication;
		 int c;
		 
		if(statusV == VehicleStatus.TRAVELING) {
			
			if(ubication + actSpeed < road.longitud) {
				ubication += actSpeed;
				totalDistance += actSpeed;
			}
			else {
				totalDistance = totalDistance + (road.longitud-ubication);
				ubication = road.longitud;
			}
			
			c = gradeContamination * (ubication - antUbication);
			totalContamination += c;
			
			road.addContamination(c);
			
			if(ubication == road.longitud) {
				
				road.cruceDestino.enter(this);
				numJunc++;
				
				statusV = VehicleStatus.WAITING;
				
				this.actSpeed = 0;
			}
			
		}
	}
	
	protected void moveToNextRoad() {
		if(numJunc == (itinerary.size()-1)) {
			statusV = VehicleStatus.ARRIVED;
			this.actSpeed = 0;
			road.exit(this);
		}
		
		else if(statusV == VehicleStatus.PENDING) {
			road = itinerary.get(numJunc).roadTo(itinerary.get(numJunc+1));
			road.enter(this);
			statusV = VehicleStatus.TRAVELING;
		}
		
		else if (statusV == VehicleStatus.WAITING){
			this.ubication = 0;
			this.actSpeed = 0;
			road.cruceDestino.roadTo(itinerary.get(numJunc+1)).enter(this); // entra bien?
			statusV = VehicleStatus.TRAVELING;
			if(road != null) {road.exit(this);}
			road = road.cruceDestino.roadTo(itinerary.get(numJunc+1));
		}
		
		else {throw new IllegalArgumentException("El estado del vehiculo debe ser pending o waiting");}

	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		
		jo.put("id", this._id);
		jo.put("speed", this.actSpeed);
		jo.put("distance", this.totalDistance);
		jo.put("co2", this.totalContamination);
		jo.put("class", this.gradeContamination);
		jo.put("status", this.statusV);
		
		if(statusV != VehicleStatus.ARRIVED) {
		jo.put("road", this.road._id);
		jo.put("location", this.ubication);
		}
		return jo;
	}

	public VehicleStatus getStatus() {
		return statusV;
	}

	public Road getRoad() {
		return road;
	}

	public int getLocation() {
		return ubication;
	}

	public int getContClass() {
		// TODO Auto-generated method stub
		return gradeContamination;
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getActSpeed() {
		return actSpeed;
	}

	public int getTotalContamination() {
		return totalContamination;
	}

	public int getTotalDistance() {
		return totalDistance;
	}
	 

}
