package simulator.model;

public class NewInterCityRoadEvent extends Event{
	protected String id;
	protected String srcJunc;
	protected String destJunc;
	protected int length;
	protected int co2limit;
	protected int maxSpeed;
	protected Weather weather;
	
	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
			super(time);
			this.id = id;
			this.srcJunc = srcJun;
			this.destJunc = destJunc;
			this.length = length;
			this.co2limit = co2Limit;
			this.maxSpeed = maxSpeed;
			this.weather = weather;
			}

	void execute (RoadMap map) {
		Road r = new InterCityRoad(id,map.getJunction(srcJunc),map.getJunction(destJunc),maxSpeed,co2limit,length,weather);
		map.addRoad(r);
	}
	
	public String toString() {
		return "New Road '"+id+"'";
	}
}
