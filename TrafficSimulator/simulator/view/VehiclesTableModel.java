package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{
	private String[] colNames = {"Id","Location","itinerary","CO2 Class","Max. Speed","Speed","Total CO2","Distance"};
	private List<Vehicle> vehList;
	
	public VehiclesTableModel(Controller _ctrl) {
		_ctrl.addObserver(this);
	}

	public int getColumnCount() {
		return colNames.length;
	}

	public int getRowCount() {
		return vehList == null ? 0 : vehList.size();
	}
	
	public String getColumnName(int col) {
		return colNames[col];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = vehList.get(rowIndex).getId();
			break;
		case 1:
			s = vehList.get(rowIndex).getRoad()+":"+vehList.get(rowIndex).getLocation();
			break;
		case 2:
			s = vehList.get(rowIndex).getItinerary();
			break;
		case 3:
			s = vehList.get(rowIndex).getContClass();
			break;
		case 4:
			s = vehList.get(rowIndex).getMaxSpeed();
			break;
		case 5:
			s = vehList.get(rowIndex).getActSpeed();
			break;
		case 6:
			s = vehList.get(rowIndex).getTotalContamination();
			break;
		case 7:
			s = vehList.get(rowIndex).getTotalDistance();
			break;
		}
		return s;
	}
	
	public void update() {
		fireTableDataChanged();		
	}
	
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		vehList = map.getVehicles();
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
