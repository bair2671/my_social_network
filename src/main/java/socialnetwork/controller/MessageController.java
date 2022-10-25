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
import socialnetwork.service.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageController {
    private Service service;
    private Long idUserLogat;
    private boolean viewPrimite;

    VBox vBox;
    Label label;
    HBox hBox;
    Button buttonRaspunde;
    Button buttonTrimisePrimite;
    Button buttonInapoi;

    TableView<MessageForTable> table;
    TableColumn<MessageForTable, String> columnExpeditor;
    TableColumn<MessageForTable, String> columnDestinatar;
    TableColumn<MessageForTable, String> columnText;
    TableColumn<MessageForTable, String> columnData;

    List<MessageForTable> tableList;
    Pagination pagination;
    int tablePageSize;

    public MessageController(Service service, Long idUserLogat){
        this.service = service;
        this.idUserLogat = idUserLogat;
        initialize();
    }

    void initialize(){
        label = new Label("Mesaje primite");
        label.getStyleClass().add("labelBig");

        buttonInapoi = new Button("Inapoi");
        buttonInapoi.getStyleClass().add("buttonGreen");
        buttonTrimisePrimite = new Button("Mesaje trimise");
        viewPrimite = true;

        pagination = new Pagination();
        pagination.setMaxPageIndicatorCount(3);
        tablePageSize = 12;
        initTable();

        hBox = new HBox();
        hBox.setSpacing(150);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(buttonTrimisePrimite,buttonInapoi);

        vBox = new VBox();
        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label,table,pagination,hBox,new Label(""));

        vBox.setBackground(new Background(new BackgroundFill(new Color(0.0,0.8,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.getStylesheets().add("app.css");

        buttonTrimisePrimite.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(viewPrimite){
                    buttonTrimisePrimite.setText("Mesaje primite");
                    label.setText("Mesaje trimise");
                }
                else{
                    buttonTrimisePrimite.setText("Mesaje Trimise");
                    label.setText("Mesaje primite");
                }
                modifyTable();
                viewPrimite=!viewPrimite;
            }
        });

        pagination.setPageFactory((pageIndex) -> {
            List<MessageForTable> messagesPage = new ArrayList<>();
            for(int i = pageIndex* tablePageSize; i<(pageIndex+1)* tablePageSize && i<tableList.size(); i++)
                messagesPage.add(tableList.get(i));
            incarcaPaginaInTabel(messagesPage);
            return new VBox();
        });
    }

    void modifyTable(){
        if(viewPrimite){
            table.getColumns().clear();
            table.getColumns().addAll(columnDestinatar, columnText, columnData);
            table.getItems().clear();
            incarcaTabel(service.mesajeTrimiseForTable(idUserLogat));
        }
        else{
            table.getColumns().clear();
            table.getColumns().addAll(columnExpeditor, columnText, columnData);
            table.getItems().clear();
            incarcaTabel(service.mesajePrimiteForTable(idUserLogat));
        }
    }

    void initTable() {
        table = new TableView<MessageForTable>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        columnExpeditor = new TableColumn<MessageForTable, String>();
        columnDestinatar = new TableColumn<MessageForTable, String>();
        columnText = new TableColumn<MessageForTable, String>();
        columnData = new TableColumn<MessageForTable, String>();

        columnExpeditor.setText("De la");
        columnDestinatar.setText("Catre");
        columnText.setText("Text");
        columnData.setText("Data");

        columnExpeditor.setCellValueFactory(new PropertyValueFactory<MessageForTable, String>("expeditor"));
        columnDestinatar.setCellValueFactory(new PropertyValueFactory<MessageForTable, String>("destinatar"));
        columnText.setCellValueFactory(new PropertyValueFactory<MessageForTable, String>("text"));
        columnData.setCellValueFactory(new PropertyValueFactory<MessageForTable, String>("data"));

        columnData.setMaxWidth(120);
        columnData.setMinWidth(120);
        columnExpeditor.setMaxWidth(180);
        columnExpeditor.setMinWidth(180);
        columnDestinatar.setMaxWidth(180);
        columnDestinatar.setMinWidth(180);

        table.setMaxWidth(540);
        table.setMaxHeight(315);

        table.getColumns().addAll(columnExpeditor, columnText, columnData);
        table.getItems().clear();
        incarcaTabel(service.mesajePrimiteForTable(idUserLogat));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                MessageForTable message = table.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void incarcaTabel(Iterable<MessageForTable> messages){
        tableList = (List<MessageForTable>)messages;
        //incarcaPaginaInTabel(tableList);
        pagination.setPageCount(((Collection<MessageForTable>) messages).size()/tablePageSize+2);  //modificare PageCount; daca nu modifici nu se incarca
        pagination.setPageCount(((Collection<MessageForTable>) messages).size()/tablePageSize+1);
        pagination.setCurrentPageIndex(0);
    }

    public void incarcaPaginaInTabel(Iterable<MessageForTable> messages){
        table.getItems().clear();
        table.getItems().addAll((Collection<MessageForTable>) messages);
    }

}
