package de.huberlin.hiwaydb.LogToDB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Scanner;

import de.huberlin.wbi.cuneiform.core.invoc.JsonReportEntry;

public class Reader {

	private static Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;

	public static void main(String[] args) {

		try {

			System.out.println("go...");

			WriteHiwayDB writer = new WriteHiwayDB();

			if (args.length == 0) {
				System.out.println("keine Eingabe...lese StandardIn");

				try (BufferedReader test = new BufferedReader(
						new InputStreamReader(System.in))) {

					String line = test.readLine();
					while (line != null) {
						writer.lineToDB(new JsonReportEntry(line));
					}
				}
			} else {
				System.out.println("Eingabe: Name der logdatei:  " + args[0]);

				String input = "D:\\Temp\\" + args[0];
				// String input = "D:\\Temp\\variant-call-09-setup.cf.log";

				if (input.endsWith(".log")) {

					fFilePath = Paths.get(input);

					int i = 0;
					try (Scanner scanner = new Scanner(fFilePath,
							ENCODING.name())) {
						while (scanner.hasNextLine()) {
							i++;
							System.out.println("line " + i);

							writer.lineToDB(new JsonReportEntry(scanner
									.nextLine()));
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
