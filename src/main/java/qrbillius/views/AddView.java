package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import qrbillius.Application;
import qrbillius.errors.ErrorChecker;
import qrbillius.qrbill.QRBillInfo;

public class AddView extends ViewController {
    private Application app;

    public TextField nameTextField;
    public TextField addressLine1TextField;
    public TextField addressLine2TextField;
    public TextField paymentAmountTextField;
    public TextField additionalInfoTextField;

    @Override
    public void init(Application app) {
        this.app = app;
    }

    @Override
    public void show() {
        nameTextField.clear();
        addressLine1TextField.clear();
        addressLine2TextField.clear();
        paymentAmountTextField.clear();
        additionalInfoTextField.clear();
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        var billInfo = new QRBillInfo(
                nameTextField.getText(),
                addressLine1TextField.getText(),
                addressLine2TextField.getText(),
                paymentAmountTextField.getText(),
                additionalInfoTextField.getText()
        );

        var result = ErrorChecker.checkBillingInformation(billInfo, false);

        if (result.hasErrors()) {
            app.showErrorResult(result);
            return;
        }

        app.getBills().add(billInfo);
        app.switchView(app.getMainView());
    }
}
