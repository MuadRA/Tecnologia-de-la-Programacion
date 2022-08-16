package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {
	
	private int pulsa;
	private JComboBox<Road> road;
	private DefaultComboBoxModel<Road> roadModel;
	JComboBox<Weather> comboBox1;
	JSpinner ticksF;
	
	public ChangeWeatherDialog(Frame frame) {
		super(frame,true);
		initGUI();
	}
	
	public void initGUI() {
		setTitle("Change Road Weather");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		JLabel info = new JLabel("<html>"+"Schedule an event to change the Weather of a road after a given number of simulation ticks from now on"+"<html>");
		info.setAlignmentX(CENTER_ALIGNMENT);
		
		JPanel boxPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.X_AXIS));
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel lblro = new JLabel("Road: ");
		roadModel = new DefaultComboBoxModel<>();
		road = new JComboBox<>(roadModel);
		road.setMaximumSize(new Dimension(80, 20));
		road.setMinimumSize(new Dimension(80, 20));
		
		JLabel lblwea = new JLabel("Weather: ");
		DefaultComboBoxModel<Weather> weath = new DefaultComboBoxModel<Weather>();
		
		weath.addElement(Weather.SUNNY);
		weath.addElement(Weather.CLOUDY);
		weath.addElement(Weather.RAINY);
		weath.addElement(Weather.WINDY);
		weath.addElement(Weather.STORM);
		
		comboBox1 = new JComboBox<Weather>(weath);
		comboBox1.setMaximumSize(new Dimension(80, 20));
		comboBox1.setMinimumSize(new Dimension(80, 20));
		comboBox1.setEditable(true);
		
		JLabel lblticks = new JLabel("Ticks:");
	    ticksF = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		ticksF.setMaximumSize(new Dimension(80, 20));
		ticksF.setMinimumSize(new Dimension(80, 20));
		ticksF.setPreferredSize(new Dimension(80, 20));
		
		boxPanel.add(lblro);
		boxPanel.add(road);
		boxPanel.add(lblwea);
		boxPanel.add(comboBox1);
		boxPanel.add(lblticks);
		boxPanel.add(ticksF);
		
		mainPanel.add(info);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(boxPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(buttonsPanel);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pulsa = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
					pulsa = 1;
					ChangeWeatherDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(okButton);
		
		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}

	protected int open(List<Road> roads) {
		roadModel.removeAllElements();
		for (Road r : roads)
			roadModel.addElement(r);
		
		setVisible(true);
		return pulsa;
	}
	
	protected String getRoadId() {
		Road r =(Road) road.getSelectedItem();
		
		return r.getId();
	}
	
	protected Weather getWeather() {
		return (Weather) comboBox1.getSelectedItem();
	}
	
	protected int getTicks() {
		return (int) ticksF.getValue();
	}
	
	
}
