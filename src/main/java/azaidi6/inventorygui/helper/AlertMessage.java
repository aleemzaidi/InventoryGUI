package azaidi6.inventorygui.helper;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class that handles error messaging for logging in.
 */
public class AlertMessage {
    static ResourceBundle languageBundle = ResourceBundle.getBundle("azaidi6.inventorygui/language/lang");

    /**
     *
     * @param errorId int id used to identify which type of error occured for logging in
     */
    public static void error(int errorId) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch(errorId) {
            case 1:
                alert.setTitle(languageBundle.getString("errorLogin"));
                alert.setContentText(languageBundle.getString("incorrectUsername"));
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle(languageBundle.getString("errorLogin"));
                alert.setContentText(languageBundle.getString("incorrectPassword"));
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle(languageBundle.getString("errorLogin"));
                alert.setContentText(languageBundle.getString("emptyUsername"));
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle(languageBundle.getString("errorLogin"));
                alert.setContentText(languageBundle.getString("emptyPassword"));
                alert.showAndWait();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + errorId);
        }

    }

}