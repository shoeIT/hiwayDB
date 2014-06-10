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
import de.huberlin.wbi.cuneiform.core.semanticmodel.JsonReportEntry;

public class HiwayDB implements HiwayDBI {
	private String configFile = "hibernate.cfg.xml";

	private SessionFactory dbSessionFactory = null;
	private Transaction tx;
	private Session session;

	private String dbURL;
	private String password;
	private String username;

	public HiwayDB(String username, String password, String dbURL) {
		this.username = username;
		this.password = password;
		this.dbURL = dbURL;
	}

	public HiwayDB() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void logToDB(JsonReportEntry entry) {
		WriteHiwayDB writer = new WriteHiwayDB(configFile);

		writer.lineToDB(entry);
	}

	@Override
	public Set<String> getHostNames() {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(configFile);
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;

		query = session.createQuery("FROM Invocation I");

		//query = session	.createQuery("select new list(hostname)  FROM Invocation I");

		resultsInvoc =  query.list();
		 	
		Set<String> tempResult = new HashSet();
		
		for(Invocation i : resultsInvoc)
		{
			//System.out.println("in getHostnames: " + i.getHostname());
			tempResult.add(i.getHostname())	;		
		}

		return tempResult;
	}

	@Override
	public Collection<InvocStat> getLogEntries() {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(configFile);
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;

		query = session.createQuery("FROM Invocation I");
		// join I.invocationId
		resultsInvoc = query.list();

		return createInvocStat(resultsInvoc);
		
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTask(long taskId) {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(configFile);
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;

		query = session.createQuery("FROM Invocation I  WHERE I.task ="
				+ taskId);
		// join I.invocationId
		resultsInvoc = query.list();

		return createInvocStat(resultsInvoc);

	}

	@Override
	public Collection<InvocStat> getLogEntriesForTasks(Set<Long> taskIds) {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(configFile);
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;
		
		String queryString = "FROM Invocation I  WHERE ";
				
		for(Long l : taskIds)
		{
			queryString+= " I.task = " + l.toString() + " or ";
		}
		
		System.out.println(queryString.substring(0,queryString.length() - 4));
		
		query = session.createQuery(queryString.substring(0,queryString.length() - 4));
		
				
		// join I.invocationId
		resultsInvoc = query.list();

		return createInvocStat(resultsInvoc);
		
	}

	@Override
	public Collection<InvocStat> getLogEntriesSince(long sinceTimestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InvocStat> getLogEntriesSinceForTask(long taskId,
			long sinceTimestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InvocStat> getLogEntriesSinceForTasks(Set<Long> taskIds,
			long sinceTimestamp) {
		// TODO Auto-generated method stubdd
		return null;
	}

	@Override
	public Set<Long> getTaskIdsForWorkflow(String workflowName) {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(configFile);
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Workflowrun> resultsWF = null;

		query = session.createQuery("FROM Workflowrun W WHERE W.wfName ='"+ workflowName+"'");

		//query = session	.createQuery("select new list(hostname)  FROM Invocation I");

		resultsWF =  query.list();
		 	
		Set<Long> tempResult = new HashSet<Long>();
		
		for(Workflowrun w : resultsWF)
		{
			//System.out.println("in getHostnames: " + i.getHostname());
			for(Invocation i : w.getInvocations())
			{
				tempResult.add(i.getTask().getTaskId());
			}
		}

		return tempResult;		
	}

	@Override
	public String getTaskName(long taskId) {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(configFile);
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Task> resultsInvoc = null;

		query = session.createQuery("FROM Task T  WHERE T.taskId ="
				+ taskId);
		// join I.invocationId
		resultsInvoc = query.list();

		if(!resultsInvoc.isEmpty())
		{
			return resultsInvoc.get(0).getTaskName();
		}
		else
		{
			return "";
		}	
		
	}

	private Collection<InvocStat> createInvocStat(List<Invocation> invocations) {

		Set<InvocStat> resultList = new HashSet<InvocStat>();
		Invocation tempInvoc;

		for (int i = 0; i < invocations.size(); i++) {
			tempInvoc = invocations.get(i);

			InvocStat invoc = new InvocStat();

			invoc.setHostName(tempInvoc.getHostname());
			invoc.setTaskId(tempInvoc.getTask().getTaskId());
			// invoc.setInvocId(tempInvoc.getInvocationId());

			// Time
			Timestat time;

			for (Timestat t : tempInvoc.getTimestats()) {
				if (t.getType().equalsIgnoreCase("invoc-time")) {
					invoc.setRealTime(t.getRealTime());
					invoc.setTimestamp(t.getDidOn().getTime());
				}
			}

			Set<FileStat> iFiles = new HashSet<FileStat>();
			Set<FileStat> oFiles = new HashSet<FileStat>();

			// Files
			for (File f : tempInvoc.getFiles()) {

				FileStat ioFile;

				for (Timestat t : f.getTimestats()) {

					ioFile = new FileStat();
					ioFile.setFileName(f.getName());
					ioFile.setRealTime(t.getRealTime());

					if (t.getType() == "file-time-stagein") {
						iFiles.add(ioFile);
					}

					if (t.getType() == "file-time-stageout") {
						oFiles.add(ioFile);
					}
				}

				invoc.setInputfiles(iFiles);
				invoc.setOutputfiles(oFiles);

				resultList.add(invoc);
			}
		}
		return resultList;
	}
}
