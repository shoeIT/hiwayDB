// default package
// Generated 27.04.2014 17:42:05 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * Task generated by hbm2java
 */
public class Task implements java.io.Serializable {

	private long taskId;
	private String taskName;
	private String language;
	private Set<WorkflowrunHasTask> workflowrunHasTasks = new HashSet<WorkflowrunHasTask>(
			0);

	public Task() {
	}

	public Task(long taskId, String language) {
		this.taskId = taskId;
		this.language = language;
	}

	public Task(long taskId, String taskName, String language,
			Set<WorkflowrunHasTask> workflowrunHasTasks) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.language = language;
		this.workflowrunHasTasks = workflowrunHasTasks;
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

	public Set<WorkflowrunHasTask> getWorkflowrunHasTasks() {
		return this.workflowrunHasTasks;
	}

	public void setWorkflowrunHasTasks(
			Set<WorkflowrunHasTask> workflowrunHasTasks) {
		this.workflowrunHasTasks = workflowrunHasTasks;
	}

}
