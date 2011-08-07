package RE56;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 *
 * @author Hyacinthe Cartiaux
 * @author Nicolas Henry
 */
public class MobilePanel extends JPanel implements ChangeListener, ItemListener {

    private JTextField powerReceived, powerSent, csi;
    private JSlider distance;
    private JButton led;
    private JLabel infoLabel;
    private JCheckBox activated;
    private JCheckBox move;
    private JComboBox comboBoxMode;
    private String voice;
    private String data;
    private String video;

    private int number;

    public void stateChanged(ChangeEvent e) {
	if (e.getSource() == distance && Main.sim != null)
	    Main.sim.setDistance(this.number, new Double(((JSlider)e.getSource()).getValue()));
	else if (e.getSource() == activated && Main.sim != null)
	{
	    Main.sim.activate(this.number, isActivated());

	    if (!isActivated())
	    {
		clear();
		Main.sim.initialize(this.number);
	    }
	}

	else if (e.getSource() == move && Main.sim != null)
	{
	    if (!isActivated())
	    {
		Main.sim.genStep(this.number);
	    }
	}
    }

    public void itemStateChanged(ItemEvent e) {
	if (Main.sim != null) {
	  Main.sim.setMode(this.number, this.getMode());
	}
    }

    public MobilePanel(int nbr, boolean random) {

	this.number = nbr;

        setBorder(BorderFactory.createTitledBorder("Mobile " + new java.lang.Integer(nbr).toString()));

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();


	activated = new JCheckBox();
	activated.setSelected(true);

	activated.addChangeListener(this);

	move = new JCheckBox();
	move.setSelected(true);

        powerReceived = new JTextField(4);
	powerReceived.setColumns(5);
        powerSent = new JTextField(4);
	powerSent.setColumns(5);
        csi = new JTextField(4);
	csi.setColumns(5);

	comboBoxMode = new JComboBox();
	comboBoxMode.addItemListener(this);
	voice = "Voice (-20)";
	data  = "Data (-15.3)";
	video = "Visio (-12)";

        comboBoxMode.addItem(voice);
        comboBoxMode.addItem(data);
        comboBoxMode.addItem(video);


        distance = new JSlider(JSlider.HORIZONTAL, 5, 50, 5);

        distance.setMinorTickSpacing(1);
        distance.setMajorTickSpacing(5); 
        distance.setPaintTicks(true);
        distance.setPaintLabels(true);

        // We'll just use the standard numeric labels for now...
        distance.setLabelTable(distance.createStandardLabels(10));

	distance.addChangeListener(this);

        c.gridx = 1;
        c.gridy = 1;

	add(new JLabel("Move : "), c);

        c.gridx = 2;

	add(move, c);

	c.gridx = 3;

	add(new JSeparator(JSeparator.VERTICAL), c);

	c.gridx = 4;

	add(new JLabel("Enable : "), c);

        c.gridx = 5;

	add(activated, c);

	c.gridx = 6;

	add(new JSeparator(JSeparator.VERTICAL), c);

        c.gridx = 7;

        add(distance, c);

	c.gridx = 8;

	add(new JLabel("km"), c);

        c.gridx = 9;

	add(new JSeparator(JSeparator.VERTICAL), c);

        c.gridx = 10;

        add(new JLabel(" Power received : "), c);

        c.gridx = 11;

        add(powerReceived, c);

	c.gridx = 12;

	add(new JLabel("dBm"), c);

	c.gridx = 13;

	add(new JSeparator(JSeparator.VERTICAL), c);

        c.gridx = 14;

        add(new JLabel(" Power sent : "), c);

        c.gridx = 15;
        add(powerSent, c);

	c.gridx = 16;

	add(new JLabel("W"), c);

	c.gridx = 17;

	add(new JSeparator(JSeparator.VERTICAL), c);

	c.gridx = 18;

	add(new JLabel("Service : "), c);

	c.gridx = 19;

        add(comboBoxMode, c);

	c.gridx = 20;

        add(new JLabel(" C/I : "), c);

        c.gridx = 21;

        add(csi, c);

	c.gridx = 22;

	add(new JSeparator(JSeparator.VERTICAL), c);

	c.gridx = 23;
	led = new JButton("  ");
	led.setOpaque(true);
	led.setBackground(java.awt.Color.BLUE);
	add(led, c);

	c.gridx = 12;
	c.gridy = 2;
	c.gridwidth = 11;
	infoLabel = new JLabel(" ");
	add(infoLabel, c);


	if (random)
	{
	    setDistance((int) Math.round(Math.random() * 45) + 5);
	    comboBoxMode.setSelectedIndex((int) Math.round(Math.random() * 10) % 3);
	}

    }

    public void clear()
    {
	powerReceived.setText("");
	powerSent.setText("");
	csi.setText("");
	led.setBackground(java.awt.Color.BLUE);
	setLabel(" ");
    }

    public void setLedColor(java.awt.Color c)
    {
	led.setBackground(c);
    }

    public void setLabel(String str)
    {
      infoLabel.setText(str);
    }

    public void setSNR(double snr)
    {
        csi.setText((new Double(snr)).toString());
    }

    public void setPr(double pr)
    {
        powerReceived.setText((new Double(Math.round(pr * 100.0) / 100.0)).toString());
    }

    public void setPm(double pm)
    {
        powerSent.setText((new Double(Math.round(pm * 100.0) / 100.0)).toString());
    }

    public void setDistance(int n)
    {
	if (n < 5)
	  n = 5;
	if (n > 50)
	  n = 50;

	distance.setValue(n);
    }

    public int getDistance()
    {
	return distance.getValue();
    }

    public int getMode()
    {
	if (comboBoxMode.getSelectedItem() == voice) return 0;
	else if (comboBoxMode.getSelectedItem() == data) return 1;
	else if (comboBoxMode.getSelectedItem() == video) return 2;
	return 0;
    }

    public boolean isActivated()
    {
	return activated.isSelected();
    }
    
    public boolean move()
    {
	return move.isSelected();
    }

}
