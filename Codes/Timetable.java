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

    private final static String DELIMITER = "\\s*\\t\\s*|\\s*,\\s*|\\s*\\r\\n\\s*";

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
                //System.out.println(rset.getString("name"));
            }
        } catch(SQLException e) {
            System.out.println(e);

        }

        insertStudent("20181425	°­ºó	2\r\n" +
                "20154282	°­ÁÖ¿µ	4\r\n" +
                "20154851	°û´Ùºó	4\r\n" +
                "20173286	±Ç±âÀº	3\r\n" +
                "20155546	±ÇÁØÈ¯	3\r\n" +
                "20183976	±è³ª¿¹	2\r\n" +
                "20164603	±èµµ¿¬	4\r\n" +
                "20164343	±è¹ÎÁ¤	3\r\n" +
                "20170501	±è¹ÎÁö	3\r\n   " +
                "20134897	±è¼ºÇÑ	3\r\n" +
                "20173700 	±è¼öÀÎ	3\r\n" +
                "20140652	±è¼öÇü	3\r\n" +
                "20183454	±è½ÂÈ£	2\r\n" +
                "20150526	±èÀ¯Áø	4\r\n" +
                "20123694	±èÀÎÈ¯	4\r\n" +
                "20144393	±èÁö¹Î	4\r\n" +
                "20172458	±èÁö¿ø	3\r\n" +
                "20152493	±èÁø¿ì	3\r\n" +
                "20131045	±èÂù¼ö	4\r\n" +
                "20172509	±èÇÏ´Ã	3\r\n" +
                "20165407	±èÇüÅÂ	2\r\n" +
                "20154994	³²´ö¿ì	3\r\n" +
                "");
    }

    private static void insertStudent(String data) {
        String[] temp = data.split("\\s*\\r\\n\\s*");
        for(String line : temp) {
            String[] studentInfo = line.split(DELIMITER);
            System.out.println(studentInfo[0] + " | " + studentInfo[1] + " | " + studentInfo[2]);
        }
    }

}