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

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.google.gson.Gson;

import javax.persistence.OneToMany;

import net.spy.memcached.internal.OperationFuture;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.json.JSONException;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;

import de.huberlin.hiwaydb.LogToDB.InvocDoc;
import de.huberlin.hiwaydb.LogToDB.WfRunDoc;
import de.huberlin.hiwaydb.dal.File;
import de.huberlin.hiwaydb.dal.Hiwayevent;
import de.huberlin.hiwaydb.dal.Inoutput;
import de.huberlin.hiwaydb.dal.Invocation;
import de.huberlin.hiwaydb.dal.Userevent;
import de.huberlin.hiwaydb.dal.Workflowrun;
import de.huberlin.wbi.cuneiform.core.semanticmodel.JsonReportEntry;

public class Reader {

	private static Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private static SessionFactory dbSessionFactory;

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

			} else if (lineIn.equalsIgnoreCase("cRead")) {
				//
				//
				// List<URI> uris = new ArrayList<URI>();
				// uris.add(URI.create("http://127.0.0.1:8091/pools"));
				//
				//
				// HiwayDBI testGet = new HiwayDBNoSQL("hiwaydb","",uris,"root",
				// "reverse","jdbc:mysql://localhost/hiwaydb");

			} else if (lineIn.equalsIgnoreCase("db")) {

				//
				// Calendar cal = Calendar.getInstance();
				// cal.set(2014, Calendar.JUNE, 22);
				//
				// System.out.println("All for host since: 1403599949182 " );
				// //_2014-06-20 17:03:58
				//
				// // java.util.Date dt = new java.util.Date();
				//
				//
				// for (InvocStat s :
				// testGet.getLogEntriesForTaskOnHostSince(989639045l,
				// "hiway", 1403599949113l)) {
				// System.out.println("Host: " + s.getHostName() + "TaskID: "
				// + s.getTaskId() + " | RealTime:" + s.getRealTime()
				// + " Date: " + s.getTimestamp());
				// }
			} else {
				
				int toLimit = Integer.parseInt(lineIn.substring(0,lineIn.lastIndexOf(",")));
				
				String db =  lineIn.substring(lineIn.lastIndexOf(",")+1,lineIn.length());


				
				
				if (toLimit < 10) {
					toLimit = 1000;
				}

				int limit = toLimit;

				System.out.println("Datenbanken " + db +"  füllen...bist Limit: "	+ toLimit);
				dbSessionFactory = getSQLSession(db);

				Session session = dbSessionFactory.openSession();
				Transaction tx = null;

				List<URI> uris = new ArrayList<URI>();
				uris.add(URI.create("http://127.0.0.1:8091/pools"));
				//
				//
				// HiwayDBI testGet = new HiwayDBNoSQL("hiwaydb","",uris,"root",

				CouchbaseClient client = new CouchbaseClient(uris, db,"reverse");

				try {

					View view = client.getView("Workflow", "WfRunAll");
					Query query = new Query();
					
					ViewResponse result = client.query(view, query);
					int startSize = result.size();
					
					
					for (int i = 0; i < 1000; i++) {

						query = new Query();
						query.setIncludeDocs(true);

						// Query the Cluster
						result = client.query(view, query);
						int resultSize = result.size();

						System.out.println("Couchbase durchgang:" + i+ " Anzahl WFs: " + resultSize);

						if (startSize < limit) {

							int  back = copyWorkflowNoSQL(result, limit, client,startSize);
							
							
							if (back == 0) {
								System.out.println("Couchbase durchgang beendet!!!!!!!!!!!!!!");
								break;
							}
							else
							{
								startSize = startSize + back;								
							}
						}
						else
						{
							System.out.println("Couchbase hat bereits genug Runs: " +resultSize);
							break;
						}	
					}

					for (int i = 0; i < 1000; i++) {

						tx = session.beginTransaction();

						org.hibernate.Query querySQL = null;

						querySQL = session.createQuery("FROM Workflowrun");

						List<Workflowrun> allWFs = new ArrayList<Workflowrun>();

						allWFs = querySQL.list();

						System.out.println("MySQL durchgang:" + i
								+ " Anzahl WFs: " + allWFs.size());

						tx.commit();

						if (allWFs.size() < limit) {
							if (copyWorkflowsSQL(allWFs, limit, session) == 0) {
								System.out
										.println("MySQL durchgang beendet!!!!!!!!!!!!!!");
								break;
							}
						}
						else
						{
							System.out.println("SQL hat bereits genug Runs: " +allWFs.size());
							break;
						}

					}

					System.out.println("fertig");

				} catch (RuntimeException e) {

					throw e; // or display error message
				} finally {
					if (session.isOpen()) {
						session.close();
					}
				}

			}
			System.out.println("juchei fertig...");

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

	private static SessionFactory getSQLSession(String db) {
		try {
			// HiwayDBI testGet = new HiwayDB( "root",
			// "reverse","jdbc:mysql://localhost/hiwaydb");

			Configuration configuration = new Configuration();
			// .configure(f);

			configuration.setProperty("hibernate.connection.url",
					"jdbc:mysql://localhost/"+db);
			configuration.setProperty("hibernate.connection.username", "root");
			configuration.setProperty("hibernate.connection.password",
					"reverse");

			configuration.setProperty("hibernate.dialect",
					"org.hibernate.dialect.MySQLInnoDBDialect");
			configuration.setProperty("hibernate.connection.driver_class",
					"com.mysql.jdbc.Driver");

			// configuration.setProperty("hibernate.connection.pool_size","10");
			configuration.setProperty("connection.provider_class",
					"org.hibernate.connection.C3P0ConnectionProvider");

			// <property
			// name="hibernate.transaction.factory_class">org.hibernate.transaction.JDBCTransactionFactory</property>

			configuration.setProperty("hibernate.transaction.factory_class",
					"org.hibernate.transaction.JDBCTransactionFactory");

			configuration.setProperty(
					"hibernate.current_session_context_class", "thread");

			configuration.setProperty("hibernate.initialPoolSize", "20");
			configuration.setProperty("hibernate.c3p0.min_size", "5");
			configuration.setProperty("hibernate.c3p0.max_size", "1000");

			configuration.setProperty("hibernate.maxIdleTime", "3600");
			configuration.setProperty(
					"hibernate.c3p0.maxIdleTimeExcessConnections", "300");

			// configuration.setProperty("hibernate.c3p0.testConnectionOnCheckout",
			// "false");
			configuration.setProperty("hibernate.c3p0.timeout", "330");
			configuration.setProperty("hibernate.c3p0.idle_test_period", "300");

			configuration.setProperty("hibernate.c3p0.max_statements", "13000");
			configuration.setProperty(
					"hibernate.c3p0.maxStatementsPerConnection", "30");

			configuration.setProperty("hibernate.c3p0.acquire_increment", "10");

			// <property name="hibernate.show_sql">true</property>
			// <property name="hibernate.use_sql_comments">true</property>

			configuration
					.addAnnotatedClass(de.huberlin.hiwaydb.dal.Hiwayevent.class);
			configuration.addAnnotatedClass(de.huberlin.hiwaydb.dal.File.class);
			configuration
					.addAnnotatedClass(de.huberlin.hiwaydb.dal.Inoutput.class);
			configuration
					.addAnnotatedClass(de.huberlin.hiwaydb.dal.Invocation.class);
			configuration.addAnnotatedClass(de.huberlin.hiwaydb.dal.Task.class);
			configuration
					.addAnnotatedClass(de.huberlin.hiwaydb.dal.Userevent.class);
			configuration
					.addAnnotatedClass(de.huberlin.hiwaydb.dal.Workflowrun.class);
			configuration
					.addAnnotatedClass(de.huberlin.hiwaydb.dal.Accesstime.class);

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

	private static int copyWorkflowNoSQL(ViewResponse result, int limit,
			CouchbaseClient client,int  currentSize) {

		
		Set<InvocStat> tempResult = new HashSet<InvocStat>();
		Gson gson = new Gson();
		Calendar cal = Calendar.getInstance();

		WfRunDoc newRun = null;
		WfRunDoc run = null;
		//int allWFscount = result.size();
		
		System.out.println("copy NoSQL anzwahl wf:" + currentSize);

		int i = 0;
		InvocStat temp = null;
		// Iterate over the found wf documents
		for (ViewRow row : result) {

			i++;
			if (currentSize + i >= limit + 1) {

				System.out.println("Break and raus: all: " + currentSize  + " | i: " + i  + " limit: " + limit);
				return 0;
			}

			run = gson.fromJson((String) row.getDocument(), WfRunDoc.class);

			if (run.getRunId() != null) {

				String newName = run.getRunId().substring(0, 36) + "_"
						+ Calendar.getInstance().getTimeInMillis();

				System.out.println("Run: " + run.getName() + " , I: " + i
						+ " | " + newName);

				// System.out.println("resrow: "+ row.getValue()) ;

				newRun = new WfRunDoc();

				newRun.setRunId(newName);
				newRun.setName(run.getName());
				newRun.setWfTime(run.getWfTime());

				newRun.setHiwayEvent(run.getHiwayEvent());
				newRun.setTaskIDs(run.getTaskIDs());

				client.set(newName, gson.toJson(newRun));

				View view = client.getView("Workflow", "WfRunInvocs");
				Query query = new Query();
				query.setIncludeDocs(true).setKey(run.getRunId());

				// Query the Cluster
				result = client.query(view, query);

				InvocDoc newInvoc = null;
				InvocDoc oldInvoc = null;

				for (ViewRow invocDoc : result) {

					oldInvoc = gson.fromJson((String) invocDoc.getDocument(),
							InvocDoc.class);

					newInvoc = new InvocDoc();

					newInvoc.setHostname(oldInvoc.getHostname());
					newInvoc.setLang(oldInvoc.getLang());
					newInvoc.setRealTime(oldInvoc.getRealTime());
					newInvoc.setRealTimeIn(oldInvoc.getRealTimeIn());
					newInvoc.setRealTimeOut(oldInvoc.getRealTimeOut());
					newInvoc.setRunId(newName);
					newInvoc.setScheduleTime(oldInvoc.getScheduleTime());
					newInvoc.setStandardError(oldInvoc.getStandardError());
					newInvoc.setStandardOut(oldInvoc.getStandardOut());
					newInvoc.setTaskId(oldInvoc.getTaskId());
					newInvoc.setTaskname(oldInvoc.getTaskname());
					newInvoc.setTimestamp(Calendar.getInstance()
							.getTimeInMillis());
					newInvoc.setInvocId(oldInvoc.getInvocId());

					newInvoc.setFiles(oldInvoc.getFiles());
					newInvoc.setInput(oldInvoc.getInput());
					newInvoc.setOutput(oldInvoc.getOutput());
					newInvoc.setUserevents(oldInvoc.getUserevents());

					String invocID = newName + "_" + newInvoc.getInvocId();

					//System.out.println("save invoc..." + invocID);
					client.set(invocID, gson.toJson(newInvoc));
				}
			}

		}
		return i;

	}

	private static int copyWorkflowsSQL(List<Workflowrun> allWFs, int limit,
			Session session) {

		Workflowrun newRun = null;
		Set<Hiwayevent> newHiwayevents = null;
		Set<Invocation> newInvocations = null;

		Set<Inoutput> newInoutputs = null;
		Set<File> newfiles = null;
		Set<Userevent> newUserevents = null;

		// Session session = dbSessionFactory.openSession();

		Calendar cal = Calendar.getInstance();

		int allWFscount = allWFs.size();
		Boolean toCommit = true;
		int i = 0;
		Transaction tx = null;

		tx = session.beginTransaction();

		for (Workflowrun run : allWFs) {

			cal = Calendar.getInstance();
			i++;
			if (allWFscount + i >= limit + 1) {
				toCommit = false;
				System.out.println("Break and comitt");
				tx.commit();
				return 0;
			}

			if (i % 1000 == 0) {
				System.out.println("comitt in between...........");
				tx.commit();
				tx = session.beginTransaction();
			}

			String newName = run.getRunId().substring(0, 36) + "_"+ cal.getTimeInMillis();

			System.out.println("Run" + run.getId() + " , " + run.getWfName()+ " , I: " + i + " | " + newName);
			// run mit allen inhalten kopieren und speichern

			newRun = new Workflowrun();
			newRun.setRunId(newName);
			newRun.setWfName(run.getWfName());
			newRun.setWfTime(run.getWfTime());

			// System.out.println("saven newRun");
			session.save(newRun);

			newHiwayevents = new HashSet<Hiwayevent>();
			Hiwayevent newEvent = null;

			for (Hiwayevent he : run.getHiwayevents()) {

				newEvent = new Hiwayevent();
				newEvent.setContent(he.getContent());
				newEvent.setType(he.getType());
				newEvent.setWorkflowrun(newRun);

				session.save(newEvent);

				newHiwayevents.add(newEvent);
			}

			if (newHiwayevents.size() > 0) {
				// System.out.println("add newHiwayEvents");
				newRun.setHiwayevents(newHiwayevents);
			}

			newInvocations = new HashSet<Invocation>();
			Invocation newInvoc = null;

			for (Invocation invoc : run.getInvocations()) {
				cal = Calendar.getInstance();

				newInvoc = new Invocation();
				newInvoc.setDidOn(cal.getTime());
				newInvoc.setHostname(invoc.getHostname());
				newInvoc.setInvocationId(invoc.getInvocationId());
				newInvoc.setRealTime(invoc.getRealTime());
				newInvoc.setRealTimeIn(invoc.getRealTimeIn());
				newInvoc.setRealTimeOut(invoc.getRealTimeOut());
				newInvoc.setScheduleTime(invoc.getScheduleTime());
				newInvoc.setStandardError(invoc.getStandardError());
				newInvoc.setStandardOut(invoc.getStandardOut());
				newInvoc.setTask(invoc.getTask());
				newInvoc.setTimestamp(cal.getTimeInMillis());
				newInvoc.setWorkflowrun(newRun);
				session.save(newInvoc);

				Userevent newUE = null;
				newUserevents = new HashSet<Userevent>();
				for (Userevent ue : invoc.getUserevents()) {
					newUE = new Userevent();

					newUE.setContent(ue.getContent());
					newUE.setInvocation(newInvoc);

					session.save(newUE);
					newUserevents.add(newUE);
				}

				if (newUserevents.size() > 0) {
					// System.out.println("add newUserevent");
					newInvoc.setUserevents(newUserevents);
				}

				Inoutput newIO = null;
				newInoutputs = new HashSet<Inoutput>();
				for (Inoutput io : invoc.getInoutputs()) {
					newIO = new Inoutput();

					newIO.setContent(io.getContent());
					newIO.setInvocation(newInvoc);
					newIO.setKeypart(io.getKeypart());
					newIO.setType(io.getType());

					session.save(newIO);
					newInoutputs.add(newIO);
				}

				if (newInoutputs.size() > 0) {
					// System.out.println("add newInoutput");
					newInvoc.setInoutputs(newInoutputs);
				}

				File newFile = null;
				newfiles = new HashSet<File>();
				for (File file : invoc.getFiles()) {
					newFile = new File();

					newFile.setName(file.getName());
					newFile.setRealTimeIn(file.getRealTimeIn());
					newFile.setRealTimeOut(file.getRealTimeOut());
					newFile.setSize(file.getSize());
					newFile.setInvocation(newInvoc);

					session.save(newFile);
					newfiles.add(newFile);
				}

				if (newfiles.size() > 0) {
					// System.out.println("add newFiles");
					newInvoc.setFiles(newfiles);
				}

				newInvocations.add(newInvoc);
			}

			if (newInvocations.size() > 0) {
				// System.out.println("add newInvocations");
				newRun.setInvocations(newInvocations);
			}

			// tx.commit();

		}

		if (toCommit) {
			System.out.println("comitt");
			tx.commit();
		}

		return 1;
	}
}
