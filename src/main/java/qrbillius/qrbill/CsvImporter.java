package qrbillius.qrbill;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import qrbillius.Settings;
import qrbillius.errors.*;
import qrbillius.io.UnicodeBOMInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter extends QRBillImporter {
    public CsvImporter(Settings settings) {
        super(settings);
    }

    @Override
    public List<QRBillInfo> load(File file) throws IOException, ErrorResultException {
        try (var parser = createParser(file)) {
            var bills = new ArrayList<QRBillInfo>();

            var nameFormatter = new FormatParser(settings.nameFormat());
            var addressLine1Formatter = new FormatParser(settings.addressLine1Format());
            var addressLine2Formatter = new FormatParser(settings.addressLine2Format());
            var paymentAmountFormatter = new FormatParser(settings.paymentAmountFormat());
            var additionalInfoFormatter = new FormatParser(settings.additionalInfoFormat());

            for (var record : parser) {
                var list = record.toList();
                try {
                    var name = nameFormatter.parse(list);
                    var addressLine1 = addressLine1Formatter.parse(list);
                    var addressLine2 = addressLine2Formatter.parse(list);
                    var paymentAmount = paymentAmountFormatter.parse(list);
                    var additionalInfo = additionalInfoFormatter.parse(list);

                    var billInfo = new QRBillInfo(name, addressLine1, addressLine2, paymentAmount, additionalInfo, record.toMap());

                    var res = ErrorChecker.checkBillingInformation(billInfo);

                    if (res.hasErrors()) {
                        res.setLineNumber((int) parser.getCurrentLineNumber());
                        throw new ErrorResultException(res);
                    }

                    bills.add(billInfo);
                } catch (FormatException e) {
                    var res = e.createErrorResult();
                    res.setLineNumber((int) parser.getCurrentLineNumber());
                    throw new ErrorResultException(res);
                }
            }

            return bills;
        }

    }

    private CSVParser createParser(File file) throws IOException {
        var format = CSVFormat
                .EXCEL
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setDelimiter(settings.csvSeparator())
                .build();

        var reader = new InputStreamReader(new UnicodeBOMInputStream(new FileInputStream(file)).skipBOM());
        return format.parse(reader);
    }
}
