package simulator.model;

public class InterCityRoad extends Road {

		InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather){
			super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

		protected void reduceTotalContamination() {
			int x = 0;
			int reduce;
			double aux;
			if(weatherConditions== Weather.SUNNY) {
				x = 2;
			}else if(weatherConditions==Weather.CLOUDY) {
				x = 3;
			}else if(weatherConditions==Weather.RAINY) {
				x = 10;
			}else if(weatherConditions==Weather.WINDY) {
				x = 15;
			}else if(weatherConditions==Weather.STORM) {
				x = 20;
			}
			aux = (100.00-x)/100.00;
			totalContamination = ((int)(aux*totalContamination));
		}
		
		protected void updateSpeedLimit() {
			
			if(limitContamination < totalContamination) {
				limitSpeed= (int)(maxSpeed*0.5);
			}else {
				limitSpeed=maxSpeed;
			}
			
		}
		
		protected int calculateVehicleSpeed(Vehicle v) {
			if(weatherConditions == Weather.STORM) {
				return (int)(limitSpeed*0.8);
			}
			else {
				return limitSpeed;
			}
		}

}
