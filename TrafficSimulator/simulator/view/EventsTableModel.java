package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{
	private List<Event> _events;
	private String[] _colNames = { "Time", "Desc." };
	
	public EventsTableModel(Controller cont) {
		cont.addObserver(this);
	}
		
	
	public void setEventsList(List<Event> events) {
		_events = events;
	}
	
	public int getColumnCount() {
		return _colNames.length;
	}

	public int getRowCount() {
		return _events == null ? 0 : _events.size();
	}
	
	public String getColumnName(int col) {
		return _colNames[col];
	}
	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _events.get(rowIndex).getTime();
			break;
		case 1:
			s = _events.get(rowIndex).toString();
			break;
		}
		return s;
	}
	
	public void update() {
		fireTableDataChanged();		
	}
	
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_events = events;
		update();
	}

	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}

	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_events = events;
		update();
	}

	public void onReset(RoadMap map, List<Event> events, int time) {
		
	}

	public void onRegister(RoadMap map, List<Event> events, int time) {
		
	}

	public void onError(String err) {
		
	}

}
