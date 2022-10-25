package socialnetwork.controller;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import socialnetwork.domain.CerereDePrietenieForTable;
import socialnetwork.domain.CerereDePrietenieStatus;
import socialnetwork.service.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CerereDePrietenieController {
    private Service service;
    private Long idUserLogat;

    VBox vBox;
    Label label;
    TableView<CerereDePrietenieForTable> table;
    HBox hBox;
    Button buttonRetrageCerere;
    Button buttonAcceptaCerere;
    Button buttonRespingeCerere;
    Button buttonInapoi;

    List<CerereDePrietenieForTable> tableList;
    Pagination pagination;
    int tablePageSize;

    public CerereDePrietenieController(Service service, Long idUserLogat){
        this.service = service;
        this.idUserLogat = idUserLogat;
        initialize();
    }

    void initialize(){
        label = new Label("Cererile tale");
        label.getStyleClass().add("labelBig");

        buttonInapoi = new Button("Inapoi");
        buttonInapoi.getStyleClass().add("buttonGreen");
        buttonRetrageCerere = new Button("Retrage cererea");
        buttonRetrageCerere.setDisable(true);
        buttonAcceptaCerere = new Button("Accepta cererea");
        buttonAcceptaCerere.setDisable(true);
        buttonRespingeCerere = new Button("Respinge cererea");
        buttonRespingeCerere.setDisable(true);

        pagination = new Pagination();
        pagination.setMaxPageIndicatorCount(3);
        tablePageSize = 12;
        initTable();

        hBox = new HBox();
        hBox.setSpacing(15);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(buttonRetrageCerere,buttonAcceptaCerere,buttonRespingeCerere,new Label("       "),buttonInapoi);

        vBox = new VBox();
        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label,table,pagination,hBox,new Label(""));

        vBox.setBackground(new Background(new BackgroundFill(new Color(0.0,0.8,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.getStylesheets().add("app.css");

        buttonRetrageCerere.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CerereDePrietenieForTable cerere = table.getSelectionModel().getSelectedItem();
                service.deleteCerereDePrietenie(cerere.getId());
                table.getItems().remove(cerere);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare","Cerere de prietenie retrasa!");
            }
        });

        buttonAcceptaCerere.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CerereDePrietenieForTable cerere = table.getSelectionModel().getSelectedItem();
                service.raspundeLaCerereDePrietenie(idUserLogat,cerere.getId(),true);
                table.getItems().clear();
                incarcaTabel(service.cereriDePrietenieForTable(idUserLogat));
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare","Cerere de prietenie acceptata!\nAcum sunteti prieteni!");
            }
        });

        buttonRespingeCerere.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CerereDePrietenieForTable cerere = table.getSelectionModel().getSelectedItem();
                service.raspundeLaCerereDePrietenie(idUserLogat,cerere.getId(),false);
                table.getItems().clear();
                incarcaTabel(service.cereriDePrietenieForTable(idUserLogat));
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Informare","Cerere de prietenie respinsa!");
            }
        });

        pagination.setPageFactory((pageIndex) -> {
            List<CerereDePrietenieForTable> cereriPage = new ArrayList<>();
            for(int i = pageIndex* tablePageSize; i<(pageIndex+1)* tablePageSize && i<tableList.size(); i++)
                cereriPage.add(tableList.get(i));
            incarcaPaginaInTabel(cereriPage);
            return new VBox();
        });
    }

    void initTable(){
        table = new TableView<CerereDePrietenieForTable>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<CerereDePrietenieForTable, String> columnExpeditor = new TableColumn<CerereDePrietenieForTable, String>();
        TableColumn<CerereDePrietenieForTable, String> columnDestinatar = new TableColumn<CerereDePrietenieForTable, String>();
        TableColumn<CerereDePrietenieForTable, CerereDePrietenieStatus> columnStatus = new TableColumn<CerereDePrietenieForTable, CerereDePrietenieStatus>();
        TableColumn<CerereDePrietenieForTable, String> columnData = new TableColumn<CerereDePrietenieForTable, String>();

        columnExpeditor.setText("Expeditor");
        columnDestinatar.setText("Destinatar");
        columnStatus.setText("Status");
        columnData.setText("Data");

        columnStatus.setMaxWidth(90);
        columnStatus.setMinWidth(90);
        columnData.setMaxWidth(120);
        columnData.setMinWidth(120);

        table.setMaxWidth(540);
        table.setMaxHeight(315);

        columnExpeditor.setCellValueFactory(new PropertyValueFactory<CerereDePrietenieForTable,String>("expeditor"));
        columnDestinatar.setCellValueFactory(new PropertyValueFactory<CerereDePrietenieForTable,String>("destinatar"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<CerereDePrietenieForTable,CerereDePrietenieStatus>("status"));
        columnData.setCellValueFactory(new PropertyValueFactory<CerereDePrietenieForTable,String>("data"));

        table.getColumns().addAll(columnExpeditor,columnDestinatar,columnStatus,columnData);
        table.getItems().clear();
        incarcaTabel(service.cereriDePrietenieForTable(idUserLogat));

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                CerereDePrietenieForTable cerere = table.getSelectionModel().getSelectedItem();
                if (cerere.getStatus().equals(CerereDePrietenieStatus.PENDING) && service.findOneCerereDePrietenie(cerere.getId()).getIdExpeditor() == idUserLogat)
                    buttonRetrageCerere.setDisable(false);
                else
                    buttonRetrageCerere.setDisable(true);
                if (cerere.getStatus().equals(CerereDePrietenieStatus.PENDING) && service.findOneCerereDePrietenie(cerere.getId()).getIdDestinatar() == idUserLogat){
                    buttonAcceptaCerere.setDisable(false);
                    buttonRespingeCerere.setDisable(false);
                }
                else {
                    buttonAcceptaCerere.setDisable(true);
                    buttonRespingeCerere.setDisable(true);
                }
            }
        });
    }

    public void incarcaTabel(Iterable<CerereDePrietenieForTable> cereri){
        tableList = (List<CerereDePrietenieForTable>)cereri;
        //incarcaPaginaInTabel(tableList);
        pagination.setPageCount(((Collection<CerereDePrietenieForTable>) cereri).size()/tablePageSize+2); //modificare pageCount; daca nu modifici nu se incarca
        pagination.setPageCount(((Collection<CerereDePrietenieForTable>) cereri).size()/tablePageSize+1);
        pagination.setCurrentPageIndex(0);
    }

    public void incarcaPaginaInTabel(Iterable<CerereDePrietenieForTable> cereri){
        table.getItems().clear();
        table.getItems().addAll((Collection<CerereDePrietenieForTable>) cereri);
    }
}
