package socialnetwork.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotificariController {
    VBox vBox;
    Label label;
    HBox hBox;
    Button buttonInapoi;

    TableView<String> table;
    TableColumn<String, String> column;

    List<String> tableList;
    Pagination pagination;
    int tablePageSize;

    public NotificariController(List<String> notificari){
        tableList = notificari;
        initialize();
    }

    void initialize(){
        label = new Label("Notificari");

        buttonInapoi = new Button("Inapoi");
        buttonInapoi.getStyleClass().add("buttonGreen");

        pagination = new Pagination();
        pagination.setMaxPageIndicatorCount(3);
        tablePageSize = 5;
        initTable();

        hBox = new HBox();
        hBox.setSpacing(150);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(buttonInapoi);

        vBox = new VBox();
        vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(table,pagination,hBox);

        vBox.setBackground(new Background(new BackgroundFill(new Color(0.0,0.8,1.0,0.0), CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.getStylesheets().add("app.css");

        pagination.setPageFactory((pageIndex) -> {
            List<String> notificariPage = new ArrayList<>();
            for(int i = pageIndex* tablePageSize; i<(pageIndex+1)* tablePageSize && i<tableList.size(); i++)
                notificariPage.add(tableList.get(i));
            incarcaPaginaInTabel(notificariPage);
            return new VBox();
        });
    }

    void modifyTable(){
        table.getColumns().clear();
        table.getColumns().addAll(column);
        table.getItems().clear();
        incarcaTabel(tableList);
    }

    void initTable() {
        table = new TableView<String>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        column = new TableColumn<String, String>();
        column.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        column.setText("Notificari");

        table.setMaxWidth(540);
        table.setMaxHeight(315);

        table.getColumns().addAll(column);
        table.getItems().clear();
        incarcaTabel(tableList);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String notificare = table.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void incarcaTabel(Iterable<String> notificari){
        //incarcaPaginaInTabel(tableList);
        pagination.setPageCount(((Collection<String>) notificari).size()/tablePageSize+2);  //modificare PageCount; daca nu modifici nu se incarca
        pagination.setPageCount(((Collection<String>) notificari).size()/tablePageSize+1);
        pagination.setCurrentPageIndex(0);
    }

    public void incarcaPaginaInTabel(Iterable<String> notificari){
        table.getItems().clear();
        table.getItems().addAll((Collection<String>) notificari);
    }

}
