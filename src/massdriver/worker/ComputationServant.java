package massdriver.worker;

import massdriver.ComputationResult;
import massdriver.ComputationService;

public class ComputationServant implements ComputationService {

	public int queryAvailableThreads() {
		return Runtime.getRuntime().availableProcessors();
	}

	public ComputationResult compute(double startH, double stepH, int h, long iterationsPerThread,
			double CC, double CS, int rootStream, int maxStreams, long baseSeed) {

		System.out.println("NOTICE: Computation in progress");
		
		int threads = this.queryAvailableThreads();
		
		// ok, let's make those threads and launch them as they are created
		WorkerThread[] workers = new WorkerThread[threads];
		for (int i=0; i<threads; i++) {
			workers[i] = new WorkerThread(startH, stepH, h, iterationsPerThread, CC, CS, 
										  new HSTPRNG(baseSeed, rootStream+i, maxStreams));
			workers[i].start();
		}
		
		ComputationResult result = new ComputationResult(startH, stepH, h);
		
		// await completion - add results as they come in
		for (WorkerThread worker : workers) {
			try {
				worker.join();
			} catch (InterruptedException e) {
				// FAILURE! Skip it
				continue;
			}
			result = result.add(worker.result);
		}
		
		System.out.println("NOTICE: Computation finished");
		
		return result;
	}

}
