package de.huberlin.hiwaydb.LogToDB;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Collection;
import org.json.JSONObject;

import com.couchbase.client.CouchbaseClient;
import com.google.gson.Gson;
import com.sun.tools.javac.util.Pair;

import de.huberlin.hiwaydb.dal.DBConnection;
import de.huberlin.hiwaydb.dal.File;
import de.huberlin.hiwaydb.dal.Hiwayevent;
import de.huberlin.hiwaydb.dal.Inoutput;
import de.huberlin.hiwaydb.dal.Invocation;
import de.huberlin.hiwaydb.dal.Task;
import de.huberlin.hiwaydb.dal.Workflowrun;
import de.huberlin.hiwaydb.useDB.HiwayDB;
import de.huberlin.hiwaydb.useDB.HiwayDBI;
import de.huberlin.wbi.cuneiform.core.semanticmodel.JsonReportEntry;

public class WriteHiwayDB {

	private SessionFactory dbSessionFactory;
	private Transaction tx;
	private Session session;
	private String configFile;
	private static final Log log = LogFactory.getLog(HiwayDB.class);

	CouchbaseClient client = null;

	private boolean noSql = false;

	// public WriteHiwayDB(String configFile) {
	//
	// DBConnection con = new DBConnection(configFile);
	//
	// dbSessionFactory = con.getDBSession();
	// }

	public WriteHiwayDB(String dbURL, String username, String password,
			String file) {
		// System.out.println("MAKE CONNECTIONNNNNNNNNNNNNNNNNNN");

		DBConnection con = new DBConnection(dbURL, username, password, file);

		dbSessionFactory = con.getDBSession();
	}

	public WriteHiwayDB(List<URI> uris, String bucketName, String password) {
		// System.out.println("MAKE CONNECTIONNNNNNNNNNNNNNNNNNN");

		try {
			client = new CouchbaseClient(uris, bucketName, password);
			noSql = true;

		} catch (Exception e) {
			System.err.println("Error connecting to Couchbase: "
					+ e.getMessage());
			System.exit(0);
		}
		// finally {
		// // Shutting down properly
		// client.shutdown();
		// }
	}

	public void shutdown() {
		if (client != null) {
			client.shutdown();
		}
	}

	private String lineToCouchbase(JsonReportEntry logEntryRow) {

		try {

			System.out.println(logEntryRow.toString());

			String runID = null;

			if (logEntryRow.getRunId() != null) {
				runID = logEntryRow.getRunId().toString();
			}

			long taskID = 0;
			if (logEntryRow.getTaskId() != null) {
				taskID = logEntryRow.getTaskId();
			}

			Long invocID = (long) 0;

			if (logEntryRow.hasInvocId()) {
				invocID = logEntryRow.getInvocId();
			}

			Long timestampTemp = logEntryRow.getTimestamp();

			String filename = null;
			if (logEntryRow.getFile() != null) {
				filename = logEntryRow.getFile();
			}

			InvocDoc invocDocument = null;
			WfRunDoc wfRunDocument = null;

			Gson gson = new Gson();

			if (runID != null) {

				String wfRunDoc = (String) client.get(runID);

				if (wfRunDoc != null) {
					wfRunDocument = gson.fromJson(wfRunDoc, WfRunDoc.class);

					// System.out.println("Haben das doc schon: ID" +
					// actualDocument.getRunId() + "_" +
					// actualDocument.getInvocId());
				} else {
					wfRunDocument = new WfRunDoc();
					wfRunDocument.setRunId(runID);

					// client.set(runID, gson.toJson(wfRunDocument));
					System.out.println("WFRun: " + runID + " gespeichert");
				}
			}

			String documentId = runID + "_" + invocID;

			String documentJSON = (String) client.get(documentId);

			String key = logEntryRow.getKey();

			if (invocID != 0) {

				if (documentJSON != null) {

					invocDocument = gson.fromJson(documentJSON, InvocDoc.class);

				} else {

					invocDocument = new InvocDoc();

					invocDocument.setInvocId(invocID);
					invocDocument.setRunId(runID);

					System.out.println("NEUES Doc ID" + documentId
							+ " angelegt.");
				}
			}

			Map<String, HashMap<String, Long>> files = null;
			HashMap<String, Long> oneFile = null;
			JSONObject valuePart;
			Map<String, String> hiwayEvents = wfRunDocument.getHiwayEvent();

			if (invocDocument != null && invocID != 0) {
				invocDocument.setTaskId(taskID);
				invocDocument.setLang(logEntryRow.getLang());
				invocDocument.setTaskname(logEntryRow.getTaskName());
				files = invocDocument.getFiles();

				if (filename != null) {
					oneFile = files.get(filename);
				}
			}

			if (oneFile == null) {
				oneFile = new HashMap<String, Long>();
			}

			switch (key) {
			case HiwayDBI.KEY_INVOC_HOST:
				invocDocument.setHostname(logEntryRow.getValueRawString());

				break;
			case "wf-name":
				wfRunDocument.setName(logEntryRow.getValueRawString());
				break;
			case "wf-time":
				String val = logEntryRow.getValueRawString();
				Long test = Long.parseLong(val, 10);
				wfRunDocument.setWfTime(test);
				
				break;
			case HiwayDBI.KEY_INVOC_TIME_SCHED:
				valuePart = logEntryRow.getValueJsonObj();
				invocDocument.setScheduleTime(GetTimeStat(valuePart));
				break;
			case JsonReportEntry.KEY_INVOC_STDERR:
				invocDocument.setStandardError(logEntryRow.getValueRawString());
				break;

			case "invoc-exec":
				valuePart = logEntryRow.getValueJsonObj();

				Map<String, String> input = invocDocument.getInput();
				input.put("invoc-exec", valuePart.toString());
				invocDocument.setInput(input);
				break;

			case JsonReportEntry.KEY_INVOC_OUTPUT:
				valuePart = logEntryRow.getValueJsonObj();

				Map<String, String> output = invocDocument.getOutput();
				output.put("invoc-output", valuePart.toString());
				invocDocument.setOutput(output);
				break;

			case JsonReportEntry.KEY_INVOC_STDOUT:
				invocDocument.setStandardOut(logEntryRow.getValueRawString());
				break;

			case "invoc-time-stagein":
				valuePart = logEntryRow.getValueJsonObj();
				invocDocument.setRealTimeIn(GetTimeStat(valuePart));
				break;
			case "invoc-time-stageout":
				valuePart = logEntryRow.getValueJsonObj();
				invocDocument.setRealTimeOut(GetTimeStat(valuePart));
				break;

			case HiwayDBI.KEY_FILE_TIME_STAGEIN:
				valuePart = logEntryRow.getValueJsonObj();

				oneFile.put("realTimeIn", GetTimeStat(valuePart));

				files.put(filename, oneFile);
				invocDocument.setFiles(files);

				break;
			case HiwayDBI.KEY_FILE_TIME_STAGEOUT:
				valuePart = logEntryRow.getValueJsonObj();

				oneFile.put("realTimeOut", GetTimeStat(valuePart));

				files.put(filename, oneFile);
				invocDocument.setFiles(files);

				break;
			case JsonReportEntry.KEY_INVOC_TIME:
				valuePart = logEntryRow.getValueJsonObj();

				try {
					invocDocument.setRealTimeIn(GetTimeStat(valuePart));
				} catch (NumberFormatException e) {
					invocDocument.setRealTimeIn(1l);
				}

				break;
			case "file-size-stagein":
				oneFile.put("size",
						Long.parseLong(logEntryRow.getValueRawString(), 10));

				files.put(filename, oneFile);
				invocDocument.setFiles(files);
				break;
			case "file-size-stageout":
				oneFile.put("size",
						Long.parseLong(logEntryRow.getValueRawString(), 10));

				files.put(filename, oneFile);
				invocDocument.setFiles(files);
				break;
			case HiwayDBI.KEY_HIWAY_EVENT:
				valuePart = logEntryRow.getValueJsonObj();

				hiwayEvents.put(valuePart.get("type").toString(),
						valuePart.toString());
				wfRunDocument.setHiwayEvent(hiwayEvents);

				break;
			case "reduction-time":
				wfRunDocument.setReductionTime(Long.parseLong(
						logEntryRow.getValueRawString(), 10));

				break;
			default:
				throw new Exception("Der Typ ist nicht bekannt.:" + key);
			}

			if (invocDocument != null) {
				client.set(documentId, gson.toJson(invocDocument));
			}

			client.set(runID, gson.toJson(wfRunDocument));

			return "";

		} catch (Exception e) {

			log.info(e);

			e.printStackTrace();
			// throw e;
			return "Fehler: " + e.getMessage();
			// return 1;

		} finally {
			// client.shutdown();
		}
	}

	public String lineToDB(JsonReportEntry logEntryRow) {

		try {

			if (noSql) {
				return lineToCouchbase(logEntryRow);
			}

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

}
