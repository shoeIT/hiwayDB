package de.huberlin.hiwaydb.useDB;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.google.gson.Gson;

import de.huberlin.hiwaydb.LogToDB.InvocDoc;
import de.huberlin.hiwaydb.LogToDB.WfRunDoc;
import de.huberlin.hiwaydb.dal.File;
import de.huberlin.hiwaydb.dal.Hiwayevent;
import de.huberlin.hiwaydb.dal.Inoutput;
import de.huberlin.hiwaydb.dal.Invocation;
import de.huberlin.hiwaydb.dal.Task;
import de.huberlin.hiwaydb.dal.Workflowrun;
import de.huberlin.wbi.cuneiform.core.semanticmodel.JsonReportEntry;

public class HiwayDBNoSQL implements HiwayDBI {

	private static final Log log = LogFactory.getLog(HiwayDB.class);

	private List<URI> dbURLs;
	private String password;
	private String bucket;
	CouchbaseClient client = null;
	Gson gson;

	public HiwayDBNoSQL(String bucket, String password, List<URI> dbURLs) {
		this.bucket = bucket;
		this.password = password;
		this.dbURLs = dbURLs;

		gson = new Gson();

		getConnection();

	}

	private void getConnection() {
		try {
			client = new CouchbaseClient(this.dbURLs, this.bucket,
					this.password);

		} catch (Exception e) {
			log.info("Error connecting to Couchbase: " + e.getMessage());
			// System.exit(0);
		}
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
		if (client == null) {
			getConnection();
		}

		View view = client.getView("dev_Invoc", "getHostNames");

		Gson gson = new Gson();
		// Set up the Query object
		Query query = new Query();

		// We the full documents and only the top 20
		query.setIncludeDocs(true).setLimit(20);

		// Query the Cluster
		ViewResponse result = client.query(view, query);

		// This ArrayList will contain all found beers
		ArrayList<HashMap<String, String>> hostnames = new ArrayList<HashMap<String, String>>();

		Set<String> tempResult = new HashSet();
		// Iterate over the found documents
		for (ViewRow row : result) {
			// Use Google GSON to parse the JSON into a HashMap
			System.out.println("resrow: " + row.getValue());
			HashMap<String, String> parsedDoc = gson.fromJson(
					(String) row.getDocument(), HashMap.class);

		}

		// shutdown();

		return tempResult;
	}

	private void shutdown() {
		if (client != null) {
			client.shutdown();
		}
	}

	private String lineToDB(JsonReportEntry logEntryRow) {

		try {

			System.out.println(logEntryRow.toString());

			String runID = null;

			if (logEntryRow.getRunId() != null) {
				runID = logEntryRow.getRunId().toString();
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
			Set<Long> taskIDsforWfRun = new HashSet<Long>();

			Gson gson = new Gson();

			if (runID != null) {

				String wfRunDoc = (String) client.get(runID);

				if (wfRunDoc != null) {
					wfRunDocument = gson.fromJson(wfRunDoc, WfRunDoc.class);

					taskIDsforWfRun = wfRunDocument.getTaskIDs();
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

			long taskID = 0;
			if (logEntryRow.getTaskId() != null) {
				taskID = logEntryRow.getTaskId();
				taskIDsforWfRun.add(taskID);
			}

			String documentId = runID + "_" + invocID;

			String documentJSON = (String) client.get(documentId);

			String key = logEntryRow.getKey();

			if (invocID != 0) {

				if (documentJSON != null) {

					invocDocument = gson.fromJson(documentJSON, InvocDoc.class);

					invocDocument.setInvocId(invocID);
					invocDocument.setRunId(runID);
					invocDocument.setTimestamp(timestampTemp);

				} else {

					invocDocument = new InvocDoc();

					invocDocument.setInvocId(invocID);
					invocDocument.setRunId(runID);
					invocDocument.setTimestamp(timestampTemp);

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
				// valuePart = logEntryRow.getValueJsonObj();
				// invocDocument.setScheduleTime(GetTimeStat(valuePart));
				invocDocument.setScheduleTime(Long.parseLong(
						logEntryRow.getValueRawString(), 10));

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
					invocDocument.setRealTime(GetTimeStat(valuePart));
				} catch (NumberFormatException e) {
					invocDocument.setRealTime(1l);
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

	private static Long GetTimeStat(JSONObject valuePart) {

		return Long.parseLong(valuePart.get("realTime").toString(), 10);
	}

	@Override
	public Set<Long> getTaskIdsForWorkflow(String workflowName) {
		if (client == null) {
			getConnection();
		}

		View view = client.getView("dev_Invoc", "getTaskIdsForWorkflow");

		// Set up the Query object
		Query query = new Query();

		query.setIncludeDocs(true).setKey("[\""+workflowName+"\"]");
		// We the full documents and only the top 20
		// .setLimit(20);
		// ["dbis11",324609906700,1404101397760]

		// Query the Cluster
		ViewResponse result = client.query(view, query);

		WfRunDoc wfRun = null;
		for (ViewRow row : result) {

			// System.out.println("resrow: "+ row.getValue()) ;
			wfRun = gson.fromJson((String) row.getDocument(), WfRunDoc.class);

		}
		if(wfRun!=null)
		{
			return wfRun.getTaskIDs();
		}
		return  new HashSet<Long>()	;
		
	}

	@Override
	public String getTaskName(long taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTask(long taskId) {

		Set<Long> ids = new HashSet();
		ids.add(taskId);

		return getLogEntriesForTasks(ids);
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTasks(Set<Long> taskIds) {
		if (client == null) {
			getConnection();
		}

		View view = client.getView("dev_Invoc", "getLogEntriesForTasks");

		// Set up the Query object
		Query query = new Query();

		// [[989639045],[324609906700]]
		String keys = "[";
		for (Long id : taskIds) {
			keys += "[" + id.toString() + "],";
		}
		keys = keys.substring(0, keys.length() - 2);

		keys += "]]";

		query.setIncludeDocs(true).setKeys(keys);
		// We the full documents and only the top 20
		// .setLimit(20);
		// ["dbis11",324609906700,1404101397760]

		// Query the Cluster
		ViewResponse result = client.query(view, query);

		// shutdown();
		return createInvocStat(result);

	}

	@Override
	public Collection<InvocStat> getLogEntriesForTaskOnHost(Long taskId,
			String hostName) {
		if (client == null) {
			getConnection();
		}

		View view = client.getView("dev_Invoc", "getLogEntriesForTaskOnHost");

		// Set up the Query object
		Query query = new Query();

		// ["dbis14",3246099067099]
		query.setIncludeDocs(true).setKey(ComplexKey.of(taskId, hostName));
		// We the full documents and only the top 20
		// .setLimit(20);
		// ["dbis11",324609906700,1404101397760]

		// Query the Cluster
		ViewResponse result = client.query(view, query);

		// This ArrayList will contain all found beers
		ArrayList<HashMap<String, String>> hostnames = new ArrayList<HashMap<String, String>>();

		InvocDoc invocDocument = new InvocDoc();
		Set<InvocStat> tempResult = new HashSet<InvocStat>();
		InvocStat temp = null;

		// shutdown();
		return createInvocStat(result);
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTaskOnHostSince(Long taskId,
			String hostName, long timestamp) {

		if (client == null) {
			getConnection();
		}

		View view = client.getView("dev_Invoc", "LogEntriesForTaskOnHostSince");

		// Set up the Query object
		Query query = new Query();

		// ["dbis14",3246099067099]
		query.setIncludeDocs(true).setRange(
				ComplexKey.of(taskId, hostName, timestamp),
				ComplexKey.of(taskId, hostName, 999999999999999999l));
		// We the full documents and only the top 20
		// .setLimit(20);
		// ["dbis11",324609906700,1404101397760]

		// Query the Cluster
		ViewResponse result = client.query(view, query);

		// This ArrayList will contain all found beers
		ArrayList<HashMap<String, String>> hostnames = new ArrayList<HashMap<String, String>>();

		InvocDoc invocDocument = new InvocDoc();
		Set<InvocStat> tempResult = new HashSet<InvocStat>();
		InvocStat temp = null;

		// shutdown();
		return createInvocStat(result);
	}

	private Set<InvocStat> createInvocStat(ViewResponse result) {
		InvocDoc invocDocument = new InvocDoc();
		Set<InvocStat> tempResult = new HashSet<InvocStat>();
		Gson gson = new Gson();

		InvocStat temp = null;
		// Iterate over the found documents
		for (ViewRow row : result) {

			// System.out.println("resrow: "+ row.getValue()) ;
			invocDocument = gson.fromJson((String) row.getDocument(),
					InvocDoc.class);

			temp = new InvocStat(invocDocument.getTaskId());
			temp.setHostName(invocDocument.getHostname());
			temp.setRealTime(invocDocument.getRealTime(),
					invocDocument.getTimestamp());

			Map<String, HashMap<String, Long>> output = invocDocument
					.getFiles();
			List<FileStat> fileStatout = new ArrayList<FileStat>();
			List<FileStat> fileStatin = new ArrayList<FileStat>();
			FileStat file = null;
			Long in = 0l;
			Long out = 0l;

			for (Entry<String, HashMap<String, Long>> val : output.entrySet()) {
				file = new FileStat();
				file.setFileName(val.getKey());
				in = val.getValue().get("realTimeIn");
				out = val.getValue().get("realTimeOut");

				file.setSize(val.getValue().get("size"));

				if (in != null && !in.equals("")) {
					file.setRealTime(in);
					fileStatin.add(file);
				}

				if (out != null && !out.equals("")) {
					file.setRealTime(out);
					fileStatout.add(file);
				}

			}
			temp.setOutputfiles(fileStatout);
			temp.setInputfiles(fileStatin);
			tempResult.add(temp);

		}

		// shutdown();
		System.out
				.println("COUuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuut:"
						+ tempResult.size());
		return tempResult;
	}

}