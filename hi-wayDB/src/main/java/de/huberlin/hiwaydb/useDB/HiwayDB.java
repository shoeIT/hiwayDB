package de.huberlin.hiwaydb.useDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

import de.huberlin.hiwaydb.dal.Accesstime;
import de.huberlin.hiwaydb.dal.File;
import de.huberlin.hiwaydb.dal.Hiwayevent;
import de.huberlin.hiwaydb.dal.Inoutput;
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
		
		dbSessionFactory = getSQLSession();
	}

	
	@Override
	public void logToDB(JsonReportEntry entry) {
		
		String i = lineToDB(entry);

		if (i.isEmpty()) {
			// log.info("Write successful!!!");
		} else {
			log.info("Fehler: " + i);
		}
	}

	@Override
	public Set<String> getHostNames() {
		Long tick =System.currentTimeMillis();
		if (dbSessionFactory == null) {
			dbSessionFactory = getSQLSession();
		}

		Accesstime at = new Accesstime();
		
		at.setTick(tick);
		at.setFunktion("getHostNames");
	    at.setInput("");
		session = dbSessionFactory.openSession();
		tx = session.beginTransaction();

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
		
		Long x = (long) tempResult.size();

		at.setReturnvolume(x);
		Long tock = System.currentTimeMillis();
		at.setTock(tock);
		at.setTicktockdif(tock-tick);
		session.save(at);
		tx.commit();
		return tempResult;
		
	}

	
	@Override
	public Collection<InvocStat> getLogEntriesForTask(long taskId) {
		Long tick =System.currentTimeMillis();
		if (dbSessionFactory == null) {
			dbSessionFactory = getSQLSession();
		}

		Accesstime at = new Accesstime();
		
		at.setTick(tick);
		at.setFunktion("getLogEntriesForTask");
	    at.setInput("");
		session = dbSessionFactory.openSession();
		tx = session.beginTransaction();

		Query query = null;
		List<Invocation> resultsInvoc = null;

		query = session.createQuery("FROM Invocation I  WHERE I.task ="
				+ taskId);
		// join I.invocationId
		resultsInvoc = query.list();
		Long x = (long) resultsInvoc.size();

		at.setReturnvolume(x);
		Long tock = System.currentTimeMillis();
		at.setTock(tock);
		at.setTicktockdif(tock-tick);
		session.save(at);
		tx.commit();
		return createInvocStat(resultsInvoc);

	}

	@Override
	public Collection<InvocStat> getLogEntriesForTasks(Set<Long> taskIds) {
		Long tick =System.currentTimeMillis();
		if (dbSessionFactory == null) {
			dbSessionFactory = getSQLSession();
		}

		Accesstime at = new Accesstime();
		
		at.setTick(tick);
		at.setFunktion("getLogEntriesForTasks");
	    at.setInput("");
		session = dbSessionFactory.openSession();
		tx = session.beginTransaction();
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
		Long x = (long) resultsInvoc.size();

		at.setReturnvolume(x);
		Long tock = System.currentTimeMillis();
		at.setTock(tock);
		at.setTicktockdif(tock-tick);
		session.save(at);
		tx.commit();
		return createInvocStat(resultsInvoc);

	}

	@Override
	public Set<Long> getTaskIdsForWorkflow(String workflowName) {
		Long tick =System.currentTimeMillis();
		if (dbSessionFactory == null) {
			dbSessionFactory = getSQLSession();
		}

		Accesstime at = new Accesstime();
		
		at.setTick(tick);
		at.setFunktion("getTaskIdsForWorkflow");
	    at.setInput("");
		session = dbSessionFactory.openSession();
		tx = session.beginTransaction();
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

		Long x = (long) tempResult.size();

		at.setReturnvolume(x);
		Long tock = System.currentTimeMillis();
		at.setTock(tock);
		at.setTicktockdif(tock-tick);
		session.save(at);
		tx.commit();
		return tempResult;
	}

	@Override
	public String getTaskName(long taskId) {
		Long tick =System.currentTimeMillis();
		if (dbSessionFactory == null) {
			dbSessionFactory = getSQLSession();
		}

		Accesstime at = new Accesstime();
		
		at.setTick(tick);
		at.setFunktion("getTaskName");
	    at.setInput("");
		session = dbSessionFactory.openSession();
		tx = session.beginTransaction();
		session = dbSessionFactory.openSession();

		Query query = null;
		List<Task> resultsInvoc = null;

		query = session.createQuery("FROM Task T  WHERE T.taskid =" + taskId);
		// join I.invocationId
		resultsInvoc = query.list();

		Long x = (long) resultsInvoc.size();

		at.setReturnvolume(x);
		Long tock = System.currentTimeMillis();
		at.setTock(tock);
		at.setTicktockdif(tock-tick);
		session.save(at);
		tx.commit();
		
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

			log.info("Invoc: " + tempInvoc.getInvocationId() + " Host:"
					+ tempInvoc.getHostname() + " Task:"
					+ tempInvoc.getTask().getTaskId() + " Time: "
					+ tempInvoc.getRealTime() + " Time: "
					+ tempInvoc.getTimestamp());

			if (tempInvoc.getHostname() != null
					&& tempInvoc.getTask().getTaskId() != 0
					&& tempInvoc.getRealTime() != null) {
				invoc.setHostName(tempInvoc.getHostname());
				invoc.setRealTime(tempInvoc.getRealTime(),
						tempInvoc.getTimestamp());

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

				log.info("Invoc mit Timestamp: " + invoc.getTimestamp()
						+ " geadded!");
				resultList.add(invoc);
			}
		}
		return resultList;
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTaskOnHost(Long taskId,
			String hostName) {
		Long tick =System.currentTimeMillis();
		if (dbSessionFactory == null) {
			dbSessionFactory = getSQLSession();
		}

		Accesstime at = new Accesstime();
		
		at.setTick(tick);
		at.setFunktion("getLogEntriesForTaskOnHost");
	    at.setInput("");
		session = dbSessionFactory.openSession();
		tx = session.beginTransaction();

		session = dbSessionFactory.openSession();

		Query query = null;
		List<Invocation> resultsInvoc = null;

		query = session.createQuery("FROM Invocation I  WHERE I.hostname ='"
				+ hostName + "' and I.task = " + taskId);

		resultsInvoc = query.list();
		Long x = (long) resultsInvoc.size();

		at.setReturnvolume(x);
		Long tock = System.currentTimeMillis();
		at.setTock(tock);
		at.setTicktockdif(tock-tick);
		session.save(at);
		tx.commit();
		
		return createInvocStat(resultsInvoc);
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTaskOnHostSince(Long taskId,
			String hostName, long timestamp) {
		Long tick =System.currentTimeMillis();
		if (dbSessionFactory == null) {
			dbSessionFactory = getSQLSession();
		}

		Accesstime at = new Accesstime();
		
		at.setTick(tick);
		at.setFunktion("getLogEntriesForTaskOnHostSince");
	    at.setInput("");
		session = dbSessionFactory.openSession();
		tx = session.beginTransaction();
		Query query = null;
		List<Invocation> resultsInvoc = null;
		//
		// Date seit = new Date(timestamp);
		// java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
		// "yyyy-MM-dd HH:mm:ss");
		//
		// String currentTime = sdf.format(seit);

		query = session.createQuery("FROM Invocation I  WHERE I.hostname ='"
				+ hostName + "' and I.Timestamp >" + timestamp
				+ " and I.task = " + taskId);

		log.info("Suche mit Query: FROM Invocation I  WHERE I.hostname ='"
				+ hostName + "' and I.Timestamp >" + timestamp
				+ " and I.task = " + taskId);

		resultsInvoc = query.list();
		log.info("Ergebnisse Size: " + resultsInvoc.size());

		Long x = (long) resultsInvoc.size();

		at.setReturnvolume(x);
		Long tock = System.currentTimeMillis();
		at.setTock(tock);
		at.setTicktockdif(tock-tick);
		session.save(at);
		tx.commit();
		
		return createInvocStat(resultsInvoc);
	}

	private String lineToDB(JsonReportEntry logEntryRow) {

		try {

			System.out.println(logEntryRow.toString());

			session = dbSessionFactory.openSession();
			tx = session.beginTransaction();
			Query query = null;
			List<Task> resultsTasks = null;
			List<Workflowrun> resultsWfRun = null;
			List<File> resultsFile = new ArrayList();

			String runID = null;
			Long wfId = null;
			;
			if (logEntryRow.getRunId() != null) {
				runID = logEntryRow.getRunId().toString();

				query = session
						.createQuery("FROM Workflowrun E WHERE E.runid='"
								+ runID + "'");

				resultsWfRun = query.list();
			}

			Workflowrun wfRun = null;
			if (resultsWfRun != null && !resultsWfRun.isEmpty()) {

				wfRun = resultsWfRun.get(0);
				wfId = wfRun.getId();
				// log.info("WF run ist nicht empty:" + wfId);
			}

			long taskID = 0;
			if (logEntryRow.getTaskId() != null) {
				taskID = logEntryRow.getTaskId();

				query = session.createQuery("FROM Task E WHERE E.taskid ="
						+ taskID);
				resultsTasks = query.list();
			}

			Long invocID = (long) 0;

			if (logEntryRow.hasInvocId()) {
				invocID = logEntryRow.getInvocId();
			}

			query = session
					.createQuery("FROM Invocation E WHERE E.invocationid ="
							+ invocID + " and E.workflowrun='" + wfId + "'");
			List<Invocation> resultsInvoc = query.list();

			Long timestampTemp = logEntryRow.getTimestamp();

			String filename = null;
			if (logEntryRow.getFile() != null) {
				filename = logEntryRow.getFile();

				// query = session.createQuery("FROM File E WHERE E.name='"
				// + filename + "' AND E.invocation" + invocID);

				query = session.createQuery("FROM File E WHERE E.name='"
						+ filename + "'");

				List<File> resultsFileTemp = query.list();

				for (File f : resultsFileTemp) {
					if (f.getInvocation().getInvocationId() == invocID) {
						resultsFile.add(f);
					}
				}
			}

			Task task = null;
			if (resultsTasks != null && !resultsTasks.isEmpty()) {
				task = resultsTasks.get(0);
			}
			Invocation invoc = null;
			if (resultsInvoc != null && !resultsInvoc.isEmpty()) {
				invoc = resultsInvoc.get(0);
			}

			File file = null;
			if (resultsFile != null && !resultsFile.isEmpty()) {
				file = resultsFile.get(0);
				// System.out.println("File haben wir:" + file.getName() +
				// file.getId());
			}

			// tx = session.beginTransaction();

			if (wfRun == null && runID != null) {

				wfRun = new Workflowrun();
				wfRun.setRunId(runID);
				session.save(wfRun);
			}

			if (taskID != 0 && (task == null)) {
				task = new Task();

				task.setTaskId(taskID);
				task.setTaskName(logEntryRow.getTaskName());
				task.setLanguage(logEntryRow.getLang());

				session.save(task);
				// System.out.println("Neuer.. Tasks in DB speichern ID: "
				// + task.getTaskId());
			}

			if (invocID != 0 && (invoc == null)) {
				invoc = new Invocation();
				invoc.setTimestamp(timestampTemp);
				invoc.setInvocationId(invocID);
				invoc.setTask(task);
				invoc.setWorkflowrun(wfRun);
				session.save(invoc);
			}

			if (file == null && filename != null) {

				file = new File();
				file.setName(filename);
				file.setInvocation(invoc);
				session.save(file);
			}

			String key = logEntryRow.getKey();

			// public static final String KEY_INVOC_TIME_STAGEIN =
			// "invoc-time-stagein";
			// public static final String KEY_INVOC_TIME_STAGEOUT =
			// "invoc-time-stageout";

			JSONObject valuePart;
			switch (key) {
			case HiwayDBI.KEY_INVOC_HOST:
				invoc.setHostname(logEntryRow.getValueRawString());
				break;
			case "wf-name":
				wfRun.setWfName(logEntryRow.getValueRawString());
				break;
			case "wf-time":
				String val = logEntryRow.getValueRawString();
				Long test = Long.parseLong(val, 10);

				wfRun.setWfTime(test);
				break;
			case HiwayDBI.KEY_INVOC_TIME_SCHED:
				valuePart = logEntryRow.getValueJsonObj();
				invoc.setScheduleTime(GetTimeStat(valuePart));
				break;

			case JsonReportEntry.KEY_INVOC_STDERR:
				invoc.setStandardError(logEntryRow.getValueRawString());
				break;

			case "invoc-exec":
				valuePart = logEntryRow.getValueJsonObj();

				Inoutput input = new Inoutput();
				input.setKeypart("invoc-exec");
				input.setInvocation(invoc);
				input.setContent(valuePart.toString());
				input.setType("input");
				session.save(input);
				break;

			case JsonReportEntry.KEY_INVOC_OUTPUT:
				valuePart = logEntryRow.getValueJsonObj();

				Inoutput output = new Inoutput();
				output.setKeypart("invoc-output");
				output.setInvocation(invoc);
				output.setContent(valuePart.toString());
				output.setType("output");
				session.save(output);
				break;

			case JsonReportEntry.KEY_INVOC_STDOUT:
				invoc.setStandardOut(logEntryRow.getValueRawString());
				break;

			case "invoc-time-stagein":
				valuePart = logEntryRow.getValueJsonObj();

				invoc.setRealTimeIn(GetTimeStat(valuePart));

				// invoc.setRealTimeIn(realtimein)
				break;
			case "invoc-time-stageout":
				valuePart = logEntryRow.getValueJsonObj();

				invoc.setRealTimeOut(GetTimeStat(valuePart));

				// invoc.setRealTimeIn(realtimein)
				break;

			case HiwayDBI.KEY_FILE_TIME_STAGEIN:
				valuePart = logEntryRow.getValueJsonObj();
				file.setRealTimeIn(GetTimeStat(valuePart));

				break;
			case HiwayDBI.KEY_FILE_TIME_STAGEOUT:
				valuePart = logEntryRow.getValueJsonObj();

				file.setRealTimeOut(GetTimeStat(valuePart));
				break;

			case JsonReportEntry.KEY_INVOC_TIME:
				valuePart = logEntryRow.getValueJsonObj();

				try {
					invoc.setRealTime(GetTimeStat(valuePart));
				} catch (NumberFormatException e) {
					invoc.setRealTime(1l);
				}

				break;
			case "file-size-stagein":

				file.setSize(Long.parseLong(logEntryRow.getValueRawString(), 10));

				break;
			case "file-size-stageout":

				file.setSize(Long.parseLong(logEntryRow.getValueRawString(), 10));

				break;
			case HiwayDBI.KEY_HIWAY_EVENT:
				valuePart = logEntryRow.getValueJsonObj();

				Hiwayevent he = new Hiwayevent();
				he.setWorkflowrun(wfRun);
				he.setContent(valuePart.toString());
				he.setType(valuePart.get("type").toString());
				session.save(he);

				break;
			case "reduction-time":

				break;
			default:
				throw new Exception("Der Typ ist nicht bekannt.:" + key);
			}

			tx.commit();

			return "";
		} catch (org.hibernate.exception.ConstraintViolationException e) {

			System.out.println("name: " + e.getConstraintName());
			String message = e.getSQLException().getMessage();

			if (message.contains("RundID_UNIQUE")) {
				System.out.println("runIDUnique");
			} else if ((message.contains("JustOneFile"))) {
				System.out.println("runIDUnique");
			}

			if (tx != null)
				tx.rollback();
			return message;

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			log.info(e);

			// e.printStackTrace();
			// throw e;
			return "Fehler: " + e.getMessage();
			// return 1;

		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static Long GetTimeStat(JSONObject valuePart) {

		// Timestat timeStat = new Timestat();
		// timeStat.setNminPageFault(Long.parseLong(valuePart.get("nMinPageFault")
		// .toString(), 10));
		// timeStat.setNforcedContextSwitch(Long.parseLong(
		// valuePart.get("nForcedContextSwitch").toString(), 10));
		// timeStat.setAvgDataSize(Long.parseLong(valuePart.get("avgDataSize")
		// .toString(), 10));
		// timeStat.setNsocketRead(Long.parseLong(valuePart.get("nSocketRead")
		// .toString(), 10));
		// timeStat.setNioWrite(Long.parseLong(valuePart.get("nIoWrite")
		// .toString(), 10));
		// timeStat.setAvgResidentSetSize(Long.parseLong(
		// valuePart.get("avgResidentSetSize").toString(), 10));
		// timeStat.setNmajPageFault(Long.parseLong(valuePart.get("nMajPageFault")
		// .toString(), 10));
		// timeStat.setNwaitContextSwitch(Long.parseLong(
		// valuePart.get("nWaitContextSwitch").toString(), 10));
		// timeStat.setUserTime(Double.parseDouble(valuePart.get("userTime")
		// .toString()));
		// timeStat.setRealTime(Double.parseDouble(valuePart.get("realTime")
		// .toString()));
		// timeStat.setSysTime(Double.parseDouble(valuePart.get("sysTime")
		// .toString()));
		// timeStat.setNsocketWrite(Long.parseLong(valuePart.get("nSocketWrite")
		// .toString(), 10));
		// timeStat.setMaxResidentSetSize(Long.parseLong(
		// valuePart.get("maxResidentSetSize").toString(), 10));
		// timeStat.setAvgStackSize(Long.parseLong(valuePart.get("avgStackSize")
		// .toString(), 10));
		// timeStat.setNswapOutMainMem(Long.parseLong(
		// valuePart.get("nSwapOutMainMem").toString(), 10));
		// timeStat.setNioRead(Long.parseLong(valuePart.get("nIoRead").toString(),
		// 10));
		// timeStat.setNsignal(Long.parseLong(valuePart.get("nSignal").toString(),
		// 10));
		// timeStat.setAvgTextSize(Long.parseLong(valuePart.get("avgTextSize")
		// .toString(), 10));

		return Long.parseLong(valuePart.get("realTime").toString(), 10);
	}

	private SessionFactory getSQLSession() {
		try {

			if (dbURL != null && username != null) {

			
				Configuration configuration = new Configuration();
				// .configure(f);

				configuration.setProperty("hibernate.connection.url", dbURL);
				configuration.setProperty("hibernate.connection.username",
						username);
				if (this.password != null) {
					configuration.setProperty("hibernate.connection.password",
							this.password);
				} else {
					configuration.setProperty("hibernate.connection.password",
							"");
				}

				configuration.setProperty("hibernate.dialect",
						"org.hibernate.dialect.MySQLInnoDBDialect");
				configuration.setProperty("hibernate.connection.driver_class",
						"com.mysql.jdbc.Driver");
				// configuration.setProperty("hibernate.connection.password.driver_class",
				// "com.mysql.jdbc.Driver");
				configuration.setProperty("hibernate.connection.pool_size",
						"10");

				// configuration.setProperty("hibernate.c3p0.min_size","5");
				// configuration.setProperty("hibernate.c3p0.max_size","20");
				// configuration.setProperty("hibernate.c3p0.timeout","1800");
				// configuration.setProperty("hibernate.c3p0.max_statements","50");

				configuration
						.addAnnotatedClass(de.huberlin.hiwaydb.dal.Hiwayevent.class);
				configuration
						.addAnnotatedClass(de.huberlin.hiwaydb.dal.File.class);
				configuration
						.addAnnotatedClass(de.huberlin.hiwaydb.dal.Inoutput.class);
				configuration
						.addAnnotatedClass(de.huberlin.hiwaydb.dal.Invocation.class);
				configuration
						.addAnnotatedClass(de.huberlin.hiwaydb.dal.Task.class);
				configuration
						.addAnnotatedClass(de.huberlin.hiwaydb.dal.Userevent.class);
				configuration
						.addAnnotatedClass(de.huberlin.hiwaydb.dal.Workflowrun.class);
				configuration
				.addAnnotatedClass(de.huberlin.hiwaydb.dal.Accesstime.class);

				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties());
				SessionFactory sessionFactory = configuration
						.buildSessionFactory(builder.build());
				return sessionFactory;

			} else {
				java.io.File f = new java.io.File(configFile);

				Configuration configuration = new Configuration().configure(f);
				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties());
				SessionFactory sessionFactory = configuration
						.buildSessionFactory(builder.build());
				return sessionFactory;
			}

		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

	}

}
