import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.FlowLayout;

class Test2 extends JFrame {

    Test2()
    {
        setTitle("JList : SINGLE_INTERVAL_SELECTION and SINGLE_SELECTION");
        setLayout(new FlowLayout());
        setJList1();
        setJList2();
        setSize(1000,500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setJList1()
    {
        String[] names = {"Windows","Ubuntu","Macintosh","Linux","Fedora"};
        JList jl = new JList(names);
        jl.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane js = new JScrollPane(jl);
        js.setPreferredSize(new Dimension(200,200));
        add(new JLabel("SINGLE_INTERVAL_SELECTION"));
        add(js);
    }

    private void setJList2()
    {
        String[] names = {"Symbian","Android","Apple iOS","BlackBerry","Windows Mobile"};
        JList jl = new JList(names);
        jl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane js = new JScrollPane(jl);
        js.setPreferredSize(new Dimension(200,200));
        add(new JLabel("SINGLE_SELECTION"));
        add(js);
    }
}