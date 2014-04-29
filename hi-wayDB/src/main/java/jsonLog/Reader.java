package jsonLog;

import java.io.FileReader;
import java.io.IOException;
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

import au.com.bytecode.opencsv.CSVReader;
import dal.Invocation;
import dal.Task;
import dal.Workflowrun;
import dal.WorkflowrunHasTask;

public class Reader {

	private static Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	
	public static void main(String[] args) {

		try {

			System.out.println("go...");
//
//			if (args.length == 0)
//				System.out.println("keine Eingabe...")
//			else {
				System.out.println("Lesedatei in JsonReportEntry gemacht... ");

				//SessionFactory sessionFactory = getDBSession();
				
				String input = "D:\\Temp\\wordcount.cf.log";
				
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
				 WorkflowrunHasTask runTask= null;
				 Task task = null;


				if (input.endsWith(".log")) {

					fFilePath = Paths.get(input);
					
					int i = 0;
					try (Scanner scanner = new Scanner(fFilePath,ENCODING.name())) {
						while (scanner.hasNextLine()) {
							i++;
							System.out.println("line " + i);
							zeile = new JsonReportEntry(scanner.nextLine());
							
							if(wfRun==null)
							{
								wfRun = new Workflowrun();
								wfRun.setRunId(zeile.getRunId().toString());								
							}
							
							if(task==null)
							{
								task = new Task();
								//task(zeile.getRunId().toString());								
							}
							
							if(runTask ==null)
							{
								runTask = new WorkflowrunHasTask();
								//runTask.set
							}
							
							System.out.println(zeile.toString());
														
							key = zeile.getKey();
							
							switch(key)
							{
							case "invoc-host":
								
								System.out.println(zeile.getValue());
								if(invoc ==null)
								{
									invoc = new Invocation();
									
									invoc.setInvocationId(zeile.getInvocId());
								}
								else
								{
									invoc.setHostname(zeile.getValue());
								}
								
								break;
							case "invoc-time":
								valuePart = zeile.getValueJsonObj();
								System.out.println(valuePart.get("value"));
								break;
								
							}
//							payload = new JsonReportEntry(zeile.getValue());
//							
						String test2 = zeile.getValue();
//							 = null;
									
						System.out.println(test2);						
//													
//							Session session = sessionFactory.openSession();
//							session.beginTransaction();
//							List<Task> result = session.createQuery("from Task").list();
//							for (Task event : (List<Task>) result) {
//								System.out.println("Task (" + event.getTaskName() + ") : "
//										+ event.getLanguage());
//							}
//							session.getTransaction().commit();
//							session.close();
						}
					}
				} 
				else {
					// dann wohl ein JSON String
				}

				System.out.println();
//			}

			
			 Session session = getDBSession().openSession();
			 session.beginTransaction();
			
			 Task task1 = new Task();
			 task1.setLanguage("egal");
			 task1.setTaskId(2);
			 task1.setTaskName("egal1");
			
			 
			 Task task2 = new Task();
			 task2.setLanguage("egal");
			 task2.setTaskId(2);
			 task2.setTaskName("egal1");
			 
			 session.save(task1);
			 //session.save( new Event( "A follow up event", new Date() ) );
			 session.getTransaction().commit();
			
			 session.close();
			
			

			System.out.println("juchei");

		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	private static Invocation createInvoc(JsonReportEntry entry)
	{
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
