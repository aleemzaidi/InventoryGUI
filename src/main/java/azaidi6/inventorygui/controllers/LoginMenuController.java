package azaidi6.inventorygui.controllers;

import azaidi6.inventorygui.InventoryApplication;
import azaidi6.inventorygui.helper.AlertMessage;
import azaidi6.inventorygui.helper.PasswordUtil;
import azaidi6.inventorygui.helper.TimeConversion;
import azaidi6.inventorygui.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static azaidi6.inventorygui.dao.UserDAO.*;

public class LoginMenuController implements Initializable {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label appTitle;

    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Label zoneLabel;

    ResourceBundle languageBundle = ResourceBundle.getBundle("azaidi6.inventorygui/language/lang");

    /**
     * Used to log user in and load main menu view
     * @throws IOException throws input or output exception when error loading main menu view occurs
     */
    public void onLoginClicked() throws IOException {

        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.isEmpty() || username.isBlank()) {
            AlertMessage.error(3);
            loginActivity(false);
        } else if(password.isBlank() || password.isEmpty()) {
            AlertMessage.error(4);
            loginActivity(false);
        } else if(!checkUsername(username)) {
            AlertMessage.error(1);
            loginActivity(false);
        } else if(!userLogin(username, password)) {
            AlertMessage.error(2);
            loginActivity(false);
        } else if(userLogin(username, password)) {
            InventoryApplication.setUser(new User(getUserId(username), username));
            Parent parent = FXMLLoader.load(Objects.requireNonNull(InventoryApplication.class.getResource("MainMenu.FXML")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) (loginButton.getScene().getWindow());
            stage.setScene(scene);
            stage.show();
            loginActivity(true);
        }
    }

    /**
     * Used to exit the application when exit button is clicked
     */
    public void onExitClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, languageBundle.getString("exitConfirmation"));
        alert.setTitle(languageBundle.getString("exitTitle"));
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appTitle.setText(languageBundle.getString("InventoryApp"));
        usernameLabel.setText(languageBundle.getString("Username"));
        passwordLabel.setText(languageBundle.getString("Password"));
        loginButton.setText(languageBundle.getString("Login"));
        exitButton.setText(languageBundle.getString("Exit"));

        zoneLabel.setText(TimeConversion.getZoneId().toString());
    }

    /**
     * LAMBDA EXPRESSION: LINES 109-117 to get login success and return file output message
     */
    interface LogActivity {
        public String result(Boolean success);
    }

    LogActivity logActivity = (success) -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss");
        return (success) ? usernameField.getText() + ", LOGIN_SUCCESS, " + formatter.format(TimeConversion.convertToUTC(LocalDateTime.now())) :
                usernameField.getText() + ", LOGIN_FAILED, " + formatter.format(TimeConversion.convertToUTC(LocalDateTime.now()));
    };

    /**
     * Logs login activity in login_activity.txt file
     * @param success true or false if login was successful
     * @throws IOException throws input or output exception when error writing to login_activity.txt occurs
     */
    private void loginActivity(Boolean success) throws IOException {
        FileWriter fwritter = new FileWriter("login_activity.txt", true);
        fwritter.write(logActivity.result(success) + "\n");
        fwritter.close();
    }
}
