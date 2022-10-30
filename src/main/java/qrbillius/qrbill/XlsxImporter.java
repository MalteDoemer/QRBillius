package qrbillius.qrbill;

import org.xlsx4j.exceptions.Xlsx4jException;
import qrbillius.Settings;
import qrbillius.errors.ErrorChecker;
import qrbillius.errors.ErrorResultException;
import qrbillius.errors.FormatException;
import qrbillius.io.XlsxFormat;
import qrbillius.io.XlsxParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XlsxImporter extends QRBillImporter {
    public XlsxImporter(Settings settings) {
        super(settings);
    }

    @Override
    public List<QRBillInfo> load(File file) throws IOException, ErrorResultException {
        var records = createParser(file);
        var bills = new ArrayList<QRBillInfo>();

        var nameFormatter = new FormatParser(settings.nameFormat());
        var addressLine1Formatter = new FormatParser(settings.addressLine1Format());
        var addressLine2Formatter = new FormatParser(settings.addressLine2Format());
        var paymentAmountFormatter = new FormatParser(settings.paymentAmountFormat());
        var additionalInfoFormatter = new FormatParser(settings.additionalInfoFormat());

        int lineNo = 2; // line 1 contains the header
        for (var record : records) {
            var list = record.toList();

            try {
                var name = nameFormatter.parse(list);
                var addressLine1 = addressLine1Formatter.parse(list);
                var addressLine2 = addressLine2Formatter.parse(list);
                var paymentAmount = paymentAmountFormatter.parse(list);
                var additionalInfo = additionalInfoFormatter.parse(list);

                var billInfo = new QRBillInfo(name, addressLine1, addressLine2, paymentAmount, additionalInfo);

                var res = ErrorChecker.checkBillingInformation(billInfo);

                if (res.hasErrors()) {
                    res.setLineNumber(lineNo);
                    throw new ErrorResultException(res);
                }

                bills.add(billInfo);
            } catch (FormatException e) {
                var res = e.createErrorResult();
                res.setLineNumber(lineNo);
                throw new ErrorResultException(res);
            }

            lineNo++;
        }

        return bills;
    }

    private XlsxParser createParser(File file) throws IOException {
        var format = XlsxFormat
                .DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setWorksheet(0)
                .build();

        try {
            return format.parse(file);
        } catch (Xlsx4jException e) {
            // TODO: handle Xlsx4JException better
            throw new RuntimeException(e);
        }
    }
}
