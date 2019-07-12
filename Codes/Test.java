import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private boolean isStudentID = false;
    private boolean isName = false;
    private boolean isDepartment = false;
    private boolean isGrade = false;
    private boolean isAllCols = false;
    private int col_ID;
    private int col_Name;
    private int col_Dept;
    private int col_Grade;

    private List<Student> studentArrayList;

    private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://localhost:3306/timetable?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "1234";
    private final static String DELIMITER = "\\s*\\t\\s*|\\s*,\\s*|\\s*\\r\\n\\s*";

    public Test(String path, String encoding) {

        BufferedReader br = null;
        studentArrayList = new ArrayList<>();

        Connection conn = null; // JDBC
        Statement stmt = null;  // JDBC

        try {

            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));

            String header = br.readLine();
            String[] columns = null;

            /* 불러오는 파일의 확장자는 .xls/.csv/.txt/ 이어야만 하며
               첫 줄에는 반드시 학번/이름or성명/학과/학년으로 된 헤더가 모두 있어야만 합니다.
               * 헤더의 순서는 상관 없습니다. *
               * 다른 부가적인 헤더들은 있어도 상관 없습니다. ex) 성별, 연락처 등 **/


            /*          Check a header      */
            if(header != null){
                columns = header.split(DELIMITER);
                for(int i = 0; i < columns.length ; i++){
                    if(columns[i].matches("학번")){
                        isStudentID = true;
                        col_ID = i;
                    }
                    else if(columns[i].matches("성명|이름")){
                        isName = true;
                        col_Name = i;
                    }
                    else if(columns[i].matches("학과")){
                        isDepartment = true;
                        col_Dept = i;
                    }
                    else if(columns[i].matches("학년")){
                        isGrade = true;
                        col_Grade = i;
                    }
                    else
                        continue;
                }

                if(isStudentID && isName && isDepartment && isGrade){
                    isAllCols = true;
                }
            }

            else{
                System.out.println("헤더(첫 줄)가 없거나 잘못되었습니다.");
            }

            /*      Check if all columns is contained       */
            if(isAllCols) {
                try {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                    stmt = conn.createStatement();

                    PreparedStatement pStmt = conn.prepareStatement("insert into student values(?,?,?,?,'서가', 0)");

                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] temp = line.split(DELIMITER);
                        String studentID = temp[col_ID];
                        String name = temp[col_Name];
                        String dept = temp[col_Dept];
                        int grade = Integer.parseInt(temp[col_Grade]);

                        pStmt.setString(1, studentID);
                        pStmt.setString(2, name);
                        pStmt.setString(3, dept);
                        pStmt.setInt(4, grade);

                        pStmt.executeUpdate();

                        studentArrayList.add(new Student(studentID, name, dept, grade, "서가", false));
                    }
                    System.out.println("DB 업데이트 완료");

                } catch(SQLException e) {
                    System.out.println(e.getMessage().substring(17, 25) + " 학생 중복");
                }
            }

            else{
                System.out.println("헤더(첫 줄)가 없거나 잘못되었습니다.");
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}