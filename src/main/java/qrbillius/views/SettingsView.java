package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import net.codecrete.qrbill.generator.Language;
import qrbillius.Application;
import qrbillius.Settings;
import qrbillius.SettingsManager;
import qrbillius.errors.ErrorChecker;
import qrbillius.errors.ErrorConstants;
import qrbillius.errors.ErrorMessage;
import qrbillius.qrbill.QRBillGenerator;

import java.io.IOException;

public class SettingsView extends ViewController {

    public TextField accountField;
    public TextField nameField;
    public TextField addressLine1Field;
    public TextField addressLine2Field;
    public ChoiceBox<Language> languageChoiceBox;
    public TextField wordTemplateField;
    public TextField csvSeparatorField;
    public TextField importNameFormatField;
    public TextField importAddressLine1FormatField;
    public TextField importAddressLine2FormatField;
    public TextField importPaymentAmountFormatField;
    public TextField importAdditionalInfoFormatField;
    private Application app;

    @Override
    public void init(Application app) {
        this.app = app;

        languageChoiceBox.getItems().addAll(Language.values());
    }

    @Override
    public void show() {
        var settings = app.getSettings();

        accountField.setText(settings.account());
        nameField.setText(settings.address().getName());
        addressLine1Field.setText(settings.address().getAddressLine1());
        addressLine2Field.setText(settings.address().getAddressLine2());
        languageChoiceBox.getSelectionModel().select(settings.language());
        wordTemplateField.setText(settings.wordTemplate());
        csvSeparatorField.setText(settings.csvSeparator());
        importNameFormatField.setText(settings.nameFormat());
        importAddressLine1FormatField.setText(settings.addressLine1Format());
        importAddressLine2FormatField.setText(settings.addressLine2Format());
        importPaymentAmountFormatField.setText(settings.paymentAmountFormat());
        importAdditionalInfoFormatField.setText(settings.additionalInfoFormat());
    }

    public void onWordTemplateSelectButtonClick(ActionEvent actionEvent) {
        var chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word", "*.docx"));
        var file = chooser.showOpenDialog(app.getStage());

        // file == null means that the user canceled the selection
        if (file == null)
            return;

        wordTemplateField.setText(file.getAbsolutePath());
    }


    public void onCancelButtonClicked(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onSaveButtonClicked(ActionEvent actionEvent) {

        var settings = createSettings();

        var result = ErrorChecker.checkSettings(settings);
        if (result.hasErrors()) {
            app.showErrorResult(result);
            return;
        }

        try {
            app.setSettings(settings);
            SettingsManager.save(settings);
        } catch (IOException e) {
            e.printStackTrace();
            var message = new ErrorMessage(ErrorConstants.IO_ERROR_OCCURRED, e.getLocalizedMessage());
            app.showErrorMessage(message);
        }

        app.switchView(app.getMainView());
    }

    private Settings createSettings() {
        return new Settings(
            accountField.getText(),
            QRBillGenerator.createAddress(nameField.getText(), addressLine1Field.getText(), addressLine2Field.getText()),
            languageChoiceBox.getValue(),
            wordTemplateField.getText(),
            csvSeparatorField.getText(),
            importNameFormatField.getText(),
            importAddressLine1FormatField.getText(),
            importAddressLine2FormatField.getText(),
            importPaymentAmountFormatField.getText(),
            importAdditionalInfoFormatField.getText()
        );
    }
}