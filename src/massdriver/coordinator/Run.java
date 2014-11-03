package massdriver.coordinator;

import java.rmi.RMISecurityManager;
import java.util.Scanner;

import massdriver.ComputationResult;

public class Run {

	final static long seed = 985456376;
	
	public static void main(String[] args) {
		// Wow. This is a lot of things to do :)
		System.out.println("Mass Driver v1.0 coordinator");
		System.out.println("Krzysztof Golinowski, Piotr Maślanka");
		
		if (args.length == 0) {
			System.out.println("Please pass names of remote nodes in the form IPADDR:PORT as arguments.");
			System.out.println("Aborting.");
			return;
		}
		
		
		// get data from stdin
		double CC, CS;
		int H, iterations;
		System.out.print("Enter H, iterations, CC and CS: ");
		Scanner sin = new Scanner(System.in);
		H = sin.nextInt();
		iterations = sin.nextInt();
		CC = sin.nextDouble();
		CS = sin.nextDouble();
		
		if (System.getSecurityManager() == null) System.setSecurityManager(new RMISecurityManager());
		
		System.out.println("Querying nodes...");
		
		// setup all the RMI stuff
		AskingForThreadsThread[] aftts = new AskingForThreadsThread[args.length];
		for (int i=0; i<args.length; i++) {
			aftts[i] = new AskingForThreadsThread(args[i]);
			aftts[i].start();
		}
		
		int total_threads = 0;
		int live_nodes = 0;
		for (AskingForThreadsThread aftt : aftts) {
			try {
				aftt.join();
			} catch (InterruptedException e) {
				aftt.compserv = null;
				aftt.threads = 0;
			}
			total_threads += aftt.threads;
			
			if (aftt.compserv == null)
				System.out.format("WARNING: Node %s cannot be reached. It will not participate in computations.\n", aftt.nodename);
			else
				live_nodes++;
		}
		
		if (total_threads == 0) {
			System.out.println("ERROR: Cannot compute with nothing to compute with!");
			System.out.println("Aborting.");
			return;
		}
		
		System.out.format("Computations starting with total %d threads on %d nodes\n", total_threads, live_nodes);
		
		
		// prepare executor threads. Assign them stream IDs as necessary
		int streamId = 0;
		ExecutorThread[] ets = new ExecutorThread[args.length];
		for (int i=0; i<args.length; i++) {
			ets[i] = new ExecutorThread(aftts[i].compserv, aftts[i].threads, 1, 1, H, CC, CS, iterations, seed, streamId, total_threads, aftts[i].nodename);
			ets[i].start();
			streamId += aftts[i].threads;
		}
		
		ComputationResult cr = new ComputationResult(1, 1, H);
		
		System.out.println("Computation orders dispatched, awaiting all replies...");
		
		for (ExecutorThread et : ets) {
			try {
				et.join();
			} catch (InterruptedException e) {
				continue;
			}
			if (et.result == null) continue;
			cr = cr.add(et.result);
		}
		
		System.out.println("Replies collected, results are as follows: ");
		
		for (int i=1; i<H+1; i++) {
			double all_iters = cr.iterations.get(i-1);
			long trans = cr.transported.get(i-1);
			long abso = cr.absorbed.get(i-1);
			long refle = cr.reflected.get(i-1);
			
			System.out.format("---\tH = %d\n", i);
			System.out.format("   Absorption ratio:  %f\n", abso/all_iters);
			System.out.format("   Transmission ratio:  %f\n", trans/all_iters);
			System.out.format("   Reflection ratio:  %f\n", refle/all_iters);
		}
		
		
	}

}
