import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Swiss extends JFrame {

    private JTextField fileName = new JTextField(25);
    private JButton save = new JButton("Select file *.txt");
    private static Swiss ssp;


    public Swiss(){
        super("Split file by line");
        setLayout(new FlowLayout());
        save.addActionListener(new Swiss.SaveInDB());
        add(save);
        fileName.setEditable(false);
        add(fileName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350,100);
        setVisible(true);
    }

    class SaveInDB implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser c = new JFileChooser();
            int rVal = c.showSaveDialog(Swiss.this);
            if (rVal == JFileChooser.APPROVE_OPTION){
                fileName.setText(c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName());
                if(!fileName.getText().endsWith(".txt"))
                    fileName.setText(c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName() + ".txt");

                try {
//                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName.getText()), "cp1251"));//"UTF-8"
//                    FileInputStream fin = new FileInputStream(fileName.getText());
//                    int numAvailable = fin.available();
//                    int numByte = 100;
//
//                    byte[] buffer;
//
//                    while (numAvailable > 0){
//                        if (numAvailable < numByte){
//                            buffer = new byte[numAvailable];
//                        }
//                        else {
//                            buffer = new byte[numByte];
//                        }
//                        // считываем буфер
//                        fin.read(buffer, fin.available() - numAvailable, buffer.length);
//                        StringBuilder stringBuffer = new StringBuilder();
//                        for (byte byteBuffer : buffer){
//                            char alphabet = (char) byteBuffer;
//                            stringBuffer.append(alphabet);
//                        }
//                        stringList.add(stringBuffer.toString());
//                        numAvailable = numAvailable - numByte;
//                    }
//                    fin.close();

                    List<String> stringList = new ArrayList<String>();
                    BufferedReader reader = new BufferedReader(new FileReader(fileName.getText()));

                    while (reader.ready()) {
                        stringList.add(reader.readLine());
                    }
                    reader.close();



                    try {
                        getJDBC(fileName.getText(), stringList);
                    }catch (SQLException ex){
                        JOptionPane.showMessageDialog(null, "Error SQL!","Error",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "File processed!" + " File name " + fileName.getText(),"Hello",
                            JOptionPane.PLAIN_MESSAGE);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "File not found!","Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (UnsupportedEncodingException ex) {
                    JOptionPane.showMessageDialog(null, "Unsupported encoding!","Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (IOException ex){
                    JOptionPane.showMessageDialog(null, "Error!","Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if (rVal == JFileChooser.CANCEL_OPTION){
                fileName.setText("You pressed cancel!");
            }
        }
    }

    public List<Object> staticStrings(String[] wordString, int lengthString) {
        String maxWord = wordString[0];
        String minWord = wordString[0];
        int kolWord = 0;
        int lengthWord = 0;
        for (int i = 0; i < wordString.length; i++) {

            if(wordString[i].length() > maxWord.length()){
                maxWord = wordString[i];
            }
            if(wordString[i].length() < minWord.length()){
                minWord = wordString[i];
            }
            kolWord = kolWord + 1;
            lengthWord = lengthWord + wordString[i].length();
        }

        double avrWord = lengthWord * 1.0 / kolWord * 1.0;

        List<Object> stat = new ArrayList<Object>();
        stat.add(maxWord);
        stat.add(minWord);
        stat.add(lengthString);
        stat.add(avrWord);
        return stat;
    }

    public void getJDBC(String fileName, List<String> stringList) throws IOException, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Connection connection = null;

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");
        properties.setProperty("useSSL", "false");
        properties.setProperty("autoReconnect", "true");

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/h2",properties);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String insertDB = "insert into strings(name) values (?)";
        String insertStatistic = "insert into statistic(longest, shortest,length, average) values (?,?,?,?)";
        PreparedStatement psDB = null;
        PreparedStatement psStatistic = null;

        for (String stringFile : stringList) {
            if (stringFile.length() < 2) {
                continue;
            }
            String[] wordString = stringFile.split(" ");
            List<Object> statistic = staticStrings(wordString, stringFile.length());
            connection.setAutoCommit(false);
            psStatistic = connection.prepareStatement(insertStatistic);
            psStatistic.setString(1, (String) statistic.get(0));
            psStatistic.setString(2, (String) statistic.get(1));
            psStatistic.setInt(3, (Integer) statistic.get(2));
            psStatistic.setDouble(4, (Double) statistic.get(3));
            psStatistic.executeUpdate();
            psDB = connection.prepareStatement(insertDB);
            psDB.setString(1, stringFile);
            psDB.executeUpdate();
            connection.commit();

        }
        psDB.close();
        psStatistic.close();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ssp = new Swiss();

            }
        });
    }
}
