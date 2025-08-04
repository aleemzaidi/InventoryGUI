/*

Name: Aleem Zaidi
Student ID: 010394156
Course: C482
JavaDoc location: InventoryGUI/JavaDocs

 */

/**

 * description of a logical or runtime error:
 * <p>
 *     Added plus 1 to part/product id so it starts with 1 instead of 0.
 *     getting part/product from id caused out of bounds exceptions because of the add.
 *     Subtract 1 when using any methods to get id from the inventory list.
 *
 *     Error when getting and associating parts to products that were not yet created in Add Product Form.
 *     Fixed this issue by modifying the code to add associated parts to a temporary ObservableList then
 *     adding all parts from the ObservableList to the product after creating in inventory.
 * </p>

 * future enhancement:
 * <p>
 *     1. When removing a part from inventory, also remove that part from associated parts in all products.
 *     2. Adding barcode ids for products to easily scan in search.
 * </p>

 */

package azaidi6.inventorygui;

import azaidi6.inventorygui.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/** This class creates an app for inventory management*/
public class InventoryApplication extends Application {

    private static User user;
    private Stage stage;

    /** This method sets the application title and initiates the controller method*/
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stage.setTitle("ForgeLine Industries - ManuTrack");

        initInventoryController();
    }

    /** Initializer for JavaFX GUI. creates application window with MainMenu.fxml */
    public void initInventoryController() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryApplication.class.getResource("loginMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 250);
        stage.setScene(scene);

        stage.show();
    }

    /**
     *
     * @return returns the User object logged into the application. Used to get person logged into the application.
     */
    public static User getUser() {
        return user;
    }

    /**
     *
     * @param user set a new User object which is later used to get person logged into the application.
     */
    public static void setUser(User user) {
        InventoryApplication.user = user;
    }

    /** Main method for launching the application on startup */
    public static void main(String[] args) {
        launch();
    }

}