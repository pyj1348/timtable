import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditStudentListWindow extends JFrame {
    private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://localhost:3306/timetable?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "1234";
    private final static String DELIMITER = "\\s*\\t\\s*|\\s*,\\s*|\\s*\\r\\n\\s*";

    private JButton changeRollButton = new JButton("��纯��");
    private JButton selectAllButton = new JButton("��ü����");
    private JButton deleteButton = new JButton("���û���");
    private JButton addButton = new JButton("�л��߰�");
    private JButton openButton = new JButton("�ҷ�����");
    private JTable table;
    private List<Student> studentArrayList;

    private JTextArea[][] taTable;
    private static final int BOOLEAN_COLUMN = 6;
    private boolean isSelectedAll;
    private String tableType;

    public EditStudentListWindow(JTextArea[][] taTable, String tableType) {
        this.isSelectedAll = false;
        this.taTable = taTable;
        this.tableType = tableType;
        this.loadDataFromDatabaseToArray();
        this.setTitle("�л�����Ʈ ����");
        this.setSize(550, 800);
        this.setLocationRelativeTo(null); // Center the frame
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        table = new JTable(new MyTableModel());

        table.setPreferredScrollableViewportSize(new Dimension(500, 70));

        table.getTableHeader().setReorderingAllowed(false); // �巡�� ����

        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // ����Ʈ���̺��������� ����
        dtcr.setHorizontalAlignment(SwingConstants.CENTER); // �������� ���������� CENTER��
        TableColumnModel tcm = table.getColumnModel() ; // ������ ���̺��� �÷����� ������

        tcm.getColumn(0).setPreferredWidth(40);
        tcm.getColumn(1).setPreferredWidth(70);
        tcm.getColumn(3).setPreferredWidth(150);
        tcm.getColumn(4).setPreferredWidth(50);

        for (int i = 0; i < tcm.getColumnCount()-1; i++) {
            tcm.getColumn(i).setCellRenderer(dtcr);
        }


        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel pnBottomMenu = new JPanel();
        pnBottomMenu.add(selectAllButton);
        pnBottomMenu.add(changeRollButton);
        pnBottomMenu.add(deleteButton);
        pnBottomMenu.add(addButton);
        pnBottomMenu.add(openButton);
        MyButtonActionListenerForEditWindow actionListener = new MyButtonActionListenerForEditWindow();
        selectAllButton.addActionListener(actionListener);
        changeRollButton.addActionListener(actionListener);
        deleteButton.addActionListener(actionListener);
        addButton.addActionListener(actionListener);
        openButton.addActionListener(actionListener);

        if(tableType.equals("BACKUP")){
            selectAllButton.setEnabled(false);
            changeRollButton.setEnabled(false);
            deleteButton.setEnabled(false);
            addButton.setEnabled(false);
            openButton.setEnabled(false);
        }

        this.add(pnBottomMenu, BorderLayout.SOUTH);
        this.setVisible(true);

    }

    private void loadDataFromDatabaseToArray(){
        this.studentArrayList = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            Statement stmt = conn.createStatement();
            String SQL = "";
            if(tableType.equals("ENTIRE")){
                SQL = "select * from student order by name";
            } else if(tableType.equals("BACKUP")){
                SQL = "select * from student where roll = '���' order by name";
            }

            ResultSet rSet = stmt.executeQuery(SQL);

            while(rSet.next()) {
                Object[] newStudentInfo =  {rSet.getString(1), rSet.getString(2), rSet.getString(3),
                        rSet.getInt(4), rSet.getString(5), false};
                studentArrayList.add(new Student(newStudentInfo));
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "�� �� ���� ������ �߻��߽��ϴ�.",
                    "Error Message", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "�� �� ���� ������ �߻��߽��ϴ�.",
                    "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readFile(String path, String encoding){
        boolean isStudentID = false;
        boolean isName = false;
        boolean isDepartment = false;
        boolean isGrade = false;
        boolean isAllCols = false;
        int col_ID = 0;
        int col_Name = 0;
        int col_Dept = 0;
        int col_Grade = 0;
        BufferedReader br;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));

            String header = br.readLine();
            String[] columns;


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
                JOptionPane.showMessageDialog(null, "������ ���(ù ��)�� ���ų� �߸��Ǿ����ϴ�.",
                        "Error Message", JOptionPane.ERROR_MESSAGE);
            }

            /*      Check if all columns is contained       */
            if(isAllCols) {
                try {
                    Class.forName(JDBC_DRIVER);
                    Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("delete from student");   // ���� DB ����

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

                    }

                    loadDataFromDatabaseToArray();
                    ((AbstractTableModel) table.getModel()).fireTableDataChanged();

                    JOptionPane.showMessageDialog(null, "���� �ҷ����⸦ ���������� �Ϸ��߽��ϴ�.");

                } catch(SQLException e) {
                    //JOptionPane.showMessageDialog(null, e.getMessage().substring(17, 25) + " �л� �ߺ��� �ߺ��˴ϴ�.",
                            //"Error Message", JOptionPane.ERROR_MESSAGE);
                }
            }

            else{
                JOptionPane.showMessageDialog(null, "������ ���(ù ��)�� ���ų� �߸��Ǿ����ϴ�.",
                        "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "���� �б⿡ �����߽��ϴ�.",
                    "Error Message", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void roadText() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rSet = stmt.executeQuery("select name, the_day, the_time from student join timetable using (student_ID)");

            while (rSet.next()) {
                if(rSet.getString(2).matches("��")){
                    this.setText(rSet.getString(1), rSet.getInt(3) -1,0 );
                } else if(rSet.getString(2).matches("ȭ")){
                    this.setText(rSet.getString(1), rSet.getInt(3) -1,1);
                } else if(rSet.getString(2).matches("��")){
                    this.setText(rSet.getString(1), rSet.getInt(3) -1,2 );
                } else if(rSet.getString(2).matches("��")){
                    this.setText(rSet.getString(1), rSet.getInt(3) -1,3);
                } else if(rSet.getString(2).matches("��")){
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

    class MyButtonActionListenerForEditWindow extends Component implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == selectAllButton) {
                isSelectedAll = !isSelectedAll;
                for (int i = 0; i < table.getRowCount(); i++) {
                    table.setValueAt(isSelectedAll, i, BOOLEAN_COLUMN);
                }
            } else if (e.getSource() == changeRollButton) {
                int rowCount = table.getRowCount();
                List<Integer> selectedIndexes = new ArrayList<>();
                for (int i = 0; i < rowCount; i++) {
                    Boolean selected = Boolean.valueOf(table.getValueAt(i, BOOLEAN_COLUMN).toString());
                    if (selected) {
                        selectedIndexes.add(i);
                    }
                }

                if(selectedIndexes.size() == 0) { // ���õ� �л� �� �˻�
                    JOptionPane.showMessageDialog(null, "������ �л��� �����ϴ�",
                            "Error Message", JOptionPane.ERROR_MESSAGE);
                } else {
                    new ChangeRollWindow(); // ��� ���� â ����
                }
            } else if (e.getSource() == deleteButton) {
                MyTableModel model = (MyTableModel) table.getModel();
                int rowCount = table.getRowCount();
                List<Integer> selectedIndexes = new ArrayList<>();
                List<String> selectedIDs = new ArrayList<>();

                for (int i = 0; i < rowCount; i++) {
                    Boolean selected = Boolean.valueOf(table.getValueAt(i, BOOLEAN_COLUMN).toString());
                    if (selected) {
                        selectedIndexes.add(i);
                        selectedIDs.add(table.getValueAt(i, 1).toString());
                    }
                }

                if(selectedIndexes.size() == 0) {   // ���õ� �л��� �˻�
                    JOptionPane.showMessageDialog(null, "������ �л��� �����ϴ�",
                            "Error Message", JOptionPane.ERROR_MESSAGE);
                } else {    // ���� ����
                    String[] options = {"Ȯ��", "���"};
                    int option = JOptionPane.showOptionDialog(null, "������ ������ �����͸� �����Ͻðڽ��ϱ�?", "Ȯ��", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);

                    if (option == 0) {
                        try {
                            Class.forName(JDBC_DRIVER);
                            Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                            PreparedStatement pStmt = conn.prepareStatement("delete from student where student_id = ?");

                            for (int i = selectedIndexes.size() - 1; i >= 0; i--) {
                                model.removeRow(selectedIndexes.get(i));
                                pStmt.setString(1, selectedIDs.get(i));
                                pStmt.executeUpdate();
                            }
                            initializeTextArea();
                            roadText();
                            JOptionPane.showMessageDialog(null, "������ " + selectedIndexes.size() + "���� �л� �����͸� �����߽��ϴ�.");    // UI �����ʿ�
                            isSelectedAll = false;

                        } catch (Exception es) {
                            JOptionPane.showMessageDialog(null, "�� �� ���� ������ �߻��߽��ϴ�.",
                                    "Error Message", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } else if (e.getSource() == addButton) {
                new AddStudentWindow(); // �л� �߰� â ����
            } else if (e.getSource() == openButton) {
                JOptionPane.showMessageDialog(null, "�ҷ����� ������ Ȯ���ڴ� .csv/.txt �̾�߸� �ϸ�\n" +
                        "ù �ٿ��� �ݵ�� �й�/�̸�or����/�а�/�г����� �� ����� ��� �־�߸� �մϴ�.\n" +
                        "* ����� ������ ��� �����ϴ�. *\n" +
                        "* �ٸ� �ΰ����� ������� �־ ��� �����ϴ�. ex) ����, ����ó ��");

                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("C:\\Users\\CAU\\Desktop"));
                int i = fc.showOpenDialog(this);
                if (i == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    String path = f.getPath();
                    readFile(path, "x-windows-949");       // File reading
                }
            }
        }

    }

    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"��ȣ","�й�","�̸�","�а�","�г�", "���", "����"};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return studentArrayList.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if(col == 0){
                return row + 1;
            }
            else {
                return studentArrayList.get(row).get(col);
            }
        }

        /*
         * JTable uses this method to determine the default renderer/ editor for
         * each cell. If we didn't implement this method, then the last column
         * would contain text ("true"/"false"), rather than a check box.
         */
        public Class getColumnClass(int col) {
            return studentArrayList.get(0).get(col).getClass(); // class �θ� �Ŀ� valueAt ����
        }

        /*
         * Don't need to implement this method unless your table's editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col ==  6) {
                return true;
            } else {
                return false;
            }
        }
        /*
         * Don't need to implement this method unless your table's data can
         * change.
         */
        public void setValueAt(Object value, int row, int col) {
            Student newStudentData = studentArrayList.get(row);
            if(col == 1){
                newStudentData.setStudentID((String) value);
            }
            else if(col == 2){
                newStudentData.setStudentName((String) value);
            }
            else if(col == 3){
                newStudentData.setDepartment((String) value);
            }
            else if(col == 4){
                newStudentData.setGrade((int) value);
            }else if(col == 5){
                newStudentData.setRoll((String) value);
            }else if(col == 6){
                newStudentData.setSelected((Boolean) value);
            }
            studentArrayList.set(row, newStudentData);
            this.fireTableCellUpdated(row, col);
        }


        public void removeRow(int index){
            studentArrayList.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
    }

    /*   �л� �߰� â Ŭ����*/
    class AddStudentWindow extends JFrame{
        private JTextField tfStudentID, tfName, tfDept;
        private JSpinner spinner;
        private JPanel pnStudentInfo;
        private JButton btConfrim, btCancel; //����, ��� ��ư
        private JRadioButton rbBookshelf, rbBackup;
        private GridBagLayout gb;
        private GridBagConstraints gbc;

        public AddStudentWindow() {
            this.setTitle("�л� �߰�");
            this.setSize(250, 280);
            this.setLocationRelativeTo(null); // Center the frame
            this.setLayout(new BorderLayout());
            this.setResizable(false);


            pnStudentInfo = new JPanel();
            gb = new GridBagLayout();
            pnStudentInfo.setLayout(gb);
            gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            //gbc.anchor = GridBagConstraints.WEST;

            JLabel lbStudentID = new JLabel("�й� :", JLabel.LEFT);
            tfStudentID = new JTextField();
            tfStudentID.setDocument(new JTextFieldLimit(8));
            tfStudentID.setToolTipText("���� 8�ڸ��� �Է��ϼ���. ex) 20001234");
            gbAdd(lbStudentID, 0, 0, 1, 1);
            gbAdd(tfStudentID, 1, 0, 2, 1);

            JLabel lbName = new JLabel("�̸� :", JLabel.LEFT);
            tfName = new JTextField();
            tfName.setDocument(new JTextFieldLimit(10));
            tfName.setToolTipText("�̸��� �Է��ϼ���.");
            gbAdd(lbName, 0, 1, 1, 1);
            gbAdd(tfName, 1, 1, 2, 1);

            JLabel lbDept = new JLabel("�а� :", JLabel.LEFT);
            tfDept = new JTextField();
            tfDept.setDocument(new JTextFieldLimit(10));
            tfDept.setToolTipText("�а��� �Է��ϼ���.");
            gbAdd(lbDept, 0, 2, 1, 1);
            gbAdd(tfDept, 1, 2, 2, 1);

            JLabel lbGrade = new JLabel("�г� :", JLabel.LEFT);
            SpinnerModel value = new SpinnerNumberModel(1, 1, 4, 1);
            spinner = new JSpinner(value);
            gbAdd(lbGrade, 0, 3, 1, 1);
            gbAdd(spinner, 1, 3, 2, 1);

            JFormattedTextField txt = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
            NumberFormatter formatter = (NumberFormatter) txt.getFormatter();
            DecimalFormat decimalFormat = new DecimalFormat("0");
            formatter.setFormat(decimalFormat);
            formatter.setAllowsInvalid(false);

            JLabel lbRoll = new JLabel("��� : ");
            JPanel pnRoll = new JPanel(new FlowLayout(FlowLayout.LEFT));
            rbBookshelf = new JRadioButton("����",true);
            rbBackup = new JRadioButton("���",true);

            ButtonGroup group = new ButtonGroup();
            group.add(rbBookshelf);
            group.add(rbBackup);
            pnRoll.add(rbBookshelf);
            pnRoll.add(rbBackup);
            gbAdd(lbRoll, 0,4,1,1);
            gbAdd(pnRoll,1,4,2,1);

            JPanel pnButtons = new JPanel();
            btConfrim = new JButton("Ȯ��");
            btCancel = new JButton("���");
            MyButtonActionListenerForAddWindow actionListener = new MyButtonActionListenerForAddWindow();
            btConfrim.addActionListener(actionListener);
            btCancel.addActionListener(actionListener);

            pnButtons.add(btConfrim);
            pnButtons.add(btCancel);

            this.add(pnStudentInfo, BorderLayout.CENTER);
            this.add(pnButtons, BorderLayout.SOUTH);
            this.getRootPane().setDefaultButton(btConfrim);
            this.setVisible(true);

        }

        private void gbAdd(JComponent c, int x, int y, int w, int h){
            gbc.gridx = x;
            gbc.gridy = y;
            gbc.gridwidth = w;
            gbc.gridheight = h;
            //gb.setConstraints(c, gbc);
            gbc.insets = new Insets(2, 2, 2, 2);
            pnStudentInfo.add(c, gbc);
        }//gbAdd

        class MyButtonActionListenerForAddWindow implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getSource() == btConfrim){
                    String studentID = tfStudentID.getText();
                    String name = tfName.getText();
                    String dept = tfDept.getText();
                    int grade = (int) spinner.getValue();
                    String roll = new String();
                    if(rbBookshelf.isSelected()){
                        roll = "����";
                    }
                    if(rbBackup.isSelected()){
                        roll = "���";
                    }

                    boolean isAllVaild = true;

                    /*      Check if ID is vaild      */
                    if(studentID.matches("")){
                        JOptionPane.showMessageDialog(null, "�й��� �Է����ּ���.",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                        isAllVaild = false;
                    }
                    if(!studentID.matches(("[0-9]{8}"))){
                        JOptionPane.showMessageDialog(null, "�й��� �ùٸ��� �ʽ��ϴ�.\n���� 8�ڸ��� �Է��ϼ���.\nex) 20001234",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                        isAllVaild = false;
                    }
                    if(!studentID.matches(("20[0-9]{6}"))){
                        JOptionPane.showMessageDialog(null, "�й��� �ùٸ��� �ʽ��ϴ�.\nex) 20001234",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                        isAllVaild = false;
                    }

                    /*      Check if a name is vaild      */
                    if(name.matches("")){
                        JOptionPane.showMessageDialog(null, "�̸��� �Է����ּ���.",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                        isAllVaild = false;
                    }
                    else if(!name.matches(("[��-��]{2}[��-��]*[(]*[)]*"))){
                        JOptionPane.showMessageDialog(null, "�̸��� �ùٸ��� �ʽ��ϴ�.",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                        isAllVaild = false;
                    }

                    /*      Check if a department is vaild      */
                    if(dept.matches("")){
                        JOptionPane.showMessageDialog(null, "�а��� �Է����ּ���.",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                        isAllVaild = false;
                    }
                    else if(!dept.matches(("[��-��]{3}[��-��]*"))){
                        JOptionPane.showMessageDialog(null, "�а��� �ùٸ��� �ʽ��ϴ�.",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                        isAllVaild = false;
                    }

                    /* All conditions is vaild */
                    if(isAllVaild) {
                        try {
                            String[] options = {"Ȯ��", "���"};

                            int option = JOptionPane.showOptionDialog(null,"�Է��Ͻ� �����ͷ� �߰��Ͻðڽ��ϱ�?","Ȯ��", 0,JOptionPane.INFORMATION_MESSAGE,null,options,null);

                            if (option == 0) { // Yes
                                Class.forName(JDBC_DRIVER);
                                Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                                PreparedStatement pStmt = conn.prepareStatement("insert into student values(?,?,?,?,?, 0)");
                                pStmt.setString(1, studentID);
                                pStmt.setString(2, name);
                                pStmt.setString(3, dept);
                                pStmt.setInt(4, grade);
                                pStmt.setString(5, roll);
                                pStmt.executeUpdate();


                                JOptionPane.showMessageDialog(null, name + " �л��� �����͸� ���������� �߰��Ͽ����ϴ�.");

                                loadDataFromDatabaseToArray();
                                ((AbstractTableModel) table.getModel()).fireTableDataChanged();

                                dispose();
                            }
                        } catch(SQLException es) {
                            JOptionPane.showMessageDialog(null, es.getMessage().substring(17, 25) + " �л��� �ߺ��˴ϴ�",
                                    "Error Message", JOptionPane.ERROR_MESSAGE);
                        } catch (ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(null, "�� �� ���� ������ �߻��߽��ϴ�.",
                                    "Error Message", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                else if(e.getSource() == btCancel){
                    dispose();
                }

            }
        }

    }

    class ChangeRollWindow extends JFrame{

        private JButton btConfrim, btCancel; //����, ��� ��ư
        private JRadioButton rbBookshelf, rbBackup;

        ChangeRollWindow(){
            this.setTitle("��� ����");
            this.setSize(250, 125);
            this.setLocationRelativeTo(null); // Center the frame
            this.setLayout(new BorderLayout());
            this.setResizable(false);
            this.setVisible(true);

            JPanel pnRoll = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel lbRoll = new JLabel("��� : ");
            rbBookshelf = new JRadioButton("����", true);
            rbBackup = new JRadioButton("���", true);

            ButtonGroup group = new ButtonGroup();
            group.add(rbBookshelf);
            group.add(rbBackup);

            pnRoll.add(lbRoll);
            pnRoll.add(rbBookshelf);
            pnRoll.add(rbBackup);
            this.add(pnRoll, BorderLayout.CENTER);

            JPanel pnButtons = new JPanel();
            btConfrim = new JButton("Ȯ��");
            btCancel = new JButton("���");
            MyButtonActionListenerForRollChangeWindow actionListener = new MyButtonActionListenerForRollChangeWindow();
            btConfrim.addActionListener(actionListener);
            btCancel.addActionListener(actionListener);

            pnButtons.add(btConfrim);
            pnButtons.add(btCancel);
            this.getRootPane().setDefaultButton(btConfrim);
            this.add(pnButtons, BorderLayout.SOUTH);
        }
        class MyButtonActionListenerForRollChangeWindow implements ActionListener{   // ��� ���� â ������

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btConfrim) {

                    String roll = new String();

                    if(rbBookshelf.isSelected()){
                        roll = "����";
                    }
                    if(rbBackup.isSelected()){
                        roll = "���";
                    }

                    String[] options = {"Ȯ��", "���"};
                    int option = JOptionPane.showOptionDialog(null,roll + "(��)�� �����Ͻðڽ��ϱ�?","Ȯ��", 0,JOptionPane.INFORMATION_MESSAGE,null,options,null);

                    if(option == 0) {
                        MyTableModel model = (MyTableModel) table.getModel();
                        int rowCount = table.getRowCount();
                        List<Integer> selectedIndexes = new ArrayList<>();
                        List<String> selectedIDs = new ArrayList<>();

                        for (int i = 0; i < rowCount; i++) {
                            Boolean selected = Boolean.valueOf(table.getValueAt(i, BOOLEAN_COLUMN).toString());
                            if (selected) {
                                selectedIndexes.add(i);
                                selectedIDs.add(table.getValueAt(i, 1).toString());
                            }
                        }
                        try {
                            Class.forName(JDBC_DRIVER);
                            Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
                            PreparedStatement pStmt = conn.prepareStatement("update student set roll = ? where student_ID = ?;");

                            pStmt.setString(1, roll);

                            for (int i = selectedIndexes.size() - 1; i >= 0; i--) {
                                pStmt.setString(2, selectedIDs.get(i));
                                pStmt.executeUpdate();
                            }

                            JOptionPane.showMessageDialog(null, "������ " + selectedIndexes.size() + "���� �л� ����� �����Ͽ����ϴ�.");    // UI �����ʿ�


                            loadDataFromDatabaseToArray();
                            ((AbstractTableModel) table.getModel()).fireTableDataChanged();
                            isSelectedAll = false;

                            dispose();

                        } catch (Exception es) {
                            JOptionPane.showMessageDialog(null, "�� �� ���� ������ �߻��߽��ϴ�.",
                                    "Error Message", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                } else if (e.getSource() == btCancel) {
                    dispose();
                }
            }
        }

    }
}