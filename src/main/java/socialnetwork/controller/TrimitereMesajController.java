package socialnetwork.controller;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.Service;

import java.util.ArrayList;


public class TrimitereMesajController {
    Service service;
    Long idExpeditor;
    Long idDestinatar;

    TextArea textArea;
    Button buttonTrimite;
    Button buttonRenunta;

    HBox hbox;
    VBox vbox;

    Label labelCatre;

    Stage stage;

    public TrimitereMesajController(Service service,Long idExpeditor,Long idDestinatar){
        this.service = service;
        this.idExpeditor = idExpeditor;
        this.idDestinatar = idDestinatar;
        initialize();
    }

    public void initialize(){
        textArea = new TextArea();
        textArea.setMinSize(100,40);
        textArea.setMaxSize(200,120);

        buttonTrimite = new Button("Trimite");
        buttonRenunta = new Button("Renunta");
        buttonRenunta.getStyleClass().add("buttonWhite");

        hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        Utilizator user = service.findOneUtilizator(idDestinatar);
        hbox.getChildren().addAll(new Label(),buttonTrimite,buttonRenunta,new Label(" "));

        labelCatre = new Label("Mesaj pentru "+user.getFirstName()+" "+user.getLastName()+":");
        labelCatre.setAlignment(Pos.CENTER_LEFT);
        //labelCatre.getStyleClass().add("labelBig");

        vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(labelCatre,textArea,hbox);

        vbox.setBackground(new Background(new BackgroundFill(new Color(0.0,0.8,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));
        vbox.getStylesheets().add("app.css");

        stage = new Stage();
        stage.setScene(new Scene(vbox,250,250));
        stage.setTitle("Trimitere mesaj");
        stage.show();

        buttonRenunta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.close();
            }
        });

        buttonTrimite.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ArrayList<Long> dest = new ArrayList<>();
                dest.add(idDestinatar);
                try {
                    service.addMessage(1L, idExpeditor, dest, textArea.getText().toString());
                    stage.close();
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare","Mesaj trimis cu succes!");
                }
                catch(Exception e){
                    MessageAlert.showErrorMessage(null,e.getMessage());
                }
            }
        });

    }
}
