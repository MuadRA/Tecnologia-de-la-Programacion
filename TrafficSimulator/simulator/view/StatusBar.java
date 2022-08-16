package simulator.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	private Controller control;
	private int _time;
	private JPanel p2;
	private JLabel lbl;
	private JLabel lbl2;
	public StatusBar(Controller _ctrl) {
		_ctrl.addObserver(this);
		init();
	}
	
	public void init() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		lbl = new JLabel("Time: " + _time);
		lbl2 = new JLabel(" ");
		lbl2.setAlignmentX(LEFT_ALIGNMENT);
		p2 = new JPanel();
		p2.add(lbl2);
		
		this.add(lbl);
		this.add(p2);
	}
	
	private void updateTime(int time) {
		lbl.setText("Time: " + time);
	}
	
	private void updateEvent(Event e) {
		lbl2.setText("Event Added: " + e.toString());
	}
	
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		updateTime(time);
	}
	
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		lbl2.setText("");
	}

	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		updateEvent(e);
	}

	public void onReset(RoadMap map, List<Event> events, int time) {
		
	}

	public void onRegister(RoadMap map, List<Event> events, int time) {
		
	}

	public void onError(String err) {
		
	}

}
