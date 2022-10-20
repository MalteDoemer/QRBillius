package qrbillius.qrbill;


import qrbillius.SettingsManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AbstractExporter {
    protected List<QRBillInfo> bills;
    protected QRBillConfig config;

    public AbstractExporter(List<QRBillInfo> bills, QRBillConfig config) {
        this.bills = bills;
        this.config = config;
    }

    public abstract void export(File file) throws IOException;
}
