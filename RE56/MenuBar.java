package RE56;

import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author Hyacinthe Cartiaux
 * @author Nicolas Henry
 */
public class MenuBar extends JMenuBar implements ActionListener {

    JMenuItem nbrMobiles, start, stop, quit;

    public MenuBar() {
        JMenu settings, program;

        program = new JMenu("Program");
        settings = new JMenu("Settings");

        start = new JMenuItem("Start");
        start.addActionListener(this);
        program.add(start);

        stop = new JMenuItem("Stop");
        stop.addActionListener(this);
        program.add(stop);

        quit = new JMenuItem("Quit");
        quit.addActionListener(this);
        program.add(quit);

        nbrMobiles = new JMenuItem("Number of mobiles");
        nbrMobiles.addActionListener(this);

        settings.add(nbrMobiles);

        super.add(program);
        super.add(settings);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quit) { System.exit(0);
        } 
	else if (e.getSource() == nbrMobiles) { new MobileSettings(); } 
	else if (e.getSource() == start) { 
	  if (Main.sim == null)
	  {
	    Main.sim = new Simulation();
	    new Thread(Main.sim).start();
	  }
        }
	else if (e.getSource() == stop) { 
	  if (Main.sim != null)
	    Main.sim.stopThread();
	    Main.sim = null;
        }

    }
}
