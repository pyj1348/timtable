public class Student{

    private String studentID;
    private String studentName;
    private String department;
    private int grade;
    private String roll;
    private boolean selected;

    Student(){
        this.studentID = null;
        this.studentName = null;
        this.department = null;
        this.grade = 0;
        this.roll = null;
        this.selected = false;
    }

    Student(Object[] data){
        this.studentID = (String) data[0];
        this.studentName = (String) data[1];
        this.department = (String) data[2];
        this.grade = (int) data[3];
        this.roll = (String) data[4];
        this.selected = (Boolean) data[5];
    }

    Student(String studentID, String studentName, String department, int grade, String roll, boolean selected){
        this.studentID = studentID;
        this.studentName = studentName;
        this.department = department;
        this.grade = grade;
        this.roll = roll;
        this.selected = selected;
    }

    public String getStudentID(){ return this.studentID; }
    public String getStudentName(){ return this.studentName; }
    public String getDepartment(){ return this.department; }
    public int getGrade(){ return this.grade; }
    public String getRoll() { return this.roll; }
    public boolean getSeleted(){ return this.selected; }
    public void setStudentID(String studentID){ this.studentID = studentID; }
    public void setStudentName(String studentName){ this.studentName = studentName; }
    public void setDepartment(String department){ this.department = department; }
    public void setGrade(int grade){ this.grade = grade; }
    public void setRoll(String roll){ this.roll = roll; }
    public void setSelected(Boolean selected){ this.selected = selected; }

    public String toString(){ return this.studentName; }
    public Object get(int index){
        if(index == 1){
            return this.studentID;
        }
        else if(index == 2){
            return this.studentName;
        }
        else if(index == 3){
            return this.department;
        }
        else if(index == 4){
            return this.grade;
        }
        else if(index == 5) {
            return this.roll;
        }
        else if(index == 6) {
            return this.selected;
        }
        else
            return false;
    }
}