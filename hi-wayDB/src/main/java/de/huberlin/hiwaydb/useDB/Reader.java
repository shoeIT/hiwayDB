package de.huberlin.hiwaydb.useDB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import net.spy.memcached.internal.OperationFuture;

import org.json.JSONException;

import de.huberlin.wbi.cuneiform.core.semanticmodel.JsonReportEntry;

public class Reader {

	private static Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;

	public static void main(String[] args) {

		List<String> jsonFehler = new ArrayList<String>();

		List<String> fehler = new ArrayList<String>();

		String lineIn = "";

		try {

			try (BufferedReader test = new BufferedReader(
					new InputStreamReader(System.in))) {

				lineIn = test.readLine();

			}

			if (lineIn.equalsIgnoreCase("cWrite")) {
				
				
				System.out.println("COURCHBASe connecten...");
				
				
				HiwayDBI writer = null;
				
				List<URI> uris = new ArrayList<URI>();
				uris.add(URI.create("http://127.0.0.1:8091/pools"));

				writer = new HiwayDBNoSQL("hiwaydb",	"", uris,"","","");
				
				

					// String input = "D:\\Temp\\" + args[0];
					// e00_01_3r_variant-call-setup-09_001
					// e00_02_2r_variant-call-setup-09_001
					// e11_11_1x_variant-call-09_003
					//
					// 
					// e11_16_1x2x3x5x6x7x_variant-call-09_005.log";
					// String input =
				//i1_s368_r1_placementAware
			//	 "C:\\Users\\Hannes\\Dropbox\\Diplom Arbeit\\temp\\Logs\\wordcount.cf.log";
				String input = "C:\\Users\\Hannes\\Dropbox\\Diplom Arbeit\\temp\\Logs\\wordcount.cf.log";

				
				
					System.out.println("Input: " + input);

					if (input.endsWith(".log")) {

						fFilePath = Paths.get(input);

						String result = "";
						int i = 0;
						try (Scanner scanner = new Scanner(fFilePath,
								ENCODING.name())) {
							while (scanner.hasNextLine()) {
								i++;
								System.out.println("line " + i);

								String line = scanner.nextLine();

								line = line.replaceAll("\0", "");

								if (!line.isEmpty()) {

									try {
										writer.logToDB(new JsonReportEntry(line));
									} catch (JSONException e) {
										System.out
												.println("JSON Exception FEHLER!!!!!!!!!!!!: "
														+ e);
										//jsonFehler.add("Z" + i + " | "+ e.getMessage());
									} catch (org.hibernate.exception.ConstraintViolationException e) {
										System.out
												.println(" Hibernate FEHLER!!!!!!!!!!!!: "
														+ e);
										//fehler.add("Z" + i + " | "+ e.getMessage());
									} catch (Exception e) {
										System.out
												.println("FEHLER!!!!!!!!!!!!: "+ e);
									//fehler.add("Z" + i + " | "+ e.getMessage());
									}
								}

								if (!result.isEmpty())
									break;
							}
						}
					} else {
						System.out
								.println("Eingabe: keine Logdatei, String direkt uebergeben.");
						writer.logToDB(new JsonReportEntry(args[0].toString()));
					}
					
				//	writer.shutdown();
				  					

			} else if (lineIn.equalsIgnoreCase("cRead")) {
			
				
				List<URI> uris = new ArrayList<URI>();
				uris.add(URI.create("http://127.0.0.1:8091/pools"));
				
			HiwayDBI testGet = new HiwayDBNoSQL("hiwaydb","",uris,"","","");

			//HiwayDBI testGet = new HiwayDB();
				
				System.out.println("go...");

//				System.out.println("getLogEntriesForTask:");
//
//				 for (InvocStat f : testGet.getLogEntriesForTask(324609906700l))
//				 {
//				 System.out.println(f.toString());
//				 }
//				
//
//				System.out.println("getLogEntriesForTask mit Set:");
//
//				Set<Long> tasks = new HashSet<Long>();
//				tasks.add(989639045l);
//				tasks.add(324609906700l);
//			
//				 for (InvocStat f : testGet.getLogEntriesForTasks(tasks)) {
//				 System.out.println(f.toString());				 }
//
//		


//
//				 System.out.println("All Invocs:");
//				
//				 for (InvocStat f : testGet.getLogEntriesForTask(989639045l)) {
//				 System.out.println("Task: "
//				 + f.getTaskId() + " RealTime:" + f.getRealTime());
//				 
//				 for (FileStat fi :f.getInputFiles()){
//					 System.out.println("file in: "+ fi.getFileName() + " size: " + fi.getSize() + " time: "+ fi.getRealTime());
//				 }
//				 
//				 
//				 for (FileStat fi :f.getOutputFiles()){
//					 System.out.println("file out: "+ fi.getFileName() + " size: " + fi.getSize() + " time: "+ fi.getRealTime());
//				 }
//				 }

			//	System.out.println("All for host:");

				// for (InvocStat f :
				// testGet.getLogEntriesForHost("dbis13:8042")) {
				// System.out.println("Host: " + f.getHostName() + "TaskID: "
				// + f.getTaskId() + " | RealTime:" + f.getRealTime());
				// }
				//
				
				 Calendar cal = Calendar.getInstance();
				 cal.set(2014, Calendar.JUNE, 22);
				
				 System.out.println("All for host since: 1403599949182 " );
				 //_2014-06-20 17:03:58
				 
				// java.util.Date dt = new java.util.Date();

				//["dbis14",3246099067099,1404103422872]
				 //["dbis11",324609906700,0], ["dbis11",324609906700,0]
				
				 for (InvocStat f : testGet.getLogEntriesForTaskOnHostSince(324609906700l,
				 "dbis11",1404101397760l)) {
				 System.out.println(f.toString());
				 //+ " Date: " + f.getTimestamp());
//				 for (FileStat fi :f.getInputFiles()){
//					 System.out.println("file in: "+ fi.getFileName() + " size: " + fi.getSize() + " time: "+ fi.getRealTime());
//				 }
//				 
//				 
//				 for (FileStat fi :f.getOutputFiles()){
//					 System.out.println("file out: "+ fi.getFileName() + " size: " + fi.getSize() + " time: "+ fi.getRealTime());
//				 }
				 }

				System.out.println("All for Task on Host:");
				for (InvocStat f : testGet.getLogEntriesForTaskOnHost(
						324609906700l, "dbis11")) {
					System.out.println(f.toString());
				}
//
//				Calendar cal = Calendar.getInstance();
//
//				cal.set(2004, Calendar.MAY, 12);
//
//				System.out.println("All for Task on Host Since:");
//				for (InvocStat f : testGet.getLogEntriesForTaskOnHostSince(
//						1722821279659l, "dbis12:8042", cal.getTimeInMillis())) {
//					System.out.println(f.toString());
//				}
				
				System.out.println("TaskIDs for Workflow:variant-call-09-setup.cf");

				for (Long f : testGet.getTaskIdsForWorkflow("variant-call-09-setup.cf")) {
					System.out.println(f.toString());
				}
				
				System.out.println("getTaskNames for ID :");
				System.out.println("6: "
						+ testGet.getTaskName(6l));
				System.out.println("453506959: "
						+ testGet.getTaskName(453506959l));
				
				System.out.println("getHostnames:");

				for (String f : testGet.getHostNames()) {
					System.out.println(f.toString());
				}

			

			}
			else if (lineIn.equalsIgnoreCase("db")) {

				HiwayDBI testGet = new HiwayDB( "root", "reverse","jdbc:mysql://localhost/hiwaydb");

				//HiwayDBI testGet = new HiwayDB();
					
					System.out.println("go...");

					System.out.println("getLogEntriesForTask:");

					 for (InvocStat f : testGet.getLogEntriesForTask(324609906700l))
					 {
					 System.out.println(f.toString());
					 }
					

					System.out.println("getLogEntriesForTask mit Set:");
	
					Set<Long> tasks = new HashSet<Long>();
				tasks.add(2084368153987l);
					tasks.add(324609906700l);
				
					 for (InvocStat f : testGet.getLogEntriesForTasks(tasks)) {
					 System.out.println(f.toString());
					}
	//
					System.out.println("getHostnames:");
	
					for (String f : testGet.getHostNames()) {
						System.out.println(f.toString());
					}
	//////
					System.out.println("TaskIDs for Workflow:variant-call-09.cf");
	
				for (Long f : testGet.getTaskIdsForWorkflow("variant-call-09.cf")) {
						System.out.println(f.toString());
					}
	//
				System.out.println("getTaskNames for ID :");
					System.out.println("240495169287: "
							+ testGet.getTaskName(240495169287l));
				System.out.println("240495169287: "
					+ testGet.getTaskName(240495169287l));
	
					 System.out.println("All Invocs:");
					
				 for (InvocStat f : testGet.getLogEntriesForTask(989639045l)) {
				 System.out.println("Task: "
				 + f.getTaskId() + " RealTime:" + f.getRealTime());
					 
				 for (FileStat fi :f.getInputFiles()){
						 System.out.println("file in: "+ fi.getFileName() + " size: " + fi.getSize() + " time: "+ fi.getRealTime());
				 }
//					 
//					 
//					 for (FileStat fi :f.getOutputFiles()){
//						 System.out.println("file out: "+ fi.getFileName() + " size: " + fi.getSize() + " time: "+ fi.getRealTime());
//					 }
//					 }

				
					 Calendar cal = Calendar.getInstance();
					 cal.set(2014, Calendar.JUNE, 22);
					
					 System.out.println("All for host since: 1403599949182 " );
					 //_2014-06-20 17:03:58
					 
					// java.util.Date dt = new java.util.Date();

					
					 for (InvocStat s : testGet.getLogEntriesForTaskOnHostSince(989639045l,
					 "hiway", 1403599949113l)) {
					 System.out.println("Host: " + s.getHostName() + "TaskID: "
					 + s.getTaskId() + " | RealTime:" + s.getRealTime()
					 + " Date: " + s.getTimestamp());
					 }

//					System.out.println("All for Task on Host:");
//					for (InvocStat f : testGet.getLogEntriesForTaskOnHost(
//							1722821279659l, "dbis13:8042")) {
//						System.out.println(f.toString());
					}
	//
//					Calendar cal = Calendar.getInstance();
	//
//					cal.set(2004, Calendar.MAY, 12);
	//
//					System.out.println("All for Task on Host Since:");
//					for (InvocStat f : testGet.getLogEntriesForTaskOnHostSince(
//							1722821279659l, "dbis12:8042", cal.getTimeInMillis())) {
//						System.out.println(f.toString());
//					}

				}
			else {
				HiwayDB writer = null;

//				if (args.length == 0) {
//					System.out.println("keine Eingabe...lese StandardIn");
//
//					// writer = new WriteHiwayDB("hibernate.cfg.xml");
//					writer = new WriteHiwayDB("jdbc:mysql://localhost/hiwaydb",
//							"root", "keanu7.", "hibernate.cfg.xml");
//
//					try (BufferedReader test = new BufferedReader(
//							new InputStreamReader(System.in))) {
//
//						String line = test.readLine();
//						while (line != null) {
//							writer.lineToDB(new JsonReportEntry(line));
//						}
//					}
//				} 
//				else {
//					System.out.println("Eingabe: Name der logdatei:  "
//							+ args[0] + " Config: " + args[1]);

//					if (args[1] != "") {
//						writer = new WriteHiwayDB(args[1]);
//					} else {
//						writer = new WriteHiwayDB("hibernate.cfg.xml");
//					}
				
				writer = new HiwayDB("root", "reverse","jdbc:mysql://localhost/hiwaydb"	);

					// String input = "D:\\Temp\\" + args[0];
					// e00_01_3r_variant-call-setup-09_001
					// e00_02_2r_variant-call-setup-09_001
					// e11_11_1x_variant-call-09_003
					//
					// 
					// e11_16_1x2x3x5x6x7x_variant-call-09_005.log";
					// String input =
			//	 "C:\\Users\\Hannes\\Dropbox\\Diplom Arbeit\\temp\\Logs\\wordcount.cf.log"; i1_s368_r2_placementAware
				String input = "C:\\Users\\Hannes\\Dropbox\\Diplom Arbeit\\temp\\Logs\\i1_s368_r3_greedyQueue.log";


					System.out.println("Input: " + input);

					if (input.endsWith(".log")) {

						fFilePath = Paths.get(input);

						String result = "";
						int i = 0;
						try (Scanner scanner = new Scanner(fFilePath,
								ENCODING.name())) {
							while (scanner.hasNextLine()) {
								i++;
								System.out.println("line " + i);

								String line = scanner.nextLine();

								line = line.replaceAll("\0", "");

								if (!line.isEmpty()) {

									try {
										writer.logToDB((new JsonReportEntry(line)));
									} catch (JSONException e) {
										System.out
												.println("FEHLER!!!!!!!!!!!!: "
														+ line);
										jsonFehler.add("Z" + i + " | "
												+ e.getMessage());
									} catch (org.hibernate.exception.ConstraintViolationException e) {
										System.out
												.println("FEHLER!!!!!!!!!!!!: "
														+ line);
										fehler.add("Z" + i + " | "
												+ e.getMessage());
									} catch (Exception e) {
										System.out
												.println("FEHLER!!!!!!!!!!!!: "
														+ line);
										fehler.add("Z" + i + " | "
												+ e.getMessage());
									}
								}

								if (!result.isEmpty())
									break;
							}
						}
					} else {
						System.out
								.println("Eingabe: keine Logdatei, String direkt uebergeben.");
						writer.logToDB((new JsonReportEntry(args[0].toString())));
					}
				}
				System.out.println("juchei fertig...");
			//}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			System.out.println("JSONFehler:");
			for (String s : fehler) {
				System.out.println(s + "|");
			}

			System.out.println("Fehler:");
			for (String s : fehler) {
				System.out.println(s + "|");
			}

		}

	}
}
