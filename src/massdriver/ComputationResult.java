package massdriver;

import java.io.Serializable;
import java.util.Vector;

public class ComputationResult implements Serializable {
	public Vector<Long> iterations = new Vector<Long>();
	public Vector<Long> transported = new Vector<Long>();
	public Vector<Long> reflected = new Vector<Long>();
	public Vector<Long> absorbed = new Vector<Long>();
	
	public double startingH;
	public double stepH;
	public int h;	//	amount of depths to check
	
	public void extend(long iterations, long transported, long reflected, long absorbed) {
		this.iterations.add(iterations);
		this.transported.add(transported);
		this.reflected.add(reflected);
		this.absorbed.add(absorbed);
	}
	
	public ComputationResult(double startingH, double stepH, int h) {
		this.startingH = startingH;
		this.stepH = stepH;
		this.h = h;
	}
	
	public ComputationResult add(ComputationResult other) {
		if (this.iterations.size() == 0) return other;
		
		ComputationResult cr = new ComputationResult(this.startingH, this.stepH, this.h);
		for (int i=0; i<this.iterations.size(); i++)
			cr.extend(other.iterations.get(i)+this.iterations.get(i), 
					  other.transported.get(i)+this.transported.get(i), 
					  other.reflected.get(i)+this.reflected.get(i), 
					  other.absorbed.get(i)+this.absorbed.get(i));
		return cr;
	}
}
