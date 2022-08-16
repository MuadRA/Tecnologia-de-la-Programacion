package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver {
	private Controller control;
	private RoadMap _map;
	private int timeAct;
	private boolean _stopped;
	private Object[] options = {"Si","No"};
	private JButton cargar;
	private JButton contClass;
	private JButton weather;
	private JButton play;
	private JButton pause;
	private JButton exit;
	
	public ControlPanel(Controller control) {
		this.control = control;
		this.control.addObserver(this);
		init();
	}
	
	public void init() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		cargar = new JButton();
		contClass = new JButton();
		weather = new JButton();
		play = new JButton();
		pause = new JButton();
		exit = new JButton();
		
		JLabel lblticks = new JLabel("Ticks:");
	    JSpinner ticksF = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		ticksF.setMaximumSize(new Dimension(80, 40));
		ticksF.setMinimumSize(new Dimension(80, 40));
		ticksF.setPreferredSize(new Dimension(80, 40));
		
		ImageIcon icono = new ImageIcon("resources/icons/open.png");
		cargar.setIcon(icono);
		icono = new ImageIcon("resources/icons/co2class.png");
		contClass.setIcon(icono);
		icono = new ImageIcon("resources/icons/weather.png");
		weather.setIcon(icono);
		icono = new ImageIcon("resources/icons/run.png");
		play.setIcon(icono);
		icono = new ImageIcon("resources/icons/stop.png");
		pause.setIcon(icono);
		icono = new ImageIcon("resources/icons/exit.png");
		exit.setIcon(icono);
		
		this.add(cargar);
		this.add(contClass);
		this.add(weather);
		this.add(play);
		this.add(pause);
		this.add(lblticks);
		this.add(ticksF);
		this.add(new JSeparator());
		this.add(exit);
		
		cargar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				loadFile();
			}
			
		});
		
		contClass.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				selectClass();
			}
			
		});
		
		weather.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				selectWeather();
			}
			
		});
		
		play.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int n = (int) ticksF.getValue();
				enableToolBar(false);
				_stopped = false;
				run_sim(n);
			}
			
		});
		
		pause.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				stop();
				enableToolBar(true);
			}
			
		});
		
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showOptionDialog(new Frame(), "¿Estas seguro que deseas salir?", "Exit Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,null);
				if (n == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
			
		});
		
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
			control.run(1,null);
			} 	catch (Exception e) {
				// TODO show error message
			_stopped = true;
			return;
			}
			SwingUtilities.invokeLater(new Runnable() {
			
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}
	
	private void stop() {
		_stopped = true;
	}
	
	private void enableToolBar(boolean act) {
		if(act == true) {
			cargar.setEnabled(true);
			contClass.setEnabled(true);
			weather.setEnabled(true);
			play.setEnabled(true);
			exit.setEnabled(true);
		}
		
		else {
			cargar.setEnabled(false);
			contClass.setEnabled(false);
			weather.setEnabled(false);
			play.setEnabled(false);
			exit.setEnabled(false);
		}
	}
	
	private void selectClass() {
		ChangeCO2ClassDialog change = new ChangeCO2ClassDialog((Frame) SwingUtilities.getWindowAncestor(this));
		int pulsado = change.open(_map.getVehicles());
		
		if(pulsado == 1) {
			SetContClassEvent e;
			List<Pair<String,Integer>> par = new ArrayList<Pair<String,Integer>>();
			Pair<String,Integer> aux = new Pair<String,Integer>(change.getVehicleId(), change.getContClass());
			par.add(aux);
			e = new SetContClassEvent(change.getTicks()+timeAct,par);
			control.addEvent(e);
		}
	}
	
	private void selectWeather() {
		ChangeWeatherDialog weath = new ChangeWeatherDialog((Frame) SwingUtilities.getWindowAncestor(this));
		int pulsado = weath.open(_map.getRoads());
		
		if(pulsado == 1) {
			SetWeatherEvent sw;
			List<Pair<String,Weather>> ws = new ArrayList<Pair<String,Weather>>();
			Pair<String,Weather> aux = new Pair<String,Weather>(weath.getRoadId(),weath.getWeather());
			ws.add(aux);
			sw = new SetWeatherEvent(weath.getTicks()+timeAct,ws);
			control.addEvent(sw);
		}
	}
	
	private void loadFile() {
	    JFileChooser fileChooser = new JFileChooser();
	    FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.JSON", "JSON");
	    fileChooser.setFileFilter(filtro);
	    
	   int seleccion = fileChooser.showOpenDialog(null);
	   if(seleccion == JFileChooser.APPROVE_OPTION) {
		   File fichero = fileChooser.getSelectedFile();
			InputStream targetStream;
			try {
				targetStream = new FileInputStream(fichero);
				control.reset();
				control.loadEvents(targetStream);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "El archivo no existe", "Error message", JOptionPane.WARNING_MESSAGE);
			}
	   }
	}
	
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_map = map;
		timeAct = time;
	}

	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_map = map;
	}

	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_map = map;
		timeAct = time;
	}

	public void onReset(RoadMap map, List<Event> events, int time) {
		
	}

	public void onRegister(RoadMap map, List<Event> events, int time) {
		
	}

	public void onError(String err) {
		
	}

}
