package qrbillius.views;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import qrbillius.Application;
import qrbillius.errors.ErrorConstants;
import qrbillius.errors.ErrorMessage;
import qrbillius.qrbill.*;

import java.io.File;
import java.io.IOException;

public class MainView extends ViewController {

    public Button removeButton;
    public Button exportButton;
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
        exportButton.setDisable(isEmpty);
    }


    @Override
    public void show() {

    }

    private void onItemsChange(ListChangeListener.Change<? extends QRBillInfo> change) {
        // Disable PDF creation if there are no bills
        exportButton.setDisable(app.getBills().size() == 0);
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

    public void onExportButtonClick(ActionEvent actionEvent) {
        var chooser = new FileChooser();

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf", "*.PDF"),
                new FileChooser.ExtensionFilter("DOCX", "*.docx", "*.DOCX")
        );

        var file = chooser.showSaveDialog(app.getStage());

        // file == null means that the user canceled the selection
        if (file == null)
            return;

        var ext = FilenameUtils.getExtension(file.getName());

        var exporter = createExporter(ext);

        try {
            exporter.export(file);
        } catch (IOException e) {
            e.printStackTrace();
            var message = new ErrorMessage(ErrorConstants.IO_ERROR_OCCURRED, e.getLocalizedMessage());
            app.showErrorMessage(message);
        }
    }

    public void onSettingsButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getSettingsView());
    }


    private AbstractExporter createExporter(String ext) {
        if ("docx".compareToIgnoreCase(ext) == 0) {
            return new DocxExporter(app.getBills(), app.getSettings());
        } else if ("pdf".compareToIgnoreCase(ext) == 0) {
            return new PdfExporter(app.getBills(), app.getSettings());
        } else {
            // since the file chooser only allows .pdf and .docx this
            // branch should never be reached.
            throw new RuntimeException("unsupported format");
        }

    }
}
