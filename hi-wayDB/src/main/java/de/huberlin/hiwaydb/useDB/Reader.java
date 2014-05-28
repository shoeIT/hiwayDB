package de.huberlin.hiwaydb.useDB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import de.huberlin.hiwaydb.LogToDB.WriteHiwayDB;
import de.huberlin.hiwaydb.dal.File;
import de.huberlin.wbi.cuneiform.core.invoc.JsonReportEntry;

public class Reader {

	private static Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;

	public static void main(String[] args) {

		try {

			HiwayDBI testGet = new HiwayDB();

			System.out.println("go...");

			for (InvocStat f : testGet.getLogEntriesForTask(1317103212)) {
				System.out.println("Invoc: " + f.getInvocId() + " , Task: "
						+ f.getTaskId() + " RealTime:" + f.getRealTime());
			}

			WriteHiwayDB writer = null;

			if (args.length == 0) {
				System.out.println("keine Eingabe...lese StandardIn");

				writer = new WriteHiwayDB("hibernate.cfg.xml");

				try (BufferedReader test = new BufferedReader(
						new InputStreamReader(System.in))) {

					String line = test.readLine();
					while (line != null) {
						writer.lineToDB(new JsonReportEntry(line));
					}
				}
			} else {
				System.out.println("Eingabe: Name der logdatei:  " + args[0]
						+ " Config: " + args[1]);

				if (args[1] != "") {
					writer = new WriteHiwayDB(args[1]);
				} else {
					writer = new WriteHiwayDB("hibernate.cfg.xml");
				}

				String input = "D:\\Temp\\" + args[0];
				// String input = "D:\\Temp\\variant-call-09-setup.cf.log";

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

							if (!line.isEmpty()) {

							//	result = writer.lineToDB(new JsonReportEntry(line));

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
			System.out.println("juchei fertig ohne Fehler...");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}