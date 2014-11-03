package massdriver.coordinator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import massdriver.ComputationService;


// this thread is just asking for threads and setting up it's own RMI
// data from it will be reused later
public class AskingForThreadsThread extends Thread {

	// this data will later be useful
	public ComputationService compserv;	// if this is null when this finished, node is declared DEAD
	public int threads;
	
	public String nodename;
	
	public AskingForThreadsThread(String nodename) {
		this.nodename = nodename;
	}
	
	public void run() {
		try {
			Registry registry = LocateRegistry.getRegistry(this.nodename);
			this.compserv = (ComputationService)registry.lookup("massdriver-compute");
			this.threads = this.compserv.queryAvailableThreads();
		} catch (RemoteException e) {			
		} catch (NotBoundException e) {
		}
	}
	
}
