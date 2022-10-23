package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import net.codecrete.qrbill.generator.Language;
import qrbillius.Application;
import qrbillius.errors.ErrorChecker;
import qrbillius.errors.ErrorConstants;
import qrbillius.errors.ErrorMessage;

import java.io.IOException;

public class SettingsView extends ViewController {

    public TextField accountField;
    public TextField nameField;
    public TextField addressLine1Field;
    public TextField addressLine2Field;
    public ChoiceBox<Language> languageChoiceBox;
    private Application app;

    @Override
    public void init(Application app) {
        this.app = app;

        languageChoiceBox.getItems().addAll(Language.values());
    }

    @Override
    public void show(Object arg) {
        var settings = app.getSettings();

        accountField.setText(settings.getCreditorAccount());
        nameField.setText(settings.getCreditorName());
        addressLine1Field.setText(settings.getCreditorAddressLine1());
        addressLine2Field.setText(settings.getCreditorAddressLine2());
        languageChoiceBox.getSelectionModel().select(settings.getLanguage());
    }

    public void onCancelButtonClicked(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onSaveButtonClicked(ActionEvent actionEvent) {
        var settings = app.getSettings();

        settings.setCreditorAccount(accountField.getText());
        settings.setCreditorName(nameField.getText());
        settings.setCreditorAddressLine1(addressLine1Field.getText());
        settings.setCreditorAddressLine2(addressLine2Field.getText());
        settings.setLanguage(languageChoiceBox.getValue());

        var result = ErrorChecker.checkSettings(settings);
        if (result.hasErrors()) {
            app.showErrorResult(result);
            return;
        }

        try {
            settings.save();
        } catch (IOException e) {
            e.printStackTrace();
            var message = new ErrorMessage(ErrorConstants.IO_ERROR_OCCURRED, e.getLocalizedMessage());
            app.showErrorMessage(message);
        }

        app.switchView(app.getMainView());
    }
}
