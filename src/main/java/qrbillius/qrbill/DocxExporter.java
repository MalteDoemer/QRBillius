package qrbillius.qrbill;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import qrbillius.SettingsManager;

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
            mainDocumentPart.addStyledParagraphOfText("Title", "Hello World!");
            mainDocumentPart.addParagraphOfText("Welcome");

            try (var stream = new FileOutputStream(file)) {
                wordPackage.save(stream);
            }
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }
    }
}
