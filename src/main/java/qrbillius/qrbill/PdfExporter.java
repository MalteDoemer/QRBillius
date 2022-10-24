package qrbillius.qrbill;

import net.codecrete.qrbill.canvas.PDFCanvas;
import net.codecrete.qrbill.generator.GraphicsFormat;
import net.codecrete.qrbill.generator.OutputSize;
import net.codecrete.qrbill.generator.QRBill;
import qrbillius.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfExporter extends QRBillExporter {
    public PdfExporter(Settings settings) {
        super(settings);
    }

    @Override
    public void export(File file, List<QRBillInfo> bills) throws IOException {
        var bytes = createPDFData(bills);
        try (var stream = new FileOutputStream(file)) {
            stream.write(bytes);
        }
    }

    private byte[] createPDFData(List<QRBillInfo> billInfos) throws IOException {
        var bills = QRBillGenerator.createBills(billInfos, settings, GraphicsFormat.PDF, OutputSize.A4_PORTRAIT_SHEET);


        var first = bills.get(0);
        var bytes = QRBill.generate(first);

        for (int i = 1; i < bills.size(); i++) {
            var bill = bills.get(i);

            try (var canvas = new PDFCanvas(bytes, PDFCanvas.NEW_PAGE_AT_END)) {
                QRBill.draw(bill, canvas);
                bytes = canvas.toByteArray();
            }
        }

        return bytes;
    }
}
