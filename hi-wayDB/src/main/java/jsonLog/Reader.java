package jsonLog;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import dal.Task;

public class Reader {

	public static void main(String[] args) {
		
		 try {
			 
			 //
			    		 
		 System.out.println("Start press button");
		
			System.in.read();
			
			Configuration configuration = new Configuration().configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
			applySettings(configuration.getProperties());
			SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
			
			//Session session = sessionFactory.openSession();
			//session.beginTransaction();
			//session.save( new Event( "Our very first event!", new Date() ) );
			//session.save( new Event( "A follow up event", new Date() ) );
			//session.getTransaction().commit();
			//session.close();
			
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			List result = session.createQuery( "from Task" ).list();
			for ( Task event : (List<Task>) result ) {
			    System.out.println( "Task (" + event.getTaskName() + ") : " + event.getLanguage());
			}
			session.getTransaction().commit();
			session.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
	
}


