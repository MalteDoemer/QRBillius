package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import qrbillius.Application;

public class MainView extends ViewController {

    public Button removeButton;
    public Button createPDFButton;
    private Application app;

    @Override
    public void init(Application app) {
        this.app = app;
    }

    @Override
    public void show() {

    }

    public void onOpenButtonClick(ActionEvent event) {}

    public void onAddButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getAddView());
    }

    public void onRemoveButtonClick(ActionEvent actionEvent) {

    }

    public void onCreatePDFButtonClick(ActionEvent actionEvent) {
    }

    public void onSettingsButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getSettingsView());
    }
}
