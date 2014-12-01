package com.accelkey.algorythm;

public class Position {
	private long xy;
	private long xz;
	private long yz;
	public long getXy() {
		return xy;
	}
	public void setXy(long xy) {
		this.xy = xy;
	}
	public long getXz() {
		return xz;
	}
	public void setXz(long xz) {
		this.xz = xz;
	}
	public long getYz() {
		return yz;
	}
	public void setYz(long yz) {
		this.yz = yz;
	}
	public Position(float xy, float xz, float yz) {
		this.xy = (Math.round(Math.toDegrees(xy)/90)*90);
		this.xz = (Math.round(Math.toDegrees(xz)/90)*90);
		this.yz = (Math.round(Math.toDegrees(yz)/90)*90);
	}
	@Override
	public boolean equals(Object o) {
		if (xy == ((Position)o).getXy() && 
				xz == ((Position)o).getXz() && 
				yz == ((Position)o).getYz()) 
			return true;
		else return false;
	}
	

	
	
	

}
