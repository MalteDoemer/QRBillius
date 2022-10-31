package qrbillius.qrbill;

import qrbillius.Settings;
import qrbillius.errors.ErrorResultException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class QRBillImporter {

    protected final Settings settings;

    public QRBillImporter(Settings settings) {
        this.settings = settings;
    }

    public abstract List<QRBillInfo> load(File file) throws IOException, ErrorResultException;
}
