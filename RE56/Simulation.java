package RE56;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.Math;
import java.util.ArrayList;

/**
 *
 * @author Hyacinthe Cartiaux
 * @author Nicolas Henry
 */
public class Simulation implements Runnable {

    public int numberOfMobiles;
    double step = 0.05;
    double Gm = 2.15, Ga = 12;
    double f = 2200;
    double noise = -102;
    double[] tabServ = {-20, -15.3, -12};
    double PmMax = 1.55, PmMin = 0.1;
    // Nombre de cycle
    int N = 100; // soit une durée de 6.6 ms
    // Durée d'un cycle en microsecondes
    int cycle = 100; // en vrai 0.66 ms
    double tolerance = 2;

    double sumPr;
    int nbCycle = 0;
    //Variable de parcours des boucles

    int nbrMobiles;
    ArrayList mobiles;

    void setDistance(int nbrmobile, double distance)
    {
	((Mobile) mobiles.get(nbrmobile - 1)).setDistance(distance);
	System.out.println("Mobile " + nbrmobile + " Distance " + (new Double(distance)).toString());
    }

    void setMode(int nbrmobile, int mode)
    {
	((Mobile) mobiles.get(nbrmobile - 1)).setMode(mode);
	System.out.println("Mobile " + nbrmobile + " Mode " + (new Integer(mode)).toString());
    }

    void activate(int nbrmobile, boolean mode)
    {
	((Mobile) mobiles.get(nbrmobile - 1)).setActivated(mode);
    }

    void initialize(int nbrmobile)
    {
	((Mobile) mobiles.get(nbrmobile - 1)).initialize();
    }

    void genStep(int nbrmobile)
    {
	((Mobile) mobiles.get(nbrmobile - 1)).genStep();
    }


    double powerReceived(double Pm, double Gm, double Ga, double f, double d) {
        return (Pm + Gm + Ga - 20 * Math.log10(f) - 20 * Math.log10(d) - 32.44);
    }

    double watt2dBm(double P) {
        return (10 * Math.log10(1000 * P));
    }

    double dBm2watt(double P) {
        return (Math.pow(10, P / 10) / 1000);
    }

    double dB2watt(double P) {
        return (Math.pow(10, P / 10));
    }

    double watt2dB(double P) {
        return (10 * Math.log10(P));
    }

    double snr(double Pr, double sum, double noise) {
	double ratio = watt2dB(Pr / (sum - Pr + noise));
        return ratio;
    }

    /* public Simulation(int nbr_mobiles, ArrayList mobiles) {
        this.nbrMobiles = nbr_mobiles;
	this.mobiles = mobiles;
    } */

    public Simulation()
    {
	Mobile m;
	MobilePanel currentmp;
 
	this.nbrMobiles = Main.nbrMobiles;
	this.mobiles = new ArrayList(Main.nbrMobiles);

	for (int i = 0 ; i < Main.nbrMobiles ; i++)
	{
	  currentmp = (MobilePanel) Main.mobilePanelList.get(i);

	  m = new Mobile(currentmp.isActivated(), currentmp.getMode());
	  System.out.println("Mobile " + i + " Activated = " + currentmp.isActivated() + " Mode = " + currentmp.getMode() + " Distance = " + currentmp.getDistance());
	  m.d = currentmp.getDistance();
	  mobiles.add((Object) m);
	}
	
    }

    public volatile boolean stopThread = false;

    public void run() {
        int i;
        int nbrcycles = 0;
        Mobile currentM;
	MobilePanel currentmp;

	int cdist;

        while (!stopThread) {
                // traitement

                nbrcycles++;

                System.out.println(">-------- Cycle " + nbrcycles + " in progress ... --------<");
                System.out.println("1. Power received");

                sumPr = 0;

                for (i = 0 ; i < this.nbrMobiles ; i++)
                {
                   currentM = (Mobile) mobiles.get(i);
		   currentmp = (MobilePanel) Main.mobilePanelList.get(i);

		   if (currentM.isActivated() && currentmp.move())
		   {
			cdist = currentmp.getDistance();
			currentmp.setDistance(cdist + currentM.step);
		   }

		}

                for (i = 0 ; i < this.nbrMobiles ; i++)
                {
                   currentM = (Mobile) mobiles.get(i);
		   currentmp = (MobilePanel) Main.mobilePanelList.get(i);

                   if (currentM.isActivated())
                   {
                       currentM.Pr = powerReceived(watt2dBm(currentM.Pm), Gm, Ga, f, currentM.d);
                       System.out.println("Power received, cell phone " + i + " = " + currentM.Pr + " dBm");
		       currentmp.setPr(currentM.Pr);
                   }
                }

                for (i = 0 ; i < this.nbrMobiles ; i++)
                {
                    currentM = (Mobile) mobiles.get(i);
                    if (currentM.isActivated())
                    {
                        currentM = (Mobile) mobiles.get(i);
                        sumPr = sumPr + currentM.Pr;
                    }
                }

                System.out.println("2. Pr sum = " + sumPr + "dBm");

                System.out.println("3. C/I's calculation :");

                for (i = 0 ; i < this.nbrMobiles ; i++)
                {
                    currentM = (Mobile) mobiles.get(i);
		    currentmp = (MobilePanel) Main.mobilePanelList.get(i);

                    if (currentM.isActivated())
                    {
                        currentM.snr = snr(dBm2watt(currentM.Pr), dBm2watt(sumPr), dB2watt(noise));
                        System.out.println("C/I calculate, cell phone " + i + " = " + currentM.snr);
			currentmp.setSNR(currentM.snr);
                    }
                }

                System.out.println("4. Calculation of new C/I");

                for (i = 0 ; i < this.nbrMobiles ; i++)
                {
                    currentM = (Mobile) mobiles.get(i);
		    currentmp = (MobilePanel) Main.mobilePanelList.get(i);

                    if (currentM.isActivated())
                    {
		      if (currentM.snr < currentM.targetSnr())
		      {
			  if (currentM.Pm <= PmMax - step)
			  {
			    currentM.Pm = currentM.Pm + step;
                            System.out.println("C/I Required, cell phone " + i + " = " + currentM.targetSnr() + "dB");
                            System.out.println("Power of cell phone " + i + " = " + currentM.Pm + " W");
			    currentmp.setPm(currentM.Pm);
			    currentmp.setLabel("Progress");
			    currentmp.setLedColor(java.awt.Color.BLUE);
			  }
			  else
			  {
			    System.out.println("C/I Required, cell phone " + i + " = " + currentM.targetSnr() + "dB");
                            System.out.println("Maximum power of cell phone " + i + " attained! Service unavailable (" + PmMax + " W)");
			    currentmp.setLabel("Max power attained! Service unavailable");
			    currentmp.setLedColor(java.awt.Color.RED);
			  }
		      }
		      else if (currentM.snr > currentM.targetSnr() + tolerance)
		      {
			  if (currentM.Pm >= PmMin + step)
			  {
			      currentM.Pm = currentM.Pm - step;
			      System.out.println("C/I Required, cell phone " + i + " = " + currentM.targetSnr() + "dB");
			      System.out.println("Power of cell phone " + i + " = " + currentM.Pm + " W");
			      currentmp.setPm(currentM.Pm);
			      currentmp.setLabel("Progress");
			      currentmp.setLedColor(java.awt.Color.GREEN);
			  }
			  else
			  {
			      System.out.println("C/I Required, cell phone " + i + " = " + currentM.targetSnr() + "dB");
			      System.out.println("Minimum power of cell phone " + i + " attained! Service available (" + PmMin + " W)");
			      currentmp.setLabel("Minimum power attained! Service available");
			      currentmp.setPm(currentM.Pm);
			      currentmp.setLedColor(java.awt.Color.GREEN);
			  }

		      }
		      else
		      {
			System.out.println("C/I Required, cell phone " + i + " = " + currentM.targetSnr() + "dB");
                        System.out.println("Power of cell phone " + i + " = " + currentM.Pm + " W. Service Available!");
			currentmp.setPm(currentM.Pm);
			currentmp.setLabel("Service Available");
			currentmp.setLedColor(java.awt.Color.GREEN);
		      }
		    }
                }

		System.out.println(">-------- End Cycle" + nbrcycles + ". --------<");
		
		try { Thread.currentThread().sleep(500); } catch (Exception e) { }

        }
    }

    public void stopThread() {
        this.stopThread = true;

	MobilePanel currentmp;

	for (int i = 0 ; i < Main.nbrMobiles ; i++)
	{
	    currentmp = (MobilePanel) Main.mobilePanelList.get(i);
	    currentmp.clear();
	}

    }
}
