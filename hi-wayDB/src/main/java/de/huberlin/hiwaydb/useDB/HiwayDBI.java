package de.huberlin.hiwaydb.useDB;

import java.util.Collection;
import java.util.Set;

import de.huberlin.wbi.cuneiform.core.invoc.JsonReportEntry;

public interface HiwayDBI {
	
	public void setConfigFile(String configFile);
	public String getConfigFile();
	public int logToDB(JsonReportEntry entry);
	public Set<InvocStat> getLogEntriesSinceForTask (long taskID, long sinceTimestamp );
	public Set<InvocStat> getLogEntriesForTask (long taskID );
	public Set<InvocStat> getLogEntriesSinceForTask (Collection<Long> taskID, long sinceTimestamp );
	public Set<InvocStat> getLogEntriesForTask (Collection<Long> taskID );
	
	//	public Set<InvocStat> getLogEntriesSinceForTask (long taskID, long sinceTimestamp );
//	public Set<InvocStat> getLogEntriesSinceForHost (String hostname, long sinceTimestamp );
//	public Set<InvocStat> getLogEntriesSince (long sinceTimestamp );
//	public Set<InvocStat> getLogEntries (long taskID );
//	public Set<InvocStat> getLogEntriesAll();
}


