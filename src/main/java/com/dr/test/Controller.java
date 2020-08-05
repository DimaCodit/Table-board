package com.dr.test;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableColumn.CellDataFeatures;

import java.lang.reflect.Array;
import java.util.*;

public class Controller {
    //private ObservableList<List> itemsData = FXCollections.observableArrayList();
    private Settings settings;
    private Boolean settingsMode;

    @FXML
    private TableView table;
    @FXML
    private StackPane stackPane;
    @FXML
    private TableColumn sampleColumn;
    @FXML
    private ContextMenuFix columnMenu;
    @FXML
    private Button addButton;
    @FXML
    private Button exportButton;
    @FXML
    private Button importButton;
    @FXML
    private Button closeButton;
    @FXML
    private ToolBar settingsPanel;
    @FXML
    private TextField portField;
    private String fontSize;
    private String theme;

    private TableColumn getColumnFromEvent(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        TableColumnHeader tableColumnHeader = (TableColumnHeader) source.getParentPopup().getOwnerNode();
        TableColumn tableColumn = (TableColumn) tableColumnHeader.getTableColumn();
        return tableColumn;
    }
    public ToolBar getSettingsPanel() {
        return settingsPanel;
    }

    public void clear() {
      //  itemsData.clear();
    }

    public int getPort(){
        return settings.getPort();
    }

    @FXML
    private void closePanel(ActionEvent event) {
        saveSettings();
        settingsPanel.setVisible(false);
        settingsMode = false;
    }

    private void saveSettings() {
        settings.setColumnsSettings(table.getColumns());
        settings.setPort(Integer.valueOf(portField.getText()));
        settings.setFontSize(fontSize);
        settings.setTheme(theme);
        settings.saveProps();
    }

    @FXML
    private void renameColumn(ActionEvent event) {
        TableColumn tableColumn = getColumnFromEvent(event);
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setHeaderText(null);
        dialog.setContentText("Имя колонки:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            tableColumn.setText(name);
        });
    }
    @FXML
    private void delColumn(ActionEvent event) {
        TableColumn tableColumn = getColumnFromEvent(event);
        table.getColumns().remove(tableColumn);
    }
    @FXML
    private void addColumn(ActionEvent event) throws CloneNotSupportedException {
        addColumnWithName("Новая колонка", 100);
    }

    private void addColumnWithName(String name, double width)  {

        int countCols = table.getColumns().size() - 1;
        TableColumn column = new TableColumn<>(name);
        ContextMenuFix menu = columnMenu;
        menu.setTableColumn(column);
        column.setContextMenu(menu);
        column.setPrefWidth(width);
        column.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(countCols).toString());
            }
        });
        table.getColumns().add(column);
    }
    @FXML
    private void initialize() {
//        KeyCombination kc = new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN);
//        Mnemonic mn = new Mnemonic(closeButton, kc);

    }

    public void init(Stage stage, Scene scene) throws CloneNotSupportedException {
        setAc(scene);
        setAccToFullScreen(stage, scene);
        settingsMode = settingsPanel.isVisible();
        table.setPlaceholder(new Label(""));
        settings = new Settings();
        setSettings();
    }

    private void setSettings()  {
        
        ObservableList columnsList = table.getColumns();
        columnsList.remove(1, columnsList.size());

        Map columnSettings = settings.getColumnsSettings();
        String[] columnsNames = (String[])columnSettings.get("columnsNames");
        String[] widthsValues = (String[])columnSettings.get("widthsValues");
        for (int i = 0; i < columnsNames.length; i++) {
            addColumnWithName(columnsNames[i], Double.valueOf(widthsValues[i]));
        }
        int port = settings.getPort();
        fontSize = settings.getFontSize();
        theme    = settings.getTheme();
        portField.setText(Integer.toString(port));
        table.setStyle("-fx-font-size:" + fontSize + "px");
        stackPane.setStyle("-fx-base:" + theme);

    }

    private void setAc(Scene scene) {

        KeyCombination kc = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
        Runnable rn = new Runnable() {
            public void run() {
                boolean paneIsVisible = settingsPanel.isVisible();
                settingsPanel.setVisible(!paneIsVisible);
                if (!paneIsVisible) {
                    table.getItems().clear();
                    settingsMode = true;
                }
                else {
                    settings.saveProps();
                    settingsMode = false;
                }
            }
        };
        scene.getAccelerators().put(kc, rn);
    }

    private void setAccToFullScreen(Stage stage, Scene scene) {

        KeyCombination kc = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_ANY);
        Runnable rn = new Runnable() {
            public void run() {

                if (stage.isFullScreen()) {
                    stage.setFullScreen(false);
                }
                else {
                    stage.setFullScreen(true);
                }
            }
        };
        scene.getAccelerators().put(kc, rn);
    }

    public void fillByList(List<String[]> list) {
        int sizeColumns = table.getColumns().size() - 1;
        for (int i = 0; i < list.size(); i++) {
            String[] innerList = list.get(i);
            ArrayList<String> itemlist = new ArrayList(Arrays.asList(innerList));
            if (itemlist.size() < sizeColumns) {
                int addcount = sizeColumns - itemlist.size();
                while (addcount > 0){
                    itemlist.add("");
                    addcount--;
                }
            }

            if (!settingsMode){
                table.getItems().add(
                        FXCollections.observableArrayList(itemlist));
            }
        }
    }


    public void setFontSize(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        fontSize = menuItem.getUserData().toString();
        table.setStyle("-fx-font-size:" + fontSize + "px");
    }

    public void setTheme(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        theme = menuItem.getUserData().toString();
        stackPane.setStyle("-fx-base:" + theme);
    }

    public void importSettings(ActionEvent actionEvent) {
    }

    public void exportSettings(ActionEvent actionEvent) {
    }

    public void cancelButton(ActionEvent actionEvent) {

        setSettings();
        settingsPanel.setVisible(false);
        settingsMode = false;

    }
}

