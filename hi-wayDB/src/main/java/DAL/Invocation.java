// default package
// Generated 27.04.2014 17:42:05 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * Invocation generated by hbm2java
 */
public class Invocation implements java.io.Serializable {

	private long invocationId;
	private WorkflowrunHasTask workflowrunHasTask;
	private String hostname;
	private Long scheduleTime;
	private String standardOut;
	private String standardError;
	private Set<Userevent> userevents = new HashSet<Userevent>(0);
	private Set<Output> outputs = new HashSet<Output>(0);
	private Set<Filestagingevent> filestagingevents = new HashSet<Filestagingevent>(
			0);
	private Set<Timestat> timestats = new HashSet<Timestat>(0);
	private Set<Stagingevent> stagingevents = new HashSet<Stagingevent>(0);

	public Invocation() {
	}

	public Invocation(long invocationId, WorkflowrunHasTask workflowrunHasTask) {
		this.invocationId = invocationId;
		this.workflowrunHasTask = workflowrunHasTask;
	}

	public Invocation(long invocationId, WorkflowrunHasTask workflowrunHasTask,
			String hostname, Long scheduleTime, String standardOut,
			String standardError, Set<Userevent> userevents,
			Set<Output> outputs, Set<Filestagingevent> filestagingevents,
			Set<Timestat> timestats, Set<Stagingevent> stagingevents) {
		this.invocationId = invocationId;
		this.workflowrunHasTask = workflowrunHasTask;
		this.hostname = hostname;
		this.scheduleTime = scheduleTime;
		this.standardOut = standardOut;
		this.standardError = standardError;
		this.userevents = userevents;
		this.outputs = outputs;
		this.filestagingevents = filestagingevents;
		this.timestats = timestats;
		this.stagingevents = stagingevents;
	}

	public long getInvocationId() {
		return this.invocationId;
	}

	public void setInvocationId(long invocationId) {
		this.invocationId = invocationId;
	}

	public WorkflowrunHasTask getWorkflowrunHasTask() {
		return this.workflowrunHasTask;
	}

	public void setWorkflowrunHasTask(WorkflowrunHasTask workflowrunHasTask) {
		this.workflowrunHasTask = workflowrunHasTask;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Long getScheduleTime() {
		return this.scheduleTime;
	}

	public void setScheduleTime(Long scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getStandardOut() {
		return this.standardOut;
	}

	public void setStandardOut(String standardOut) {
		this.standardOut = standardOut;
	}

	public String getStandardError() {
		return this.standardError;
	}

	public void setStandardError(String standardError) {
		this.standardError = standardError;
	}

	public Set<Userevent> getUserevents() {
		return this.userevents;
	}

	public void setUserevents(Set<Userevent> userevents) {
		this.userevents = userevents;
	}

	public Set<Output> getOutputs() {
		return this.outputs;
	}

	public void setOutputs(Set<Output> outputs) {
		this.outputs = outputs;
	}

	public Set<Filestagingevent> getFilestagingevents() {
		return this.filestagingevents;
	}

	public void setFilestagingevents(Set<Filestagingevent> filestagingevents) {
		this.filestagingevents = filestagingevents;
	}

	public Set<Timestat> getTimestats() {
		return this.timestats;
	}

	public void setTimestats(Set<Timestat> timestats) {
		this.timestats = timestats;
	}

	public Set<Stagingevent> getStagingevents() {
		return this.stagingevents;
	}

	public void setStagingevents(Set<Stagingevent> stagingevents) {
		this.stagingevents = stagingevents;
	}

}
