package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import net.codecrete.qrbill.generator.Language;
import qrbillius.Application;
import qrbillius.config.ConfigurationManager;
import qrbillius.config.ProfileConfiguration;
import qrbillius.errors.ErrorChecker;
import qrbillius.errors.ErrorConstants;
import qrbillius.errors.ErrorMessage;
import qrbillius.qrbill.QRBillGenerator;

import java.io.IOException;

public class ConfigView extends ViewController {
    private Application app;
    public TextField accountField;
    public TextField nameField;
    public TextField addressLine1Field;
    public TextField addressLine2Field;
    public ChoiceBox<Language> languageChoiceBox;

    @Override
    public void init(Application app) {
        this.app = app;
        languageChoiceBox.getItems().addAll(Language.values());
    }

    @Override
    public void show() {
        populateFieldsFromConfig();
    }

    private void populateFieldsFromConfig() {
        var config = app.getProfileConfiguration();
        accountField.setText(config.account());
        nameField.setText(config.address().getName());
        addressLine1Field.setText(config.address().getAddressLine1());
        addressLine2Field.setText(config.address().getAddressLine2());
        languageChoiceBox.getSelectionModel().select(config.language());
    }

    private ProfileConfiguration createProfileConfig() {
        return new ProfileConfiguration(
                accountField.getText(),
                QRBillGenerator.createAddress(nameField.getText(), addressLine1Field.getText(), addressLine2Field.getText()),
                languageChoiceBox.getValue()
        );
    }

    public void onSaveButtonClicked(ActionEvent actionEvent) {
        var config = createProfileConfig();
        var result = ErrorChecker.checkProfileConfig(config);

        if (result.hasErrors()) {
            app.showErrorResult(result);
            return;
        }

        app.setProfileConfiguration(config);
        app.switchView(app.getMainView());
    }

    public void onCancelButtonClicked(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }
}
