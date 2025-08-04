package azaidi6.inventorygui.controllers;

import azaidi6.inventorygui.InventoryApplication;
import azaidi6.inventorygui.dao.InventoryDAO;
import azaidi6.inventorygui.model.Inventory;
import azaidi6.inventorygui.model.Part;
import azaidi6.inventorygui.model.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Class for controlling main menu of Inventory Management System */
public class InventoryController implements Initializable {

    @FXML
    private Label userLabel;

    @FXML
    private Button addPartButton;

    @FXML
    private Button addProductButton;

    @FXML
    private Button deletePartButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Button modifyPartButton;

    @FXML
    private Button modifyProductButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button newUserButton;

    @FXML
    private Button testButton;

    @FXML
    private Button logoutButton;

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
    private GridPane mainGrid;

    ResourceBundle languageBundle = ResourceBundle.getBundle("azaidi6.inventorygui/language/lang");

    /** Checks for integer inputs in search field */
    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Searches for products/parts and updates corresponding table with search parameters
     * @param event Used to grab the text field that triggered the event
     * */
    @FXML
    protected void onSearch(KeyEvent event) {
        TextField searchInput = (TextField)event.getSource();
        if(searchInput.getId().equals("searchPartField")) {
            if(!(searchInput.getText().isEmpty())) {
                if(isInteger(searchInput.getText())) {
                    ObservableList<Part> result = FXCollections.observableArrayList();
                    try {
                        if(Inventory.lookupPart(Integer.parseInt(searchInput.getText())) != null) {
                            result.add(Inventory.lookupPart(Integer.parseInt(searchInput.getText())));
                        }
                        partsTable.setItems(result);
                        if(partsTable.getSelectionModel().isEmpty()) {
                            partsTable.setPlaceholder(new Label("No Parts found from search!"));
                        }
                    } catch(Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Search Error");
                        alert.setContentText(e.getMessage());
                    }
                } else {
                    try {
                        partsTable.setItems(Inventory.lookupPart(searchInput.getText().toLowerCase()));
                        if(partsTable.getSelectionModel().isEmpty()) {
                            partsTable.setPlaceholder(new Label("No Parts found from search!"));
                        }
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Search Error");
                        alert.setContentText(e.getMessage());
                    }
                }
            } else {
                partsTable.setItems(Inventory.getAllParts());
                partsTable.setPlaceholder(new Label("No content in table"));
            }
        } else if(searchInput.getId().equals("searchProductField")) {
            if(!(searchInput.getText().isEmpty())) {
                if(isInteger(searchInput.getText())) {
                    ObservableList<Product> result = FXCollections.observableArrayList();
                    try {
                        if(Inventory.lookupProduct(Integer.parseInt(searchInput.getText())) != null) {
                            result.add(Inventory.lookupProduct(Integer.parseInt(searchInput.getText())));
                        }
                        productsTable.setItems(result);
                        if(productsTable.getSelectionModel().isEmpty()) {
                            productsTable.setPlaceholder(new Label("No Products found!"));
                        }
                    } catch(Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Search Error");
                        alert.setContentText(e.getMessage());
                    }


                } else {
                    try {
                        productsTable.setItems(Inventory.lookupProduct(searchInput.getText().toLowerCase()));
                        if(productsTable.getSelectionModel().isEmpty()) {
                            productsTable.setPlaceholder(new Label("No Products found!"));
                        }
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Search Error");
                        alert.setContentText(e.getMessage());
                    }
                }
            } else {
                productsTable.setItems(Inventory.getAllProducts());
                productsTable.setPlaceholder(new Label("No content in table"));
            }

        }
    }

    /** Handles creation of Add Part Form */
    @FXML
    protected void onAddPartButtonClicked() {
        try {
            FXMLLoader addPartFXLM = new FXMLLoader(InventoryApplication.class.getResource("AddPart.fxml"));
            Scene addPartScene = new Scene(addPartFXLM.load(), 450, 500);
            Stage stage = new Stage();
            stage.setTitle("Add Part");
            stage.setScene(addPartScene);
            stage.show();
            stage.setOnHiding( windowEvent -> {addPartButton.setDisable(false);});
            stage.setOnCloseRequest( windowEvent -> {addPartButton.setDisable(false);});
            addPartButton.setDisable(true);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create add part form!", e);
        }

    }

    /** Handles creation of New User Form */
    @FXML
    protected void onNewUserButtonClicked() {
        if(InventoryApplication.getUser().toString().equalsIgnoreCase("admin")) {
            try {
                FXMLLoader newUserFXLM = new FXMLLoader(InventoryApplication.class.getResource("NewUser.fxml"));
                Scene addPartScene = new Scene(newUserFXLM.load());
                Stage stage = new Stage();
                stage.setTitle("Add New User");
                stage.setScene(addPartScene);
                stage.show();
                stage.setOnHiding( windowEvent -> {newUserButton.setDisable(false);});
                stage.setOnCloseRequest( windowEvent -> {newUserButton.setDisable(false);});
                newUserButton.setDisable(true);
            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new user form!", e);
            }
        }

    }

    /** Handles creation of Unit Test Menu */
    @FXML
    protected void onTestButtonClicked() {
        try {
            FXMLLoader newUserFXLM = new FXMLLoader(InventoryApplication.class.getResource("UnitTests.fxml"));
            Scene addPartScene = new Scene(newUserFXLM.load());
            Stage stage = new Stage();
            stage.setTitle("Unit Tests");
            stage.setScene(addPartScene);
            stage.show();
            stage.setOnHiding( windowEvent -> {testButton.setDisable(false);});
            stage.setOnCloseRequest( windowEvent -> {testButton.setDisable(false);});
            testButton.setDisable(true);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create unit test menu!", e);
        }

    }

    /** Handles creation of Add Product Form */
    @FXML
    protected void onAddProductButtonClicked() {
        try {
            FXMLLoader addProductFXLM = new FXMLLoader(InventoryApplication.class.getResource("AddProduct.fxml"));
            Scene addProductScene = new Scene(addProductFXLM.load(), 800, 500);
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.setScene(addProductScene);
            stage.show();
            stage.setOnHiding( windowEvent -> {addProductButton.setDisable(false);});
            stage.setOnCloseRequest( windowEvent -> {addProductButton.setDisable(false);});
            addProductButton.setDisable(true);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create add product form!", e);
        }

    }

    /**
     * Updates view to reports menu view when reports button is clicked
     * @throws IOException throws input or output exception when error loading reports menu view occurs
     */
    @FXML
    protected void onReportButtonClicked() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(InventoryApplication.class.getResource("ReportsMenu.FXML")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) (reportsButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Used to exit the application when exit button is clicked
     */
    public void onExitButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, languageBundle.getString("exitConfirmation"));
        alert.setTitle(languageBundle.getString("exitTitle"));
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }

    }

    /**
     * Logs user out and loads login menu view when logout button is clicked
     * @throws IOException throws input or output exception when error loading login menu view occurs
     */
    public void onLogoutClicked() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, languageBundle.getString("logoutConfirmation"));
        alert.setTitle(languageBundle.getString("Logout"));
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(InventoryApplication.class.getResource("loginMenu.FXML")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) (logoutButton.getScene().getWindow());
            stage.setScene(scene);
            stage.show();
        }


    }

    /** Checks if Part is selected in Parts table and enables modify/delete buttons if true */
    @FXML
    protected void onPartSelected() {
        modifyPartButton.setDisable((partsTable.getSelectionModel().isEmpty()));
        deletePartButton.setDisable((partsTable.getSelectionModel().isEmpty()));
    }

    /** Checks if Product is selected in Products table and enables modify/delete buttons if true */
    @FXML
    protected void onProductSelected() {
        modifyProductButton.setDisable((productsTable.getSelectionModel().isEmpty()));
        deleteProductButton.setDisable((productsTable.getSelectionModel().isEmpty()));
    }

    /** Handles deletion of Part from inventory */
    @FXML
    protected void onDeletePartButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            InventoryDAO.deletePart(partsTable.getSelectionModel().getSelectedItem());
            partsTable.setItems(Inventory.getAllParts());
        }

        modifyPartButton.setDisable(!(partsTable.getSelectionModel().isEmpty()));
        deletePartButton.setDisable(!(partsTable.getSelectionModel().isEmpty()));

    }

    /** Handles creation of Modify Part Form and sends selected Part data to form */
    @FXML
    protected void onModifyPartButtonClicked() throws IOException {
        if(partsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }
        FXMLLoader modifyPartFXML = new FXMLLoader(InventoryApplication.class.getResource("ModifyPart.fxml"));
        Scene modifyPartScene = new Scene(modifyPartFXML.load(), 450, 500);
        Stage stage = new Stage();
        stage.setTitle("Modify Part");
        stage.setScene(modifyPartScene);

        ModifyPartController modPartController = modifyPartFXML.getController();
        modPartController.sendPart(partsTable.getSelectionModel().getSelectedItem());

        modifyPartButton.setDisable(true);
        stage.showAndWait();
        modifyPartButton.setDisable(false);

    }

    /** Handles deletion of Product from inventory */
    @FXML
    protected void onDeleteProductButtonClicked() {
        if(productsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            if(InventoryDAO.getAllAssociatedParts(productsTable.getSelectionModel().getSelectedItem().getId()).isEmpty()) {
                InventoryDAO.deleteProduct(productsTable.getSelectionModel().getSelectedItem());
                productsTable.setItems(Inventory.getAllProducts());
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Associated Parts");
                alert.setContentText("Cannot delete products with associated parts");
                alert.showAndWait();
            }

        }

        modifyProductButton.setDisable(!(productsTable.getSelectionModel().isEmpty()));
        deleteProductButton.setDisable(!(productsTable.getSelectionModel().isEmpty()));

    }

    /** Handles creation of Modify Product Form and sends selected Product data to form */
    @FXML
    protected void onModifyProductButtonClicked() throws IOException {
        if(productsTable.getSelectionModel().getSelectedItem() == null){
            return;
        }
        FXMLLoader modifyProductFXML = new FXMLLoader(InventoryApplication.class.getResource("ModifyProduct.fxml"));
        Scene modifyProductScene = new Scene(modifyProductFXML.load(), 800, 500);
        Stage stage = new Stage();
        stage.setTitle("Modify Part");
        stage.setScene(modifyProductScene);

        ModifyProductController modProductController = modifyProductFXML.getController();
        modProductController.sendProduct(productsTable.getSelectionModel().getSelectedItem());

        modifyProductButton.setDisable(true);
        stage.showAndWait();
        modifyProductButton.setDisable(false);

    }

    /** Initializer for InventoryController. Sets default values for Parts/Products tables */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(!(InventoryApplication.getUser().toString().equalsIgnoreCase("admin"))) {
            newUserButton.setDisable(true);
            testButton.setDisable(true);
        }

        //load username into userLabel
        userLabel.setText("Welcome, " + InventoryApplication.getUser().toString());

        //load parts from the database into the Inventory object
        Inventory.setAllParts(InventoryDAO.getAllParts());

        //load the Inventory object parts into the partsTable tableview
        partsTable.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //load products from the database into the Inventory object
        Inventory.setAllProducts(InventoryDAO.getAllProducts());

        //load the Inventory object products into the productsTable tableview
        productsTable.setItems(Inventory.getAllProducts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProductButton.setDisable((productsTable.getSelectionModel().isEmpty()));
        deleteProductButton.setDisable((productsTable.getSelectionModel().isEmpty()));
        modifyPartButton.setDisable((partsTable.getSelectionModel().isEmpty()));
        deletePartButton.setDisable((partsTable.getSelectionModel().isEmpty()));
    }
}