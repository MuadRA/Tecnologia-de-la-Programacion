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

import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {
	
	private int pulsa;
	private JComboBox<Vehicle> vehicle;
	private DefaultComboBoxModel<Vehicle> vehicleModel;
	JComboBox<Integer> comboBox1;
	JSpinner ticksF;
	
	public ChangeCO2ClassDialog(Frame frame){
		super(frame, true);
		initGUI();
	}
	
	private void initGUI() {
		
		setTitle("Change CO2 class");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		JLabel info = new JLabel("<html>"+"Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now on"+"<html>");
		info.setAlignmentX(CENTER_ALIGNMENT);
		
		JPanel boxPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.X_AXIS));
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel lblveh = new JLabel("Vehicle: ");
		vehicleModel = new DefaultComboBoxModel<>();
		vehicle = new JComboBox<>(vehicleModel);
		vehicle.setMaximumSize(new Dimension(80, 20));
		vehicle.setMinimumSize(new Dimension(80, 20));
		JLabel lblco2 = new JLabel("CO2 Class: ");
		DefaultComboBoxModel<Integer> co2nums = new DefaultComboBoxModel<Integer>();
		
		co2nums.addElement(0);
		co2nums.addElement(1);
		co2nums.addElement(2);
		co2nums.addElement(3);
		co2nums.addElement(4);
		co2nums.addElement(5);
		co2nums.addElement(6);
		co2nums.addElement(7);
		co2nums.addElement(8);
		co2nums.addElement(9);
		co2nums.addElement(10);
		
		comboBox1 = new JComboBox<Integer>(co2nums);
		comboBox1.setMaximumSize(new Dimension(80, 20));
		comboBox1.setMinimumSize(new Dimension(80, 20));
		comboBox1.setEditable(true);
		
		JLabel lblticks = new JLabel("Ticks:");
	    ticksF = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		ticksF.setMaximumSize(new Dimension(80, 20));
		ticksF.setMinimumSize(new Dimension(80, 20));
		ticksF.setPreferredSize(new Dimension(80, 20));
		
		boxPanel.add(lblveh);
		boxPanel.add(vehicle);
		boxPanel.add(lblco2);
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
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
					pulsa = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(okButton);
		
		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	protected int open(List<Vehicle> listVeh) {
		vehicleModel.removeAllElements();
		for (Vehicle v : listVeh)
			vehicleModel.addElement(v);
		
		setVisible(true);
		return pulsa;
	}
	
	protected String getVehicleId() {
		Vehicle v =(Vehicle) vehicle.getSelectedItem();
		
		return v.getId();
	}
	
	protected int getContClass() {
		return (int) comboBox1.getSelectedItem();
	}
	
	protected int getTicks() {
		return (int) ticksF.getValue();
	}
}
