package qrbillius.qrbill;

import jakarta.xml.bind.JAXBException;
import net.codecrete.qrbill.generator.GraphicsFormat;
import net.codecrete.qrbill.generator.OutputSize;
import net.codecrete.qrbill.generator.QRBill;

import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.P;
import qrbillius.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocxExporter extends QRBillExporter {

    public DocxExporter(Settings settings) {
        super(settings);
    }

    @Override
    public void export(File file, List<QRBillInfo> bills) throws IOException {
        try {
            var wordPackage = loadWordTemplate();

            var actualBills = removeUnmappedBills(bills);

            var mapData = createDataMap(actualBills);
            var merged = MailMerger.getConsolidatedResultCrude(wordPackage, mapData, false);

            addQRBills(merged, actualBills);

            merged.save(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<QRBillInfo> removeUnmappedBills(List<QRBillInfo> bills) {
        var list = new ArrayList<QRBillInfo>();

        for (var bill : bills) {
            if (bill.hasImportedValues())
                list.add(bill);
        }

        return list;
    }

    private void addQRBills(WordprocessingMLPackage wordPackage, List<QRBillInfo> bills) throws Exception {

        var document = wordPackage.getMainDocumentPart().getContents();
        var body = document.getBody();
        var content = body.getContent();

        var sectPrIndices = new ArrayList<Integer>();

        for (int i = 0; i < content.size(); i++) {
            Object c = content.get(i);
            if (c instanceof P) {
                var ppr = ((P) c).getPPr();

                if (ppr != null) {
                    var sectPr = ppr.getSectPr();
                    if (sectPr != null) {
                        sectPrIndices.add(i);
                    }
                }
            }
        }

        for (int i = 0; i < sectPrIndices.size(); i++) {
            var index = sectPrIndices.get(i) + i;
            var bill = bills.get(i);
            var rel = createImageRelationShip(wordPackage, bill);
            var image = createImageParagraph(rel);
            content.add(index, image);

            if (i == sectPrIndices.size() - 1) {
                // remove the last page break to avoid an empty page
                content.remove(index + 1);
            }
        }

    }

    private List<Map<DataFieldName, String>> createDataMap(List<QRBillInfo> bills) {
        var res = new ArrayList<Map<DataFieldName, String>>();

        for (var bill : bills) {
            if (!bill.hasImportedValues())
                continue;

            var map = new HashMap<DataFieldName, String>();
            bill.getImportedValues().forEach((key, val) -> {
                map.put(new DataFieldName(key), val);
            });

            res.add(map);
        }

        return res;
    }

    private WordprocessingMLPackage loadWordTemplate() throws Exception {

        var path = settings.wordTemplate();
        var file = new File(path);

        try (var stream = new FileInputStream(file)) {
            return WordprocessingMLPackage.load(stream);
        }
    }

    private byte[] createImageData(QRBillInfo info) {
        var bill = QRBillGenerator.createBill(info, settings, GraphicsFormat.PNG, OutputSize.QR_BILL_ONLY);
        return QRBill.generate(bill);
    }

    private Relationship createImageRelationShip(WordprocessingMLPackage wordPackage, QRBillInfo info) throws Exception {
        var imageData = createImageData(info);
        var imagePart = BinaryPartAbstractImage.createImagePart(wordPackage, wordPackage.getMainDocumentPart(), imageData, "image/png");
        return imagePart.getRelLast();
    }

    private P createImageParagraph(Relationship rel) throws JAXBException {
        var factory = Context.getWmlObjectFactory();

        var p = factory.createP();
        var r = factory.createR();
        p.getContent().add(r);

        var drawing = createDrawing(rel);
        var wrapped = factory.createRDrawing(drawing);
        r.getContent().add(wrapped);

        return p;
    }


    private Drawing createDrawing(Relationship rel) throws JAXBException {
        var openXML = "<w:drawing" + namespaces + ">"
                + "<wp:anchor allowOverlap=\"true\" behindDoc=\"false\" distB=\"0\" distL=\"114300\" distR=\"114300\" distT=\"0\" layoutInCell=\"true\" locked=\"false\" relativeHeight=\"251659264\" simplePos=\"false\" wp14:anchorId=\"0F89EC94\" wp14:editId=\"39459E11\">"
                + "<wp:simplePos x=\"0\" y=\"0\"/>"

                + "<wp:positionH relativeFrom=\"page\">"
                + "<wp:align>right</wp:align>"

                + "</wp:positionH>"

                + "<wp:positionV relativeFrom=\"page\">"
                + "<wp:posOffset>6894830</wp:posOffset>"
                + "</wp:positionV>"

                + "<wp:extent cx=\"7563600\" cy=\"3780000\"/>"
                + "<wp:effectExtent b=\"0\" l=\"0\" r=\"0\" t=\"0\"/>"
                + "<wp:wrapNone/>"
                + "<wp:docPr descr=\"Qr code\" id=\"1\" name=\"Picture 1\"/>"

                + "<wp:cNvGraphicFramePr>"
                + "<a:graphicFrameLocks noChangeAspect=\"true\"/>"
                + "</wp:cNvGraphicFramePr>"

                + "<a:graphic>"
                + "<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                + "<pic:pic>"
                + "<pic:nvPicPr>"
                + "<pic:cNvPr descr=\"Qr code\" id=\"1\" name=\"Picture 1\"/>"

                + "<pic:cNvPicPr>"
                + "<a:picLocks noChangeArrowheads=\"true\" noChangeAspect=\"true\"/>"
                + "</pic:cNvPicPr>"

                + "</pic:nvPicPr>"

                + "<pic:blipFill>"
                + "<a:blip r:embed=\"" + rel.getId() + "\">"

                + "<a:extLst>"
                + "<a:ext uri=\"{28A0092B-C50C-407E-A947-70E740481C1C}\">"
                + "<a14:useLocalDpi val=\"false\"/>"

                + "</a:ext>"
                + "</a:extLst>"
                + "</a:blip>"

                + "<a:srcRect/>"

                + "<a:stretch>"
                + "<a:fillRect/>"
                + "</a:stretch>"

                + "</pic:blipFill>"

                + "<pic:spPr bwMode=\"auto\">"
                + "<a:xfrm>"
                + "<a:off x=\"0\" y=\"0\"/>"
                + "<a:ext cx=\"7563600\" cy=\"3780000\"/>"
                + "</a:xfrm>"

                + "<a:prstGeom prst=\"rect\">"
                + "<a:avLst/>"
                + "</a:prstGeom>"

                + "<a:noFill/>"

                + "<a:ln>"
                + "<a:noFill/>"
                + "</a:ln>"

                + "</pic:spPr>"
                + "</pic:pic>"
                + "</a:graphicData>"
                + "</a:graphic>"

                + "<wp14:sizeRelH relativeFrom=\"page\">"
                + "<wp14:pctWidth>0</wp14:pctWidth>"
                + "</wp14:sizeRelH>"

                + "<wp14:sizeRelV relativeFrom=\"page\">"
                + "<wp14:pctHeight>0</wp14:pctHeight>"
                + "</wp14:sizeRelV>"

                + "</wp:anchor>"
                + "</w:drawing>";

        return (Drawing) XmlUtils.unmarshalString(openXML);
    }

    private final static String namespaces = " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
            + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
            + "xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\" "
            + "xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" "
            + "xmlns:a14=\"http://schemas.microsoft.com/office/drawing/2010/main\" "
            + "xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\" "
            + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" ";
}
