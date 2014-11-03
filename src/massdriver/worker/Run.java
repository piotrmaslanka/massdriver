package massdriver.worker;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import massdriver.ComputationService;

public class Run {

	public static void main(String[] args) {
		// Run it!
		System.out.println("Mass Driver v1.0 servant");
		System.out.println("Krzysztof Golinowski, Piotr Maślanka");

		if (args.length != 1) {
			System.out.println("Please pass a hostname (or hostname:port) on first argument");
			System.out.println("Aborting.");
			return;
		}
		
		System.out.println("Please wait, attempting to register...");
		
		String hostName = args[0];
				
		if (System.getSecurityManager() == null) System.setSecurityManager(new RMISecurityManager());	
		
		try {
			Naming.rebind("//"+hostName+"/massdriver-compute", new ComputationServant());
		} catch (RemoteException e) {
			System.out.println("ERROR: Startup of Java RMI failed.");
			System.out.println("Aborting.");
			return;
		} catch (MalformedURLException e) {
			System.out.println("ERROR: Malformed URL exception - what is exactly that, Java?");
			return;
		}
		
		System.out.format("OK, listening on %s\n", hostName);
	}

}
