package jsonLog;

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
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

import dal.Invocation;
import dal.Task;
import dal.Workflowrun;
import dal.WorkflowrunHasTask;
import de.huberlin.cuneiform.dag.JsonReportEntry;

public class Reader {

	private static Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;

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

			
			try( BufferedReader test = new BufferedReader(new InputStreamReader(System.in)) ) {
				
//				while(test.readLine()
//				;
				
			}
			
			// alle Tasks holen
			Session session = getDBSession().openSession();
			session.beginTransaction();
			List<Task> allTasks = session.createQuery("from Task").list();
			session.getTransaction().commit();
			session.close();

			JsonReportEntry zeile;

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
			WorkflowrunHasTask runTask = null;
			Task task = null;

			session = getDBSession().openSession();
			session.beginTransaction();

			if (input.endsWith(".log")) {

				fFilePath = Paths.get(input);

				int i = 0;
				try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
					while (scanner.hasNextLine()) {
						i++;
						System.out.println("line " + i);
						zeile = new JsonReportEntry(scanner.nextLine());
						System.out.println(zeile.toString());

						if (wfRun == null
								|| (wfRun.getRunId() != null && wfRun
										.getRunId().equals(
												zeile.getRunId().toString()))) {
							wfRun = new Workflowrun();
							if (zeile.getRunId() != null) {
								wfRun.setRunId(zeile.getRunId().toString());
								session.save(wfRun);
							}
						}

						if (zeile.getTaskId() != 0
								&& (task == null || task.getTaskId() != zeile
										.getTaskId())) {
							task = new Task();

							task.setTaskId(zeile.getTaskId());
							task.setTaskName(zeile.getTaskName());
							task.setLanguage("bash");

							boolean alreadyInDB = false;

							for (Task tempTask : allTasks) {
								if (tempTask.getTaskId() == task.getTaskId()) {
									task = tempTask;
									alreadyInDB = true;
									break;
								}
							}

							if (!alreadyInDB) {
								session.save(task);
								System.out
										.println("Neuer.. Tasks in DB speichern ID: "
												+ task.getTaskId());
							}

							runTask = new WorkflowrunHasTask();
							runTask.setTask(task);
							runTask.setWorkflowrun(wfRun);

							// session.save(runTask);
						}

						if (invoc == null) {
							invoc = new Invocation();
							invoc.setInvocationId(zeile.getInvocId());
							// invoc.setWorkflowrunHasTask(runTask);

						}

						key = zeile.getKey();

						switch (key) {
						case "invoc-host":

							System.out.println(zeile.getValue());
							if (invoc == null) {
								invoc = new Invocation();

								invoc.setInvocationId(zeile.getInvocId());
							} else {
								invoc.setHostname(zeile.getValue());
							}

							break;
						case "invoc-time":
							valuePart = zeile.getValueJsonObj();
							System.out.println("nMinPageFault: "
									+ valuePart.get("nMinPageFault"));
							System.out.println("nForcedContextSwitch: "
									+ valuePart.get("nForcedContextSwitch"));
							System.out.println("nSocketRead: "
									+ valuePart.get("nSocketRead"));
							System.out.println("realTime: "
									+ valuePart.get("realTime"));
							break;

						}

					}
				}

			} else {
				// dann wohl ein JSON String
			}

			System.out.println();

			session.getTransaction().commit();

			session.close();

			System.out.println("juchei");

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private static Invocation createInvoc(JsonReportEntry entry) {
		Invocation invoc = new Invocation();

		invoc.setInvocationId(entry.getInvocId());

		return invoc;

	}

	private static SessionFactory getDBSession() {

		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(builder.build());

		return sessionFactory;
	}

}
