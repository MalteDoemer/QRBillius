package qrbillius.qrbill;


import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class QRBillExporter {
    protected List<QRBillInfo> bills;
    protected QRBillConfig config;

    public QRBillExporter(List<QRBillInfo> bills, QRBillConfig config) {
        this.bills = bills;
        this.config = config;
    }

    public abstract void export(File file) throws IOException;
}
