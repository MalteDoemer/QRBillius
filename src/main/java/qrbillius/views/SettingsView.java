package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import net.codecrete.qrbill.generator.Language;
import qrbillius.Application;
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
    public void show(Object arg) {
        var settings = app.getSettings();

        accountField.setText(settings.account());
        nameField.setText(settings.address().getName());
        addressLine1Field.setText(settings.address().getAddressLine1());
        addressLine2Field.setText(settings.address().getAddressLine2());
        languageChoiceBox.getSelectionModel().select(settings.language());
        csvSeparatorField.setText(settings.csvSeparator());
        importNameFormatField.setText(settings.nameFormat());
        importAddressLine1FormatField.setText(settings.addressLine1Format());
        importAddressLine2FormatField.setText(settings.addressLine2Format());
        importPaymentAmountFormatField.setText(settings.paymentAmountFormat());
        importAdditionalInfoFormatField.setText(settings.additionalInfoFormat());
    }

    public void onCancelButtonClicked(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onSaveButtonClicked(ActionEvent actionEvent) {
        var settings = app.getSettings();

        settings.setAccount(accountField.getText());
        settings.setAddress(QRBillGenerator.createAddress(nameField.getText(), addressLine1Field.getText(), addressLine2Field.getText()));
        settings.setLanguage(languageChoiceBox.getValue());
        settings.setCsvSeparator(csvSeparatorField.getText());
        settings.setNameFormat(importNameFormatField.getText());
        settings.setAddressLine1Format(importAddressLine1FormatField.getText());
        settings.setAddressLine2Format(importAddressLine2FormatField.getText());
        settings.setPaymentAmountFormat(importPaymentAmountFormatField.getText());
        settings.setAdditionalInfoFormat(importAdditionalInfoFormatField.getText());

        var result = ErrorChecker.checkSettings(settings);
        if (result.hasErrors()) {
            app.showErrorResult(result);
            return;
        }

        try {
            SettingsManager.save(settings);
        } catch (IOException e) {
            e.printStackTrace();
            var message = new ErrorMessage(ErrorConstants.IO_ERROR_OCCURRED, e.getLocalizedMessage());
            app.showErrorMessage(message);
        }

        app.switchView(app.getMainView());
    }
}
