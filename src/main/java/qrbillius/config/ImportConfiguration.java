package qrbillius.config;

public record ImportConfiguration(String csvSeparator,
                                  String nameFormat,
                                  String addressLine1Format,
                                  String addressLine2Format,
                                  String paymentAmountFormat,
                                  boolean paymentAmountRequired,
                                  String additionalInfoFormat) {
}
