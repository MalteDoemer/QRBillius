package qrbillius.views;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import qrbillius.Application;
import qrbillius.config.ExportConfiguration;
import qrbillius.errors.ErrorChecker;
import qrbillius.errors.ErrorConstants;
import qrbillius.errors.ErrorMessage;
import qrbillius.qrbill.QRBillExporter;

import java.io.IOException;

public class ExportView extends ViewController {

    public CheckBox openPDFWhenFinishedCheckBox;
    public TextField pdfTemplateField;
    public CheckBox pdfTemplateCheckBox;
    public Button pdfTemplateSelectButton;
    private Application app;

    @Override
    public void init(Application app) {
        this.app = app;
        pdfTemplateCheckBox.selectedProperty().addListener(this::onPDFTemplateCheckBoxChanged);
    }

    @Override
    public void show() {
        populateFieldsFromConfig();
        updatePDFTemplateFields();
    }

    private void populateFieldsFromConfig() {
        var config = app.getExportConfiguration();
        openPDFWhenFinishedCheckBox.setSelected(config.openPDFWhenFinished());
        pdfTemplateCheckBox.setSelected(config.enablePDFTemplate());
        pdfTemplateField.setText(config.pdfTemplate());
    }

    private void onPDFTemplateCheckBoxChanged(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        updatePDFTemplateFields();
    }

    private void updatePDFTemplateFields() {
        var enabled = pdfTemplateCheckBox.isSelected();
        pdfTemplateField.setDisable(!enabled);
        pdfTemplateSelectButton.setDisable(!enabled);
    }

    private boolean saveFieldsToConfig() {
        var config = createExportConfig();
        var result = ErrorChecker.checkExportConfig(config, app.getBills().size());

        if (result.hasErrors()) {
            app.showErrorResult(result);
            return false;
        }

        app.setExportConfiguration(config);
        return true;
    }

    private ExportConfiguration createExportConfig() {
        return new ExportConfiguration(
                openPDFWhenFinishedCheckBox.isSelected(),
                pdfTemplateCheckBox.isSelected(),
                pdfTemplateField.getText()
        );
    }

    public void onExportButtonClicked(ActionEvent actionEvent) {
        if (!saveFieldsToConfig()) {
            // config is invalid, so don't export
            return;
        }

        try {
            var exportConfig = app.getExportConfiguration();

            var profileConfig = app.getProfileConfiguration();
            var res = ErrorChecker.checkProfileConfig(profileConfig);

            if (res.hasErrors()) {
                var msg = new ErrorMessage(ErrorConstants.CONFIG_INVALID);
                app.showErrorMessage(msg);
                app.switchView(app.getConfigView());
                return;
            }

            var chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

            if (app.getLastOpenedFolder() != null) {
                chooser.setInitialDirectory(app.getLastOpenedFolder());
            }

            var file = chooser.showSaveDialog(app.getStage());

            // file == null means that the user canceled the selection
            if (file == null)
                return;

            app.setLastedOpenedFolder(file.getParentFile());

            var exporter = new QRBillExporter(exportConfig, profileConfig);
            exporter.export(file, app.getBills());

            if (exportConfig.openPDFWhenFinished()) {
                app.getHostServices().showDocument(file.toURI().toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            var message = new ErrorMessage(ErrorConstants.IO_ERROR_OCCURRED, e.getLocalizedMessage());
            app.showErrorMessage(message);
        }

        app.switchView(app.getMainView());
    }

    public void onCancelButtonClicked(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onPDFTemplateSelectButtonClick(ActionEvent actionEvent) {
        var chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        if (app.getLastOpenedFolder() != null) {
            chooser.setInitialDirectory(app.getLastOpenedFolder());
        }

        var file = chooser.showOpenDialog(app.getStage());

        // file == null means that the user canceled the selection
        if (file == null)
            return;

        app.setLastedOpenedFolder(file.getParentFile());

        pdfTemplateField.setText(file.getAbsolutePath());
    }


}
