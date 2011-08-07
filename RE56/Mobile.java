package RE56;

/**
 *
 * @author Hyacinthe Cartiaux
 * @author Nicolas Henry
 */
public class Mobile {

    public double Pm;
    public double Pr;
    public double d;
    public double snr;

    public int step;

    public static final int VOICE = 0;
    public static final int DATA = 1;
    public static final int VISIO = 2;

    public int service;

    private boolean active;

    public Mobile(boolean active, int service) {
        this.active = active;
        this.service = service;
	initialize();
	genStep();
    }

    public int genStep()
    {
	int i;

	i = (int) Math.round(Math.random() * 10)  % 4;
	if (i == 0)
	  step = -2;
	else if (i == 1)
	  step = -1;
	else if (i == 2)
	  step = 1;
	else if (i == 3)
	  step = 2;

	return step;
    }

    public void initialize()
    {
	Pm = 0.1;
	Pr = 0;
	snr = 0;
    }

    public boolean isActivated() {
        return this.active;
    }

    public boolean setActivated(boolean active) {
        this.active = active;
        return this.active;
    }

    public void setDistance(double d)
    {
	this.d = d;
    }

    public void setMode(int mode)
    {
	this.service = mode;
    }

    public double targetSnr()
    {
        if (service == VOICE)
            return -20;
        else if (service == DATA)
            return -15.3;
        else if (service == VISIO)
            return -12;
        else return 0;
    }
}
