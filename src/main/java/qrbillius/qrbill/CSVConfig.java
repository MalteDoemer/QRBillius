package qrbillius.qrbill;

public record CSVConfig(String separator,
                        String nameFormat,
                        String addressLine1Format,
                        String addressLine2Format,
                        String paymentAmountFormat,
                        String additionalInfoFormat) {
}
