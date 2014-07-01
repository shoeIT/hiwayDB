package de.huberlin.hiwaydb.useDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import de.huberlin.hiwaydb.LogToDB.WriteHiwayDB;
import de.huberlin.hiwaydb.dal.DBConnection;
import de.huberlin.hiwaydb.dal.File;
import de.huberlin.hiwaydb.dal.Invocation;
import de.huberlin.hiwaydb.dal.Task;
import de.huberlin.hiwaydb.dal.Workflowrun;
import de.huberlin.wbi.cuneiform.core.semanticmodel.JsonReportEntry;

public class HiwayDB implements HiwayDBI {
	private String configFile = "hibernate.cfg.xml";

	private static final Log log = LogFactory.getLog(HiwayDB.class);

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
		DBConnection con = new DBConnection("hibernate.cfg.xml");
		dbSessionFactory = con.getDBSession();
	}

	@Override
	public void logToDB(JsonReportEntry entry) {
		WriteHiwayDB writer = new WriteHiwayDB(this.dbURL, this.username,
				this.password, "hibernate.cfg.xml");

		String i = writer.lineToDB(entry);

		if (i.isEmpty()) {
			//log.info("Write successful!!!");
		} else {
			log.info("Fehler: " + i);
		}
	}

	@Override
	public Set<String> getHostNames() {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(this.dbURL, this.username,
					this.password, "hibernate.cfg.xml");

			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;

		query = session.createQuery("FROM Invocation I");

		// query = session
		// .createQuery("select new list(hostname)  FROM Invocation I");

		resultsInvoc = query.list();

		Set<String> tempResult = new HashSet();

		for (Invocation i : resultsInvoc) {
			// System.out.println("in getHostnames: " + i.getHostname());
			if (i.getHostname() != null) {
				tempResult.add(i.getHostname());
			}
		}

		return tempResult;
	}

	// @Override
	// public Collection<InvocStat> getLogEntries() {
	// if (dbSessionFactory == null) {
	// DBConnection con = new DBConnection(configFile);
	// dbSessionFactory = con.getDBSession();
	// }
	//
	// session = dbSessionFactory.openSession();
	//
	// Query query = null;
	// List<Invocation> resultsInvoc = null;
	//
	// query = session.createQuery("FROM Invocation I");
	// // join I.invocationId
	// resultsInvoc = query.list();
	//
	// return createInvocStat(resultsInvoc);
	//
	// }

	@Override
	public Collection<InvocStat> getLogEntriesForTask(long taskId) {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(this.dbURL, this.username,
					this.password, "hibernate.cfg.xml");
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
			DBConnection con = new DBConnection(this.dbURL, this.username,
					this.password, "hibernate.cfg.xml");
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;

		String queryString = "FROM Invocation I  WHERE ";

		for (Long l : taskIds) {
			queryString += " I.task = " + l.toString() + " or ";
		}

		System.out.println(queryString.substring(0, queryString.length() - 4));

		query = session.createQuery(queryString.substring(0,
				queryString.length() - 4));

		// join I.invocationId
		resultsInvoc = query.list();

		return createInvocStat(resultsInvoc);

	}

	@Override
	public Set<Long> getTaskIdsForWorkflow(String workflowName) {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(this.dbURL, this.username,
					this.password, "hibernate.cfg.xml");
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Workflowrun> resultsWF = null;

		query = session.createQuery("FROM Workflowrun W WHERE W.wfname ='"
				+ workflowName + "'");

		// query = session
		// .createQuery("select new list(hostname)  FROM Invocation I");

		resultsWF = query.list();

		Set<Long> tempResult = new HashSet<Long>();

		for (Workflowrun w : resultsWF) {
			// System.out.println("in getHostnames: " + i.getHostname());
			for (Invocation i : w.getInvocations()) {
				tempResult.add(i.getTask().getTaskId());
			}
		}

		return tempResult;
	}

	@Override
	public String getTaskName(long taskId) {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(this.dbURL, this.username,
					this.password, "hibernate.cfg.xml");
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Task> resultsInvoc = null;

		query = session.createQuery("FROM Task T  WHERE T.taskid =" + taskId);
		// join I.invocationId
		resultsInvoc = query.list();

		if (!resultsInvoc.isEmpty()) {
			return resultsInvoc.get(0).getTaskName();
		} else {
			return "";
		}

	}

	private Collection<InvocStat> createInvocStat(List<Invocation> invocations) {

		Set<InvocStat> resultList = new HashSet<InvocStat>();
		Invocation tempInvoc;

		for (int i = 0; i < invocations.size(); i++) {
			tempInvoc = invocations.get(i);

			InvocStat invoc = new InvocStat(tempInvoc.getTask().getTaskId());


			log.info("Invoc: " + tempInvoc.getInvocationId()  + " Host:" +  tempInvoc.getHostname() + 
					" Task:" + tempInvoc.getTask().getTaskId() + " Time: " +  tempInvoc.getRealTime()
					+ " Time: " +tempInvoc.getTimestamp() );
			
			if (tempInvoc.getHostname() != null
					&& tempInvoc.getTask().getTaskId() != 0
					&& tempInvoc.getRealTime() != null) {
				invoc.setHostName(tempInvoc.getHostname());
					invoc.setRealTime(tempInvoc.getRealTime(), tempInvoc.getTimestamp());

				Set<FileStat> iFiles = new HashSet<FileStat>();
				Set<FileStat> oFiles = new HashSet<FileStat>();

				// Files
				for (File f : tempInvoc.getFiles()) {

					FileStat iFile = new FileStat();
					iFile.setFileName(f.getName());
					
					FileStat oFile = new FileStat();
					oFile.setFileName(f.getName());

					if (f.getRealTimeIn() != null) {
						iFile.setRealTime(f.getRealTimeIn());
						iFile.setSize(f.getSize());
						iFiles.add(iFile);
					}

					if (f.getRealTimeOut() != null) {
						oFile.setRealTime(f.getRealTimeOut());
						oFile.setSize(f.getSize());
						oFiles.add(oFile);
					}									
				}
				
				invoc.setInputfiles(iFiles);
				invoc.setOutputfiles(oFiles);	
				
				log.info("Invoc mit Timestamp: "  + invoc.getTimestamp() + " geadded!");
				resultList.add(invoc);
			}
		}
		return resultList;
	}

	// @Override
	// public Collection<InvocStat> getLogEntriesForHost(String hostName) {
	// if (dbSessionFactory == null) {
	// DBConnection con = new DBConnection(configFile);
	// dbSessionFactory = con.getDBSession();
	// }
	//
	// session = dbSessionFactory.openSession();
	//
	// Query query = null;
	// List<Invocation> resultsInvoc = null;
	//
	// query = session.createQuery("FROM Invocation I  WHERE I.hostname ='"+
	// hostName+"'");
	//
	// resultsInvoc = query.list();
	//
	// return createInvocStat(resultsInvoc);
	// }

	// @Override
	// public Collection<InvocStat> getLogEntriesForHostSince(String hostName,
	// long timestamp) {
	// if (dbSessionFactory == null) {
	// DBConnection con = new DBConnection(configFile);
	// dbSessionFactory = con.getDBSession();
	// }
	//
	// session = dbSessionFactory.openSession();
	//
	// Query query = null;
	// List<Invocation> resultsInvoc = null;
	//
	// query = session.createQuery("FROM Invocation I  WHERE I.hostname ='"+
	// hostName+"' and I.didOn > " + timestamp);
	//
	// resultsInvoc = query.list();
	//
	// return createInvocStat(resultsInvoc);
	// }

	@Override
	public Collection<InvocStat> getLogEntriesForTaskOnHost(Long taskId,
			String hostName) {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(this.dbURL, this.username,
					this.password, "hibernate.cfg.xml");
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;

		query = session.createQuery("FROM Invocation I  WHERE I.hostname ='"
				+ hostName + "' and I.task = " + taskId);

		resultsInvoc = query.list();

		return createInvocStat(resultsInvoc);
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTaskOnHostSince(Long taskId,
			String hostName, long timestamp) {
		if (dbSessionFactory == null) {
			DBConnection con = new DBConnection(this.dbURL, this.username,
					this.password, "hibernate.cfg.xml");
			dbSessionFactory = con.getDBSession();
		}

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;
//
//		Date seit = new Date(timestamp);
//		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
//				"yyyy-MM-dd HH:mm:ss");
//
//		String currentTime = sdf.format(seit);

		query = session.createQuery("FROM Invocation I  WHERE I.hostname ='"
				+ hostName + "' and I.Timestamp >" + timestamp
				+" and I.task = " + taskId);
		
		log.info("Suche mit Query: FROM Invocation I  WHERE I.hostname ='"
				+ hostName + "' and I.Timestamp >" + timestamp
				+" and I.task = " + taskId);

	
		resultsInvoc = query.list();
		log.info("Ergebnisse Size: " + resultsInvoc.size());

		return createInvocStat(resultsInvoc);
	}
}
