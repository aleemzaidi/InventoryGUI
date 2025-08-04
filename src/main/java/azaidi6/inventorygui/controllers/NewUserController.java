package azaidi6.inventorygui.controllers;

import azaidi6.inventorygui.dao.UserDAO;
import azaidi6.inventorygui.helper.PasswordUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static azaidi6.inventorygui.dao.UserDAO.*;

public class NewUserController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private Button submitButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Label errorLabel;

    /** Handles closing the window when cancel button is clicked */
    @FXML
    protected void onCancelButtonClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onSubmitButtonClicked() {

        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if(username.isEmpty() || username.isBlank()) {
            errorLabel.setText("Username field is empty!\nPlease fill out the username field");
        } else if(password.isBlank() || password.isEmpty()) {
            errorLabel.setText("Password field is emtpy!\nPlease fill out the password field!");
        } else if(checkUsername(username)) {
            errorLabel.setText("Username is already in use!\nPlease try another username!");
        } else if(!(password.equalsIgnoreCase(confirmPassword))) {
            errorLabel.setText("Password and confirm password fields do not match!");
        } else if(UserDAO.newUser(username, PasswordUtil.hashPassword(password))){
            errorLabel.setText("");
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null); // No header
            alert.setContentText("New user created successfully!");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }

}
