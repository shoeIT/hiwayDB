package de.huberlin.hiwaydb.LogToDB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

import de.huberlin.hiwaydb.dal.Invocation;
import de.huberlin.hiwaydb.dal.Output;
import de.huberlin.hiwaydb.dal.Stagingevent;
import de.huberlin.hiwaydb.dal.Task;
import de.huberlin.hiwaydb.dal.Timestat;
import de.huberlin.hiwaydb.dal.Workflowrun;
import de.huberlin.cuneiform.dag.JsonReportEntry;

public class Reader {

	private static Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private static SessionFactory dbSessionFactory;

	public static void main(String[] args) {

		try {

			System.out.println("go...");
			//
			// if (args.length == 0)
			// System.out.println("keine Eingabe...")
			// else {
			System.out.println("Lesedatei in JsonReportEntry gemacht... ");

			// SessionFactory sessionFactory = getDBSession();

			String input = "D:\\Temp\\wordcount.cf.log";
			//String input = "D:\\Temp\\variant-call-09-setup.cf.log";

			try (BufferedReader test = new BufferedReader(
					new InputStreamReader(System.in))) {

				// while(test.readLine()
				// ;

			}

			dbSessionFactory = getDBSession();
			Transaction tx = null;
			Session session = null;
			JsonReportEntry logEntryRow;

			UUID runId = null;
			Long invocId = null;
			String key = null;
			String value = null;
			String taskname = null;
			Long taskId = null;
			Long timestamp = null;
			String lang = null;
			JsonReportEntry payload = null;
			JSONObject valuePart = null;
			Invocation invoc = null;
			Workflowrun wfRun = null;

			Task task = null;

			if (input.endsWith(".log")) {

				fFilePath = Paths.get(input);

				int i = 0;
				try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
					while (scanner.hasNextLine()) {
						i++;

						try {

							System.out.println("line " + i);

							logEntryRow = new JsonReportEntry(
									scanner.nextLine());
							System.out.println(logEntryRow.toString());

							session = dbSessionFactory.openSession();
							tx = session.beginTransaction();

							String runID = null;
							if (logEntryRow.getRunId() != null) {
								runID = logEntryRow.getRunId().toString();
							}

							long taskID = 0;
							if (logEntryRow.getTaskId() != null) {
								taskID = logEntryRow.getTaskId();
							}

							//long invocID = 0;
							//if (logEntryRow.getInvocId() != 0) {
								long invocID = logEntryRow.getInvocId();
							//}

							Long timestampTemp = logEntryRow.getTimestamp();

							// hier erst checken ob WFrun, Task und Invocation
							// bereits eingetragen sind
							Query query = session
									.createQuery("FROM Workflowrun E WHERE E.runId='"
											+ runID + "'");
							List<Workflowrun> resultsWfRun = query.list();

							query = session
									.createQuery("FROM Task E WHERE E.taskId ="
											+ taskID);
							List<Task> resultsTasks = query.list();

							query = session
									.createQuery("FROM Invocation E WHERE E.invocationId ="
											+ invocID);
							List<Invocation> resultsInvoc = query.list();
							
							query = session
									.createQuery("FROM Stagingevent E WHERE inevent=1 AND E.invocation="
											+ invocID);
							List<Stagingevent> resultsStagingIn = query.list();
							
							query = session
									.createQuery("FROM Stagingevent E WHERE inevent=0 AND E.invocation="
											+ invocID);
							List<Stagingevent> resultsStagingOut = query.list();

							//tx.commit();

							if (!resultsWfRun.isEmpty()) {
								wfRun = resultsWfRun.get(0);
							}
							if (!resultsTasks.isEmpty()) {
								task = resultsTasks.get(0);
							}
							if (!resultsInvoc.isEmpty()) {
								invoc = resultsInvoc.get(0);
							}

							tx = session.beginTransaction();

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
								invoc.setInvocationId(invocID);
								invoc.setTask(task);
								invoc.setWorkflowrun(wfRun);
								session.save(invoc);
							}

							key = logEntryRow.getKey();

							switch (key) {
							case "invoc-host":
								invoc.setHostname(logEntryRow.getValue());
								break;
							case "wf-name":
								wfRun.setWfName(logEntryRow.getValue());
								break;
							case "wf-time":
								String val = logEntryRow.getValue().replace('"', ' ').trim();
								Long test = Long.parseLong(val,10);		
								
								wfRun.setWfTime(test);
								break;
							case "invoc-time-sched":
								invoc.setScheduleTime(Long.parseLong(logEntryRow.getValue().replace('"', ' ').trim(),10));
								break;
								
							case JsonReportEntry.KEY_INVOC_STDERR:
								invoc.setStandardError(logEntryRow.getValue());
								break;
							case JsonReportEntry.KEY_INVOC_OUTPUT:
								valuePart = logEntryRow.getValueJsonObj();
								
								Output output;
								
								for ( Iterator<String> iterator = valuePart.keys(); iterator.hasNext(); )
								{
									String keypart = iterator.next();
									System.out.println(keypart);
									output = new Output();
									output.setKeypart(keypart);
									output.setInvocation(invoc);
									output.setContent(valuePart.get(keypart).toString());
									session.save(output);
								}														
								
								break;
							case JsonReportEntry.KEY_INVOC_STDOUT:
								invoc.setStandardOut(logEntryRow.getValue());
								break;
							case JsonReportEntry.KEY_INVOC_TIME:
								valuePart = logEntryRow.getValueJsonObj();
								
								Timestat invocTime = GetTimeStat(valuePart);
											
								invocTime.setInvocation(invoc);
								
								session.save(invocTime);
								break;
								
							case "invoc-time-stagein":
								valuePart = logEntryRow.getValueJsonObj();
								
								if(resultsStagingIn.isEmpty())
								{
									Stagingevent se = new Stagingevent();
									
									se.setinEvent(true);
									se.setInvocation(invoc);
									session.save(se);
									
									Timestat invocTimeStage = GetTimeStat(valuePart);	
									invocTimeStage.setStagingevent(se);
									session.save(invocTimeStage);
									break;
								}															
								
							case "invoc-time-stageout":
								valuePart = logEntryRow.getValueJsonObj();

								if(resultsStagingOut.isEmpty())
								{
								Stagingevent seOut = new Stagingevent();
								
								seOut.setinEvent(false);
								seOut.setInvocation(invoc);
								session.save(seOut);
								
								Timestat invocTimeStageOut = GetTimeStat(valuePart);	
								invocTimeStageOut.setStagingevent(seOut);
								
								session.save(invocTimeStageOut);
								}
								break;
							}

							tx.commit();
						} catch (Exception e) {
							if (tx != null)
								tx.rollback();
							e.printStackTrace();
							break;
							
						} finally {
							session.close();
							
						}

					}

				}
			}

			else {
				// dann wohl ein JSON String
			}

			System.out.println("juchei");

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	private static Timestat GetTimeStat(JSONObject valuePart)	{
		
		Timestat timeStat = new Timestat();
		timeStat.setNminPageFault(Long.parseLong(valuePart.get("nMinPageFault").toString(),10));
		timeStat.setNforcedContextSwitch(Long.parseLong(valuePart.get("nForcedContextSwitch").toString(),10));
		timeStat.setAvgDataSize(Long.parseLong(valuePart.get("avgDataSize").toString(),10));
		timeStat.setNsocketRead(Long.parseLong(valuePart.get("nSocketRead").toString(),10));								
		timeStat.setNioWrite(Long.parseLong(valuePart.get("nIoWrite").toString(),10));
		timeStat.setAvgResidentSetSize(Long.parseLong(valuePart.get("avgResidentSetSize").toString(),10));
		timeStat.setNmajPageFault(Long.parseLong(valuePart.get("nMajPageFault").toString(),10));
		timeStat.setNwaitContextSwitch(Long.parseLong(valuePart.get("nWaitContextSwitch").toString(),10));
		timeStat.setUserTime(Double.parseDouble(valuePart.get("userTime").toString()));							
		timeStat.setRealTime(Double.parseDouble(valuePart.get("realTime").toString()));
		timeStat.setSysTime(Double.parseDouble(valuePart.get("sysTime").toString()));
		timeStat.setNsocketWrite(Long.parseLong(valuePart.get("nSocketWrite").toString(),10));
		timeStat.setMaxResidentSetSize(Long.parseLong(valuePart.get("maxResidentSetSize").toString(),10));
		timeStat.setAvgStackSize(Long.parseLong(valuePart.get("avgStackSize").toString(),10));
		timeStat.setNswapOutMainMem(Long.parseLong(valuePart.get("nSwapOutMainMem").toString(),10));
		timeStat.setNioRead(Long.parseLong(valuePart.get("nIoRead").toString(),10));
		timeStat.setNsignal(Long.parseLong(valuePart.get("nSignal").toString(),10));
		timeStat.setAvgTextSize(Long.parseLong(valuePart.get("avgTextSize").toString(),10));	
		
		return timeStat;
	}
	

	private static Invocation createInvoc(JsonReportEntry entry) {
		Invocation invoc = new Invocation();

		invoc.setInvocationId(entry.getInvocId());

		return invoc;

	}

	private static SessionFactory getDBSession() {

		try {
			Configuration configuration = new Configuration().configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties());
			SessionFactory sessionFactory = configuration
					.buildSessionFactory(builder.build());
			return sessionFactory;
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

	}

}
