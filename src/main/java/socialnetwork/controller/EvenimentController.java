package socialnetwork.controller;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import socialnetwork.domain.Eveniment;
import socialnetwork.domain.EvenimentForTable;
import socialnetwork.domain.UtilizatorEveniment;
import socialnetwork.service.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EvenimentController {
    private Service service;
    private Long idUserLogat;
    private boolean viewMyEvents;

    Label labelTableTitle;

    TableView<EvenimentForTable> table;
    TableColumn<EvenimentForTable,Long> columnId;
    TableColumn<EvenimentForTable,String> columnNume;
    TableColumn<EvenimentForTable,String> columnData;

    List<EvenimentForTable> tableList;
    Pagination pagination;
    int tablePageSize;

    Button buttonAbonareDezabonare;
    Button buttonSchimbaTabel;
    Button buttonActiveazaDezactiveazaNotificari;
    Button buttonCreeazaEveniment;
    Button buttonInapoi;

    Label labelCautare;
    TextField textFieldCautare;

    HBox hbox1;
    HBox hbox2;
    HBox hboxCautare;
    VBox vbox;

    public EvenimentController(Service service,Long idUserLogat){
        this.service = service;
        this.idUserLogat = idUserLogat;
        initialize();
    }

    public void initialize(){
        buttonAbonareDezabonare = new Button("Dezabonare");
        buttonSchimbaTabel = new Button("Inscrie-te la un eveniment nou");
        buttonActiveazaDezactiveazaNotificari = new Button("Dezactiveaza notificarile");
        buttonCreeazaEveniment = new Button("Creeaza un eveniment");
        buttonInapoi = new Button("Inapoi");
        buttonInapoi.getStyleClass().add("buttonGreen");
        buttonActiveazaDezactiveazaNotificari.setDisable(true);

        labelCautare = new Label("    Cauta:");
        textFieldCautare = new TextField();
        textFieldCautare.textProperty().addListener(x->handleFilter());


        hboxCautare = new HBox();
        hboxCautare.setSpacing(15);
        hboxCautare.setAlignment(Pos.CENTER_LEFT);
        hboxCautare.getChildren().addAll(new Label("  "),labelCautare,textFieldCautare);

        hbox1 = new HBox();
        hbox1.setSpacing(15);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().addAll(new Label(" "), buttonAbonareDezabonare, buttonActiveazaDezactiveazaNotificari,new Label(" "));

        hbox2 = new HBox();
        hbox2.setSpacing(15);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(new Label(" "), buttonSchimbaTabel ,buttonCreeazaEveniment,new Label("     "), buttonInapoi,new Label(" "));


        pagination = new Pagination();
        pagination.setMaxPageIndicatorCount(3);
        tablePageSize = 9;

        viewMyEvents = true;
        labelTableTitle = new Label("Evenimentele tale");
        labelTableTitle.getStyleClass().add("labelBig");

        service.stergeEvenimenteleExpirate();
        initTable();

        vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.getChildren().addAll(new Label(" "),hboxCautare,labelTableTitle,table,pagination, hbox1,new Label(""),hbox2,new Label(""));
        vbox.setBackground(new Background(new BackgroundFill(new Color(0.0,0.8,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));
        vbox.getStylesheets().add("app.css");

        buttonSchimbaTabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(viewMyEvents == true) {
                    table.getItems().clear();
                    List<Eveniment> events = new ArrayList<Eveniment>();
                    service.getAllEvents().forEach(x -> {
                        if (service.findOneUtilizatorEveniment(idUserLogat, x.getId()) == null)
                            events.add(x);
                    });
                    incarcaTabel(toTable(events));
                    buttonAbonareDezabonare.setText("Inscrie-te");
                    buttonSchimbaTabel.setText("Afiseaza evenimentele tale");
                    viewMyEvents = false;
                    labelTableTitle.setText("Gaseste evenimente");
                }
                else{
                    table.getItems().clear();
                    incarcaTabel(toTable(service.evenimenteUtilizator(idUserLogat)));
                    buttonAbonareDezabonare.setText("Dezabonare");
                    buttonSchimbaTabel.setText("Inscrie-te la evenimente");
                    viewMyEvents = true;
                    labelTableTitle.setText("Evenimentele tale");
                }
            }
        });

        buttonAbonareDezabonare.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                EvenimentForTable eveniment = table.getSelectionModel().getSelectedItem();
                if(viewMyEvents == true) {
                    if(eveniment!=null) {
                        service.dezabonareDeLaEveniment(idUserLogat,eveniment.getId());
                        table.getItems().remove(eveniment);
                    }
                    else{
                        MessageAlert.showErrorMessage(null,"Nu ati selectat niciun eveniment!");
                    }
                }
                else{
                    if(eveniment!=null) {
                        try {
                            service.inscriereLaEveniment(idUserLogat,eveniment.getId());
                            table.getItems().remove(eveniment);
                            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare","Te-ai inscris la eveniment!");
                        }
                        catch (Exception e){
                            MessageAlert.showErrorMessage(null,e.getMessage());
                        }
                    }
                    else{
                        MessageAlert.showErrorMessage(null,"Nu ati selectat niciun eveniment!");
                    }
                }
            }
        });

        buttonCreeazaEveniment.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CreareEvenimentController creareEvenimentController = new CreareEvenimentController(service,idUserLogat);
            }
        });

        buttonActiveazaDezactiveazaNotificari.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                UtilizatorEveniment utilizatorEveniment =  service.findOneUtilizatorEveniment(idUserLogat,table.getSelectionModel().getSelectedItem().getId());
                if(utilizatorEveniment.getNotificari()) {
                    service.setareNotificari(idUserLogat, table.getSelectionModel().getSelectedItem().getId(), false);
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare",
                            "Ai dezactivat notificarile pentru evenimentul \""+table.getSelectionModel().getSelectedItem().getNume()+"\"\n");
                    buttonActiveazaDezactiveazaNotificari.setText("Activeaza notificarile");
                }
                else {
                    service.setareNotificari(idUserLogat, table.getSelectionModel().getSelectedItem().getId(), true);
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare",
                            "Ai activat notificarile pentru evenimentul \""+table.getSelectionModel().getSelectedItem().getNume()+"\"\n");
                    buttonActiveazaDezactiveazaNotificari.setText("Dezactiveaza notificarile");
                }
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(viewMyEvents && newSelection!=null){
                buttonActiveazaDezactiveazaNotificari.setDisable(false);
                UtilizatorEveniment utilizatorEveniment =  service.findOneUtilizatorEveniment(idUserLogat,newSelection.getId());
                if(utilizatorEveniment.getNotificari())
                    buttonActiveazaDezactiveazaNotificari.setText("Dezactiveaza notificarile");
                else
                    buttonActiveazaDezactiveazaNotificari.setText("Activeaza notificarile");
            }
            else
                buttonActiveazaDezactiveazaNotificari.setDisable(true);
        });

        pagination.setPageFactory((pageIndex) -> {
            List<EvenimentForTable> eventsPage = new ArrayList<>();
            for(int i = pageIndex * tablePageSize; i<(pageIndex+1) * tablePageSize && i<tableList.size(); i++) {
                eventsPage.add(tableList.get(i));
            }
            incarcaPaginaInTabel(eventsPage);
            return new VBox();
        });
    }

    public void initTable(){
        table = new TableView<EvenimentForTable>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        columnId = new TableColumn<EvenimentForTable,Long>();
        columnNume = new TableColumn<EvenimentForTable,String>();
        columnData = new TableColumn<EvenimentForTable,String>();

        columnId.setText("ID");
        columnNume.setText("Nume");
        columnData.setText("Data");

        columnData.setMaxWidth(120);
        columnData.setMinWidth(120);
        columnId.setMaxWidth(90);
        columnId.setMinWidth(90);

        table.setMaxWidth(540);
        table.setMaxHeight(315);

        columnId.setCellValueFactory(new PropertyValueFactory<EvenimentForTable,Long>("id"));
        columnNume.setCellValueFactory(new PropertyValueFactory<EvenimentForTable,String>("nume"));
        columnData.setCellValueFactory(new PropertyValueFactory<EvenimentForTable,String>("data"));

        table.getColumns().addAll(columnId, columnNume, columnData);
        incarcaTabel(toTable(service.evenimenteUtilizator(idUserLogat)));

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

    public void incarcaTabel(Iterable<EvenimentForTable> events){
        tableList = (List<EvenimentForTable>)events;
        //incarcaPaginaInTabel(tableList);
        pagination.setPageCount(((Collection<EvenimentForTable>) events).size()/tablePageSize+2); //modificare pageCount; daca nu modifici nu se incarca
        pagination.setPageCount(((Collection<EvenimentForTable>) events).size()/tablePageSize+1);
        pagination.setCurrentPageIndex(0);
    }

    public void incarcaPaginaInTabel(Iterable<EvenimentForTable> events){
        table.getItems().clear();
        table.getItems().addAll((Collection<EvenimentForTable>) events);
    }

    private void handleFilter() {
        Predicate<EvenimentForTable> nameFilter = x->x.getNume().toLowerCase().startsWith(textFieldCautare.getText().toLowerCase());
        if (!viewMyEvents) {
            List<EvenimentForTable> events = new ArrayList<EvenimentForTable>();
            toTable((Collection<Eveniment>)service.getAllEvents()).forEach(x -> {
                if (service.findOneUtilizatorEveniment(idUserLogat, x.getId()) == null)
                    events.add(x);
            });
          incarcaTabel(events.
                    stream().
                    filter(nameFilter).
                    collect(Collectors.toList())
            );
        }
        else
            incarcaTabel(toTable(service.evenimenteUtilizator(idUserLogat)).
                    stream().
                    filter(nameFilter).
                    collect(Collectors.toList())
            );
    }

    private List<EvenimentForTable> toTable(Collection<Eveniment> evenimente){
        List<EvenimentForTable> eventsForTable = new ArrayList<>();
        evenimente.forEach(e->{
            eventsForTable.add(new EvenimentForTable(e.getId(),e.getNume(),e.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
        });
        return  eventsForTable;
    }
}
