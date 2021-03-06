package de.huberlin.hiwaydb.dal;

// Generated 19.05.2014 12:56:25 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Task generated by hbm2java
 */
@Entity
public class Task implements java.io.Serializable {

	@Id
	private long taskid;
	private String taskname;
	private String language;
	
	@OneToMany(mappedBy="task")
	private Set<Invocation> invocations = new HashSet<Invocation>(0);

	public Task() {
	}

	public Task(long taskId, String language) {
		this.taskid = taskId;
		this.language = language;
	}

	public Task(long taskId, String taskName, String language,
			Set<Invocation> invocations) {
		this.taskid = taskId;
		this.taskname = taskName;
		this.language = language;
		this.invocations = invocations;
	}

	public long getTaskId() {
		return this.taskid;
	}

	public void setTaskId(long taskId) {
		this.taskid = taskId;
	}

	public String getTaskName() {
		return this.taskname;
	}

	public void setTaskName(String taskName) {
		this.taskname = taskName;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Set<Invocation> getInvocations() {
		return this.invocations;
	}

	public void setInvocations(Set<Invocation> invocations) {
		this.invocations = invocations;
	}

}
