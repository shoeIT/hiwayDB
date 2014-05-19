package de.huberlin.hiwaydb.LogToDB;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

import de.huberlin.hiwaydb.dal.Invocation;
import de.huberlin.hiwaydb.dal.Task;
import de.huberlin.hiwaydb.dal.Timestat;
import de.huberlin.hiwaydb.dal.Workflowrun;
import de.huberlin.wbi.cuneiform.core.invoc.JsonReportEntry;

public class WriteHiwayDB {

	private SessionFactory dbSessionFactory;
	private Transaction tx;
	private Session session;
	
	public WriteHiwayDB()
	{
		dbSessionFactory = getDBSession();
	}

	public int lineToDB(JsonReportEntry logEntryRow) {

		try {

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

			// long invocID = 0;
			// if (logEntryRow.getInvocId() != 0) {
			long invocID = logEntryRow.getInvocId();
			// }

			Long timestampTemp = logEntryRow.getTimestamp();

			// hier erst checken ob WFrun, Task und Invocation
			// bereits eingetragen sind
			Query query = session
					.createQuery("FROM Workflowrun E WHERE E.runId='" + runID
							+ "'");
			List<Workflowrun> resultsWfRun = query.list();

			query = session
					.createQuery("FROM Task E WHERE E.taskId =" + taskID);
			List<Task> resultsTasks = query.list();

			query = session
					.createQuery("FROM Invocation E WHERE E.invocationId ="
							+ invocID);
			List<Invocation> resultsInvoc = query.list();

			query = session
					.createQuery("FROM Stagingevent E WHERE inevent=1 AND E.invocation="
							+ invocID);
//			List<Stagingevent> resultsStagingIn = query.list();
//
//			query = session
//					.createQuery("FROM Stagingevent E WHERE inevent=0 AND E.invocation="
//							+ invocID);
//			List<Stagingevent> resultsStagingOut = query.list();

			// tx.commit();

			Workflowrun wfRun = null;
			if (!resultsWfRun.isEmpty()) {
				wfRun = resultsWfRun.get(0);
			}
			Task task = null;
			if (!resultsTasks.isEmpty()) {
				task = resultsTasks.get(0);
			}
			Invocation invoc = null;
			if (!resultsInvoc.isEmpty()) {
				invoc = resultsInvoc.get(0);
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
				invoc.setInvocationId(invocID);
				invoc.setTask(task);
				invoc.setWorkflowrun(wfRun);
				session.save(invoc);
			}

			String key = logEntryRow.getKey();

			JSONObject valuePart;
			switch (key) {
			case "invoc-host":
				invoc.setHostname(logEntryRow.getValue());
				break;
			case "wf-name":
				wfRun.setWfName(logEntryRow.getValue());
				break;
			case "wf-time":
				String val = logEntryRow.getValue().replace('"', ' ').trim();
				Long test = Long.parseLong(val, 10);

				wfRun.setWfTime(test);
				break;
			case "invoc-time-sched":
				invoc.setScheduleTime(Long.parseLong(logEntryRow.getValue()
						.replace('"', ' ').trim(), 10));
				break;

			case JsonReportEntry.KEY_INVOC_STDERR:
				invoc.setStandardError(logEntryRow.getValue());
				break;
//			case JsonReportEntry.KEY_INVOC_OUTPUT:
//				valuePart = logEntryRow.getValueJsonObj();
//
//				Output output;
//
//				for (Iterator<String> iterator = valuePart.keys(); iterator
//						.hasNext();) {
//					String keypart = iterator.next();
//					System.out.println(keypart);
//					output = new Output();
//					output.setKeypart(keypart);
//					output.setInvocation(invoc);
//					output.setContent(valuePart.get(keypart).toString());
//					session.save(output);
//				}
//
//				break;
			case JsonReportEntry.KEY_INVOC_STDOUT:
				invoc.setStandardOut(logEntryRow.getValue());
				break;
			case JsonReportEntry.KEY_INVOC_TIME:
				valuePart = logEntryRow.getValueJsonObj();

				Timestat invocTime = GetTimeStat(valuePart);

				invocTime.setInvocation(invoc);

				session.save(invocTime);
				break;

//			case "invoc-time-stagein":
//				valuePart = logEntryRow.getValueJsonObj();
//
//				if (resultsStagingIn.isEmpty()) {
//					Stagingevent se = new Stagingevent();
//
//					se.setinEvent(true);
//					se.setInvocation(invoc);
//					session.save(se);
//
//					Timestat invocTimeStage = GetTimeStat(valuePart);
//					invocTimeStage.setStagingevent(se);
//					session.save(invocTimeStage);
//					break;
//				}
//
//			case "invoc-time-stageout":
//				valuePart = logEntryRow.getValueJsonObj();
//
//				if (resultsStagingOut.isEmpty()) {
//					Stagingevent seOut = new Stagingevent();
//
//					seOut.setinEvent(false);
//					seOut.setInvocation(invoc);
//					session.save(seOut);
//
//					Timestat invocTimeStageOut = GetTimeStat(valuePart);
//					invocTimeStageOut.setStagingevent(seOut);
//
//					session.save(invocTimeStageOut);
//				}
//				break;
			}

			tx.commit();

			return 1;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return -1;

		} finally {
			session.close();
		}
	}

	private static Timestat GetTimeStat(JSONObject valuePart) {

		Timestat timeStat = new Timestat();
		timeStat.setNminPageFault(Long.parseLong(valuePart.get("nMinPageFault")
				.toString(), 10));
		timeStat.setNforcedContextSwitch(Long.parseLong(
				valuePart.get("nForcedContextSwitch").toString(), 10));
		timeStat.setAvgDataSize(Long.parseLong(valuePart.get("avgDataSize")
				.toString(), 10));
		timeStat.setNsocketRead(Long.parseLong(valuePart.get("nSocketRead")
				.toString(), 10));
		timeStat.setNioWrite(Long.parseLong(valuePart.get("nIoWrite")
				.toString(), 10));
		timeStat.setAvgResidentSetSize(Long.parseLong(
				valuePart.get("avgResidentSetSize").toString(), 10));
		timeStat.setNmajPageFault(Long.parseLong(valuePart.get("nMajPageFault")
				.toString(), 10));
		timeStat.setNwaitContextSwitch(Long.parseLong(
				valuePart.get("nWaitContextSwitch").toString(), 10));
		timeStat.setUserTime(Double.parseDouble(valuePart.get("userTime")
				.toString()));
		timeStat.setRealTime(Double.parseDouble(valuePart.get("realTime")
				.toString()));
		timeStat.setSysTime(Double.parseDouble(valuePart.get("sysTime")
				.toString()));
		timeStat.setNsocketWrite(Long.parseLong(valuePart.get("nSocketWrite")
				.toString(), 10));
		timeStat.setMaxResidentSetSize(Long.parseLong(
				valuePart.get("maxResidentSetSize").toString(), 10));
		timeStat.setAvgStackSize(Long.parseLong(valuePart.get("avgStackSize")
				.toString(), 10));
		timeStat.setNswapOutMainMem(Long.parseLong(
				valuePart.get("nSwapOutMainMem").toString(), 10));
		timeStat.setNioRead(Long.parseLong(valuePart.get("nIoRead").toString(),
				10));
		timeStat.setNsignal(Long.parseLong(valuePart.get("nSignal").toString(),
				10));
		timeStat.setAvgTextSize(Long.parseLong(valuePart.get("avgTextSize")
				.toString(), 10));

		return timeStat;
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
