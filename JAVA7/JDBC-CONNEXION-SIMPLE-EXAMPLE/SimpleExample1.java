import java.sql.*;

public class SimpleExample1 {
  public static void main(String[] args) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      String url = "jdbc:mysql://localhost:3306/DATABASE_NAME";
      String user = "USER_NAME";
      String passwd = "USER_PASSWORD";
      Connection conn = DriverManager.getConnection(url, user, passwd);

      //Creating a Statement object
      Statement state = conn.createStatement();

      //The ResultSet object contains the result of the SQL query
      ResultSet result = state.executeQuery("SELECT * FROM classe");

      //We get the MetaData
      ResultSetMetaData resultMeta = result.getMetaData();
      System.out.println("\n**********************************");

      //We display the name of the columns
      for(int i = 1; i <= resultMeta.getColumnCount(); i++){
        System.out.print("\t"  + resultMeta.getColumnName(i).toUpperCase() + "\t *");
      }

      System.out.println("\n**********************************");
      while(result.next()){
        for(int i = 1; i <= resultMeta.getColumnCount(); i++)
        System.out.print("\t" + result.getObject(i).toString() +
        "\t |");
        System.out.println("\n---------------------------------");
      }

      result.close();
      state.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
