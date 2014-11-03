package massdriver.coordinator;

import java.rmi.RemoteException;

import massdriver.ComputationResult;
import massdriver.ComputationService;

public class ExecutorThread extends Thread {

	private ComputationService compServ;
	private int threadsThere;
	
	// computation parameters
	private double startH, stepH;
	private int h;
	private double CC, CS;
	private long iterations;
	
	private long seed;
	private int noStream, maxStreams;
	
	private String nodename;
	
	public ComputationResult result = null;	// available upon completion, or null if failure occurred
	
	public ExecutorThread(ComputationService compServ, int threads, double startH, double stepH, int h, double CC, double CS,
						  long iterations, long seed, int noStream, int maxStreams, String nodename) {
		this.compServ = compServ;
		this.threadsThere = threads;
		this.startH = startH;
		this.stepH = stepH;
		this.h = h;
		this.nodename = nodename;
		this.CC = CC;
		this.CS = CS;
		this.iterations = iterations;
		this.seed = seed;
		this.noStream = noStream;
		this.maxStreams = maxStreams;
	}
	
	public void run() {
		// let's run it ;)
		if (this.compServ == null) return; 
		
		try {
			this.result = this.compServ.compute(this.startH, this.stepH, this.h, this.iterations, 
												this.CC, this.CS, this.noStream, this.maxStreams, this.seed);
		} catch (RemoteException e) {
			// !!!!!!!!!! REMOTE NODE HAS FAILED !!!!
			System.out.format("WARNING: Remote node %s has failed. Its computations will not be taken into account.\n", this.nodename);
		}				
	}
	
}
