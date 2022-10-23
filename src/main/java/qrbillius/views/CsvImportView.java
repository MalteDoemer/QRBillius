package qrbillius.views;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import qrbillius.Application;

import java.io.File;

public class CsvImportView extends ViewController {

    public TextField csvSeparatorField;
    public TextField csvNameFormatField;
    public TextField csvAddressLine1FormatField;
    public TextField csvAddressLine2FormatField;
    public TextField csvPaymentAmountFormatField;
    public TextField csvAdditionalInfoFormatField;
    private Application app;
    private File file;

    @Override
    public void init(Application app) {
        this.app = app;
    }

    @Override
    public void show(Object arg) {
        var path = (String) arg;
        file = new File(path);

        var settings = app.getSettings();

        csvSeparatorField.setText(settings.getCsvSeparator());
        csvNameFormatField.setText(settings.getCsvNameFormat());
        csvAddressLine1FormatField.setText(settings.getCsvAddressLine1Format());
        csvAddressLine2FormatField.setText(settings.getCsvAddressLine2Format());
        csvPaymentAmountFormatField.setText(settings.getCsvPaymentAmountFormat());
        csvAdditionalInfoFormatField.setText(settings.getCsvAdditionalInfoFormat());
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onImportButtonClick(ActionEvent actionEvent) {

    }
}
