package massdriver.worker;

import java.util.Random;

/**
 * Hardly Scalable Threadly Parallel Random Number Generator
 */
public strictfp class HSTPRNG {
	
	final private Random random;
	final private int totStreams;
	final private int myStream;
	
	private int indexis = 0;
	
	public HSTPRNG(long seed, int streamNo, int maxStreams) {
		this.random = new Random(seed);
		this.myStream = streamNo;
		this.totStreams = maxStreams;
	}
	
	public double nextDouble() {
		while (this.indexis != this.myStream) {
			this.indexis = (this.indexis + 1) % this.totStreams;
			this.random.nextDouble();
		}
		return this.random.nextDouble();
	}

}
