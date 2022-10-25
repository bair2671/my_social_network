package socialnetwork.controller;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import socialnetwork.domain.MessageForTable;
import socialnetwork.domain.Utilizator;
import socialnetwork.pdf.GeneratorPDF;
import socialnetwork.service.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RaportMesajePrietenController {
    private Service service;
    private Long idUserLogat;
    private Long idPrieten;

    Label label;

    TableView<MessageForTable> table;

    List<MessageForTable> tableList;
    Pagination pagination;
    int tablePageSize;

    HBox hBox;
    HBox hBoxDataStart;
    HBox hBoxDataEnd;

    VBox vBox;

    Button buttonGenerarePDF;
    Button buttonInapoi;
    Button buttonGenerareRaport;

    DatePicker datePickerStart;
    DatePicker datePickerEnd;

    public RaportMesajePrietenController(Service service, Long idUserLogat , Long idPrieten){
        this.service = service;
        this.idUserLogat = idUserLogat;
        this.idPrieten = idPrieten;
        initialize();
    }

    void initialize(){
        Utilizator prieten = service.findOneUtilizator(idPrieten);
        label = new Label("Raport mesaje primite de la "+prieten.getFirstName()+" "+prieten.getLastName());
        label.getStyleClass().add("labelBig");

        buttonInapoi = new Button("Inapoi");
        buttonInapoi.getStyleClass().add("buttonGreen");
        buttonGenerarePDF = new Button("Salveaza in PDF");
        buttonGenerareRaport = new Button("Genereaza raportul");

        datePickerStart = new DatePicker();
        datePickerEnd = new DatePicker();

        hBoxDataStart = new HBox();
        hBoxDataStart.setSpacing(10);
        hBoxDataStart.setAlignment(Pos.CENTER);
        hBoxDataStart.getChildren().addAll(new Label("Data de inceput: "),datePickerStart);

        hBoxDataEnd = new HBox();
        hBoxDataEnd.setSpacing(10);
        hBoxDataEnd.setAlignment(Pos.CENTER);
        hBoxDataEnd.getChildren().addAll(new Label("   Data de sfarsit: "),datePickerEnd);

        pagination = new Pagination();
        pagination.setMaxPageIndicatorCount(3);
        pagination.setPageCount(1);
        tablePageSize = 7;
        initTable();

        hBox = new HBox();
        hBox.setSpacing(150);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(buttonGenerarePDF,buttonInapoi);

        vBox = new VBox();
        vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(new Label(""),label, hBoxDataStart, hBoxDataEnd,buttonGenerareRaport,table,pagination,hBox,new Label(""));

        vBox.setBackground(new Background(new BackgroundFill(new Color(0.0,0.8,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.getStylesheets().add("app.css");

        buttonGenerarePDF.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(table.getItems().isEmpty())
                    try{
                        if(table.getPlaceholder().isManaged()) {
                            LocalDate start = datePickerStart.getValue();
                            LocalDate end = datePickerEnd.getValue();
                            GeneratorPDF.generarePdfMesaje(service.findOneUtilizator(idUserLogat),service.findOneUtilizator(idPrieten),tableList,start,end);
                            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare","Raportul a fost salvat cu succes in format PDF");
                        }
                    }
                    catch(Exception e){
                        MessageAlert.showErrorMessage(null,"Nu ati generat un raport!");
                    }
                else{
                    LocalDate start = datePickerStart.getValue();
                    LocalDate end = datePickerEnd.getValue();
                    GeneratorPDF.generarePdfMesaje(service.findOneUtilizator(idUserLogat),service.findOneUtilizator(idPrieten),tableList,start,end);
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare","Raportul a fost salvat cu succes in format PDF");

                }
            }
        });

        buttonGenerareRaport.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    LocalDate start = datePickerStart.getValue();
                    LocalDate end = datePickerEnd.getValue();
                    table.getItems().clear();
                    incarcaTabel(service.mesajePrimiteDeLaPrietenPerioadaForTable(idUserLogat,idPrieten,LocalDateTime.of(start, LocalTime.of(0,0)),LocalDateTime.of(end,LocalTime.of(23,59))));
                    if(table.getItems().isEmpty())
                        table.setPlaceholder(new Label("Nu exista mesaje primite de la acest utilizator in aceasta perioada!"));
                }
                catch(Exception e){
                    if(e.getMessage().equals("Data de sfarsit nu poate sa fie dupa data de inceput!"))
                        MessageAlert.showErrorMessage(null,e.getMessage());
                    else
                        MessageAlert.showErrorMessage(null,"Date invalide!");
                }
            }
        });

        pagination.setPageFactory((pageIndex) -> {
            if(tableList!=null) {
                List<MessageForTable> messagesPage = new ArrayList<>();
                for (int i = pageIndex * tablePageSize; i < (pageIndex + 1) * tablePageSize && i < tableList.size(); i++)
                    messagesPage.add(tableList.get(i));
                incarcaPaginaInTabel(messagesPage);
            }
            return new VBox();
        });
    }

    void initTable(){
        table = new TableView<MessageForTable>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MessageForTable,String> columnText = new TableColumn<MessageForTable, String>();
        TableColumn<MessageForTable,LocalDateTime> columnData = new TableColumn<MessageForTable, LocalDateTime>();

        columnText.setText("Text");
        columnData.setText("Data");

        columnData.setMaxWidth(120);
        columnData.setMinWidth(120);

        table.setMaxWidth(540);
        table.setMaxHeight(315);

        columnText.setCellValueFactory(new PropertyValueFactory<MessageForTable, String>("text"));
        columnData.setCellValueFactory(new PropertyValueFactory<MessageForTable, LocalDateTime>("data"));

        table.getColumns().addAll(columnText, columnData);
        table.getItems().clear();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                MessageForTable message = table.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void incarcaTabel(Iterable<MessageForTable> messages){
        tableList = (List<MessageForTable>)messages;
        //incarcaPaginaInTabel(tableList);
        pagination.setPageCount(((Collection<MessageForTable>) messages).size()/tablePageSize+2); //modificare pageCount; daca nu modifici nu se incarca
        pagination.setPageCount(((Collection<MessageForTable>) messages).size()/tablePageSize+1);
        pagination.setCurrentPageIndex(0);
    }

    public void incarcaPaginaInTabel(Iterable<MessageForTable> messages){
        table.getItems().clear();
        table.getItems().addAll((Collection<MessageForTable>) messages);
    }
}
