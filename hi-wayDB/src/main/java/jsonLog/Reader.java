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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import au.com.bytecode.opencsv.CSVReader;
import dal.Task;

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

				if (input.endsWith(".log")) {

					fFilePath = Paths.get(input);
					try (Scanner scanner = new Scanner(fFilePath,ENCODING.name())) {
						while (scanner.hasNextLine()) {
							// processLine(scanner.nextLine());
							zeile = new JsonReportEntry(scanner.nextLine());
							
							System.out.println(zeile.toString());
							
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

			
			// Session session = sessionFactory.openSession();
			// session.beginTransaction();
			//
			// Task task = new Task();
			// task.setLanguage("egal");
			// task.setTaskId(2);
			// task.setTaskName("egal1");
			//
			// session.save(task);
			// //session.save( new Event( "A follow up event", new Date() ) );
			// session.getTransaction().commit();
			// session.close();
			//
			

			System.out.println("juchei");

		} catch (IOException e) {

			e.printStackTrace();
		}

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
