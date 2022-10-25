package socialnetwork.controller;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import socialnetwork.service.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;


public class CreareEvenimentController {
    Service service;
    Long idUserLogat;

    boolean adaugat;

    Label labelNume;
    Label labelData;
    Label labelOra;

    TextField textFieldNume;
    DatePicker datePicker;
    Spinner<Integer> spinnerOra;
    Spinner<Integer> spinnerMinut;

    Button buttonCreeaza;
    Button buttonRenunta;

    HBox hboxButoane;
    HBox hbox;
    VBox vboxLeft;
    VBox vboxRight;
    VBox vbox;

    Stage stage;

    public CreareEvenimentController(Service service,Long idUserLogat){
        this.service = service;
        this.idUserLogat = idUserLogat;
        initialize();
    }

    public void initialize(){
        adaugat = false;

        labelNume = new Label("Nume:  ");
        labelData = new Label("Data:  ");
        labelOra = new Label("Ora:  ");

        textFieldNume = new TextField();
        datePicker = new DatePicker();

        spinnerOra = new Spinner<Integer>(0,23,12);
        spinnerMinut = new Spinner<Integer>(0,59,0);
        spinnerOra.setEditable(true);
        spinnerMinut.setEditable(true);
        spinnerOra.setMaxWidth(60);
        spinnerMinut.setMaxWidth(60);

        buttonCreeaza = new Button("Creeaza");
        buttonRenunta = new Button("Renunta");
        buttonRenunta.getStyleClass().add("buttonWhite");

        vboxLeft = new VBox();
        vboxLeft.setAlignment(Pos.CENTER_RIGHT);
        vboxLeft.setSpacing(15);
        vboxLeft.getChildren().addAll(labelNume,labelData,labelOra);

        vboxRight = new VBox();
        vboxRight.setAlignment(Pos.CENTER_LEFT);
        vboxRight.setSpacing(7);
        vboxRight.getChildren().addAll(textFieldNume,datePicker,new HBox(spinnerOra,new Label(" "),spinnerMinut));

        hboxButoane = new HBox();
        hboxButoane.setSpacing(5);
        hboxButoane.setAlignment(Pos.CENTER);
        hboxButoane.setSpacing(25);
        hboxButoane.getChildren().addAll(buttonCreeaza,buttonRenunta);

        hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(vboxLeft,vboxRight);

        vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(hbox ,hboxButoane);

        vbox.setBackground(new Background(new BackgroundFill(new Color(0.0,0.8,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));
        vbox.getStylesheets().add("app.css");

        stage = new Stage();
        stage.setScene(new Scene(vbox,270,220));
        stage.setTitle("Creare eveniment");
        stage.show();

        buttonRenunta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.close();
            }
        });

        buttonCreeaza.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(), LocalTime.of(spinnerOra.getValue(),spinnerMinut.getValue()));
                    service.adaugaEveniment(textFieldNume.getText(),dateTime);
                    adaugat = true;
                    stage.close();
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare","Eveniment creat cu succes!");
                }
                catch(Exception e){
                    if(e.getMessage().equals("date"))
                        MessageAlert.showErrorMessage(null,"Nu ati introdus data!");
                    else
                        MessageAlert.showErrorMessage(null,e.getMessage());
                }
            }
        });

    }
}
