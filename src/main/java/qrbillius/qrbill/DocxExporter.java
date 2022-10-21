package qrbillius.qrbill;

import net.codecrete.qrbill.generator.GraphicsFormat;
import net.codecrete.qrbill.generator.OutputSize;
import net.codecrete.qrbill.generator.QRBill;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DocxExporter extends AbstractExporter {

    public DocxExporter(List<QRBillInfo> bills, QRBillConfig config) {
        super(bills, config);
    }

    @Override
    public void export(File file) throws IOException {
        try {
            var wordPackage = WordprocessingMLPackage.createPackage();
            var mainDocumentPart = wordPackage.getMainDocumentPart();

            var info = bills.get(0);
            var data = createImageData(info);

            var imagePart = BinaryPartAbstractImage.createImagePart(wordPackage, data);
            var inline = imagePart.createImageInline("", "QR Bill", 1, 2, false);
            var paragraph = addImageToParagraph(inline);
            mainDocumentPart.getContent().add(paragraph);

            try (var stream = new FileOutputStream(file)) {
                wordPackage.save(stream);
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createImageData(QRBillInfo info) {
        var bill = QRBillGenerator.createBill(info, config, GraphicsFormat.PNG, OutputSize.QR_BILL_ONLY);
        return QRBill.generate(bill);
    }

    private static P addImageToParagraph(Inline inline) {
        ObjectFactory factory = new ObjectFactory();
        P p = factory.createP();
        R r = factory.createR();
        p.getContent().add(r);
        Drawing drawing = factory.createDrawing();
        r.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return p;
    }
}
