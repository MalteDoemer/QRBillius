package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import qrbillius.Application;

public class DocxExportView extends ViewController {

    public enum ExportType {
        PDF,
        DOCX,
    }

    public ChoiceBox<ExportType> exportTypeChoiceBox;
    private Application app;

    @Override
    public void init(Application app) {
        this.app = app;
        exportTypeChoiceBox.getItems().addAll(ExportType.values());
    }

    @Override
    public void show(Object arg) {
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onSaveButtonClick(ActionEvent actionEvent) {


    }
}
