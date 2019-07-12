import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class Test2 extends JFrame {
    private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://localhost:3306/timetable?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "1234";

    private JButton changeRollButton = new JButton("담당변경");
    private JButton selectAllButton = new JButton("전체선택");
    private JButton deleteButton = new JButton("선택삭제");
    private JButton addButton = new JButton("학생추가");
    private JButton openButton = new JButton("불러오기");
    private JTable table;
    private List<Student> studentArrayList;
    /*
    private Object[][] data = {
            {"20120612", "김도연", "물리학과", new Integer(2), "서가", new Boolean(false)},
            {"20120693", "박용진", "컴퓨터공학과", new Integer(4), "서가", new Boolean(false)},
            {"20141234", "곽창수", "물리학과", new Integer(4), "서가", new Boolean(false)}};*/

    private static final int BOOLEAN_COLUMN = 4;
    private static boolean isSelectedAll = false;

    protected String[] columnToolTips = {null, null,
            "The person's favorite sport to participate in",
            "The number of years the person has played the sport",
            "If checked, the person eats no meat"};

    public Test2() {
        this.studentArrayList = new ArrayList<>();
        this.insertData();
        this.setTitle("학생리스트 편집");
        this.setSize(500, 800);
        this.setLocationRelativeTo(null); // Center the frame
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        table = new JTable(new MyTableModel());

        // table.getModel().addTableModelListener(new CheckBoxModelListener());;
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));


        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(); // 디폴트테이블셀렌더러를 생성
        dtcr.setHorizontalAlignment(SwingConstants.CENTER); // 렌더러의 가로정렬을 CENTER로
        TableColumnModel tcm = table.getColumnModel() ; // 정렬할 테이블의 컬럼모델을 가져옴


        for (int i = 0; i < tcm.getColumnCount()-1; i++) {
            tcm.getColumn(i).setCellRenderer(dtcr);
        }//////////////////////////////////////




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
        MyButtonActionListener actionListener = new MyButtonActionListener();
        selectAllButton.addActionListener(actionListener);
        changeRollButton.addActionListener(actionListener);
        deleteButton.addActionListener(actionListener);
        addButton.addActionListener(actionListener);
        openButton.addActionListener(actionListener);



        /*
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i = 0; i < table.getRowCount(); i++){
                    Boolean selected = Boolean.valueOf(table.getValueAt(i,BOOLEAN_COLUMN).toString());
                    if(selected){
                        System.out.println(table.getValueAt(i, 1));
                    }
                }
            }
        });*/
        this.add(pnBottomMenu, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void insertData(){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            stmt = conn.createStatement();
            ResultSet rSet = stmt.executeQuery("select * from student order by name");

            while(rSet.next()) {
                Object[] newStudent =  {rSet.getString(1), rSet.getString(2), rSet.getString(3),
                        rSet.getInt(4), rSet.getString(5), false};
                studentArrayList.add(new Student(newStudent));
            }
        } catch(SQLException e) {
            System.out.println(e);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"학번","이름","학과","학년", "담당", "선택"};

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

            return studentArrayList.get(row).get(col);
        }

        /*
         * JTable uses this method to determine the default renderer/ editor for
         * each cell. If we didn't implement this method, then the last column
         * would contain text ("true"/"false"), rather than a check box.
         */
        public Class getColumnClass(int col) {
            return studentArrayList.get(0).get(col).getClass(); // class 부른 후에 valueAt 실행
        }

        /*
         * Don't need to implement this method unless your table's editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col ==  5) {
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
            if(col == 0){
                newStudentData.setStudentID((String) value);
            }
            else if(col == 1){
                newStudentData.setStudentName((String) value);
            }
            else if(col == 2){
                newStudentData.setDepartment((String) value);
            }
            else if(col == 3){
                newStudentData.setGrade((int) value);
            }else if(col == 4){
                newStudentData.setRoll((String) value);
            }else if(col == 5){
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

    public class MyButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == selectAllButton){
                isSelectedAll = !isSelectedAll;
                for(int i = 0; i < table.getRowCount(); i++){
                    table.setValueAt(isSelectedAll, i, BOOLEAN_COLUMN);
                }
            }
            else if(e.getSource() == changeRollButton) {

            }

            else if(e.getSource() == deleteButton){
                MyTableModel model = (MyTableModel)table.getModel();
                int rowCount = table.getRowCount();
                List<Integer> selectedIndexes = new ArrayList<>();

                for(int i = 0; i < rowCount; i++){
                    Boolean selected = Boolean.valueOf(table.getValueAt(i,BOOLEAN_COLUMN).toString());
                    if(selected){
                        selectedIndexes.add(i);
                    }
                }
                for(int i = selectedIndexes.size() -1; i >= 0; i--){
                    System.out.println(table.getValueAt(selectedIndexes.get(i), 1));
                    model.removeRow(selectedIndexes.get(i));
                }
            }
            else if(e.getSource() == addButton){
                new AddStudentWindow(table);
                // 변경된 DB 리스트에 추가
            }
            else if(e.getSource() == openButton){

            }

        }
    }

}