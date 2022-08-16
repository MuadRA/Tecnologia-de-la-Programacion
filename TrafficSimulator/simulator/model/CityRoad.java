package simulator.model;

public class CityRoad extends Road{
	
	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather){
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		
	}

	protected void reduceTotalContamination() {
		
		if(weatherConditions == Weather.WINDY || weatherConditions == Weather.STORM) {
			totalContamination -= 10;
		}else{
			totalContamination -= 2;
		}
		
		if(totalContamination < 0) {
			totalContamination = 0;
		}
	}
	
	protected int calculateVehicleSpeed(Vehicle v) {
		return (int) Math.ceil((((11.0 - v.gradeContamination)) / 11.0) * limitSpeed);
	}
	
	protected void updateSpeedLimit() {	
		limitSpeed = maxSpeed;
	}
}
