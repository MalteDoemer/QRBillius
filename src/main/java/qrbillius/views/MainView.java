package qrbillius.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import net.codecrete.qrbill.generator.Bill;
import qrbillius.Application;
import qrbillius.qrbill.QRBillInfo;

public class MainView extends ViewController {

    public Button removeButton;
    public Button createPDFButton;
    public TableView<QRBillInfo> tableView;
    private Application app;

    @Override
    public void init(Application app) {
        this.app = app;

        tableView.setItems(app.getBills());
        tableView.getItems().addListener(this::onItemsChange);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().getSelectedItems().addListener(this::onSelectionChange);

        // This row factory enables you to click on an already selected item to deselect it again.
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

        var isEmpty = app.getBills().size() == 0;
        createPDFButton.setDisable(isEmpty);
    }


    @Override
    public void show() {

    }

    private void onItemsChange(ListChangeListener.Change<? extends QRBillInfo> change) {
        // Disable PDF creation if there are no bills
        createPDFButton.setDisable(app.getBills().size() == 0);
    }

    private void onSelectionChange(ListChangeListener.Change<? extends QRBillInfo> change) {
        // Disable the remove button if there aren't any selected bills
        removeButton.setDisable(change.getList().size() == 0);
    }

    public void onOpenButtonClick(ActionEvent event) {
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getAddView());
    }

    public void onRemoveButtonClick(ActionEvent actionEvent) {
        app.getBills().removeAll(tableView.getSelectionModel().getSelectedItems());
    }

    public void onCreatePDFButtonClick(ActionEvent actionEvent) {
    }

    public void onSettingsButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getSettingsView());
    }
}
