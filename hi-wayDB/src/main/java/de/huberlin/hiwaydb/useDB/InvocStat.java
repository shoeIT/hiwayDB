package de.huberlin.hiwaydb.useDB;

import java.util.HashSet;
import java.util.Set;

public class InvocStat {

	private long timestamp;
	private long realTime;
	private long taskID;
	private String hostname;
	
	private Set<FileStat> inputFiles = new HashSet<FileStat>(0);
	private Set<FileStat> outputFiles = new HashSet<FileStat>(0);

	public InvocStat() {
	}

	public InvocStat(long timestamp, long realTime, long taskID, String hostname) {
		this.timestamp = timestamp;
		this.realTime = realTime;
		this.taskID = taskID;
		this.hostname = hostname;
	}

	

	public long getTaskId() {
		return this.taskID;
	}

	public void setTaskId(long taskId) {
		this.taskID = taskId;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public long getRealTime() {
		return this.realTime;
	}

	public void setRealTime(long realtime) {
		this.realTime = realtime;
	}
	
	public Set<FileStat> getOutputfiles() {
		return this.outputFiles;
	}

	public void setOutputfiles(Set<FileStat> outputFiles) {
		this.outputFiles = outputFiles;
	}
	
	public Set<FileStat> getInputfiles() {
		return this.inputFiles;
	}

	public void setInputfiles(Set<FileStat> inputFiles) {
		this.inputFiles = inputFiles;
	}

}
