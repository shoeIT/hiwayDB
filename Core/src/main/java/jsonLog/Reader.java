package jsonLog;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Reader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
			 
			    String dbUrl = "jdbc:mysql://localhost/test";
			    
			    // String query = "Select distinct(table_name) from INFORMATION_SCHEMA.TABLES";
			    String query = "SELECT * FROM test.new_table";
			    String username = "root";
			    String password = "keanu7.";
			    
			 System.out.println("los juchei nochmal und wieder zurück");
			 
			 Connection connection;
			try {
				connection = DriverManager.getConnection(dbUrl,
				            username, password);
				
				 Statement statement = connection.createStatement();
			        ResultSet resultSet = statement.executeQuery(query);
			        while (resultSet.next()) {
			        String tableName = resultSet.getString(2);
			        System.out.println("Table name : " + tableName);
			        }
			        connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			       
		 
		 System.out.println("fertig...pressbutton");
		
			System.in.read();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
	
}


