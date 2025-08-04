package azaidi6.inventorygui.controllers;

import azaidi6.inventorygui.InventoryApplication;
import azaidi6.inventorygui.dao.InventoryDAO;
import azaidi6.inventorygui.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class for controlling Add Product Form. Handles adding new products into inventory */
public class AddProductController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private TableColumn<Part, Integer> partIdColA;

    @FXML
    private TableColumn<Part, Integer> partIdColB;

    @FXML
    private TableColumn<Part, Integer> partInvColA;

    @FXML
    private TableColumn<Part, Integer> partInvColB;

    @FXML
    private TableColumn<Part, String> partNameColA;

    @FXML
    private TableColumn<Part, String> partNameColB;

    @FXML
    private TableColumn<Part, Double> partPriceColA;

    @FXML
    private TableColumn<Part, Double> partPriceColB;

    @FXML
    private TableView<Part> partTableA;

    @FXML
    private TableView<Part> partTableB;

    @FXML
    private TextField productInvField;

    @FXML
    private TextField productMaxField;

    @FXML
    private TextField productMinField;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField productPriceField;

    @FXML
    private Button saveButton;

    /** temporary list for holding associated Parts for Product */
    protected ObservableList<Part> tempParts = FXCollections.observableArrayList();

    /** Handles closing the window when cancel button is clicked */
    @FXML
    protected void  onCancelButtonClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /** Creates and adds new Product to inventory */
    @FXML
    protected void onSaveButtonClicked() {
        try {
            int id = Inventory.getAllProducts().size() + 1;
            String name = productNameField.getText();
            int inv = Integer.parseInt(productInvField.getText());
            double price = Double.parseDouble(productPriceField.getText());
            int max = Integer.parseInt(productMaxField.getText());
            int min = Integer.parseInt(productMinField.getText());

            if(min < max) {
                if(inv >= min && inv <= max) {
                    int productId = InventoryDAO.addProduct(name, price, inv, min, max, InventoryApplication.getUser().toString(), LocalDateTime.now(), InventoryApplication.getUser().toString(), LocalDateTime.now());


                    if(!(Inventory.getAllProducts().get(Inventory.findProductIndex(productId)).getAllAssociatedParts().isEmpty())){
                        InventoryDAO.deleteAssociatedParts(productId);
//                        for(Part parts : Inventory.getAllProducts().get(Inventory.findProductIndex(productId)).getAllAssociatedParts()){
//                            //Inventory.getAllProducts().get(Inventory.findProductIndex(productId)).deleteAssociatedPart(parts);
//                        }
                    }
                    if(!(tempParts.isEmpty())){
                        for(Part parts : tempParts){
                            InventoryDAO.addAssociatedPart(productId, parts);
                            //Inventory.getAllProducts().get(Inventory.findProductIndex(productId)).addAssociatedPart(parts);
                        }
                    }

                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error in inv field");
                    alert.setContentText("inv value must be between min and max");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error in min/max field");
                alert.setContentText("min value cannot be less than max value");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid number: " + e.getMessage());
            alert.showAndWait();
        }

    }

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
        if(!(searchInput.getText().isEmpty())) {
            if(isInteger(searchInput.getText())) {
                ObservableList<Part> result = FXCollections.observableArrayList();
                try {
                    if(Inventory.lookupPart(Integer.parseInt(searchInput.getText())) != null) {
                        result.add(Inventory.lookupPart(Integer.parseInt(searchInput.getText())));
                    }
                    partTableA.setItems(result);
                    if(partTableA.getSelectionModel().isEmpty()) {
                        partTableA.setPlaceholder(new Label("No Parts found from search!"));
                    }
                } catch(Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Search Error");
                    alert.setContentText(e.getMessage());
                }

            } else {
                try {
                    partTableA.setItems(Inventory.lookupPart(searchInput.getText().toLowerCase()));
                    if(partTableA.getSelectionModel().isEmpty()) {
                        partTableA.setPlaceholder(new Label("No Parts found from search!"));
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Search Error");
                    alert.setContentText(e.getMessage());
                }
            }
        } else {
            partTableA.setItems(Inventory.getAllParts());
            partTableA.setPlaceholder(new Label("No content in table"));
        }
    }

    /** Handles adding associated parts to product */
    @FXML
    protected void onAddPartButtonClicked() {
        if(partTableA.getSelectionModel().getSelectedItem() == null){
            return;
        }
        tempParts.add(partTableA.getSelectionModel().getSelectedItem());
    }

    /** Handles removal of associated parts from product */
    @FXML
    protected void onRemovePartButtonClicked() {
        if(partTableB.getSelectionModel().getSelectedItem() == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            tempParts.remove(partTableB.getSelectionModel().getSelectedItem());
        }

    }

    /** Initializer for AddProductController class. Sets default values for tables in window */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTableA.setItems(Inventory.getAllParts());

        partIdColA.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColA.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColA.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColA.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTableB.setItems(tempParts);

        partIdColB.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColB.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColB.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColB.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
