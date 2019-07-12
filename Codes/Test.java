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

            /* �ҷ����� ������ Ȯ���ڴ� .xls/.csv/.txt/ �̾�߸� �ϸ�
               ù �ٿ��� �ݵ�� �й�/�̸�or����/�а�/�г����� �� ����� ��� �־�߸� �մϴ�.
               * ����� ������ ��� �����ϴ�. *
               * �ٸ� �ΰ����� ������� �־ ��� �����ϴ�. ex) ����, ����ó �� **/


            /*          Check a header      */
            if(header != null){
                columns = header.split(DELIMITER);
                for(int i = 0; i < columns.length ; i++){
                    if(columns[i].matches("�й�")){
                        isStudentID = true;
                        col_ID = i;
                    }
                    else if(columns[i].matches("����|�̸�")){
                        isName = true;
                        col_Name = i;
                    }
                    else if(columns[i].matches("�а�")){
                        isDepartment = true;
                        col_Dept = i;
                    }
                    else if(columns[i].matches("�г�")){
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
                System.out.println("���(ù ��)�� ���ų� �߸��Ǿ����ϴ�.");
            }

            /*      Check if all columns is contained       */
            if(isAllCols) {
                try {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                    stmt = conn.createStatement();

                    PreparedStatement pStmt = conn.prepareStatement("insert into student values(?,?,?,?,'����', 0)");

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

                        studentArrayList.add(new Student(studentID, name, dept, grade, "����", false));
                    }
                    System.out.println("DB ������Ʈ �Ϸ�");

                } catch(SQLException e) {
                    System.out.println(e.getMessage().substring(17, 25) + " �л� �ߺ�");
                }
            }

            else{
                System.out.println("���(ù ��)�� ���ų� �߸��Ǿ����ϴ�.");
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}