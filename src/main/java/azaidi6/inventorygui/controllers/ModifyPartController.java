package azaidi6.inventorygui.controllers;
import azaidi6.inventorygui.InventoryApplication;
import azaidi6.inventorygui.dao.InventoryDAO;
import azaidi6.inventorygui.model.InHouse;
import azaidi6.inventorygui.model.Inventory;
import azaidi6.inventorygui.model.Outsourced;
import azaidi6.inventorygui.model.Part;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/** Class for controlling Modify Part Form. Handles editing and saving Parts in inventory */
public class ModifyPartController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private Label companyLabel;

    @FXML
    private TextField companyNameField;

    @FXML
    private GridPane formGrid;

    @FXML
    private TextField idField;

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private TextField invField;

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
    protected void onCancelButtonClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /** Updates selected Part from table */
    @FXML
    protected void onSaveButtonClicked() {

        int id = Integer.parseInt(idField.getPromptText());
        String name = nameField.getText();
        int inv = Integer.parseInt(invField.getText());
        double price = Double.parseDouble(priceField.getText());
        int max = Integer.parseInt(maxField.getText());
        int min = Integer.parseInt(minField.getText());

        if(min < max) {
            if(inv >= min && inv <= max){
                if(inHouseRadioButton.isSelected()) {
                    int machineId = Integer.parseInt(machineIDField.getText());
                    InventoryDAO.updatePart(id, name, price, inv, min, max, InventoryApplication.getUser().toString(), machineId);
                    //Inventory.updatePart(id-1, new InHouse(id, name, price, inv, min, max, machineId));
                } else {
                    String companyName = companyNameField.getText();
                    InventoryDAO.updatePart(id, name, price, inv, min, max, InventoryApplication.getUser().toString(), companyName);
                    //Inventory.updatePart(id-1, new Outsourced(id, name, price, inv, min, max, companyName));
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


    }

    /** Used to send Part data from table to Modify Part Form
     * @param part Part to modify
     * */
    @FXML
    protected void sendPart(Part part) {
        idField.setPromptText(String.valueOf(part.getId()));
        nameField.setText(part.getName());
        invField.setText(String.valueOf(part.getStock()));
        priceField.setText(String.valueOf(part.getPrice()));
        maxField.setText(String.valueOf(part.getMax()));
        minField.setText(String.valueOf(part.getMin()));

        formGrid.getChildren().removeAll(machineIDField, machineIDLabel, companyLabel, companyNameField);

        if(part instanceof InHouse) {
            inHouseRadioButton.setSelected(true);
            formGrid.add(machineIDLabel, 0,5,1,1);
            formGrid.add(machineIDField, 1, 5, 1, 1);

            formGrid.getChildren().remove(companyLabel);
            formGrid.getChildren().remove(companyNameField);

            machineIDField.setText(String.valueOf(((InHouse) part).getMachineId()));
        } else {
            outsourcedRadioButton.setSelected(true);
            formGrid.add(companyLabel, 0,5,1,1);
            formGrid.add(companyNameField, 1, 5, 1, 1);

            formGrid.getChildren().remove(machineIDLabel);
            formGrid.getChildren().remove(machineIDField);

            companyNameField.setText(((Outsourced) part).getCompanyName());
        }
    }

    /** Initializer for Modify Part Form */
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
    }
}
