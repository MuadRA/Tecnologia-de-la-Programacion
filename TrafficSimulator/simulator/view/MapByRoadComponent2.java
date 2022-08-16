package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent2 extends JComponent implements TrafficSimObserver {
	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final Color _BLACK_LIGHT_COLOR = Color.BLACK;

	private RoadMap _map;
	
	MapByRoadComponent2(Controller ctrl){
		ctrl.addObserver(this);
		setPreferredSize(new Dimension(300,200));
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}
	
	
	private void drawMap(Graphics g) {
		drawRoads(g);
	}
	
	private void drawRoads(Graphics g) {
		int i = 0;
		
		for(Road r : _map.getRoads()) {
			int x1 = 50;
			int y1 = (i+1)*50;
			int x2 = getWidth()-100;
			int y2 = (i+1)*50;
			
			//junctions
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y1 - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			
			Color juncCol = _RED_LIGHT_COLOR;
			int idx = r.getDest().getGreenLightIndex();
			if (idx != -1 && r.equals(r.getDest().getInRoads().get(idx))) {
				juncCol = _GREEN_LIGHT_COLOR;
			}
			g.setColor(juncCol);
			g.fillOval(x2 - _JRADIUS / 2, y2 - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			
			//road color
			g.setColor(_BLACK_LIGHT_COLOR);
			g.drawLine(x1, y1, x2, y2);
			
			g.setColor(_BLACK_LIGHT_COLOR);
			g.drawString(r.getId(), x1-20, y1);
			
			drawWeatherCond(g,i,r.getWeatherConditions());
			
			drawContamination(g,i,r);
			
			drawVehicles(g,i,r);
			
			i++;
		}
	}

	private void drawWeatherCond(Graphics g, int i, Weather weatherConditions) {
		int x2 = getWidth()-100;
		int y2 = (i+1)*50;
		
		if(weatherConditions == Weather.CLOUDY) {
			g.drawImage(loadImage("cloud.png"), x2+15, y2-15, 32, 32, this);
		}
		else if(weatherConditions == Weather.RAINY) {
			g.drawImage(loadImage("rain.png"), x2+15, y2-15, 32, 32, this);
		}
		else if(weatherConditions == Weather.STORM) {
			g.drawImage(loadImage("storm.png"), x2+15, y2-15, 32, 32, this);
		}
		else if(weatherConditions == Weather.SUNNY) {
			g.drawImage(loadImage("sun.png"), x2+15, y2-15, 32, 32, this);
		}
		else if(weatherConditions == Weather.WINDY) {
			g.drawImage(loadImage("wind.png"), x2+15, y2-15, 32, 32, this);
		}
	}
	
	private void drawContamination(Graphics g, int i, Road r) {
		int x2 = getWidth()-100;
		int y2 = (i+1)*50;
		int A = r.getTotalCO2();
		int B = r.getCO2Limit();
		int C = (int) Math.floor(Math.min((double) A/(1.0 + (double) B),1.0) / 0.19);
		
		g.drawImage(loadImage("cont_" + C + ".png"), x2+55, y2-15, 32, 32, this);
		
	}


	private void drawVehicles(Graphics g, int i, Road r1) {
		int x1 = 50;
		int y1 = (i+1)*50;
		int x2 = getWidth()-100;
		int y2 = (i+1)*50;
		
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				Road r2 = v.getRoad();
				if(r1 == r2) {
					
					double roadLength = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
					double alpha = Math.atan(((double) Math.abs(x1 - x2)) / ((double) Math.abs(y1 - y2)));
					double relLoc = roadLength * ((double) v.getLocation()) / ((double) r2.getLength());
					int x = x1 + (int) ((x2 - x1) * ((double) relLoc / (double) roadLength));
					int y = (int) (Math.cos(alpha) * relLoc);
				
					int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
					g.setColor(new Color(0, vLabelColor, 0));
	
					g.drawImage(loadImage("car.png"), x, y2-10, 16, 16, this);
					g.drawString(v.getId(), x, y - 6);
				}
			}
		}
	}
	

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		setPreferredSize(new Dimension(maxW, maxH));
		setSize(new Dimension(maxW, maxH));
	}
	
	public void update(RoadMap map) {
		_map = map;
		repaint();
	}
	
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}

	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	public void onError(String err) {
		
	}

}
