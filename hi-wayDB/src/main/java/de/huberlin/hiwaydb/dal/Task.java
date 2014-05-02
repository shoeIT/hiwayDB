package de.huberlin.hiwaydb.dal;
// Generated 02.05.2014 17:34:59 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
/**
 * Task generated by hbm2java
 */
@Entity
public class Task implements java.io.Serializable {

	@Id
	private long taskId;
	private String taskName;
	private String language;
	
	@OneToMany(mappedBy="task")
	private Set<Invocation> invocations = new HashSet<Invocation>(0);
	

	public Task() {
	}

	public Task(long taskId, String language) {
		this.taskId = taskId;
		this.language = language;
	}

	public Task(long taskId, String taskName, String language, Set invocations) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.language = language;
		this.invocations = invocations;
	}

	public long getTaskId() {
		return this.taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Set getInvocations() {
		return this.invocations;
	}

	public void setInvocations(Set invocations) {
		this.invocations = invocations;
	}

}