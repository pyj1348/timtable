import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectTimeTableTypeWindow extends JFrame {

    private JButton btConfrim, btCancel; //����, ��� ��ư
    private JRadioButton rbEntire, rbBackup;
    private boolean ENTIRE = false;
    private boolean BACKUP = false;


    SelectTimeTableTypeWindow() {
        this.setTitle("�ð�ǥ ����");
        this.setSize(250, 125);
        this.setLocationRelativeTo(null); // Center the frame
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setVisible(true);

        JPanel pnRoll = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rbEntire = new JRadioButton("����", true);
        rbBackup = new JRadioButton("���", true);

        ButtonGroup group = new ButtonGroup();
        group.add(rbEntire);
        group.add(rbBackup);

        pnRoll.add(rbEntire);
        pnRoll.add(rbBackup);
        this.add(pnRoll, BorderLayout.CENTER);

        JPanel pnButtons = new JPanel();
        btConfrim = new JButton("Ȯ��");
        btCancel = new JButton("���");
        MyButtonActionListener actionListener = new MyButtonActionListener();
        btConfrim.addActionListener(actionListener);
        btCancel.addActionListener(actionListener);

        pnButtons.add(btConfrim);
        pnButtons.add(btCancel);

        this.getRootPane().setDefaultButton(btConfrim);

        this.add(pnButtons, BorderLayout.SOUTH);
    }

    class MyButtonActionListener implements ActionListener {   // ��� ���� â ������

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btConfrim) {
                if (rbEntire.isSelected()) {
                    ENTIRE = true;
                    Main frame = new Main("ENTIRE");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                }
                if (rbBackup.isSelected()) {
                    BACKUP = false;
                    Main frame = new Main("BACKUP");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                }
                dispose();

            } else if (e.getSource() == btCancel) {
                dispose();
            }
        }
    }


    public static void main(String[] args) {
        SelectTimeTableTypeWindow frame = new SelectTimeTableTypeWindow();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}