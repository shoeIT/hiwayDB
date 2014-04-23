package jsonLog;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAL {

	String dbUrl = "jdbc:mysql://localhost/test";
	String username = "root";
	String password = "keanu7.";
	
	Connection connectionToDB;
	

	// String query =
	// "Select distinct(table_name) from INFORMATION_SCHEMA.TABLES";
	String query = "SELECT * FROM test.new_table";

	public boolean connectToDB() {

		System.out.println("connecting...");

		
		try {
			connection = DriverManager.getConnection(dbUrl, username, password);

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
	}

}
