package socialnetwork.controller;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import socialnetwork.domain.Eveniment;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PrimaryStageController {
    private Stage stage;

    private Scene sceneConectare;
    private Scene sceneCreareCont;
    private Scene sceneInContViewPrieteni;
    private Scene sceneInContViewCereri;
    private Scene sceneInContViewEvenimente;
    private Scene sceneInContViewMesaje;
    private Scene sceneInContViewNotificari;
    private Scene sceneInContRaportActivitate;
    private Scene sceneInContRaportMesajePrieten;

    private Button buttonConectare;
    private Button buttonCreareCont;
    private Button buttonRenunta;

    private Service service;
    private Long idUserLogat;

    private boolean vazutNotificari;

    public PrimaryStageController(Stage stage,Service service){
        this.stage = stage;
        this.service = service;
        initialize();
    }

    public void initialize() {
        initializeSceneConectare();
        initializeSceneCreareCont();

        stage.setScene(sceneConectare);
        stage.show();
    }

    private void initializeSceneConectare(){
        vazutNotificari = false;
        Label labelWelcome = new Label("Bine ai venit in retea!");
        labelWelcome.setFont(Font.font("Arial",20));

        Label labelId = new Label("Username:  ");
        TextField textFieldUsername = new TextField();
        Label labelPassword = new Label("Parola:  ");
        PasswordField textFieldPassword = new PasswordField();

        buttonConectare = new Button("Conecteaza-te");
        buttonConectare.setMinWidth(200);
        buttonConectare.setStyle("-fx-background-color: #0859b5; -fx-text-fill: white;");
        buttonConectare.setFont(Font.font(null ,FontWeight.BOLD,13));

        buttonCreareCont = new Button("Creeaza un cont nou");
        buttonCreareCont.setStyle("-fx-background-color: #00aa00; -fx-text-fill: white;  ");
        buttonCreareCont.setFont(Font.font(null, FontWeight.BOLD, 13));

        HBox hBoxIdPassword =new HBox();
        VBox vBoxPricipal = new VBox();

        VBox vBoxLabels = new VBox();
        VBox vBoxTextFields = new VBox();

        vBoxLabels.setAlignment(Pos.CENTER);
        vBoxLabels.getChildren().addAll(labelId,new Label(""),labelPassword);
        vBoxLabels.setSpacing(4.2);
        vBoxTextFields.setAlignment(Pos.CENTER);
        vBoxTextFields.getChildren().addAll(textFieldUsername,new Label(""),textFieldPassword);

        hBoxIdPassword.setAlignment(Pos.CENTER);
        hBoxIdPassword.getChildren().addAll(vBoxLabels,new Label(" "),vBoxTextFields);

        vBoxPricipal.setAlignment(Pos.CENTER);
        vBoxPricipal.getChildren().addAll(labelWelcome,new Label(" "),new Label(" "),hBoxIdPassword,new Label(" "),buttonConectare,new Label(" "),buttonCreareCont);
        vBoxPricipal.setBackground(new Background(new BackgroundFill(new Color(0.0,0.6,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));
        //vBoxPricipal.getStylesheets().add("app.css");
        sceneConectare = new Scene(vBoxPricipal, 250, 250);

        buttonCreareCont.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                stage.setScene(sceneCreareCont);
            }
        });

        buttonConectare.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if(textFieldUsername.getText().isEmpty())
                    MessageAlert.showErrorMessage(null,"Nu ai introdus username-ul!");
                else if(textFieldPassword.getText().isEmpty())
                    MessageAlert.showErrorMessage(null,"Nu ai introdus parola!");
                else {
                    try{
                        Utilizator user = service.findUtilizatorByUsername(textFieldUsername.getText());
                        if (user != null) {
                            if (user.getPassword().equals(textFieldPassword.getText())) {
                                idUserLogat = user.getId();
                                initializeSceneInContViewPrieteni();
                                stage.setScene(sceneInContViewPrieteni);
                                //afisareNotificari();
                                textFieldUsername.clear();
                                textFieldPassword.clear();
                            } else {
                                textFieldPassword.clear();
                                MessageAlert.showErrorMessage(null, "Parola icorecta!");
                                //MessageAlert.showErrorMessage(null,user.getPassword());
                            }
                        } else {
                            textFieldUsername.clear();
                            textFieldPassword.clear();
                            MessageAlert.showErrorMessage(null, "Nu exista utilizator cu acest username!");
                        }
                    }
                    catch(Exception e){
                        //MessageAlert.showErrorMessage(null, "ID-ul trebuie sa fie un numar!");
                        MessageAlert.showErrorMessage(null, e.getMessage());
                    }
                }
            }
        });
    }

    private void initializeSceneCreareCont(){
        Label labelId2 = new Label("Username:  ");
        Label labelPrenume = new Label("Prenume:  ");
        Label labelNume = new Label("Nume:  ");
        Label labelPassword2 = new Label("Parola:  ");
        Label labelConfirmPassword = new Label("Confirmare parola:  ");

        VBox vBoxLabels2 = new VBox();
        vBoxLabels2.setSpacing(12);
        vBoxLabels2.setAlignment(Pos.CENTER_RIGHT);
        vBoxLabels2.getChildren().addAll(labelId2,labelPrenume,labelNume,labelPassword2,labelConfirmPassword);

        TextField textFieldUsername = new TextField();
        TextField textFieldPrenume = new TextField();
        TextField textFieldNume = new TextField();
        PasswordField textFieldPassword2 = new PasswordField();
        PasswordField textFieldConfirmPassword = new PasswordField();

        VBox vBoxTextFields2 = new VBox();
        vBoxTextFields2.setSpacing(4.2);
        vBoxTextFields2.setAlignment(Pos.CENTER);
        vBoxTextFields2.getChildren().addAll(textFieldUsername,textFieldPrenume,textFieldNume,textFieldPassword2,textFieldConfirmPassword);

        HBox hBoxCreareCont = new HBox();
        hBoxCreareCont.setAlignment(Pos.CENTER);
        hBoxCreareCont.getChildren().addAll(vBoxLabels2,new Label(" "),vBoxTextFields2);

        Button buttonInscriere = new Button("Inscrie-te");
        buttonInscriere.setMinWidth(120);
        buttonInscriere.setStyle("-fx-background-color: #00aa00; -fx-text-fill: white;  ");
        buttonInscriere.setFont(Font.font(null ,FontWeight.BOLD,13));

        buttonRenunta = new Button("Renunta");
        buttonRenunta.setMinWidth(120);
        //buttonRenunta.setStyle("-fx-background-color: #cf2000; -fx-text-fill: white;");
        buttonRenunta.setStyle("-fx-background-color: white; -fx-text-fill: #555555; -fx-border-color: #bbbbbb; -fx-border-width: 1px;");
        buttonRenunta.setFont(Font.font(null ,FontWeight.BOLD,13));

        HBox hBoxInscriereRenunta = new HBox();
        hBoxInscriereRenunta.setAlignment(Pos.CENTER);
        hBoxInscriereRenunta.setSpacing(5);
        hBoxInscriereRenunta.getChildren().addAll(buttonInscriere,buttonRenunta);

        VBox vBoxCreareCont = new VBox();
        vBoxCreareCont.setAlignment(Pos.CENTER);
        vBoxCreareCont.setSpacing(30);
        vBoxCreareCont.getChildren().addAll(hBoxCreareCont,hBoxInscriereRenunta);
        vBoxCreareCont.setBackground(new Background(new BackgroundFill(new Color(0.0,0.6,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));

        sceneCreareCont = new Scene(vBoxCreareCont, 300, 250);

        buttonInscriere.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String message = "";
                if(textFieldUsername.getText().isEmpty())
                    message+="Nu ai introdus Username-ul!\n";
                if(textFieldPrenume.getText().isEmpty())
                    message+="Nu ai introdus prenumele!\n";
                if(textFieldNume.getText().isEmpty())
                   message+="Nu ai introdus numele!\n";
                if(textFieldPassword2.getText().isEmpty())
                   message+="Nu ai introdus parola!\n";
                if(textFieldConfirmPassword.getText().isEmpty())
                    message+="Nu ai confirmat parola!\n";
                if(message.length()>0)
                    MessageAlert.showErrorMessage(null,message);
                else if(!textFieldPassword2.getText().equals(textFieldConfirmPassword.getText())) {
                    MessageAlert.showErrorMessage(null, "Confirmare parola esuata!");
                    textFieldPassword2.clear();
                    textFieldConfirmPassword.clear();
                }
                else {
                    try {
                        Utilizator user = new Utilizator(textFieldPrenume.getText(),textFieldNume.getText(),textFieldUsername.getText(),textFieldConfirmPassword.getText());
                        user.setId(1L);
                        service.addUtilizator(user);
                        idUserLogat = service.findUtilizatorByUsername(textFieldUsername.getText()).getId();
                        initializeSceneInContViewPrieteni();
                        stage.setScene(sceneInContViewPrieteni);
                        textFieldUsername.clear();
                        textFieldNume.clear();
                        textFieldPrenume.clear();
                        textFieldConfirmPassword.clear();
                        textFieldPassword2.clear();
                    }
                    catch (Exception e){
                        MessageAlert.showErrorMessage(null, e.getMessage());
                    }
                }
            }
        });

        buttonRenunta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                stage.setScene(sceneConectare);
            }
        });
    }

    private void initializeSceneInContViewPrieteni(){
        UtilizatorController utilizatorController = new UtilizatorController(service,idUserLogat);
        sceneInContViewPrieteni = new Scene(utilizatorController.hbox,600,500);

        utilizatorController.buttonDeconectare.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                vazutNotificari = false;
                stage.setScene(sceneConectare);
            }
        });

        utilizatorController.buttonVeziCereri.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewCereri();
                stage.setScene(sceneInContViewCereri);
            }
        });

        utilizatorController.buttonVeziMesaje.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewMesaje();
                stage.setScene(sceneInContViewMesaje);
                /*MessageController messageController = new MessageController(service,idUserLogat);
                utilizatorController.hbox.getChildren().remove(utilizatorController.vboxTabel);
                utilizatorController.vboxTabel= messageController.vBox;
                utilizatorController.hbox.getChildren().add(utilizatorController.vboxTabel);*/
            }
        });

        utilizatorController.buttonRaportActivitate.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContRaportActivitate();
                stage.setScene(sceneInContRaportActivitate);
            }
        });

        utilizatorController.buttonRaportMesajePrieten.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Utilizator user = utilizatorController.table.getSelectionModel().getSelectedItem();
                if(user!=null) {
                    initializeSceneInContRaportMesajePrieten(user.getId());
                    stage.setScene(sceneInContRaportMesajePrieten);
                }
                else
                    MessageAlert.showErrorMessage(null,"Nu ati selectat niciun utilizator!");
            }
        });

        utilizatorController.buttonVeziEvenimente.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewEvenimente();
                stage.setScene(sceneInContViewEvenimente);
                //EvenimentController messageController = new EvenimentController(service,idUserLogat);
                //utilizatorController.hbox.getChildren().remove(utilizatorController.vboxTabel);
                //utilizatorController.vboxTabel= messageController.vbox;
                //utilizatorController.hbox.getChildren().add(utilizatorController.vboxTabel);
            }
        });

        utilizatorController.buttonNotificari.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewNotificari();
                stage.setScene(sceneInContViewNotificari);
                utilizatorController.buttonNotificari.setText(("Notificari"));
            }
        });
        if(getNotificari().size()>0 && !vazutNotificari)
            utilizatorController.buttonNotificari.setText("Notificari "+"("+getNotificari().size()+")");
    }


    private void initializeSceneInContViewCereri(){
        CerereDePrietenieController cerereDePrietenieController = new CerereDePrietenieController(service,idUserLogat);
        sceneInContViewCereri = new Scene((cerereDePrietenieController.vBox),600,500);

        cerereDePrietenieController.buttonInapoi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewPrieteni();
                stage.setScene(sceneInContViewPrieteni);
            }
        });
    }

    private void initializeSceneInContViewMesaje() {
        MessageController messageController = new MessageController(service, idUserLogat);
        sceneInContViewMesaje = new Scene((messageController.vBox), 600, 500);

        messageController.buttonInapoi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewPrieteni();
                stage.setScene(sceneInContViewPrieteni);
            }
        });
    }

    private void initializeSceneInContRaportActivitate(){
        RaportActivitateController raportActivitateController = new RaportActivitateController(service,idUserLogat);
        sceneInContRaportActivitate = new Scene((raportActivitateController.vBox),600,550);

        raportActivitateController.buttonInapoi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewPrieteni();
                stage.setScene(sceneInContViewPrieteni);
            }
        });
    }

    private void initializeSceneInContRaportMesajePrieten(Long idPrieten){
        RaportMesajePrietenController raportMesajePrietenController = new RaportMesajePrietenController(service,idUserLogat,idPrieten);
        sceneInContRaportMesajePrieten = new Scene((raportMesajePrietenController.vBox),600,550);

        raportMesajePrietenController.buttonInapoi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewPrieteni();
                stage.setScene(sceneInContViewPrieteni);
            }
        });
    }

    private void initializeSceneInContViewEvenimente(){
        EvenimentController evenimentController = new EvenimentController(service,idUserLogat);
        sceneInContViewEvenimente = new Scene((evenimentController.vbox), 600,550);

        evenimentController.buttonInapoi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewPrieteni();
                stage.setScene(sceneInContViewPrieteni);
            }
        });
    }

    private void initializeSceneInContViewNotificari() {
        vazutNotificari = true;
        NotificariController notificariController = new NotificariController(getNotificari());
        sceneInContViewNotificari = new Scene((notificariController.vBox), 600,500);

        notificariController.buttonInapoi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                initializeSceneInContViewPrieteni();
                stage.setScene(sceneInContViewPrieteni);
            }
        });
    }

    private void afisareNotificari(){
        List<Eveniment> evenimente = service.evenimenteUtilizator(idUserLogat);
        evenimente.forEach(e -> {
            if(service.findOneUtilizatorEveniment(idUserLogat,e.getId()).getNotificari()) {
                if (ChronoUnit.MINUTES.between(LocalDateTime.now(), e.getData()) < 30 && ChronoUnit.MINUTES.between(LocalDateTime.now(), e.getData()) >= 10)
                    afisareNotificareEveniment("30 de minute", e);
                else if (ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) < 1 && ChronoUnit.MINUTES.between(LocalDateTime.now(), e.getData()) >= 30)
                    afisareNotificareEveniment("60 de minute", e);
                else if (ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) < 6 && ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) >= 1)
                    afisareNotificareEveniment("6 ore", e);
                else if (ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) < 12 && ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) >= 6)
                    afisareNotificareEveniment("12 ore", e);
                else if (ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) < 24 && ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) >= 12)
                    afisareNotificareEveniment("24 de ore", e);
                else if (ChronoUnit.DAYS.between(LocalDateTime.now(), e.getData()) < 2 && ChronoUnit.DAYS.between(LocalDateTime.now(), e.getData()) >= 1)
                    afisareNotificareEveniment("2 zile", e);
                else if (ChronoUnit.DAYS.between(LocalDateTime.now(), e.getData()) < 3 && ChronoUnit.DAYS.between(LocalDateTime.now(), e.getData()) >= 2)
                    afisareNotificareEveniment("3 zile", e);
            }
        });
    }

    private List<String> getNotificari(){
        List<String> notificari = new ArrayList<>();
        List<Eveniment> evenimente = service.evenimenteUtilizator(idUserLogat);
        evenimente.forEach(e -> {
            if(service.findOneUtilizatorEveniment(idUserLogat,e.getId()).getNotificari()) {
                if (ChronoUnit.MINUTES.between(LocalDateTime.now(), e.getData()) < 30 && ChronoUnit.MINUTES.between(LocalDateTime.now(), e.getData()) >= 10)
                    notificari.add(NotificareEveniment("30 de minute", e));
                else if (ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) < 1 && ChronoUnit.MINUTES.between(LocalDateTime.now(), e.getData()) >= 30)
                    notificari.add(NotificareEveniment("60 de minute", e));
                else if (ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) < 6 && ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) >= 1)
                    notificari.add(NotificareEveniment("6 ore", e));
                else if (ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) < 12 && ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) >= 6)
                    notificari.add(NotificareEveniment("12 ore", e));
                else if (ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) < 24 && ChronoUnit.HOURS.between(LocalDateTime.now(), e.getData()) >= 12)
                    notificari.add(NotificareEveniment("24 de ore", e));
                else if (ChronoUnit.DAYS.between(LocalDateTime.now(), e.getData()) < 2 && ChronoUnit.DAYS.between(LocalDateTime.now(), e.getData()) >= 1)
                    notificari.add(NotificareEveniment("2 zile", e));
                else if (ChronoUnit.DAYS.between(LocalDateTime.now(), e.getData()) < 3 && ChronoUnit.DAYS.between(LocalDateTime.now(), e.getData()) >= 2)
                    notificari.add(NotificareEveniment("3 zile", e));
            }
        });
        return notificari;
    }

    private void afisareNotificareEveniment(String timpRamas, Eveniment eveniment){
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Notificare",
                "Au mai ramas mai putin de "+timpRamas+" pana la inceperea evenimentului \"" + eveniment.getNume() + "\"!\n" +
                        "Acesta are loc pe "+eveniment.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+", la ora " + eveniment.getData().format(DateTimeFormatter.ofPattern("HH:mm")) + ".\n" +
                        "Nu uita sa participi!");
    }

    private String NotificareEveniment(String timpRamas,Eveniment eveniment){
       return "Au mai ramas mai putin de "+timpRamas+" pana la inceperea evenimentului \"" + eveniment.getNume() + "\"!\n" +
                        "Acesta are loc pe "+eveniment.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+", la ora " + eveniment.getData().format(DateTimeFormatter.ofPattern("HH:mm")) + ".\n" +
                        "Nu uita sa participi!";
    }

}
