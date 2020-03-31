import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Timetable extends JFrame {
    private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://localhost:3306/timetable?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "1234";

    private JLabel lbIDtag, lbTimeTag, lbGradeTag, lbDeptTag, lbRollTag;
    private JLabel lbIDField, lbTimeField, lbGradeField, lbRollField;
    private JLabel lbName;
    private JTextArea taDept;
    private JComboBox cbName;
    private Vector<Student> items;

    private JLabel[] days = {new JLabel("월"), new JLabel("화"), new JLabel("수"), new JLabel("목"), new JLabel("금")};
    private JLabel[] times = new JLabel[12];
    private JCheckBox[][] cbTable = new JCheckBox[12][5];
    private JTextArea[][] taTable = new JTextArea[12][5];
    private JButton bSave;

    private boolean isLoaded = false;
    private String selectedID;
    private int selectedIndex = 0;
    private int howMany = 0;
    private int MAXIMUM_TIME;
    private String tableType;


    Timetable(JTextArea[][] taTable, String tableType){
        this.tableType = tableType;
        this.loadDataFromDatabaseToArray();

        if(this.items.size() <= 1){
            if(this.tableType.equals("ENTIRE")){
                JOptionPane.showMessageDialog(null, "학생 명단이 없습니다.\n메뉴 학생 > 관리에서 학생을 추가하세요.",
                        "Error Message", JOptionPane.WARNING_MESSAGE);
            } else if(this.tableType.equals("BACKUP")){
                JOptionPane.showMessageDialog(null, "백업 학생 명단이 없습니다.\n메뉴 학생 > 관리에서 백업 학생을 추가하세요.",
                        "Error Message", JOptionPane.WARNING_MESSAGE);
            }
        }
        else{
            this.taTable = taTable;
            this.setTitle("주간시간표 관리");
            this.setSize(550, 800);
            this.setLocationRelativeTo(null); // Center the frame
            this.setLayout(null);
            this.setResizable(false);
            this.setVisible(true);

            /* Student Info */
            this.lbName = new JLabel("이름: ");
            this.cbName = new JComboBox(this.items);
            this.cbName.setEditable(true);
            this.cbName.setEditor(new MyComboBoxEditor());
            this.cbName.setSelectedIndex(0);
            this.cbName.addActionListener(new MyComboboxActionListener());

            this.lbTimeTag = new JLabel("주당 시간: ");
            this.lbTimeField = new JLabel();

            this.lbIDtag = new JLabel("학번: ");
            this.lbGradeTag = new JLabel("학년: ");
            this.lbDeptTag = new JLabel("학과: ");
            this.lbRollTag = new JLabel("담당: ");

            this.lbIDField = new JLabel();
            this.lbGradeField = new JLabel();
            this.taDept = new JTextArea();
            this.lbRollField = new JLabel();
            this.lbTimeField = new JLabel();

            this.taDept.setLineWrap(true);
            this.taDept.setWrapStyleWord(true);
            this.taDept.setEditable(false);
            this.taDept.setBackground(lbName.getBackground());
            this.lbName.setFont(lbName.getFont().deriveFont(Font.BOLD, 15f));
            this.cbName.setFont(cbName.getFont().deriveFont(Font.BOLD, 15f));
            this.lbIDtag.setFont(lbIDtag.getFont().deriveFont(Font.BOLD, 15f));
            this.lbIDField.setFont(lbIDField.getFont().deriveFont(Font.BOLD, 15f));
            this.lbDeptTag.setFont(lbDeptTag.getFont().deriveFont(Font.BOLD, 15f));
            this.taDept.setFont(taDept.getFont().deriveFont(Font.BOLD, 15f));
            this.lbGradeTag.setFont(lbGradeTag.getFont().deriveFont(Font.BOLD, 15f));
            this.lbGradeField.setFont(lbGradeField.getFont().deriveFont(Font.BOLD, 15f));
            this.lbRollTag.setFont(lbRollTag.getFont().deriveFont(Font.BOLD, 15f));
            this.lbRollField.setFont(lbRollField.getFont().deriveFont(Font.BOLD, 15f));
            this.lbTimeTag.setFont(lbRollTag.getFont().deriveFont(Font.BOLD, 15f));
            this.lbTimeField.setFont(lbRollField.getFont().deriveFont(Font.BOLD, 15f));

            this.lbName.setBounds(30, 10, 50, 20);
            this.cbName.setBounds(100, 10, 80, 20);
            this.lbTimeTag.setBounds(350, 10, 80, 20);
            this.lbTimeField.setBounds(450, 10, 50, 20);

            this.lbIDtag.setBounds(30, 60, 50, 20);
            this.lbIDField.setBounds(100, 60, 110, 20);
            this.lbDeptTag.setBounds(30, 100, 50, 20);
            this.taDept.setBounds(100, 100, 150, 40);

            this.lbGradeTag.setBounds(275, 60, 50, 20);
            this.lbGradeField.setBounds(345, 60, 110, 20);
            this.lbRollTag.setBounds(275, 100, 50, 20);
            this.lbRollField.setBounds(345, 100, 110, 20);


            this.add(lbName);
            this.add(cbName);
            this.add(lbTimeTag);
            this.add(lbTimeField);
            this.add(lbIDtag);
            this.add(lbIDField);
            this.add(lbDeptTag);
            this.add(taDept);
            this.add(lbGradeTag);
            this.add(lbGradeField);
            this.add(lbDeptTag);
            this.add(taDept);
            this.add(lbRollTag);
            this.add(lbRollField);

            /*  Personal TimeTable   */

            bSave = new JButton("저장");
            bSave.setFont(lbName.getFont().deriveFont(Font.BOLD, 15f));
            bSave.setBounds(430, 700, 70, 30);
            bSave.addActionListener(new MyButtonActionListener());
            bSave.setEnabled(false);
            this.add(bSave);

            for(int i = 0; i < days.length; i++){
                days[i].setFont(lbName.getFont().deriveFont(Font.BOLD, 15f));
                days[i].setBounds(110 +(i*80), 160, 20, 20);
                this.add(days[i]);
            }

            for(int i = 0; i < times.length; i++){
                times[i] = new JLabel((i+9) + "-" + (i+10));
                times[i].setFont(lbName.getFont().deriveFont(Font.BOLD, 15f));
                times[i].setBounds(50, 200 + (i*40), 40, 20);
                this.add(times[i]);
            }


            for(int i=0; i<12; i++) {
                for (int j = 0; j < 5; j++) {
                    cbTable[i][j] = new JCheckBox();
                    cbTable[i][j].setBounds(110 + (j*80), 200 + (i*40), 20, 20);
                    cbTable[i][j].setEnabled(false);
                    cbTable[i][j].addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent e) {
                            howMany = e.getStateChange() == 1 ? (howMany + 1) : (howMany - 1);
                            if(howMany > MAXIMUM_TIME) {
                                JOptionPane.showMessageDialog(null, "주당 " + MAXIMUM_TIME + "시간을 초과할 수 없습니다.",
                                        "Error Message", JOptionPane.WARNING_MESSAGE);
                            }else {
                                if(howMany == 0){
                                    lbTimeField.setText("");
                                }else {
                                    lbTimeField.setText(howMany + "시간");
                                }
                                if(isLoaded){
                                    bSave.setEnabled(true);
                                }
                            }
                        }
                    });
                    this.add(cbTable[i][j]);
                }

            }

            /*  Menu */
            JMenuBar menuBar = new JMenuBar();

            JMenu mEdit = new JMenu("편집");
            JMenuItem miSetting = new JMenuItem("설정");
            JMenuItem miDeleteAll = new JMenuItem("초기화");


            miSetting.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ChangeTimeWindow();
                }
            });

            miDeleteAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] options = {"확인", "취소"};
                    int option = JOptionPane.showOptionDialog(null, "정말로 시간표를 초기화시겠습니까?", "확인", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);

                    if (option == 0) {
                        try {
                            Class.forName(JDBC_DRIVER);
                            Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

                            String SQL = "";
                            if(tableType.equals("ENTIRE")){
                                Statement stmt = conn.createStatement();
                                stmt.executeUpdate("delete from timetable");
                            } else if(tableType.equals("BACKUP")){
                                Statement stmt = conn.createStatement();
                                ResultSet rSet = stmt.executeQuery("select student_ID from student join timetable using (student_ID) where roll = '백업'");

                                PreparedStatement pStmt = conn.prepareStatement("delete from timetable where student_ID = ?");

                                while (rSet.next()) {
                                    pStmt.setString(1, rSet.getString(1));
                                    pStmt.executeUpdate();
                                }
                            }

                            Statement stmt = conn.createStatement();
                            stmt.executeUpdate("update setting set `value` = 12 where name = 'maximum_time'");
                            MAXIMUM_TIME = 12;

                            initializeTextArea();
                            cbName.setSelectedIndex(0);
                            if(tableType.equals("ENTIRE")){
                                JOptionPane.showMessageDialog(null, "종합시간표가 초기화되었습니다.");
                            } else if(tableType.equals("BACKUP")){
                                JOptionPane.showMessageDialog(null, "백업시간표가 초기화되었습니다.");
                            }
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }

                }
            });

            mEdit.add(miSetting);
            mEdit.add(miDeleteAll);
            menuBar.add(mEdit);
            this.getRootPane().setDefaultButton(bSave);
            this.setJMenuBar(menuBar);
        }


    }

    private void loadDataFromDatabaseToArray(){
        this.items = new Vector<>();
        Student firstItem = new Student("", "선택","", 0, "", false);
        this.items.add(firstItem);
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            Statement stmt = conn.createStatement();
            String SQL = "";
            if(tableType.equals("ENTIRE")){
                SQL = "select student_ID, name from student order by name";
            } else if(tableType.equals("BACKUP")){
                SQL = "select student_ID, name from student where roll = '백업' order by name";
            }


            ResultSet rSet = stmt.executeQuery(SQL);

            while (rSet.next()) {
                Student std = new Student();
                std.setStudentID(rSet.getString(1));
                std.setStudentName(rSet.getString(2));
                items.add(std);
            }

            rSet = stmt.executeQuery("select * from setting where name = 'maximum_time'");

            while (rSet.next()) {
                    MAXIMUM_TIME = rSet.getInt("value");
            }

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "알 수 없는 에러가 발생했습니다.",
                    "Error Message", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "알 수 없는 에러가 발생했습니다.",
                    "Error Message", JOptionPane.ERROR_MESSAGE);
        }

    }


    class MyComboboxActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {


            if (cbName.getSelectedIndex() == -1){
                cbName.setSelectedIndex(selectedIndex);
            }

            if (cbName.getSelectedIndex() == 0){
                lbIDField.setText("");
                lbGradeField.setText("");
                taDept.setText("");
                lbRollField.setText("");
                lbTimeField.setText("");

                for(int i=0; i<12; i++) {
                    for (int j = 0; j < 5; j++) {
                        cbTable[i][j].setEnabled(false);
                        cbTable[i][j].setSelected(false);
                    }
                }
            }

            //System.out.println(cbName.getSelectedIndex());
            if(cbName.getSelectedIndex() >= 1) {

                selectedIndex = cbName.getSelectedIndex();
                if (e.getActionCommand().matches("comboBoxChanged")) {
                    selectedID = ((Student) cbName.getItemAt(cbName.getSelectedIndex())).getStudentID();
                    try {
                        Class.forName(JDBC_DRIVER);
                        Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                        PreparedStatement pStmt = conn.prepareStatement("select * from student where student_ID = ?");
                        pStmt.setString(1, selectedID);
                        ResultSet rSet = pStmt.executeQuery();

                        while (rSet.next()) {
                            lbIDField.setText(rSet.getString("student_ID"));
                            lbGradeField.setText(rSet.getString("grade") + "학년");
                            taDept.setText(rSet.getString("department"));
                            lbRollField.setText(rSet.getString("roll"));
                        }
                        for(int i=0; i<12; i++) {
                            for (int j = 0; j < 5; j++) {
                                cbTable[i][j].setEnabled(true);
                                cbTable[i][j].setSelected(false);
                            }
                        }
                        howMany = 0;

                        pStmt = conn.prepareStatement("select * from timetable where student_ID = ?");
                        pStmt.setString(1, selectedID);
                        rSet = pStmt.executeQuery();

                        isLoaded = false;

                        while (rSet.next()) {
                            if(rSet.getString(2).matches("월")){
                                cbTable[rSet.getInt(3) - 1][0].setSelected(true);
                            } else if(rSet.getString(2).matches("화")){
                                cbTable[rSet.getInt(3) - 1][1].setSelected(true);
                            } else if(rSet.getString(2).matches("수")){
                                cbTable[rSet.getInt(3) - 1][2].setSelected(true);
                            } else if(rSet.getString(2).matches("목")){
                                cbTable[rSet.getInt(3) - 1][3].setSelected(true);
                            } else if(rSet.getString(2).matches("금")){
                                cbTable[rSet.getInt(3) - 1][4].setSelected(true);
                            }
                        }

                        isLoaded = true;
                        bSave.setEnabled(false);


                    } catch (SQLException es) {
                        JOptionPane.showMessageDialog(null, "알 수 없는 에러가 발생했습니다.",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "알 수 없는 에러가 발생했습니다.",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        }
    }

    class MyComboBoxEditor implements ComboBoxEditor{
        JTextField tfName;

        MyComboBoxEditor(){
            this.tfName = new JTextField();

        }

        @Override
        public Component getEditorComponent() {
            return this.tfName;
        }

        @Override
        public void setItem(Object anObject) {
            this.tfName.setText(anObject.toString());
        }

        @Override
        public Object getItem() {
            return this.tfName;
        }

        @Override
        public void selectAll() {
            this.tfName.selectAll();

        }

        @Override
        public void addActionListener(ActionListener l) {
            this.tfName.addActionListener(l);
        }

        @Override
        public void removeActionListener(ActionListener l) {
            this.tfName.removeActionListener(l);

        }
    }


    class MyButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Class.forName(JDBC_DRIVER);
                Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                PreparedStatement pStmt  =  conn.prepareStatement("delete from timetable where student_ID = ?");
                pStmt.setString(1, selectedID);
                pStmt.executeUpdate();

                pStmt = conn.prepareStatement("insert into timetable values(?,?,?)");
                pStmt.setString(1, selectedID);

                String name = ((Student) cbName.getItemAt(cbName.getSelectedIndex())).getStudentName();
                for(int i=0; i<12; i++) {
                    pStmt.setInt(3, i+1);
                    for (int j = 0; j < 5; j++) {
                        if(cbTable[i][j].isSelected()){
                            if(j == 0) {
                                pStmt.setString(2, "월");
                            } else if(j==1){
                                pStmt.setString(2, "화");
                            } else if(j==2){
                                pStmt.setString(2, "수");
                            } else if(j==3){
                                pStmt.setString(2, "목");
                            } else if(j==4){
                                pStmt.setString(2, "금");
                            }
                            pStmt.executeUpdate();

                        }
                    }
                }
                initializeTextArea();
                roadText();

                JOptionPane.showMessageDialog(null, name + " 학생의 시간표를 저장했습니다.");
                bSave.setEnabled(false);


            }catch(SQLException es) {
                JOptionPane.showMessageDialog(null, "알 수 없는 에러가 발생했습니다.",
                        "Error Message", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "알 수 없는 에러가 발생했습니다.",
                        "Error Message", JOptionPane.ERROR_MESSAGE);
            }

        }

    }
    private void roadText() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            Statement stmt = conn.createStatement();
            String SQL = "";
            if(tableType.equals("ENTIRE")){
                SQL = "select name, the_day, the_time from student join timetable using (student_ID)";
            } else if(tableType.equals("BACKUP")){
                SQL = "select name, the_day, the_time from student join timetable using (student_ID) where roll = '백업'";
            }

            ResultSet rSet = stmt.executeQuery(SQL);

            while (rSet.next()) {
                if(rSet.getString(2).matches("월")){
                    this.setText(rSet.getString(1), rSet.getInt(3) -1,0 );
                } else if(rSet.getString(2).matches("화")){
                    this.setText(rSet.getString(1), rSet.getInt(3) -1,1);
                } else if(rSet.getString(2).matches("수")){
                    this.setText(rSet.getString(1), rSet.getInt(3) -1,2 );
                } else if(rSet.getString(2).matches("목")){
                    this.setText(rSet.getString(1), rSet.getInt(3) -1,3);
                } else if(rSet.getString(2).matches("금")){
                    this.setText(rSet.getString(1), rSet.getInt(3) -1,4);
                }
            }

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setText(String newName, int row, int col){

        if(taTable[row][col].getText().equals("")){
            taTable[row][col].setText(newName);
        }else {
            String[] oldText = taTable[row][col].getText().split(" ");
            List<String> names = new ArrayList<>();
            for (String temp : oldText) {
                names.add(temp);
            }

            names.add(newName);
            Collections.sort(names);
            String newText = names.get(0);
            for (int i = 1; i < names.size(); i++) {
                newText = newText.concat(" " + names.get(i));
            }

            taTable[row][col].setText(newText);
        }
    }

    private void initializeTextArea(){
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 5; j++){
                taTable[i][j].setText("");
            }
        }
    }
    class ChangeTimeWindow extends JFrame {

        private JButton btConfrim, btCancel; //확인, 취소 버튼
        private JSpinner spinner;

        ChangeTimeWindow() {
            this.setTitle("설정");
            this.setSize(250, 125);
            this.setLocationRelativeTo(null); // Center the frame
            this.setLayout(new BorderLayout());
            this.setResizable(false);
            this.setVisible(true);

            JPanel pnTime = new JPanel(new FlowLayout(FlowLayout.CENTER));

            JLabel lbGrade = new JLabel("주당 최대시간 :", JLabel.LEFT);
            SpinnerModel value = new SpinnerNumberModel(MAXIMUM_TIME, 10, 20, 1);
            this.spinner = new JSpinner(value);

            pnTime.add(lbGrade);
            pnTime.add(spinner);

            JPanel pnButtons = new JPanel();
            btConfrim = new JButton("확인");
            btCancel = new JButton("취소");
            btConfrim.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // DB 변경 및 변수 변경

                    MAXIMUM_TIME = (int) spinner.getValue();

                    try {
                        Class.forName(JDBC_DRIVER);

                        Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);


                        PreparedStatement pStmt = conn.prepareStatement("update setting set `value` = ? where name = 'maximum_time'");

                        pStmt.setInt(1, MAXIMUM_TIME);
                        pStmt.executeUpdate();



                        JOptionPane.showMessageDialog(null, "주당 최대시간이 변경되었습니다.");
                        dispose();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            btCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            pnButtons.add(btConfrim);
            pnButtons.add(btCancel);

            this.add(pnTime, BorderLayout.CENTER);
            this.add(pnButtons, BorderLayout.SOUTH);
        }


    }

}