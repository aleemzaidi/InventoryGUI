module azaidi6.inventorygui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires jbcrypt;


    opens azaidi6.inventorygui to javafx.fxml;
    exports azaidi6.inventorygui;
    exports azaidi6.inventorygui.controllers;
    opens azaidi6.inventorygui.controllers to javafx.fxml;
    exports azaidi6.inventorygui.model;
    opens azaidi6.inventorygui.model to javafx.fxml;
}