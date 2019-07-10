import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class EditStudentListWindow extends JFrame {

    EditStudentListWindow(){
        this.setTitle("학생리스트 편집");
        this.setSize(500, 800);
        this.setLocationRelativeTo(null); // Center the frame
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        String data[][]={ {"20120612","김도연","물리학과", "2"},
                {"20120693","박용진","컴퓨터공학과", "4"},
                {"20141234","곽창수","물리학과", "4"}};

        String column[]={"학번","이름","학과","학년"};
        final JTable jt=new JTable(data,column);
        jt.setCellSelectionEnabled(true);
        ListSelectionModel select= jt.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                /*
                String Data = null;
                int[] row = jt.getSelectedRows();
                int[] columns = jt.getSelectedColumns();
                for (int i = 0; i < row.length; i++) {
                    for (int j = 0; j < columns.length; j++) {
                        Data = (String) jt.getValueAt(row[i], columns[j]);
                    } }
                System.out.println("Table element selected is: " + Data);
*/
                System.out.println(jt.getValueAt(jt.getSelectedRow(), 0).toString());
            }

        });
        JScrollPane sp=new JScrollPane(jt);
        this.add(sp, BorderLayout.CENTER);

    }

}
