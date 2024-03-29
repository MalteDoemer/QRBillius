package qrbillius.views;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import qrbillius.Application;
import qrbillius.qrbill.QRBillInfo;

import java.util.HashMap;
import java.util.Map;

public class MainView extends ViewController {
    public TableView<QRBillInfo> tableView;
    public Button importButton;
    public Button addButton;
    public Button removeButton;
    public Button exportButton;
    public Button configButton;

    @Override
    public void init(Application app) {
        super.init(app);

        tableView.setItems(app.getBills());
        tableView.getItems().addListener(this::onItemsChange);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().getSelectedItems().addListener(this::onSelectionChange);

        keyboardShortcuts.put(new KeyCodeCombination(KeyCode.O, KeyCodeCombination.SHORTCUT_DOWN), () -> importButton.fire());
        keyboardShortcuts.put(new KeyCodeCombination(KeyCode.N, KeyCodeCombination.SHORTCUT_DOWN), () -> addButton.fire());
        keyboardShortcuts.put(new KeyCodeCombination(KeyCode.DELETE), () -> removeButton.fire());
        keyboardShortcuts.put(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.SHORTCUT_DOWN), () -> exportButton.fire());
        keyboardShortcuts.put(new KeyCodeCombination(KeyCode.X, KeyCodeCombination.SHORTCUT_DOWN), () -> configButton.fire());

        setTableViewRowFactory();
        configureExportButton();
    }

    /**
     * This function set the row factory of the table view so that
     * it enables the user to click on an already selected row to deselect it again.
     */
    private void setTableViewRowFactory() {
        tableView.setRowFactory(tableView2 -> {
            final TableRow<QRBillInfo> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                final int index = row.getIndex();
                if (index >= 0 && index < tableView.getItems().size() && tableView.getSelectionModel().isSelected(index)) {
                    tableView.getSelectionModel().clearSelection(index);
                    event.consume();
                }
            });
            return row;
        });
    }

    /**
     * This function enables/disables the export button based on whether the bills list is empty
     */
    private void configureExportButton() {
        var isEmpty = app.getBills().size() == 0;
        exportButton.setDisable(isEmpty);
    }

    private void onItemsChange(ListChangeListener.Change<? extends QRBillInfo> change) {
        configureExportButton();
    }

    private void onSelectionChange(ListChangeListener.Change<? extends QRBillInfo> change) {
        // Disable the remove button if there aren't any selected bills
        removeButton.setDisable(change.getList().size() == 0);
    }

    public void onImportButtonClick(ActionEvent actionEvent) {
        var chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(app.getUiResources().getString("allFileExtensions"), "*.csv", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx")
        );

        // open the file chooser at the last used location
        if (app.getLastOpenedFolder() != null) {
            chooser.setInitialDirectory(app.getLastOpenedFolder());
        }

        // display the file chooser dialog
        var file = chooser.showOpenDialog(app.getStage());

        // file == null means that the user canceled the selection
        if (file == null)
            return;

        // store the last opened folder
        app.setLastedOpenedFolder(file.getParentFile());

        // this is a little ugly, but we have to somehow pass along
        // the file that was selected ...
        var controller = (ImportView) app.getImportView().controller();
        controller.setFile(file);

        app.switchView(app.getImportView());
    }


    public void onAddButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getAddView());
    }

    public void onRemoveButtonClick(ActionEvent actionEvent) {
        app.getBills().removeAll(tableView.getSelectionModel().getSelectedItems());
    }

    public void onExportButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getExportView());
    }

    public void onConfigButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getConfigView());
    }
}
