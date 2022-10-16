package qrbillius.errors;

import net.codecrete.qrbill.generator.*;
import qrbillius.SettingsManager;
import qrbillius.qrbill.QRBillGenerator;
import qrbillius.qrbill.QRBillInfo;

import java.math.BigDecimal;

public class ErrorChecker {

    private static final BigDecimal AMOUNT_MAX = BigDecimal.valueOf(99999999999L, 2);       // 999 999 999.99
    private static final BigDecimal AMOUNT_MIN = BigDecimal.valueOf(1, 2);                  // 0.01

    private final ErrorResult result;

    private ErrorChecker() {
        result = new ErrorResult();
    }

    public static ErrorResult checkSettings(SettingsManager settings) {
        var checker = new ErrorChecker();

        checker.checkAccount(settings.getCreditorAccount());
        checker.checkAddress(settings.getCreditorName(), settings.getCreditorAddressLine1(), settings.getCreditorAddressLine2());

        return checker.getResult();
    }

    public static ErrorResult checkBillingInformation(QRBillInfo info) {
        var checker = new ErrorChecker();

        checker.checkAddress(info.name(), info.addressLine1(), info.addressLine2());
        checker.checkPaymentAmount(info.amount());
        checker.checkAdditionalInfo(info.additionalInfo());

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

    private void checkPaymentAmount(String paymentAmount) {
        try {
            var amount = QRBillGenerator.parsePaymentAmount(paymentAmount);

            // amount is optional
            if (amount == null)
                return;

            if (AMOUNT_MIN.compareTo(amount) > 0 || AMOUNT_MAX.compareTo(amount) < 0)
                result.addMessage(ErrorConstants.PAYMENT_AMOUNT_OUTSIDE_VALID_RANGE);

        } catch (NumberFormatException e) {
            result.addMessage(ErrorConstants.PAYMENT_AMOUNT_INVALID);
        }
    }
}
