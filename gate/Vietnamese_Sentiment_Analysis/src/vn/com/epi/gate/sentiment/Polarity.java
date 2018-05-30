package vn.com.epi.gate.sentiment;

/**
 * Posted from May 25, 2018 1:44 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public enum Polarity {

	POSITIVE, NEGATIVE, NEUTRAL, NO_OPINION;

	public Polarity reverse() {
		return this == POSITIVE ? NEGATIVE :
			this == NEGATIVE ? POSITIVE : this;
	}
	
}
