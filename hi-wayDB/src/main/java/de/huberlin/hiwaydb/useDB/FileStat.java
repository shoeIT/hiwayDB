package de.huberlin.hiwaydb.useDB;

public class FileStat {
	
	private long size;
	private long realTime;
	private String filename;
	
	public FileStat() {
	}

	public FileStat(long size, long realTime, String filename) {
		this.size = size;
		this.realTime = realTime;
		this.filename = filename;
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

	public long getRealTime() {
		return this.realTime;
	}

	public void setRealTime(long realtime) {
		this.realTime = realtime;
	}

}
