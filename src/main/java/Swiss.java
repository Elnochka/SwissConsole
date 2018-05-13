
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Swiss {
    private static List<String> listStrings = new ArrayList<String>();
    private static List<ListString> listListStrings = new ArrayList<ListString>();

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Swiss swiss = new Swiss();
        String fileName = ""; if(args.length == 0 ){
            System.out.println("You should enter file name in command line");
            System.exit(0);
        }else{
            fileName = args[0];
        }

        swiss.getListStrings(fileName);
        swiss.getJDBC();
    }

    public List<String> getListStrings(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        try {

            while (reader.ready()) {
                String stringFile = reader.readLine();
                listStrings.add(stringFile);
                getListListStrings(stringFile);
            }

        }catch (IOException ex) {
            ex.printStackTrace();

        }
        reader.close();
        return listStrings;
    }

    public List<ListString> getListListStrings(String stringFile){
        ListString listString = new ListString();

        String[] wordString = stringFile.split(" ");

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

        listString.setLongest(maxWord);
        listString.setAverage(avrWord);
        listString.setShortest(minWord);
        listString.setLength(stringFile.length());
        listListStrings.add(listString);

        return listListStrings;
    }

    public void getJDBC(){
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");
        properties.setProperty("useSSL", "false");
        properties.setProperty("autoReconnect", "true");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/h2",properties);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        String insertStrings = "insert into strings(name) values (?)";
        String insertStatistic = "insert into statistic(longest, shortest,length, average) values (?,?,?,?)";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(insertStrings);
            for (String stringVar : listStrings) {
                preparedStatement.setString(1, stringVar);
                preparedStatement.executeUpdate();
            }
            preparedStatement = connection.prepareStatement(insertStatistic);
            for (ListString stringVar : listListStrings) {
                preparedStatement.setString(1, stringVar.getLongest());
                preparedStatement.setString(2, stringVar.getShortest());
                preparedStatement.setInt(3, stringVar.getLength());
                preparedStatement.setDouble(4, stringVar.getAverage());
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
            connection.close();

        } catch (SQLException ex){
            ex.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

}
