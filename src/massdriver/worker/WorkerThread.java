package massdriver.worker;

import massdriver.ComputationResult;

public strictfp class WorkerThread extends Thread {
	final private HSTPRNG random;
	final private double CC;
	final private double CS;
	final private long iterations;
	final private double startH;
	final private double stepH;
	final private int h;
	
	public ComputationResult result = null;	// filled in upon completion
	
	// helper variables
	final private double C;
	final private double NINV_C;
	final private double P_ABSOR;
	final private double P_BOUNC;

	public WorkerThread(double startH, double stepH, int h, long iterations, double CC, double CS, HSTPRNG random) {
		this.startH = startH;
		this.stepH = stepH;
		this.h = h;
		this.iterations = iterations;
		this.CC = CC;
		this.CS = CS;
		this.random = random;
		
		// compute helpers
		this.C = CC + CS;
		this.NINV_C = -1/this.C;
		this.P_ABSOR = CC / this.C;
		this.P_BOUNC = CS / this.C;
	}
	
	
	public void run() {
		
		ComputationResult result = new ComputationResult(this.startH, this.stepH, this.h);

		double H = this.startH;
		
		for (int k=0; k<this.h; k++) {
			long transmitted = 0;
			long absorbed = 0;
			long reflected = 0;
			
			for (long i=0; i<this.iterations; i++) {
				// simulate a single neutron
				double x = 0;
				double dir = 0;
				
				while (true) {
					x += this.NINV_C * Math.log(this.random.nextDouble()) * Math.cos(dir);
					if (x < 0) { reflected++; break; }
					if (x >= H) { transmitted++; break; }
					if (this.random.nextDouble() < this.P_ABSOR) { absorbed++; break; }				
					dir = this.random.nextDouble() * Math.PI;
				}
			}
			
			result.extend(this.iterations, transmitted, reflected, absorbed);
			H += this.stepH;
		}

		this.result = result;
	}
}
