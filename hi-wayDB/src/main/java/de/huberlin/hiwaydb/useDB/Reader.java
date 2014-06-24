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

import org.json.JSONException;

import com.couchbase.client.CouchbaseClient;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import de.huberlin.hiwaydb.LogToDB.WriteHiwayDB;
import de.huberlin.hiwaydb.dal.File;
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

			if (lineIn.equalsIgnoreCase("c")) {
				
				System.out.println("couchbase connecten...");

				// (Subset) of nodes in the cluster to establish a connection
				List<URI> hosts = Arrays.asList(new URI(
						"http://127.0.0.1:8091/pools"));

				// Name of the Bucket to connect to
				String bucket = "default";

				// Password of the bucket (empty) string if none
				String password = "";

				// Connect to the Cluster
				CouchbaseClient client = new CouchbaseClient(hosts, bucket,
						password);

				// Store a Document
				client.set("my-first-document", "Hello Couchbase!").get();

				// Retreive the Document and print it
				System.out.println(client.get("my-first-document"));

				// Shutting down properly
				client.shutdown();

			} else if (lineIn.equalsIgnoreCase("db")) {

			HiwayDBI testGet = new HiwayDB("jdbc:mysql://localhost/hiwaydb", "root", "keanu7.");

			//HiwayDBI testGet = new HiwayDB();
				
				System.out.println("go...");

			//	System.out.println("getLogEntriesForTask:");

				// for (InvocStat f : testGet.getLogEntriesForTask(1317103212))
				// {
				// System.out.println("Task: "
				// + f.getTaskId() + " RealTime:" + f.getRealTime());
				// }
				//

//				System.out.println("getLogEntriesForTask mit Set:");
//
//				Set<Long> tasks = new HashSet<Long>();
//				tasks.add((long) 466017906);
//				tasks.add(1317103212l);
//				tasks.add(121135303675312l);

				// for (InvocStat f : testGet.getLogEntriesForTasks(tasks)) {
				// System.out.println("Task: "
				// + f.getTaskId() + " RealTime:" + f.getRealTime());
				// }

				System.out.println("getHostnames:");

				for (String f : testGet.getHostNames()) {
					System.out.println(f.toString());
				}

//				System.out.println("TaskIDs for Workflow:variant-call-09.cf");
//
//				for (Long f : testGet.getTaskIdsForWorkflow("variant-call-09.cf")) {
//					System.out.println(f.toString());
//				}
//
//				System.out.println("getTaskNames for ID :");
//				System.out.println("466017906: "
//						+ testGet.getTaskName(466017906));
//				System.out.println("1357269702: "
//						+ testGet.getTaskName(1357269702));

				// System.out.println("All Invocs:");
				//
				// for (InvocStat f : testGet.getLogEntries()) {
				// System.out.println("Task: "
				// + f.getTaskId() + " RealTime:" + f.getRealTime());
				// }

			//	System.out.println("All for host:");

				// for (InvocStat f :
				// testGet.getLogEntriesForHost("dbis13:8042")) {
				// System.out.println("Host: " + f.getHostName() + "TaskID: "
				// + f.getTaskId() + " | RealTime:" + f.getRealTime());
				// }
				//
				// System.out.println("All for host since:");
				//
				// cal.set(2004, Calendar.MAY, 12);
				//
				// for (InvocStat f : testGet.getLogEntriesForHostSince(
				// "dbis13:8042", cal.getTimeInMillis())) {
				// System.out.println("Host: " + f.getHostName() + "TaskID: "
				// + f.getTaskId() + " | RealTime:" + f.getRealTime()
				// + " Date: " + f.getTimestamp());
				// }

//				System.out.println("All for Task on Host:");
//				for (InvocStat f : testGet.getLogEntriesForTaskOnHost(
//						1722821279659l, "dbis13:8042")) {
//					System.out.println(f.toString());
//				}
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

			} else {
				WriteHiwayDB writer = null;

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
				
				writer = new WriteHiwayDB("jdbc:mysql://localhost/hiwaydb",
						"root", "keanu7.", "hibernate.cfg.xml");

					// String input = "D:\\Temp\\" + args[0];
					// e00_01_3r_variant-call-setup-09_001
					// e00_02_2r_variant-call-setup-09_001
					// e11_11_1x_variant-call-09_003
					//
					// 
					// e11_16_1x2x3x5x6x7x_variant-call-09_005.log";
					// String input =
					// "C:\\Users\\Hannes\\Dropbox\\Diplom Arbeit\\other files\\Logs\\wordcount.cf.log";
					String input = "C:\\Users\\Hannes\\Dropbox\\Diplom Arbeit\\other files\\Logs\\loglog.log";

					System.out.println("Input: " + input);

					if (input.endsWith(".log")) {

						fFilePath = Paths.get(input);

						int result = 0;
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
										result = writer
												.lineToDB(new JsonReportEntry(
														line));
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

								if (result == -1)
									break;
							}
						}
					} else {
						System.out
								.println("Eingabe: keine Logdatei, String direkt uebergeben.");
						writer.lineToDB(new JsonReportEntry(args[0].toString()));
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
