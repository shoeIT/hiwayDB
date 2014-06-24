package de.huberlin.hiwaydb.dal;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
				
				//java.io.File f = new java.io.File(configFile);
				//java.io.File f = new java.io.File("c:\\home\\hiway\\hibernate.cfg.xml"); 

				System.out.println("make connection!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				Configuration configuration = new Configuration();
				//.configure(f);

				configuration.setProperty("hibernate.connection.url",
						"jdbc:mysql://localhost/hiwaydb");
				configuration.setProperty("hibernate.connection.username",
						"root");
				configuration.setProperty("hibernate.connection.password",
						//		"keanu7.");
					"reverse");
				configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
				configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
				//configuration.setProperty("hibernate.connection.password.driver_class", "com.mysql.jdbc.Driver");
				configuration.setProperty("hibernate.connection.pool_size", "10");
				
//				configuration.setProperty("hibernate.c3p0.min_size","5");
//				configuration.setProperty("hibernate.c3p0.max_size","20");
//				configuration.setProperty("hibernate.c3p0.timeout","1800");
//				configuration.setProperty("hibernate.c3p0.max_statements","50");
				
				
				configuration.addAnnotatedClass(de.huberlin.hiwaydb.dal.Hiwayevent.class);
				configuration.addAnnotatedClass(de.huberlin.hiwaydb.dal.File.class);
				configuration.addAnnotatedClass(de.huberlin.hiwaydb.dal.Inoutput.class);
				configuration.addAnnotatedClass(de.huberlin.hiwaydb.dal.Invocation.class);
				configuration.addAnnotatedClass(de.huberlin.hiwaydb.dal.Task.class);
				configuration.addAnnotatedClass(de.huberlin.hiwaydb.dal.Userevent.class);
				configuration.addAnnotatedClass(de.huberlin.hiwaydb.dal.Workflowrun.class);
				
			
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
