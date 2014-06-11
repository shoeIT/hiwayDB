package de.huberlin.hiwaydb.useDB;

import java.util.Collection;
import java.util.Set;

import de.huberlin.wbi.cuneiform.core.semanticmodel.JsonReportEntry;

public interface HiwayDBI {

	public static final String KEY_FILE_TIME_STAGEIN = "file-time-stagein";

	public static final String KEY_FILE_TIME_STAGEOUT = "file-time-stageout";
	public static final String KEY_HIWAY_EVENT = "hiway-event";
	// Keys of entries in the log
	public static final String KEY_INVOC_HOST = "invoc-host";
	public static final String KEY_INVOC_TIME_SCHED = "invoc-time-sched";
	public static final String KEY_INVOC_TIME_STAGEIN = "invoc-time-stagein";
	public static final String KEY_INVOC_TIME_STAGEOUT = "invoc-time-stageout";

	public Set<String> getHostNames();

	public Collection<InvocStat> getLogEntries();

	public Collection<InvocStat> getLogEntriesForTask(long taskId);

	public Collection<InvocStat> getLogEntriesForTasks(Set<Long> taskIds);

	public Collection<InvocStat> getLogEntriesSince(long sinceTimestamp);

	public Collection<InvocStat> getLogEntriesSinceForTask(long taskId,
			long sinceTimestamp);

	public Collection<InvocStat> getLogEntriesSinceForTasks(Set<Long> taskIds,
			long sinceTimestamp);

	public Set<Long> getTaskIdsForWorkflow(String workflowName);

	public String getTaskName(long taskId);

	public void logToDB(JsonReportEntry entry);

}
