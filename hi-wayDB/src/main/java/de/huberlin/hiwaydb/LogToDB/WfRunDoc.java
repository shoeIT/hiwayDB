package de.huberlin.hiwaydb.LogToDB;

import java.util.HashMap;
import java.util.Map;

public class WfRunDoc {

	private String name;
	private String runId;
	private Long wfTime;
	private Long reductionTime;
	
	private Map<String, String>  hiwayEvent = new HashMap<String, String> (0);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public Long getWfTime() {
		return wfTime;
	}

	public void setWfTime(Long wfTime) {
		this.wfTime = wfTime;
	}

	public Map<String, String> getHiwayEvent() {
		return hiwayEvent;
	}

	public void setHiwayEvent(Map<String, String> hiwayEvent) {
		this.hiwayEvent = hiwayEvent;
	}

	public Long getReductionTime() {
		return reductionTime;
	}

	public void setReductionTime(Long reductionTime) {
		this.reductionTime = reductionTime;
	}

}
