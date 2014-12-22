package de.uni.konstanz.eventdetection;

public class DescriptorBounds {
	
	private double lowValue = 0;
	private double highValue = 0;
	
	public DescriptorBounds( double lowValue, double highValue ) {
		this.lowValue = lowValue;
		this.highValue = highValue;
	}

	public double getLowValue() {
		return lowValue;
	}

	public double getHighValue() {
		return highValue;
	}

	public void setLowValue(double lowValue) {
		this.lowValue = lowValue;
	}

	public void setHighValue(double highValue) {
		this.highValue = highValue;
	}
	
	

}
