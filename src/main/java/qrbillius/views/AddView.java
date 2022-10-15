package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import qrbillius.Application;

public class AddView extends ViewController {

    public TextField nameTextField;
    public TextField addressLine1TextField;
    public TextField addressLine2TextField;
    public TextField paymentAmountTextField;
    public TextField additionalInfoTextField;
    private Application app;
    @Override
    public void init(Application app) {
        this.app = app;

    }

    @Override
    public void show() {

    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
    }
}
