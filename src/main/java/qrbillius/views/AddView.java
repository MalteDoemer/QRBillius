package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import qrbillius.Application;
import qrbillius.errors.ErrorChecker;
import qrbillius.qrbill.QRBillInfo;

import java.util.Map;

public class AddView extends ViewController {
    public TextField nameTextField;
    public TextField addressLine1TextField;
    public TextField addressLine2TextField;
    public TextField paymentAmountTextField;
    public TextField additionalInfoTextField;
    public Button cancelButton;

    @Override
    public void init(Application app) {
        super.init(app);

        keyboardShortcuts.put(new KeyCodeCombination(KeyCode.ESCAPE), () -> cancelButton.fire());
    }

    @Override
    public void show() {
        super.show();
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
