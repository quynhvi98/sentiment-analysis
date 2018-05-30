package vn.com.epi.orm.sa;

/**
 * Posted from May 30, 2018 10:43 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class StopWatch {
	
	private long time = 0;
	private long startTime = 0;
	private long stopTime = 0;
	
	public void start(){
		startTime = System.currentTimeMillis();
	}
	
	public void stop(){
		
		stopTime = System.currentTimeMillis();
		time += stopTime - startTime;
	}

	public long getTime() {
		return time;
	}
}
