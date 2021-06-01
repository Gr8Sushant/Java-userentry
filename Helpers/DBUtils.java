package Helpers;
import java.sql.*;

public class DBUtils {
    public static Connection getDbConnection() throws SQLException {
        // jdbc:mysql://localhost:3306/college

        String connectionString = "jdbc:mysql://localhost:3306/videogamestation? useTimezone=true&serverTimezone=UTC"; // + Config.dbHost + ":" + Config.dbPort + "/" + Config.dbName;
        Driver d =new com.mysql.jdbc.Driver();
        DriverManager.registerDriver(d);

        return DriverManager.getConnection(connectionString, Config.dbUser, Config.dbPass);
    }}