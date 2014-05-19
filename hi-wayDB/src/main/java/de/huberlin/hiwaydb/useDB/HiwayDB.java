package de.huberlin.hiwaydb.useDB;

import java.util.Set;

import de.huberlin.hiwaydb.LogToDB.WriteHiwayDB;
import de.huberlin.wbi.cuneiform.core.invoc.JsonReportEntry;

public class HiwayDB implements HiwayDBI
{

	@Override
	public int logToDB(JsonReportEntry entry) {
		WriteHiwayDB writer = new WriteHiwayDB();
		
		return writer.lineToDB(entry);
		 
	}

		@Override
	public Set<InvocStat> getLogEntriesSinceForTask(long taskID,
			long sinceTimestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<InvocStat> getLogEntriesForTask(long taskID) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
