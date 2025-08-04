package azaidi6.inventorygui.model;

import javafx.beans.property.SimpleStringProperty;

public class User {

    private int userId;
    private final SimpleStringProperty username = new SimpleStringProperty();

    /**
     * Constructor for User object
     * @param userId int of user id
     * @param username string of username
     */
    public User(int userId, String username) {
        this.userId = userId;
        this.username.set(username);
    }

    /**
     * Used to get the user id
     * @return returns the int of the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Used to set the user id
     * @param userId int of user id to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Used to get username
     * @return returns string of the username
     */
    public SimpleStringProperty getUsername() {
        return username;
    }

    /**
     * Used to set username
     * @param username string of username to set
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * Used to set default display of Contact object
     * @return returns string of contact name
     */
    @Override
    public String toString() {
        return username.get();
    }

}
