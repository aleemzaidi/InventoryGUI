package azaidi6.inventorygui.controllers;

import azaidi6.inventorygui.InventoryApplication;
import azaidi6.inventorygui.dao.InventoryDAO;
import azaidi6.inventorygui.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/** Class for controlling Add Part Form. Handles adding a new part to inventory */
public class AddPartController implements Initializable {

    @FXML
    private GridPane formGrid;

    @FXML
    private Button cancelButton;

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private TextField invField;

    @FXML
    private Label companyLabel;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField machineIDField;

    @FXML
    private Label machineIDLabel;

    @FXML
    private TextField maxField;

    @FXML
    private TextField minField;

    @FXML
    private TextField nameField;

    @FXML
    private RadioButton outsourcedRadioButton;

    @FXML
    private TextField priceField;

    @FXML
    private Button saveButton;

    /** Handles closing the window when cancel button is clicked */
    @FXML
    protected void  onCancelButtonClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /** Creates a part and adds to allParts list when saveButton is clicked. */
    @FXML
    protected void onSaveButtonClicked() {

        try {
            String name = nameField.getText();
            int inv = Integer.parseInt(invField.getText());
            double price = Double.parseDouble(priceField.getText());
            int max = Integer.parseInt(maxField.getText());
            int min = Integer.parseInt(minField.getText());

            if(min < max) {
                if(inv >= min && inv <= max) {
                    if(inHouseRadioButton.isSelected()) {
                        int machineId = Integer.parseInt(machineIDField.getText());
                        InventoryDAO.addPart(name, price, inv, min, max, InventoryApplication.getUser().toString(), LocalDateTime.now(), InventoryApplication.getUser().toString(), LocalDateTime.now(), machineId);
                    } else {
                        String companyName = companyNameField.getText();
                        InventoryDAO.addPart(name, price, inv, min, max, InventoryApplication.getUser().toString(), LocalDateTime.now(), InventoryApplication.getUser().toString(), LocalDateTime.now(), companyName);
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

    /** Enables saveButton when either inHouseRadioButton or outsourcedRadioButton is selected */
    @FXML
    protected void onRadioSelect() {
        saveButton.setDisable(!(inHouseRadioButton.isSelected() | outsourcedRadioButton.isSelected()));
    }

    /** Initializer for AddPartController class. Sets default values for Add Part Form */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formGrid.getChildren().removeAll(machineIDField, machineIDLabel, companyLabel, companyNameField);

        inHouseRadioButton.setOnAction(inHouseEvent ->
        {
            formGrid.add(machineIDLabel, 0,5,1,1);
            formGrid.add(machineIDField, 1, 5, 1, 1);

            formGrid.getChildren().remove(companyLabel);
            formGrid.getChildren().remove(companyNameField);
        });

        outsourcedRadioButton.setOnAction(outsourcedEvent ->
        {
            formGrid.add(companyLabel, 0,5,1,1);
            formGrid.add(companyNameField, 1, 5, 1, 1);

            formGrid.getChildren().remove(machineIDLabel);
            formGrid.getChildren().remove(machineIDField);
        });

        saveButton.setDisable(!(inHouseRadioButton.isSelected() | outsourcedRadioButton.isSelected()));
    }
}
