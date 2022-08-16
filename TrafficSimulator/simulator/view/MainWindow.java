package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import extra.jtable.EventEx;
import simulator.control.Controller;
import simulator.model.Event;

public class MainWindow extends JFrame{
	private Controller _ctrl;

	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		// Events Table
		EventsTableModel evTab = new EventsTableModel(_ctrl);
		JTable evTable = new JTable(evTab);
		JPanel eventsView = createViewPanel(evTable, "Events");
		JScrollPane scroll = new JScrollPane(evTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		scroll.getViewport().setBackground(Color.WHITE);
		evTable.setShowGrid(false);
		eventsView.setPreferredSize(new Dimension(500, 200));
		eventsView.add(scroll);
		tablesPanel.add(eventsView);
		
		// Vehicle Table
		VehiclesTableModel vehTab = new VehiclesTableModel(_ctrl);
		JTable vehTable = new JTable(vehTab);
		JPanel vehiclesView = createViewPanel(vehTable,"Vehicles");
		JScrollPane scroll2 = new JScrollPane(vehTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll2.getViewport().setBackground(Color.WHITE);
		vehTable.setShowGrid(false);
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		vehiclesView.add(scroll2);
		tablesPanel.add(vehiclesView);
		
		//Roads Table
		RoadsTableModel roadTab = new RoadsTableModel(_ctrl);
		JTable roadTable = new JTable(roadTab);
		JPanel roadsView = createViewPanel(roadTable,"Roads");
		JScrollPane scroll3 = new JScrollPane(roadTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll3.getViewport().setBackground(Color.WHITE);
		roadTable.setShowGrid(false);
		roadsView.setPreferredSize(new Dimension(500, 200));
		roadsView.add(scroll3);
		tablesPanel.add(roadsView);
		
		//Junctions Table
		JunctionsTableModel juncTab = new JunctionsTableModel(_ctrl);
		JTable juncTable = new JTable(juncTab);
		JPanel junctionsView = createViewPanel(juncTable,"Junctions");
		JScrollPane scroll4 = new JScrollPane(juncTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll4.getViewport().setBackground(Color.WHITE);
		juncTable.setShowGrid(false);
		junctionsView.setPreferredSize(new Dimension(500,200));
		junctionsView.add(scroll4);
		tablesPanel.add(junctionsView);
		// ...
		
		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		// TODO add a map for MapByRoadComponent
		JPanel mapComponent = createViewPanel(new MapByRoadComponent2(_ctrl),"Map by Road");
		mapComponent.setPreferredSize(new Dimension(500,400));
		mapsPanel.add(mapComponent);
		// ...
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel(new BorderLayout());
		Border blackline = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,3), title);
		p.add(new JScrollPane(c));
		p.setBorder(blackline);
		return p;
	}

}
