package de.huberlin.hiwaydb.useDB;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class InvocStat {

	private Date timestamp;
	private double realTime;
	private long taskID;
	private String hostname;
	private long invocID;
	private Set<FileStat> ionputFiles = new HashSet<FileStat>(0);
	//private Set<FileStat> outputFiles = new HashSet<FileStat>(0);

	public InvocStat() {
	}

	public InvocStat(Date timestamp, long realTime, long taskID, String hostname, long invocId) {
		this.timestamp = timestamp;
		this.realTime = realTime;
		this.taskID = taskID;
		this.hostname = hostname;
		this.invocID = invocId;
	}

	

	public long getTaskId() {
		return this.taskID;
	}

	public void setTaskId(long taskId) {
		this.taskID = taskId;
	}
	
	public long getInvocId() {
		return this.invocID;
	}

	public void setInvocId(long invocId) {
		this.invocID = invocId;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date date) {
		this.timestamp = date;
	}
	
	
	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public double getRealTime() {
		return this.realTime;
	}

	public void setRealTime(double realtime) {
		this.realTime = realtime;
	}
	
//	public Set<FileStat> getOutputfiles() {
//		return this.outputFiles;
//	}
//
//	public void setOutputfiles(Set<FileStat> outputFiles) {
//		this.outputFiles = outputFiles;
//	}
	
	public Set<FileStat> getIonputfiles() {
		return this.ionputFiles;
	}

	public void setIonputfiles(Set<FileStat> ionputFiles) {
		this.ionputFiles = ionputFiles;
	}

}
