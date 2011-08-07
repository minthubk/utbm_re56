package RE56;

import java.lang.Thread;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 *
 * @author Hyacinthe Cartiaux
 * @author Nicolas Henry
 */
public class Main {

    private static JFrame frame;
    private static Container contentPane;

    public static ArrayList mobilePanelList;
    public static int nbrMobiles;

    public static Simulation sim;

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        frame = new JFrame("Management of cell phones power v1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setJMenuBar(new MenuBar());

        contentPane = frame.getContentPane();

        contentPane.setLayout(new GridBagLayout());

	nbrMobiles = 1;
        displayMobile(5);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	frame.setLocationRelativeTo(null);

        frame.setResizable(true);

        frame.setVisible(true);

    }

    public static void displayMobile(int nbr) {
	nbrMobiles = nbr;
        contentPane.removeAll();
  
	mobilePanelList = new ArrayList(nbr);
 
        GridBagConstraints c = new GridBagConstraints();

        for (int i = 0; i < nbr; i++) {
            MobilePanel m = new MobilePanel(i + 1, true);
	    
	    mobilePanelList.add((Object) m);

            c.gridx = 0;
            c.gridy = i;

            frame.getContentPane().add(m, c);
        }

        frame.pack();
    }


    public static void main(String[] args)
    {
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });

	/* ArrayList mobiles = new ArrayList(5);
	Mobile m;


	m = new Mobile(true, Mobile.DATA);
	m.d = 5;
	m.service = Mobile.VISIO;
        mobiles.add((Object) m);

	m = new Mobile(true, Mobile.DATA);
	m.d = 5;
	m.service = Mobile.VOICE;
        mobiles.add((Object) m);

	m = new Mobile(true, Mobile.DATA);
	m.d = 5;
	m.service = Mobile.DATA;
        mobiles.add((Object) m);

	m = new Mobile(true, Mobile.DATA);
	m.d = 5;
	m.service = Mobile.VISIO;
        mobiles.add((Object) m);

	m = new Mobile(true, Mobile.DATA);
	m.d = 30;
	m.service = Mobile.VISIO;
        mobiles.add((Object) m);


        new Thread(new Simulation(5, mobiles)).start();

	*/
    }
}
