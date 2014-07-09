package de.huberlin.hiwaydb.LogToDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.tools.javac.util.Pair;

import de.huberlin.hiwaydb.dal.Userevent;

public class InvocDoc {

	private Long timestamp;
	private String runId;
	private Long invocId;
	private String taskname;
	private Long taskId;
	private String lang;
	private String key;
	private String value;
	
	private Set<String> userevents = new HashSet<String>(0);
	private Map<String, String>  input = new HashMap<String, String> (0);
	private Set<Pair<String,String>> output = new HashSet<Pair<String,String>>(0);
	
	private 	Map<String, HashMap<String,Long>> files = new HashMap<String, HashMap<String,Long>>();
	

	public 	Map<String, HashMap<String,Long>> getFiles() {
		return this.files;
	}

	public void setFiles(Map<String, HashMap<String,Long>> files) {
		this.files = files;
	}
	
private 	ArrayList<HashMap<String, Long[]>> files2 = new ArrayList<HashMap<String, Long[]>>(0);
	

	public 	ArrayList<HashMap<String, Long[]>> getFiles2() {
		return this.files2;
	}

	public void setFiles2(	ArrayList<HashMap<String, Long[]>> files2) {
		this.files2 = files2;
	}
	
	
	public Set<Pair<String,String>> getOutput() {
		return this.output;
	}

	public void setOutput(Set<Pair<String,String>> output) {
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


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

}
