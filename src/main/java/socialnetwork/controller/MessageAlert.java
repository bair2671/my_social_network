package socialnetwork.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {
    static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        if(text.length()>100) {
            message.setHeight(500);
            message.setWidth(800);
        }
        message.setHeaderText(header);
        if(type == Alert.AlertType.INFORMATION)
            message.setTitle("Mesaj de informare");
        if(type == Alert.AlertType.WARNING)
            message.setTitle("Mesaj de avertizare");
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }

    static void showErrorMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setHeaderText("Eroare!");
        message.setTitle("Mesaj de eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}


