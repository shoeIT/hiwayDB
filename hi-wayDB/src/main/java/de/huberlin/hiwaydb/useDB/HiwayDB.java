package de.huberlin.hiwaydb.useDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import de.huberlin.hiwaydb.LogToDB.WriteHiwayDB;
import de.huberlin.hiwaydb.dal.DBConnection;
import de.huberlin.hiwaydb.dal.File;
import de.huberlin.hiwaydb.dal.Invocation;
import de.huberlin.hiwaydb.dal.Task;
import de.huberlin.hiwaydb.dal.Timestat;
import de.huberlin.hiwaydb.dal.Workflowrun;
import de.huberlin.wbi.cuneiform.core.invoc.JsonReportEntry;

public class HiwayDB implements HiwayDBI {
	private String configFile = "hibernate.cfg.xml";

	private SessionFactory dbSessionFactory = null;
	private Transaction tx;
	private Session session;

	@Override
	public int logToDB(JsonReportEntry entry) {
		WriteHiwayDB writer = new WriteHiwayDB(configFile);

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
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(configFile);
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;

		query = session.createQuery("FROM Invocation I  WHERE I.task ="	+ taskID);
		//join I.invocationId 
		resultsInvoc = query.list();
		
		Set<InvocStat> resultList = new HashSet<InvocStat>();
		Invocation tempInvoc;
		
		
		for (int i = 0; i < resultsInvoc.size(); i++) {
			tempInvoc = resultsInvoc.get(i);
			
			InvocStat invoc = new InvocStat();
			
			invoc.setHostname(tempInvoc.getHostname());
			invoc.setTaskId(taskID);
			invoc.setInvocId(tempInvoc.getInvocationId());
						
			//Time
			Timestat time;
			
			for(Timestat t : tempInvoc.getTimestats())
			{
				if(t.getType().equalsIgnoreCase("invoc-time")){
					invoc.setRealTime(t.getRealTime());
					invoc.setTimestamp(t.getDidOn());
				}
			}
			
			Set<FileStat> ioFiles = new HashSet<FileStat>();
						
			//Files
			for(File f : tempInvoc.getFiles()){
				
				FileStat ioFile;
				
				for(Timestat t : f.getTimestats())
				{
						ioFile =  new FileStat();
						ioFile.setFilename(f.getName());
						ioFile.setRealTime(t.getRealTime());
						ioFile.setType(t.getType());	
						
						ioFiles.add(ioFile);
				}
				
			}
			
			invoc.setIonputfiles(ioFiles);
			
					
			resultList.add(invoc);
			
		}

		return resultList;
	}

	@Override
	public Set<InvocStat> getLogEntriesSinceForTask(Collection<Long> taskID,
			long sinceTimestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<InvocStat> getLogEntriesForTask(Collection<Long> taskID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConfigFile(String configFile) {
		this.configFile = configFile;

	}

	@Override
	public String getConfigFile() {
		return this.configFile;
	}

}
