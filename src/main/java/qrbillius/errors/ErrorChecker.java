package qrbillius.errors;

import net.codecrete.qrbill.generator.Payments;
import org.apache.pdfbox.pdmodel.PDDocument;
import qrbillius.config.ExportConfiguration;
import qrbillius.config.ImportConfiguration;
import qrbillius.config.ProfileConfiguration;
import qrbillius.qrbill.QRBillGenerator;
import qrbillius.qrbill.QRBillInfo;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class ErrorChecker {

    private static final BigDecimal AMOUNT_MAX = BigDecimal.valueOf(99999999999L, 2);       // 999 999 999.99
    private static final BigDecimal AMOUNT_MIN = BigDecimal.valueOf(1, 2);                  // 0.01

    private final ErrorResult result;

    private ErrorChecker() {
        result = new ErrorResult();
    }

    public static ErrorResult checkProfileConfig(ProfileConfiguration config) {
        var checker = new ErrorChecker();

        checker.checkAccount(config.account());
        checker.checkAddress(config.address().getName(), config.address().getAddressLine1(), config.address().getAddressLine2());

        return checker.getResult();
    }

    public static ErrorResult checkImportConfig(ImportConfiguration config) {
        var checker = new ErrorChecker();

        checker.checkCSVSeparator(config.csvSeparator());

        return checker.getResult();
    }

    public static ErrorResult checkExportConfig(ExportConfiguration config, int numberOfBills) {
        var checker = new ErrorChecker();

        checker.checkPDFTemplate(config, numberOfBills);

        return checker.getResult();
    }

    private void checkPDFTemplate(ExportConfiguration config, int numberOfBills) {
        if (config.enablePDFTemplate()) {
            var pdfFile = new File(config.pdfTemplate());

            try (var pdfDoc = PDDocument.load(pdfFile)) {
                var numPages = pdfDoc.getNumberOfPages();

                if (numPages != numberOfBills) {
                    result.addMessage(ErrorConstants.PDF_TEMPLATE_NUMBER_OF_PAGES_MISMATCH);
                }

            } catch (IOException e) {
                result.addMessage(ErrorConstants.IO_ERROR_OCCURRED, e.getLocalizedMessage());
            }
        }
    }

    public static ErrorResult checkBillingInformation(QRBillInfo info, boolean paymentAmountRequired) {
        var checker = new ErrorChecker();

        checker.checkAddress(info.getName(), info.getAddressLine1(), info.getAddressLine2());
        checker.checkPaymentAmount(info.getAmount(), paymentAmountRequired);
        checker.checkAdditionalInfo(info.getAdditionalInfo());
        return checker.getResult();
    }

    private ErrorResult getResult() {
        return result;
    }

    private void checkAccount(String account) {
        if (account.isBlank()) {
            result.addMessage(ErrorConstants.ACCOUNT_MISSING);
        } else if (!Payments.isValidIBAN(account)) {
            result.addMessage(ErrorConstants.ACCOUNT_INVALID);
        } else if (Payments.isQRIBAN(account)) {
            // QR IBAN numbers are currently not supported
            // since they need a special reference
            result.addMessage(ErrorConstants.QR_IBAN_UNSUPPORTED);
        }
    }

    private void checkAddress(String name, String addressLine1, String addressLine2) {
        if (name.isBlank())
            result.addMessage(ErrorConstants.NAME_FIELD_MISSING);
        else if (name.length() > 70)
            result.addMessage(ErrorConstants.NAME_FIELD_TOO_LONG);

        if (addressLine1.isBlank())
            result.addMessage(ErrorConstants.ADDRESS_LINE1_MISSING);
        else if (addressLine1.length() > 70)
            result.addMessage(ErrorConstants.ADDRESS_LINE1_TOO_LONG);

        if (addressLine2.isBlank())
            result.addMessage(ErrorConstants.ADDRESS_LINE2_MISSING);
        else if (addressLine2.length() > 70)
            result.addMessage(ErrorConstants.ADDRESS_LINE2_TOO_LONG);
    }

    private void checkAdditionalInfo(String additionalInfo) {
        if (additionalInfo.length() > 140)
            result.addMessage(ErrorConstants.ADDITIONAL_INFO_TOO_LONG);
    }

    private void checkPaymentAmount(String paymentAmount, boolean required) {
        try {
            var amount = QRBillGenerator.parsePaymentAmountWithoutZero(paymentAmount);

            // amount may be optional
            if (amount == null) {
                if (required)
                    result.addMessage(ErrorConstants.PAYMENT_AMOUNT_MISSING_OR_ZERO);
                return;
            }

            if (AMOUNT_MIN.compareTo(amount) > 0 || AMOUNT_MAX.compareTo(amount) < 0)
                result.addMessage(ErrorConstants.PAYMENT_AMOUNT_OUTSIDE_VALID_RANGE);

        } catch (NumberFormatException e) {
            result.addMessage(ErrorConstants.PAYMENT_AMOUNT_INVALID);
        }
    }

    private void checkCSVSeparator(String csvSeparator) {
        if (csvSeparator.isBlank()) {
            result.addMessage(ErrorConstants.CSV_SEPARATOR_EMPTY);
        }
    }
}
