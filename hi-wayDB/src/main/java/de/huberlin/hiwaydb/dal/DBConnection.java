package de.huberlin.hiwaydb.dal;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DBConnection {
	
	private String configFile;
	
	public DBConnection(String file) {
		this.configFile = file;
	}

	public String getConfigFile() {
		return this.configFile;
	}

	public void setConfigFile(String file) {
		this.configFile = file;
	}
	
	public SessionFactory getDBSession() {
		try {
			java.io.File f = new java.io.File(configFile);
			
			Configuration configuration = new Configuration().configure(f);
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
