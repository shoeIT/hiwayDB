package de.huberlin.hiwaydb.useDB;

public class FileStat {
	
	private long size;
	private double realTime;
	private String filename;
	private String type;
	
	public FileStat() {
	}

	public FileStat(long size, long realTime, String filename, String type) {
		this.size = size;
		this.realTime = realTime;
		this.filename = filename;
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public long getSize() {
		return this.size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public double getRealTime() {
		return this.realTime;
	}

	public void setRealTime(double d) {
		this.realTime = d;
	}

}
