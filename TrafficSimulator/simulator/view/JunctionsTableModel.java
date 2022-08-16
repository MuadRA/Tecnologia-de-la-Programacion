package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
	private List<Junction> _junctions;
	private String[] _colNames = { "Id", "Green","Queues" };
	
	public JunctionsTableModel(Controller _ctrl) {
		_ctrl.addObserver(this);
	}

	public int getColumnCount() {
		return _colNames.length;
	}

	public int getRowCount() {
		return _junctions == null ? 0 : _junctions.size();
	}
	
	public String getColumnName(int col) {
		return _colNames[col];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		int i;
		switch (columnIndex) {
		case 0:
			s = _junctions.get(rowIndex).getId();
			break;
		case 1:
			i = _junctions.get(rowIndex).getGreenLightIndex();
			if(i == -1)
				s = "NONE";
			else
				s = i;
			break;
		case 2:
			s = _junctions.get(rowIndex).getColas();
		}
		return s;
	}
	
	public void update() {
		fireTableDataChanged();		
	}
	
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_junctions = map.getJunctions();
		update();
	}

	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}

	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
	}

	public void onReset(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
