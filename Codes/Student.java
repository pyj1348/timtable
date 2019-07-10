public class Student{
    private String studentID = null;
    private String studentName = null;
    private String department = null;
    private int grade = 0;
    private boolean selected = false;

    Student(Object[] data){
        this.studentID = (String) data[0];
        this.studentName = (String) data[1];
        this.department = (String) data[2];
        this.grade = (int) data[3];
        this.selected = (Boolean) data[4];
    }

    Student(String studentID, String studentName, String department, int grade, boolean selected){
        this.studentID = studentID;
        this.studentName = studentName;
        this.department = department;
        this.grade = grade;
        this.selected = selected;
    }

    public String getStudentID(){ return this.studentID; }
    public String getStudentName(){ return this.studentName; }
    public String getDepartment(){ return this.department; }
    public int getGrade(){ return this.grade; }
    public boolean getSeleted(){ return this.selected; }
    public void setStudentID(String studentID){ this.studentID = studentID; }
    public void setStudentName(String studentName){ this.studentName = studentName; }
    public void setDepartment(String department){ this.department = department; }
    public void setGrade(int grade){ this.grade = grade; }
    public void setSelected(Boolean selected){ this.selected = selected; }
    public Object get(int index){
        if(index == 0){
            return this.studentID;
        }
        else if(index == 1){
            return this.studentName;
        }
        else if(index == 2){
            return this.department;
        }
        else if(index == 3){
            return this.grade;
        }
        else if(index == 4) {
            return this.selected;
        }
        else
            return false;
    }
}