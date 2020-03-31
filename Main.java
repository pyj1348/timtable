import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends JFrame {
    private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://localhost:3306/timetable?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "1234";
    private final static String DELIMITER = "\\s*\\t\\s*|\\s*,\\s*|\\s*\\r\\n\\s*";

    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private JPanel pnTimetable;
    private JTextArea[][] taTable = new JTextArea[12][5];

    private String tableType;

    Main (String tableType){


        /* clipboard
        String myString = "This text will be copied into clipboard";
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);*/

        this.setTitle("중앙대학교 도서관 근로학생 시간표 관리 프로그램  ver. 1.0");
        this.setSize(1800, 1000);
        this.setLocationRelativeTo(null); // Center the frame
        this.setLayout(new BorderLayout());

        this.tableType = tableType;

        JLabel lbTitle = new JLabel("", Label.LEFT);
        if(this.tableType.equals("ENTIRE")){
            lbTitle.setText("도서관 근로학생 종합시간표");
        } else if(this.tableType.equals("BACKUP")){
            lbTitle.setText("도서관 근로학생 백업시간표");
        }

        lbTitle.setFont(lbTitle.getFont().deriveFont(40f));
        Dimension lbTitleSize = lbTitle.getPreferredSize();
        lbTitle.setSize(lbTitleSize);
        this.add(lbTitle, BorderLayout.NORTH);

        /*      Timetable       */
        this.gbl = new GridBagLayout();
        this.pnTimetable = new JPanel();
        this.pnTimetable.setLayout(this.gbl);
        this.gbc = new GridBagConstraints();
        this.gbc.fill = GridBagConstraints.BOTH;

        this.gbAdd(new Label("시간", Label.CENTER), 0,0, 1, 1, 0.0, 0.0);
        this.gbAdd(new Label("월요일", Label.CENTER), 1,0, 1, 1,1.0, 0.0);
        this.gbAdd(new Label("화요일", Label.CENTER), 2,0, 1, 1, 1.0, 0.0);
        this.gbAdd(new Label("수요일", Label.CENTER), 3,0, 1, 1, 1.0, 0.0);
        this.gbAdd(new Label("목요일", Label.CENTER), 4,0, 1, 1, 1.0, 0.0);
        this.gbAdd(new Label("금요일", Label.CENTER), 5,0, 1, 1, 1.0, 0.0);

        this.gbAdd( new Label("09:00 ~ 10:00", Label.CENTER), 0,1, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("10:00 ~ 11:00", Label.CENTER), 0,2, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("11:00 ~ 12:00", Label.CENTER), 0,3, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("12:00 ~ 13:00", Label.CENTER), 0,4, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("13:00 ~ 14:00", Label.CENTER), 0,5, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("14:00 ~ 15:00", Label.CENTER), 0,6, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("15:00 ~ 16:00", Label.CENTER), 0,7, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("16:00 ~ 17:00", Label.CENTER), 0,8, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("17:00 ~ 18:00", Label.CENTER), 0,9, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("18:00 ~ 19:00", Label.CENTER), 0,10, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("19:00 ~ 20:00", Label.CENTER), 0,11, 1, 1, 0.0, 1.0);
        this.gbAdd(new Label("20:00 ~ 21:00", Label.CENTER), 0,12, 1, 1, 0.0, 1.0);

        for(int i=0; i<12; i++) {
            for (int j = 0; j < 5; j++) {
                this.taTable[i][j] = new JTextArea();
                this.taTable[i][j].setSize(100, 100);
                this.taTable[i][j].setPreferredSize(new Dimension(50, 10));
                this.taTable[i][j].setMaximumSize(new Dimension(50, 10));
                this.taTable[i][j].setMinimumSize(new Dimension(50, 10));
                this.taTable[i][j].setLineWrap(true);
                this.taTable[i][j].setWrapStyleWord(true);
                this.taTable[i][j].setDocument(new JTextFieldLimit(130));
                this.taTable[i][j].setEditable(false);
                this.gbAdd(this.taTable[i][j], j+1, i+1, 1, 1, 0.0, 0.0);
            }


            this.add(pnTimetable, BorderLayout.CENTER);
        }

        /*      Title      */

        JMenuBar menuBar = new JMenuBar();

        JMenu mStudent = new JMenu("학생");
        JMenuItem miStudent1 = new JMenuItem("관리");

        JMenu mTimetable = new JMenu("시간표");
        JMenuItem miTimetable1 = new JMenuItem("관리");
        JMenuItem miTimetable2 = new JMenuItem("저장");

        miStudent1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new EditStudentListWindow();
                new EditStudentListWindow(taTable, tableType);
            }
        });

        miTimetable1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new Test();
                new Timetable(taTable, tableType);
            }
        });
        miTimetable2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FileWriter csvWriter = null;
                try {

                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("C:\\Users\\CAU\\Desktop"));
                    int retrival = chooser.showSaveDialog(null);
                    if (retrival == JFileChooser.APPROVE_OPTION) {
                        csvWriter = new FileWriter(chooser.getSelectedFile() + ".csv");
                        for (int i = 0; i < 12; i++) {
                            String sb = new String();
                            for (int j = 0; j < 5; j++) {
                                if(taTable[i][j].getText().equals("")){
                                    sb = sb.concat(" ");
                                }else {
                                    sb = sb.concat(taTable[i][j].getText());
                                }
                                if (j != 4) {
                                    sb = sb.concat(",");
                                }
                            }
                            sb = sb.concat("\n");
                            csvWriter.write(sb);
                        }
                    }


                    csvWriter.flush();
                    csvWriter.close();
                    JOptionPane.showMessageDialog(null, "파일을 저장했습니다.");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        mStudent.add(miStudent1);
        mTimetable.add(miTimetable1);
        mTimetable.add(miTimetable2);

        menuBar.add(mStudent);
        menuBar.add(mTimetable);

        this.setJMenuBar(menuBar);
        this.roadText();


    }
    private void gbAdd(Component c, int x, int y, int gridwidth, int gridheight, double weightx, double weighty) {
        this.gbc.gridx = x;
        this.gbc.gridy = y;
        this.gbc.gridwidth  = gridwidth;
        this.gbc.gridheight = gridheight;
        this.gbc.weightx = weightx;
        this.gbc.weighty = weighty;
        this.gbc.insets = new Insets(1, 1, 1, 1);
        this.gbl.setConstraints(c, this.gbc);

        this.pnTimetable.add(c);
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

}