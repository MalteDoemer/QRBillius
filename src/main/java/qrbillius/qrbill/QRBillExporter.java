package qrbillius.qrbill;


import qrbillius.Settings;
import qrbillius.errors.ErrorResultException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class QRBillExporter {
    protected final Settings settings;

    public QRBillExporter(Settings settings) {
        this.settings = settings;
    }

    public abstract void export(File file, List<QRBillInfo> bills) throws IOException, ErrorResultException;
}
