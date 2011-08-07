package RE56;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 *
 * @author Hyacinthe Cartiaux
 * @author Nicolas Henry
 */
public class MobileSettings implements ActionListener {

    private JFrame frame;
    private SpinnerNumberModel numbermodel; //, speedmodel;
    private JButton b;

    public MobileSettings() {
        frame = new JFrame("Mobile settings");

        Container contentPane = frame.getContentPane();

        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(3, 3, 3, 3);

        c.gridx = 1;
        c.gridy = 1;

        contentPane.add(new JLabel("Number of Mobiles"), c);

        numbermodel = new SpinnerNumberModel(5, 1, 7, 1);
        JSpinner number = new JSpinner(numbermodel);

        c.gridx = 2;
        c.gridy = 1;

        contentPane.add(number, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;

        b = new JButton("Validate");

        b.addActionListener(this);

        contentPane.add(b, c);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = screenSize.width / 3;
        int height = screenSize.height / 4;
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 3;

        frame.setLocation(x, y);
        frame.setResizable(true);
        frame.setVisible(true);

        frame.pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == b)
        {
	    if (Main.sim != null)
	    {
		Main.sim.stopThread();
		Main.sim = null;
	    }
            frame.setVisible(false);
            Main.displayMobile(numbermodel.getNumber().intValue());
        }
    }
}
