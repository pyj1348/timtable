import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
    private final static String DELIMITER = "\\s*\\t\\s*|\\s*,\\s*";

    public Test(String path, String encoding) {

        BufferedReader br = null;
        studentArrayList = new ArrayList<>();

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
                String line;
                while ((line = br.readLine()) != null) {
                    String[] temp = line.split(DELIMITER);
                    String studentID = temp[col_ID];
                    String name = temp[col_Name];
                    String dept = temp[col_Dept];
                    int grade = Integer.parseInt(temp[col_Grade]);
                    studentArrayList.add(new Student(studentID, name, dept, grade, false));
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
