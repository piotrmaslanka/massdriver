package massdriver;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ComputationService extends Remote, Serializable {
	/**
	 * Ask how many threads are available on this system
	 * @return amount of threads available
	 */
	public int queryAvailableThreads() throws RemoteException;
	
	/**
	 * Performs a simulation
	 * @param startH starting material thickness
	 * @param stepH amount of increment in material checking
	 * @param h amount of different thicknesses to check
	 * @param iterations total iterations to perform on EACH THREAD on EACH different thickness
	 * @param CC material's CC
	 * @param CS material's CS
	 * @param rootStream number of first available streams. Streams used will be
	 * 		rootStream, rootStream+1, rootStream+2, ..., rootStream+n-2, where n is amount of threads used
	 * @param maxStreams total amount of RNG streams
	 * @param baseSeed seed of the primary RNG
	 * @return result of computation
	 */
	public ComputationResult compute(double startH, double stepH, int h, long iterationsPerThread, double CC, double CS,
									 int rootStream, int maxStreams, long baseSeed) throws RemoteException;
}
