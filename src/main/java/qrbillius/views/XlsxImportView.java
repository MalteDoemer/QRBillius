package qrbillius.views;

import javafx.event.ActionEvent;
import qrbillius.Application;

import java.io.File;

public class XlsxImportView extends ViewController {

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

    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        app.switchView(app.getMainView());
    }

    public void onImportButtonClick(ActionEvent actionEvent) {

    }
}
