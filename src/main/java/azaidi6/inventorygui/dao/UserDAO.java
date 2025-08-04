package azaidi6.inventorygui.dao;

import azaidi6.inventorygui.helper.JDBC;
import azaidi6.inventorygui.helper.PasswordUtil;
import azaidi6.inventorygui.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     *
     * @param username string of username inputted on login screen.
     * @param password string of password inputted on login screen.
     * @return returns true if username and password match in database, otherwise returns false if no match.
     */
    public static boolean userLogin(String username, String password) {
        JDBC.openConnection();
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                String storedHashedPassword = rs.getString("password");
                JDBC.closeConnection();
                return PasswordUtil.checkPassword(password, storedHashedPassword);
            }
        } catch (SQLException e) {
            JDBC.closeConnection();
            System.out.println("Error Logging in:" + e.getMessage());
        }
        JDBC.closeConnection();
        return false;
    }

    /**
     *
     * @param username string of username inputted on the login screen.
     * @return returns true if username is found in database, otherwise returns false if not found.
     */
    public static boolean checkUsername(String username) {
        JDBC.openConnection();
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                JDBC.closeConnection();
                return true;
            }
        } catch (SQLException e) {
            JDBC.closeConnection();
            System.out.println("Error Logging in:" + e.getMessage());
        }
        JDBC.closeConnection();
        return false;
    }

    /**
     *
     * @param username string of username inputted on the login screen.
     * @return returns the id of the username in the database.
     */
    public static int getUserId(String username) {
        JDBC.openConnection();
        int userId = -1;
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                userId =  rs.getInt("id");
            }

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }
        JDBC.closeConnection();
        return userId;
    }

    public static ObservableList<User> getUsers() {

        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String sql = "SELECT id, username FROM users";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username")
                );
                users.add(user);
            }
            JDBC.closeConnection();
            return users;

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

    }

    public static boolean newUser(String username, String password) {

        try {
            JDBC.openConnection();
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                JDBC.closeConnection();
                return true;
            }

        } catch (SQLException e) {
            JDBC.closeConnection();
            throw new RuntimeException(e);
        }

        JDBC.closeConnection();
        return false;

    }

}