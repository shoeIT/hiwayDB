package de.huberlin.hiwaydb.dal;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DBConnection {

	private String configFile;

	private String dbURL;
	private String password;
	private String username;

	public DBConnection(String dbURL, String username, String password, String file) {
		this.dbURL = dbURL;
		this.password = password;
		this.username = username;
		this.configFile = file;
	}

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

			if (password != null && dbURL != null && username != null) {
				
				java.io.File f = new java.io.File(configFile);

				Configuration configuration = new Configuration().configure(f);

				configuration.setProperty("hibernate.connection.url",
						this.dbURL);
				configuration.setProperty("hibernate.connection.username",
						this.username);
				configuration.setProperty("hibernate.connection.password",
						this.password);

				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties());
				SessionFactory sessionFactory = configuration
						.buildSessionFactory(builder.build());
				return sessionFactory;

			} else {
				java.io.File f = new java.io.File(configFile);

				Configuration configuration = new Configuration().configure(f);
				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties());
				SessionFactory sessionFactory = configuration
						.buildSessionFactory(builder.build());
				return sessionFactory;
			}

		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

	}

}
