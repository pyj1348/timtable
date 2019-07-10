import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class Test extends JFrame {
    private boolean DEBUG = false;

    protected String[] columnToolTips = {null, null,
            "The person's favorite sport to participate in",
            "The number of years the person has played the sport",
            "If checked, the person eats no meat"};

    public Test() {
        this.setTitle("학생리스트 편집");
        this.setSize(500, 800);
        this.setLocationRelativeTo(null); // Center the frame

        this.setLayout(new BorderLayout());

        JTable table = new JTable(new MyTableModel());

        table.setPreferredScrollableViewportSize(new Dimension(500, 70));

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        this.add(scrollPane, BorderLayout.CENTER);


        JButton b1 = new JButton("확인");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println();
            }
        });
        this.add(b1, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"학번","이름","학과","학년", "선택"};

        private Object[][] data = {
                {"20120612", "김도연", "물리학과", new Integer(2), new Boolean(false)},
                {"20120693", "박용진", "컴퓨터공학과", new Integer(4), new Boolean(false)},
                {"20141234", "곽창수", "물리학과", new Integer(4), new Boolean(false)}};

        public List<Integer> a(){
            List<Integer> selectedRows = new ArrayList<>();
            for(int i = 0; i < data.length; i++){
                if(data[i][4].equals(true)){
                    selectedRows.add(i);
                }
            }
            return selectedRows;
        }


        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/ editor for
         * each cell. If we didn't implement this method, then the last column
         * would contain text ("true"/"false"), rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.

            if (col ==  4) {
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
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                        + " to " + value + " (an instance of "
                        + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }
}


    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
