package com.accelkey.algorythm;

public class Position {
	private long xy;
	private long xz;
	private long yz;

	public Position(float xy, float xz, float yz) {
		this.xy = (Math.round(Math.toDegrees(xy)));
		this.xz = (Math.round(Math.toDegrees(xz)));
		this.yz = (Math.round(Math.toDegrees(yz)));
	}

	public Position(long _xy, long _xz, long _yz) {
		this.xy = _xy;
		this.xz = _xz;
		this.yz = _yz;

	}

	public  Position(Position p) {
		this.xy = p.getXy();
		this.xz = p.getXz();
		this.yz = p.getYz();
	}

	public Position(float[] orientation) {
		if(orientation.length >= 3) {
			this.xy = (Math.round(Math.toDegrees(orientation[0])));
			this.xz = (Math.round(Math.toDegrees(orientation[1])));
			this.yz = (Math.round(Math.toDegrees(orientation[2])));
		}
	}

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


	@Override
	public boolean equals(Object o) {
		if (xy == ((Position)o).getXy() && 
				xz == ((Position)o).getXz() && 
				yz == ((Position)o).getYz()) 
			return true;
		else return false;
	}

	public boolean moreThan(Position position, int degree) {
		if (Math.abs(this.getXy() - position.getXy()) > degree
                || Math.abs(this.getXz() - position.getXz()) > degree
                || Math.abs(this.getYz() - position.getYz()) > degree)
			return true;
		return false;
	}

	public void print() {
		System.out.println("");
		System.out.println("xy: " + getXy());
		System.out.println("xz: " + getXz());
		System.out.println("yz: " + getYz());
		System.out.println("");
	}
}
