package simulator.model;

public abstract class Event {

	protected int _time;

	protected Event(int time) {
		if (time < 1)
			throw new IllegalArgumentException("Time must be positive (" + time + ")");
		else
			_time = time;
	}

	public int getTime() {
		return _time;
	}

	abstract void execute(RoadMap map);
}
