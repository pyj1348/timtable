import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

public class AddStudentWindow extends JFrame{

    private JTextField tfStudentID, tfName, tfDept;
    private JSpinner spinner;
    private JPanel pnStudentInfo;
    private JButton btConfrim, btCancel; //가입, 취소 버튼
    private GridBagLayout gb;
    private GridBagConstraints gbc;

    private Student newStudent;

    private JTable jTable;

    public AddStudentWindow(JTable jTable) {
        this.jTable = jTable;
        this.setTitle("학생 추가");
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

        JLabel lbStudentID = new JLabel("학번 :", JLabel.LEFT);
        tfStudentID = new JTextField();
        tfStudentID.setDocument(new JTextFieldLimit(8));
        tfStudentID.setToolTipText("숫자 8자리를 입력하세요. ex) 20001234");
        gbAdd(lbStudentID, 0, 0, 1, 1);
        gbAdd(tfStudentID, 1, 0, 2, 1);

        JLabel lbName = new JLabel("이름 :", JLabel.LEFT);
        tfName = new JTextField();
        tfName.setDocument(new JTextFieldLimit(10));
        tfName.setToolTipText("이름을 입력하세요.");
        gbAdd(lbName, 0, 1, 1, 1);
        gbAdd(tfName, 1, 1, 2, 1);

        JLabel lbDept = new JLabel("학과 :", JLabel.LEFT);
        tfDept = new JTextField();
        tfDept.setDocument(new JTextFieldLimit(10));
        tfDept.setToolTipText("학과를 입력하세요.");
        gbAdd(lbDept, 0, 2, 1, 1);
        gbAdd(tfDept, 1, 2, 2, 1);

        JLabel lbGrade = new JLabel("학년 :", JLabel.LEFT);
        SpinnerModel value = new SpinnerNumberModel(1, 1, 4, 1);
        spinner = new JSpinner(value);
        gbAdd(lbGrade, 0, 3, 1, 1);
        gbAdd(spinner, 1, 3, 2, 1);

        JFormattedTextField txt = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
        NumberFormatter formatter = (NumberFormatter) txt.getFormatter();
        DecimalFormat decimalFormat = new DecimalFormat("0");
        formatter.setFormat(decimalFormat);
        formatter.setAllowsInvalid(false);

        JLabel lbRoll = new JLabel("담당 : ");
        JPanel pnRoll = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton rbBookshelf = new JRadioButton("서가",true);
        JRadioButton rbBackup = new JRadioButton("백업",true);

        ButtonGroup group = new ButtonGroup();
        group.add(rbBookshelf);
        group.add(rbBackup);
        pnRoll.add(rbBookshelf);
        pnRoll.add(rbBackup);
        gbAdd(lbRoll, 0,4,1,1);
        gbAdd(pnRoll,1,4,2,1);

        JPanel pnButtons = new JPanel();
        btConfrim = new JButton("확인");
        btCancel = new JButton("취소");
        MyButtonActionListener actionListener = new MyButtonActionListener();
        btConfrim.addActionListener(actionListener);
        btCancel.addActionListener(actionListener);

        pnButtons.add(btConfrim);
        pnButtons.add(btCancel);

        this.add(pnStudentInfo, BorderLayout.CENTER);
        this.add(pnButtons, BorderLayout.SOUTH);
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

    public class MyButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btConfrim){
                String studentID = tfStudentID.getText();
                String studentName = tfName.getText();
                String department = tfDept.getText();
                int grade = (int) spinner.getValue();

                boolean isAllVaild = true;

                /*      Check if ID is vaild      */
                if(studentID.matches("")){
                    JOptionPane.showMessageDialog(null, "학번을 입력해주세요.",
                            "Error Message", JOptionPane.ERROR_MESSAGE);
                    isAllVaild = false;
                }
                else if(!studentID.matches(("[0-9]{8}|20[0-9]{6}"))){
                    JOptionPane.showMessageDialog(null, "학번이 올바르지 않습니다.\n숫자 8자리를 입력하세요.\nex) 20001234",
                            "Error Message", JOptionPane.ERROR_MESSAGE);
                    isAllVaild = false;
                }

                /*      Check if a name is vaild      */
                if(studentName.matches("")){
                    JOptionPane.showMessageDialog(null, "이름을 입력해주세요.",
                            "Error Message", JOptionPane.ERROR_MESSAGE);
                    isAllVaild = false;
                }
                else if(!studentName.matches(("[가-힝]{2}[가-힝]*"))){
                    JOptionPane.showMessageDialog(null, "이름이 올바르지 않습니다.",
                            "Error Message", JOptionPane.ERROR_MESSAGE);
                }

                /*      Check if a department is vaild      */
                if(department.matches("")){
                    JOptionPane.showMessageDialog(null, "학과를 입력해주세요.",
                            "Error Message", JOptionPane.ERROR_MESSAGE);
                    isAllVaild = false;
                }
                else if(!department.matches(("[가-힝]{3}[가-힝]*"))){
                    JOptionPane.showMessageDialog(null, "학과가 올바르지 않습니다.",
                            "Error Message", JOptionPane.ERROR_MESSAGE);
                    isAllVaild = false;
                }

                if(isAllVaild) {
                    newStudent = new Student(studentID, studentName, department, grade, "서가", false);
                    dispose();
                }
            }
            else if(e.getSource() == btCancel){
                EditStudentListWindow.MyTableModel model = (EditStudentListWindow.MyTableModel) jTable.getModel();
                dispose();
            }

        }
    }
}