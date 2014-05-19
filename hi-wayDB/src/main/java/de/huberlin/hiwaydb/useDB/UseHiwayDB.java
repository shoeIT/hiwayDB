package de.huberlin.hiwaydb.useDB;

import java.util.Set;

import de.huberlin.wbi.cuneiform.core.invoc.JsonReportEntry;

public interface UseHiwayDB {
	
	public int logToDB(JsonReportEntry entry);
	public Set<InvocStat> getLogEntries (long taskID, String hostname);

}


