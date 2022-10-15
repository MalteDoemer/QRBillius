package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import qrbillius.Application;

public class SettingsView extends ViewController {

    public TextField accountField;
    public TextField nameField;
    public TextField addressLine1Field;
    public TextField addressLine2Field;
    public TextField languageField;
    private Application app;

    @Override
    public void init(Application app) {
        this.app = app;
    }

    @Override
    public void show() {

    }

    public void onCancelButtonClicked(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onSaveButtonClicked(ActionEvent actionEvent) {

    }
}
