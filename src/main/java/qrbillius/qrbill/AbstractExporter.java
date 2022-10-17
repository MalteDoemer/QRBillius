package qrbillius.qrbill;


import qrbillius.SettingsManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AbstractExporter {
    protected List<QRBillInfo> bills;
    protected SettingsManager settings;

    public AbstractExporter(List<QRBillInfo> bills, SettingsManager settings) {
        this.bills = bills;
        this.settings = settings;
    }

    public abstract void export(File file) throws IOException;
}
