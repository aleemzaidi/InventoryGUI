package azaidi6.inventorygui.controllers;

import azaidi6.inventorygui.InventoryApplication;
import azaidi6.inventorygui.dao.InventoryDAO;
import azaidi6.inventorygui.dao.UserDAO;
import azaidi6.inventorygui.model.Inventory;
import azaidi6.inventorygui.model.Part;
import azaidi6.inventorygui.model.Product;
import azaidi6.inventorygui.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<User> userCombo;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<Part, Integer> partIdCol1;

    @FXML
    private TableColumn<Part, Integer> partInvCol1;

    @FXML
    private TableColumn<Part, String> partNameCol1;

    @FXML
    private TableColumn<Part, Double> partPriceCol1;

    @FXML
    private TableView<Part> partsTable1;

    @FXML
    private TableColumn<Part, Integer> partIdCol11;

    @FXML
    private TableColumn<Part, Integer> partInvCol11;

    @FXML
    private TableColumn<Part, String> partNameCol11;

    @FXML
    private TableColumn<Part, Double> partPriceCol11;

    @FXML
    private TableView<Part> partsTable11;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, Integer> productInvCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, Integer> productIdCol1;

    @FXML
    private TableColumn<Product, Integer> productInvCol1;

    @FXML
    private TableColumn<Product, String> productNameCol1;

    @FXML
    private TableColumn<Product, Double> productPriceCol1;

    @FXML
    private TableView<Product> productsTable1;

    @FXML
    private TableColumn<Product, Integer> productIdCol11;

    @FXML
    private TableColumn<Product, Integer> productInvCol11;

    @FXML
    private TableColumn<Product, String> productNameCol11;

    @FXML
    private TableColumn<Product, Double> productPriceCol11;

    @FXML
    private TableView<Product> productsTable11;

    ResourceBundle languageBundle = ResourceBundle.getBundle("azaidi6.inventorygui/language/lang");

    /**
     * Updates contactScheduleTable with all of selected contact's appointments in the database
     */
    public void userSelect() {
        partsTable11.setItems(InventoryDAO.getPartsByUser(userCombo.getValue().toString()));
        partsTable11.setPlaceholder(new Label("No data found for selected user."));

        productsTable11.setItems(InventoryDAO.getProductsByUser(userCombo.getValue().toString()));
        productsTable11.setPlaceholder(new Label("No data found for selected user."));
    }

    /**
     * Updates view to main menu view when back button is clicked
     * @throws IOException throws input or output exception when error loading main menu view occurs
     */
    @FXML
    protected void onBackButtonClicked() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(InventoryApplication.class.getResource("MainMenu.FXML")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) (backButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userCombo.setItems(UserDAO.getUsers());

        //load the Inventory object parts into the partsTable tableview
        partsTable.setItems(InventoryDAO.getMinParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //load the Inventory object parts into the partsTable tableview
        partsTable1.setItems(InventoryDAO.getMaxParts());

        partIdCol1.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol1.setCellValueFactory(new PropertyValueFactory<>("price"));

        //load the Inventory object parts into the partsTable tableview
        //partsTable11.setItems(InventoryDAO.getPartsByUser(InventoryApplication.getUser().toString()));
        partsTable11.setPlaceholder(new Label("Please select a user"));

        partIdCol11.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol11.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol11.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol11.setCellValueFactory(new PropertyValueFactory<>("price"));

        //load the Inventory object products into the productsTable tableview
        productsTable.setItems(InventoryDAO.getMinProducts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //load the Inventory object products into the productsTable tableview
        productsTable1.setItems(InventoryDAO.getMaxProducts());

        productIdCol1.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol1.setCellValueFactory(new PropertyValueFactory<>("price"));

        //productsTable11.setItems(InventoryDAO.getProductsByUser(InventoryApplication.getUser().toString()));
        productsTable11.setPlaceholder(new Label("Please select a user"));

        productIdCol11.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol11.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol11.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol11.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
