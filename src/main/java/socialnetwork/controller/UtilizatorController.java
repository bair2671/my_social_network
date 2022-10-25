package socialnetwork.controller;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class UtilizatorController {
    private Service service;
    private Long idUserLogat;
    private boolean viewFriends;

    Label labelTableTitle;

    TableView<Utilizator> table;
    TableColumn<Utilizator,String> columnUsername;
    TableColumn<Utilizator,String> columnPrenume;
    TableColumn<Utilizator,String> columnNume;

    List<Utilizator> tableList;
    int tableListSize;
    Pagination pagination;
    int tablePageSize;

    Button buttonStergeAdauga;
    Button buttonAddNewFriend;
    Button buttonDeconectare;
    Button buttonVeziCereri;
    Button buttonVeziEvenimente;
    Button buttonTrimiteMesaj;
    Button buttonVeziMesaje;
    Button buttonRaportActivitate;
    Button buttonRaportMesajePrieten;
    Button buttonNotificari;

    Label labelCautare;
    TextField textFieldCautare;

    HBox hbox1;
    HBox hboxButoaneTabel;
    HBox hboxCautare;
    VBox vboxTabel;
    VBox vboxMeniu;
    HBox hbox;

    public UtilizatorController(Service service,Long idUserLogat){
        this.service = service;
        this.idUserLogat = idUserLogat;
        initialize();
    }

    public void initialize(){
        buttonDeconectare = new Button("Deconectare");

        buttonStergeAdauga = new Button("Sterge");
        buttonAddNewFriend = new Button("Adauga prieteni noi");
        buttonVeziCereri = new Button("Cereri de prietenie");

        buttonVeziEvenimente = new Button("Evenimente");
        buttonTrimiteMesaj = new Button("Trimite mesaj");
        buttonVeziMesaje = new Button("Mesaje");
        buttonRaportActivitate = new Button("Raport activitate");
        buttonRaportMesajePrieten = new Button("Raport Mesaje");
        buttonNotificari = new Button("Notificari");

        buttonNotificari.setMinWidth(130);
        buttonVeziEvenimente.setMinWidth(130);
        buttonVeziMesaje.setMinWidth(130);
        buttonVeziCereri.setMinWidth(130);
        buttonRaportMesajePrieten.setMinWidth(130);
        buttonRaportActivitate.setMinWidth(130);
        initializeButtons();

        labelCautare = new Label("Cauta:");
        textFieldCautare = new TextField();
        textFieldCautare.textProperty().addListener(x->handleFilter());


        hboxCautare = new HBox();
        hboxCautare.setSpacing(5);
        hboxCautare.setAlignment(Pos.CENTER_LEFT);
        hboxCautare.getChildren().addAll(new Label("  "),labelCautare,textFieldCautare);

        Utilizator user = service.findOneUtilizator(idUserLogat);
        hbox1 = new HBox();
        hbox1.setSpacing(40);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().addAll(new Label("Utilizator  : "+user.getFirstName()+" "+user.getLastName()+"    \n" +
                "Username : "+user.getUsername()),new Label("      "),buttonDeconectare);
        hbox1.setMinHeight(40);

        hboxButoaneTabel = new HBox();
        hboxButoaneTabel.setSpacing(15);
        hboxButoaneTabel.setAlignment(Pos.CENTER);
        hboxButoaneTabel.getChildren().addAll(new Label(" "),buttonStergeAdauga,buttonTrimiteMesaj,new Label(" "),buttonAddNewFriend,new Label(" "));


        pagination = new Pagination();
        pagination.setMaxPageIndicatorCount(3);
        pagination.getStyleClass().add("pagination");
        tablePageSize = 8;

        viewFriends = true;
        labelTableTitle = new Label("Prietenii tai");
        labelTableTitle.getStyleClass().add("labelBig");
        initTable();

        vboxTabel = new VBox();
        vboxTabel.setAlignment(Pos.CENTER);
        vboxTabel.getChildren().addAll(hbox1,new Label(" "),hboxCautare,new Label(" "),labelTableTitle,table,pagination,new Label(""), hboxButoaneTabel,new Label(""),new Label(""),new Label(""));

        vboxMeniu = new VBox();
        vboxMeniu.setAlignment(Pos.CENTER);
        vboxMeniu.setSpacing(20);
        vboxMeniu.getChildren().addAll(buttonVeziCereri,buttonVeziMesaje,buttonVeziEvenimente,buttonNotificari,buttonRaportActivitate,buttonRaportMesajePrieten);

        hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(50);
        hbox.getChildren().addAll(vboxMeniu,vboxTabel);

        hbox.setBackground(new Background(new BackgroundFill(new Color(0.0,0.8,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));
        hbox.getStylesheets().add("app.css");

        buttonAddNewFriend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(viewFriends==true) {
                    table.getItems().clear();
                    List<Utilizator> users = new ArrayList<Utilizator>();
                    service.getAllUsers().forEach(x -> {
                        if (service.findOnePrietenie(idUserLogat, x.getId()) == null && x.getId() != idUserLogat)
                            users.add(x);
                    });
                    viewFriends=false;
                    incarcaTabel(users);
                    buttonStergeAdauga.setText("Adauga");
                    buttonAddNewFriend.setText("Afiseaza prietenii");
                    labelTableTitle.setText("Gaseste prieteni");
                }
                else{
                    table.getItems().clear();
                    viewFriends=true;
                    incarcaTabel(service.getUserFriends(idUserLogat));
                    buttonStergeAdauga.setText("Sterge");
                    buttonAddNewFriend.setText("Adauga prieteni noi");
                    labelTableTitle.setText("Prietenii tai");
                }
            }
        });

        buttonStergeAdauga.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Utilizator user = table.getSelectionModel().getSelectedItem();
                if(viewFriends==true) {
                    if(user!=null) {
                        service.deletePrietenie(idUserLogat, user.getId());
                        table.getItems().remove(user);
                    }
                    else{
                        MessageAlert.showErrorMessage(null,"Nu ati selectat niciun utilizator!");
                    }
                }
                else{
                    if(user!=null) {
                        try {
                            service.addCerereDePrietenie(1L, idUserLogat, user.getId());
                            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare","Cerere de prietenie trimisa!");
                        }
                        catch (Exception e){
                            MessageAlert.showErrorMessage(null,e.getMessage());
                        }
                    }
                    else{
                        MessageAlert.showErrorMessage(null,"Nu ati selectat niciun utilizator!");
                    }
                }
            }
        });

        buttonTrimiteMesaj.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Utilizator user = table.getSelectionModel().getSelectedItem();
                if(user!=null) {
                    TrimitereMesajController trimitereMesajController = new TrimitereMesajController(service,idUserLogat,user.getId());
                }
                else{
                    MessageAlert.showErrorMessage(null,"Nu ati selectat niciun utilizator!");
                }
            }
        });

        pagination.setPageFactory((pageIndex) -> {
            List<Utilizator> usersPage = new ArrayList<>();
            for(int i = pageIndex * tablePageSize; i<(pageIndex+1) * tablePageSize && i<tableList.size(); i++)
                usersPage.add(tableList.get(i));
            incarcaPaginaInTabel(usersPage);
            /*if(viewFriends)
                incarcaPaginaInTabel(service.getPageUserFriends(idUserLogat,pageIndex*tablePageSize,tablePageSize));
            else
                incarcaPaginaInTabel(service.getPageUsers(pageIndex*tablePageSize,tablePageSize));*/
            return new VBox();
        });

    }

    public void initTable(){
        table = new TableView<Utilizator>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMinWidth(360);

        columnUsername = new TableColumn<Utilizator,String>();
        columnPrenume = new TableColumn<Utilizator,String>();
        columnNume = new TableColumn<Utilizator,String>();

        columnUsername.setText("Username");
        columnPrenume.setText("Prenume");
        columnNume.setText("Nume");

        columnUsername.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("username"));
        columnPrenume.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("firstName"));
        columnNume.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("lastName"));

        table.getColumns().addAll(columnUsername,columnPrenume,columnNume);
        incarcaTabel(service.getUserFriends(idUserLogat));

        /*table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Utilizator user = table.getSelectionModel().getSelectedItem();
                if()
                    buttonStergeAdauga.setDisable(false);
                else
                    buttonStergeAdauga.setDisable(true);
            }
        });*/

    }

    public void incarcaTabel(Iterable<Utilizator> users){
        tableList = (List<Utilizator>)users;
        tableListSize = ((Collection<Utilizator>)users).size();
        pagination.setPageCount(tableListSize/tablePageSize+2); //modificare pageCount; daca nu modifici nu se incarca
        pagination.setPageCount(tableListSize/tablePageSize+1);
        pagination.setCurrentPageIndex(0);
    }

    public void incarcaPaginaInTabel(Iterable<Utilizator> users){
        table.getItems().setAll((Collection<Utilizator>) users);
    }

    private void handleFilter() {
        Predicate<Utilizator> nameFilter = x->x.getFirstName().toLowerCase().startsWith(textFieldCautare.getText().toLowerCase()) ||
                x.getLastName().toLowerCase().startsWith(textFieldCautare.getText().toLowerCase());
        if (!viewFriends) {
            List<Utilizator> users = new ArrayList<Utilizator>();
            service.getAllUsers().forEach(x -> {
                if (service.findOnePrietenie(idUserLogat, x.getId()) == null && x.getId() != idUserLogat && !service.cereriDePrieteniePrimite(idUserLogat).contains(x))
                    users.add(x);
            });
            incarcaTabel(users.
                    stream().
                    filter(nameFilter).
                    collect(Collectors.toList())
            );
        }
        else
            incarcaTabel(service.getUserFriends(idUserLogat).
                stream().
                filter(nameFilter).
                collect(Collectors.toList())
        );
    }

    private void setButtonStyle(Button button, String color){
        if(color.equals("blue"))
            button.getStyleClass().add("buttonBlue");
        if(color.equals("green"))
            button.getStyleClass().add("buttonGreen");
        if(color.equals("white"))
            button.getStyleClass().add("buttonWhite");
    }

    private void initializeButtons(){
        buttonDeconectare = new Button("Deconectare");

        buttonStergeAdauga = new Button("Sterge");
        buttonAddNewFriend = new Button("Adauga prieteni noi");
        buttonVeziCereri = new Button("Cereri de prietenie");

        buttonVeziEvenimente = new Button("Evenimente");
        buttonTrimiteMesaj = new Button("Trimite mesaj");
        buttonVeziMesaje = new Button("Mesaje");
        buttonRaportActivitate = new Button("Raport activitate");
        buttonRaportMesajePrieten = new Button("Raport Mesaje");
        buttonNotificari = new Button("Notificari");

        buttonNotificari.setMinWidth(130);
        buttonVeziEvenimente.setMinWidth(130);
        buttonVeziMesaje.setMinWidth(130);
        buttonVeziCereri.setMinWidth(130);
        buttonRaportMesajePrieten.setMinWidth(130);
        buttonRaportActivitate.setMinWidth(130);

        setButtonStyle(buttonDeconectare,"green");

        setButtonStyle(buttonStergeAdauga,"blue");
        setButtonStyle(buttonAddNewFriend,"blue");
        setButtonStyle(buttonTrimiteMesaj,"blue");

        setButtonStyle(buttonVeziCereri,"blue");
        setButtonStyle(buttonVeziMesaje,"blue");
        setButtonStyle(buttonVeziEvenimente,"blue");
        setButtonStyle(buttonNotificari,"blue");
        setButtonStyle(buttonRaportMesajePrieten,"blue");
        setButtonStyle(buttonRaportActivitate,"blue");
    }
}
