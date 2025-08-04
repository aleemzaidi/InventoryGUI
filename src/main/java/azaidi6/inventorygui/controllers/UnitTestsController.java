package azaidi6.inventorygui.controllers;

import azaidi6.inventorygui.dao.InventoryDAO;
import azaidi6.inventorygui.helper.JDBC;
import azaidi6.inventorygui.helper.TimeConversion;
import azaidi6.inventorygui.model.InHouse;
import azaidi6.inventorygui.model.Inventory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static azaidi6.inventorygui.dao.UserDAO.*;

public class UnitTestsController {

    @FXML
    private TextArea infoArea;

    @FXML
    private Button closeButton;

    /** Handles closing the window when close button is clicked */
    @FXML
    protected void onCloseButtonClicked() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /** Handles unit testing 1 when unitTest1 button is clicked */
    @FXML
    protected void onUnitTest1() {
        infoArea.appendText("Initiating Unit Test 1");

        infoArea.appendText("\n\nTesting successful database connection: ");
        try {
            String protocol = "jdbc";
            String vendor = ":mysql:";
            String location = "//localhost/";
            String databaseName = "manu_track";
            String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
            String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
            String userName = "sqlUser"; // Username
            String password = "Passw0rd!";
            Class.forName(driver); // Locate Driver
            DriverManager.getConnection(jdbcUrl, userName, password);
            infoArea.appendText("Passed");
        } catch (Exception e) {
            infoArea.appendText("Failed");
        }

        infoArea.appendText("\nTesting failed database connection: ");
        try {
            String protocol = "jdbc";
            String vendor = ":mysql:";
            String location = "//localhost/";
            String databaseName = "manu_track";
            String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
            String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
            String userName = "sqlUser"; // Username
            String password = "password";
            Class.forName(driver); // Locate Driver
            DriverManager.getConnection(jdbcUrl, userName, password);
            infoArea.appendText("Failed");
        } catch (Exception e) {
            infoArea.appendText("Passed");
        }
        infoArea.appendText("\nTesting add InHouse part into inventory: ");
        int partId = -1;
        try {
            JDBC.openConnection();
            String partSQL = "INSERT INTO parts (name, price, stock, min, max, createdBy, createdDate, lastUpdatedBy, lastUpdatedDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String machineSQL = "INSERT INTO in_house (part_id, machine_id) VALUES (?, ?)";

            PreparedStatement ps = JDBC.connection.prepareStatement(partSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, "unittest1");
            ps.setDouble(2, 100.00);
            ps.setInt(3, 1);
            ps.setInt(4, 1);
            ps.setInt(5, 10);
            ps.setString(6, "unittest1");
            ps.setObject(7, TimeConversion.convertToUTC(LocalDateTime.now()));
            ps.setString(8, "unittest1");
            ps.setObject(9, TimeConversion.convertToUTC(LocalDateTime.now()));
            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                partId = rs.getInt(1);
            }

            Inventory.addPart(new InHouse(partId, "unittest1", 100.00, 1, 1, 10, 1000));
            ps = JDBC.connection.prepareStatement(machineSQL);
            ps.setInt(1, partId);
            ps.setInt(2, 1000);
            ps.executeUpdate();
            infoArea.appendText("Passed");
            JDBC.closeConnection();


        } catch (SQLException e) {
            JDBC.closeConnection();
            infoArea.appendText("Failed");
        }

        infoArea.appendText("\nTesting edit part from InHouse to Outsourced: ");
        try {
            InventoryDAO.updatePart(partId, "unittest1", 100.00, 5, 1, 10, "unittest1", "unittest1");
            infoArea.appendText("Passed");
        } catch (Exception e) {
            infoArea.appendText("Failed");
        }

        infoArea.appendText("\nTesting deleting part: ");
        try {
            InventoryDAO.deletePart(Inventory.lookupPart(partId));
            infoArea.appendText("Passed");
        } catch (Exception e) {
            infoArea.appendText("Failed");
            System.out.println(e.getMessage());
        }

        infoArea.appendText("\n\nUnit Test 1 Complete.\n\n");
    }

    /** Handles unit testing 2 when unitTest2 button is clicked */
    @FXML
    protected void onUnitTest2() {

        infoArea.appendText("Initiating Unit Test 2");

        infoArea.appendText("\n\nTesting successful user login: ");
        String username = "test";
        String password = "test";

        if(username.isEmpty() || username.isBlank()) {
            infoArea.appendText("Failed");
        } else if(password.isBlank() || password.isEmpty()) {
            infoArea.appendText("Failed");
        } else if(!checkUsername(username)) {
            infoArea.appendText("Failed");
        } else if(!userLogin(username, password)) {
            infoArea.appendText("Failed");
        } else if(userLogin(username, password)) {
            infoArea.appendText("Passed");
        }

        infoArea.appendText("\nTesting empty username on login: ");
        username = "";
        password = "test";

        if(username.isEmpty() || username.isBlank()) {
            infoArea.appendText("Passed");
        } else if(password.isBlank() || password.isEmpty()) {
            infoArea.appendText("Failed");
        } else if(!checkUsername(username)) {
            infoArea.appendText("Failed");
        } else if(!userLogin(username, password)) {
            infoArea.appendText("Failed");
        } else if(userLogin(username, password)) {
            infoArea.appendText("Failed");
        }

        infoArea.appendText("\nTesting invalid username on login: ");
        username = "username";
        password = "test";

        if(username.isEmpty() || username.isBlank()) {
            infoArea.appendText("Failed");
        } else if(password.isBlank() || password.isEmpty()) {
            infoArea.appendText("Failed");
        } else if(!checkUsername(username)) {
            infoArea.appendText("Passed");
        } else if(!userLogin(username, password)) {
            infoArea.appendText("Failed");
        } else if(userLogin(username, password)) {
            infoArea.appendText("Failed");
        }

        infoArea.appendText("\nTesting empty password on login: ");
        username = "test";
        password = "";

        if(username.isEmpty() || username.isBlank()) {
            infoArea.appendText("Failed");
        } else if(password.isBlank() || password.isEmpty()) {
            infoArea.appendText("Passed");
        } else if(!checkUsername(username)) {
            infoArea.appendText("Failed");
        } else if(!userLogin(username, password)) {
            infoArea.appendText("Failed");
        } else if(userLogin(username, password)) {
            infoArea.appendText("Failed");
        }

        infoArea.appendText("\nTesting wrong password on login: ");
        username = "test";
        password = "testestest";

        if(username.isEmpty() || username.isBlank()) {
            infoArea.appendText("Failed");
        } else if(password.isBlank() || password.isEmpty()) {
            infoArea.appendText("Failed");
        } else if(!checkUsername(username)) {
            infoArea.appendText("Failed");
        } else if(!userLogin(username, password)) {
            infoArea.appendText("Passed");
        } else if(userLogin(username, password)) {
            infoArea.appendText("Failed");
        }

        infoArea.appendText("\n\nUnit Test 2 Complete.\n\n");

    }

}
