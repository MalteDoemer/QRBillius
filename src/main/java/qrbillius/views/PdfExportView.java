package qrbillius.views;

import qrbillius.Application;

import java.io.File;

public class PdfExportView extends ViewController {

    private File file;

    @Override
    public void init(Application app) {

    }

    @Override
    public void show(Object arg) {
        var path = (String) arg;
        file = new File(path);
    }
}
