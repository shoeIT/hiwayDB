package de.huberlin.hiwaydb.useDB;

import java.util.Set;

import de.huberlin.hiwaydb.LogToDB.WriteHiwayDB;
import de.huberlin.wbi.cuneiform.core.invoc.JsonReportEntry;

public class HiwayDB implements UseHiwayDB
{

	@Override
	public int logToDB(JsonReportEntry entry) {
		WriteHiwayDB writer = new WriteHiwayDB();
		
		return writer.lineToDB(entry);
		 
	}

	@Override
	public Set<InvocStat> getLogEntries(long taskID, String hostname) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
