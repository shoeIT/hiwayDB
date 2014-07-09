package de.huberlin.hiwaydb.useDB;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import de.huberlin.hiwaydb.LogToDB.WriteHiwayDB;
import de.huberlin.hiwaydb.dal.DBConnection;
import de.huberlin.hiwaydb.dal.File;
import de.huberlin.hiwaydb.dal.Invocation;
import de.huberlin.hiwaydb.dal.Task;
import de.huberlin.hiwaydb.dal.Workflowrun;
import de.huberlin.wbi.cuneiform.core.semanticmodel.JsonReportEntry;

public class HiwayDBNoSQL implements HiwayDBI {
	private String configFile = "hibernate.cfg.xml";

	private static final Log log = LogFactory.getLog(HiwayDB.class);

	private SessionFactory dbSessionFactory = null;
	private Transaction tx;
	private Session session;

	private List<URI>  dbURLs;
	private String password;
	private String bucket;

//	List<URI> uris = new ArrayList<URI>();
//	uris.add(URI.create("http://127.0.0.1:8091/pools"));
//
//	writer = new WriteHiwayDB(uris,	"hiwaydb", "");
	
	public HiwayDBNoSQL(String bucket, String password, List<URI> dbURLs) {
		this.bucket = bucket;
		this.password = password;
		this.dbURLs = dbURLs;
	}

	@Override
	public Set<String> getHostNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Long> getTaskIdsForWorkflow(String workflowName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskName(long taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTask(long taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTasks(Set<Long> taskIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTaskOnHost(Long taskId,
			String hostName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InvocStat> getLogEntriesForTaskOnHostSince(Long taskId,
			String hostName, long timestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logToDB(JsonReportEntry entry) {
		// TODO Auto-generated method stub
		
	}

}
