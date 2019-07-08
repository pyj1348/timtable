import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Timetable {
	private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final static String DB_URL = "jdbc:mysql://localhost:3306/timetable?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private final static String USER_NAME = "root";
	private final static String PASSWORD = "1234";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Connection conn = null;
		Statement stmt = null;
		// TODO Auto-generated method stub
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("select * from student"); 

			while(rset.next()) {
				System.out.println(rset.getString("name"));
			}
			} catch(SQLException e) {
				System.out.println(e);
			
		}
	}

}
