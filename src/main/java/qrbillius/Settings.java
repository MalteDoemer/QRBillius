package qrbillius;

import net.codecrete.qrbill.generator.Address;
import net.codecrete.qrbill.generator.Language;

import java.util.Objects;

public final class Settings {
    private String account;
    private Address address;
    private Language language;
    private String csvSeparator;
    private String nameFormat;
    private String addressLine1Format;
    private String addressLine2Format;
    private String paymentAmountFormat;
    private String additionalInfoFormat;

    public Settings(String account,
                    Address address,
                    Language language,
                    String csvSeparator,
                    String nameFormat,
                    String addressLine1Format,
                    String addressLine2Format,
                    String paymentAmountFormat,
                    String additionalInfoFormat) {
        this.account = account;
        this.address = address;
        this.language = language;
        this.csvSeparator = csvSeparator;
        this.nameFormat = nameFormat;
        this.addressLine1Format = addressLine1Format;
        this.addressLine2Format = addressLine2Format;
        this.paymentAmountFormat = paymentAmountFormat;
        this.additionalInfoFormat = additionalInfoFormat;
    }

    public String account() {
        return account;
    }

    public Address address() {
        return address;
    }

    public Language language() {
        return language;
    }

    public String csvSeparator() {
        return csvSeparator;
    }

    public String nameFormat() {
        return nameFormat;
    }

    public String addressLine1Format() {
        return addressLine1Format;
    }

    public String addressLine2Format() {
        return addressLine2Format;
    }

    public String paymentAmountFormat() {
        return paymentAmountFormat;
    }

    public String additionalInfoFormat() {
        return additionalInfoFormat;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setCsvSeparator(String csvSeparator) {
        this.csvSeparator = csvSeparator;
    }

    public void setNameFormat(String nameFormat) {
        this.nameFormat = nameFormat;
    }

    public void setAddressLine1Format(String addressLine1Format) {
        this.addressLine1Format = addressLine1Format;
    }

    public void setAddressLine2Format(String addressLine2Format) {
        this.addressLine2Format = addressLine2Format;
    }

    public void setPaymentAmountFormat(String paymentAmountFormat) {
        this.paymentAmountFormat = paymentAmountFormat;
    }

    public void setAdditionalInfoFormat(String additionalInfoFormat) {
        this.additionalInfoFormat = additionalInfoFormat;
    }
}
