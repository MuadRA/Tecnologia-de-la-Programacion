package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	private String[] colNames = {"Id","Length","Weather","Max. Speed","Speed Limit","Total CO2","CO2 Limit"};
	private List<Road> roadList;
	
	public RoadsTableModel(Controller _ctrl) {
		_ctrl.addObserver(this);
	}

	public int getColumnCount() {
		return colNames.length;
	}

	public int getRowCount() {
		return roadList == null ? 0 : roadList.size();
	}
	
	public String getColumnName(int col) {
		return colNames[col];
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch(columnIndex){
		case 0:
			s = roadList.get(rowIndex).getId();
			break;
		case 1:
			s = roadList.get(rowIndex).getLength();
			break;
		case 2:
			s = roadList.get(rowIndex).getWeatherConditions();
			break;
		case 3:
			s = roadList.get(rowIndex).getMaxSpeed();
			break;
		case 4:
			s = roadList.get(rowIndex).getLimitSpeed();
			break;
		case 5:
			s = roadList.get(rowIndex).getTotalCO2();
			break;
		case 6:
			s = roadList.get(rowIndex).getCO2Limit();
			break;
		}
		return s;
	}
	
	public void update() {
		fireTableDataChanged();		
	}

	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		roadList = map.getRoads();
		update();
	}

	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}

	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
	}

	public void onReset(RoadMap map, List<Event> events, int time) {
		
	}

	public void onRegister(RoadMap map, List<Event> events, int time) {
		
	}

	public void onError(String err) {
		
	}

}
