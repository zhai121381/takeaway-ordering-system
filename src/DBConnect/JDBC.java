package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class JDBC {
    public static Connection connection() {
        Connection connection=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/appdb?useUnicode=true&characterEncoding=utf8","root","123456");
            System.out.println("connect success");
        } catch (ClassNotFoundException e) {
            System.out.println("failed to find Drivers");
        }
        catch (SQLException e) {
            System.out.println("error! please check the username and password!");
        }
        return connection;
    }
}
