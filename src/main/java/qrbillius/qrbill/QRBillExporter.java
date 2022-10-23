package qrbillius.qrbill;


import qrbillius.Settings;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class QRBillExporter {
    protected List<QRBillInfo> bills;
    protected Settings settings;

    public QRBillExporter(List<QRBillInfo> bills, Settings settings) {
        this.bills = bills;
        this.settings = settings;
    }

    public abstract void export(File file) throws IOException;
}
