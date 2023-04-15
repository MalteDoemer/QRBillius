package qrbillius.qrbill;

import qrbillius.config.ImportConfiguration;
import qrbillius.errors.ErrorChecker;
import qrbillius.errors.ErrorResultException;
import qrbillius.errors.FormatException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QRBillImporter {
    private final ImportConfiguration config;
    private final InputParser parser;

    public QRBillImporter(ImportConfiguration config, InputParser parser) {
        this.config = config;
        this.parser = parser;
    }

    public List<QRBillInfo> load() throws ErrorResultException {
        var bills = new ArrayList<QRBillInfo>();

        var nameFormatter = new FormatParser(config.nameFormat());
        var addressLine1Formatter = new FormatParser(config.addressLine1Format());
        var addressLine2Formatter = new FormatParser(config.addressLine2Format());
        var paymentAmountFormatter = new FormatParser(config.paymentAmountFormat());
        var additionalInfoFormatter = new FormatParser(config.additionalInfoFormat());

        for (var record : parser) {
            try {
                var name = nameFormatter.parse(record);
                var addressLine1 = addressLine1Formatter.parse(record);
                var addressLine2 = addressLine2Formatter.parse(record);
                var paymentAmount = paymentAmountFormatter.parse(record);
                var additionalInfo = additionalInfoFormatter.parse(record);

                var billInfo = new QRBillInfo(name, addressLine1, addressLine2, paymentAmount, additionalInfo);

                var res = ErrorChecker.checkBillingInformation(billInfo, config.paymentAmountRequired());

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

