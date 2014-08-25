package de.huberlin.hiwaydb.LogToDB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;




public class InvocDoc {

	private Long timestamp;
	private String runId;
	private Long invocId;
	private String taskname;
	private Long taskId;
	private String lang;

	private String hostname;
	private Long scheduleTime;
	private String standardError;
	private String standardOut;
	
	private Long realTimeIn;
	private Long realTimeOut;
	private Long realTime;
	
	private Set<String> userevents = new HashSet<String>(0);
	private Map<String, String>  input = new HashMap<String, String> (0);
	private Map<String, String> output = new HashMap<String, String> (0);
	private Map<String, HashMap<String,Long>> files = new HashMap<String, HashMap<String,Long>>();
	

	public 	Map<String, HashMap<String,Long>> getFiles() {
		return this.files;
	}

	public void setFiles(Map<String, HashMap<String,Long>> files) {
		this.files = files;
	}
	
	
	public Map<String, String> getOutput() {
		return this.output;
	}

	public void setOutput(Map<String, String> output) {
		this.output = output;
	}

	public  Map<String, String> getInput() {
		return this.input;
	}

	public void setInput (Map<String, String> input) {
		this.input = input;
	}
	
	public Set<String> getUserevents() {
		return this.userevents;
	}

	public void setUserevents(Set<String> userevents) {
		this.userevents = userevents;
	}


	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public Long getInvocId() {
		return invocId;
	}

	public void setInvocId(Long invocId) {
		this.invocId = invocId;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Long getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Long scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getStandardError() {
		return standardError;
	}

	public void setStandardError(String standardError) {
		this.standardError = standardError;
	}

	public String getStandardOut() {
		return standardOut;
	}

	public void setStandardOut(String standardOut) {
		this.standardOut = standardOut;
	}

	public Long getRealTimeIn() {
		return realTimeIn;
	}

	public void setRealTimeIn(Long realTimeIn) {
		this.realTimeIn = realTimeIn;
	}

	public Long getRealTimeOut() {
		return realTimeOut;
	}

	public void setRealTimeOut(Long realTimeOut) {
		this.realTimeOut = realTimeOut;
	}

	public Long getRealTime() {
		return realTime;
	}

	public void setRealTime(Long realTime) {
		this.realTime = realTime;
	}

}
