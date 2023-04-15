package qrbillius.qrbill;

import org.apache.pdfbox.pdmodel.PDDocument;
import net.codecrete.qrbill.canvas.PDFCanvas;
import net.codecrete.qrbill.generator.GraphicsFormat;
import net.codecrete.qrbill.generator.OutputSize;
import net.codecrete.qrbill.generator.QRBill;
import qrbillius.config.ExportConfiguration;
import qrbillius.config.ProfileConfiguration;

import java.io.*;
import java.util.List;

public class QRBillExporter {
    private final ExportConfiguration exportConfig;

    private final ProfileConfiguration profileConfig;

    public QRBillExporter(ExportConfiguration exportConfig, ProfileConfiguration profileConfig) {
        this.exportConfig = exportConfig;
        this.profileConfig = profileConfig;
    }

    public void export(File file, List<QRBillInfo> bills) throws IOException {

        var bytes = exportConfig.enablePDFTemplate()
                ? createPDFDataWithTemplate(bills)
                : createPDFDataNoTemplate(bills);

        try (var stream = new FileOutputStream(file)) {
            stream.write(bytes);
        }
    }

    private byte[] createPDFDataNoTemplate(List<QRBillInfo> billInfos) throws IOException {
        var bills = QRBillGenerator.createBills(billInfos, profileConfig, GraphicsFormat.PDF, OutputSize.A4_PORTRAIT_SHEET);

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

    private byte[] createPDFDataWithTemplate(List<QRBillInfo> billInfos) throws IOException {
        var bills = QRBillGenerator.createBills(billInfos, profileConfig, GraphicsFormat.PDF, OutputSize.A4_PORTRAIT_SHEET);

        var templateFile = new File(exportConfig.pdfTemplate());

        try (var stream = new FileInputStream(templateFile)) {
            var bytes = stream.readAllBytes();

            for (int i = 0; i < bills.size(); i++) {
                var bill = bills.get(i);

                try (var canvas = new PDFCanvas(bytes, i)) {
                    QRBill.draw(bill, canvas);
                    bytes = canvas.toByteArray();
                }
            }

            return bytes;
        }
    }
}
