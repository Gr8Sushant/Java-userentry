import Helpers.DBUtils;

import java.sql.*;

public class UserRegistration {
    private Connection con;
        public UserRegistration() {
            try {
                con = DBUtils.getDbConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        void insert(String textFname, String textLname, String textPhn, String type){
            try {
                String insert = "INSERT INTO users (textFname, textLname, textPhn, type) Values (?,?,?,?)";
                PreparedStatement statement = con.prepareStatement(insert);
                statement.setString(1, textFname);
                statement.setString(2, textLname);
                statement.setString(3, textPhn);
                statement.setString(4, type);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        }


