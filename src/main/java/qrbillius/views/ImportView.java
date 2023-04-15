package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.io.FilenameUtils;
import qrbillius.Application;
import qrbillius.config.ConfigurationManager;
import qrbillius.config.ImportConfiguration;
import qrbillius.errors.*;
import qrbillius.io.FileExtensions;
import qrbillius.qrbill.CSVInputParser;
import qrbillius.qrbill.InputParser;
import qrbillius.qrbill.QRBillImporter;
import qrbillius.qrbill.XLSXInputParser;

import java.io.File;
import java.io.IOException;

public class ImportView extends ViewController {

    Application app;

    public TextField nameFormatField;
    public TextField addressLine1FormatField;
    public TextField addressLine2FormatField;
    public TextField paymentAmountFormatField;
    public CheckBox paymentAmountRequiredCheckBox;
    public TextField additionalInfoFormatField;
    public TextField csvSeparatorField;
    public Label importHeaderLabel;
    private File file;

    @Override
    public void init(Application app) {
        this.app = app;
    }

    @Override
    public void show() {
        setImportHeaderLabelText();
        configureCSVSeparatorField();
        populateFieldsFromConfig();
    }

    private void setImportHeaderLabelText() {
        var template = app.getUiResources().getString("importViewHeader");
        var text = String.format(template, file.getName());
        importHeaderLabel.setText(text);
    }

    /**
     * Enables / Disables the CSV separator field depending on the file extension.
     */
    private void configureCSVSeparatorField() {
        var ext = FilenameUtils.getExtension(file.getName());
        var parsed = FileExtensions.parse(ext);
        csvSeparatorField.setDisable(parsed != FileExtensions.CSV);
    }

    private void populateFieldsFromConfig() {
        var config = app.getImportConfiguration();
        csvSeparatorField.setText(config.csvSeparator());
        nameFormatField.setText(config.nameFormat());
        addressLine1FormatField.setText(config.addressLine1Format());
        addressLine2FormatField.setText(config.addressLine2Format());
        paymentAmountFormatField.setText(config.paymentAmountFormat());
        paymentAmountRequiredCheckBox.setSelected(config.paymentAmountRequired());
        additionalInfoFormatField.setText(config.additionalInfoFormat());
    }

    private boolean saveFieldsToConfig() {
        var config = createImportConfig();
        var result = ErrorChecker.checkImportConfig(config);

        if (result.hasErrors()) {
            app.showErrorResult(result);
            return false;
        }

        app.setImportConfiguration(config);
        return true;
    }

    private ImportConfiguration createImportConfig() {
        return new ImportConfiguration(
                csvSeparatorField.getText(),
                nameFormatField.getText(),
                addressLine1FormatField.getText(),
                addressLine2FormatField.getText(),
                paymentAmountFormatField.getText(),
                paymentAmountRequiredCheckBox.isSelected(),
                additionalInfoFormatField.getText()
        );
    }

    public void onImportButtonClicked(ActionEvent actionEvent) {
        if (!saveFieldsToConfig()) {
            // config is invalid, so don't import
            return;
        }

        try (var parser = getParser()) {
            var importer = new QRBillImporter(app.getImportConfiguration(), parser);
            var bills = importer.load();
            app.getBills().addAll(bills);
        } catch (ErrorResultException e) {
            app.showErrorResult(e.getResult());
        } catch (Exception e) {
            e.printStackTrace();
            var message = new ErrorMessage(ErrorConstants.IO_ERROR_OCCURRED, e.getLocalizedMessage());
            app.showErrorMessage(message);
        }

        app.switchView(app.getMainView());
    }

    private InputParser getParser() throws Exception {
        var ext = FilenameUtils.getExtension(file.getName());

        switch (FileExtensions.parse(ext)) {
            case CSV -> {
                // Note: csvSeparatorField should already be validated (i.e. it is not blank)
                assert !csvSeparatorField.getText().isBlank();

                return CSVInputParser.create(file, csvSeparatorField.getText());
            }
            case XLSX -> {
                return XLSXInputParser.create(file);
            }
            default -> {
                var result = new ErrorResult();
                result.addMessage(new ErrorMessage(ErrorConstants.UNSUPPORTED_FILE_EXTENSION, ext));
                throw new ErrorResultException(result);
            }
        }
    }

    public void onCancelButtonClicked(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
