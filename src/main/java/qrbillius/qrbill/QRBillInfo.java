package qrbillius.qrbill;

import javafx.beans.property.SimpleStringProperty;


/**
 * This class holds all the data of the recipient of a QR-Bill.
 */
public final class QRBillInfo {
    private final SimpleStringProperty name;
    private final SimpleStringProperty addressLine1;
    private final SimpleStringProperty addressLine2;
    private final SimpleStringProperty amount;
    private final SimpleStringProperty additionalInfo;

    public QRBillInfo(String name, String addressLine1, String addressLine2, String amount, String additionalInfo) {
        this.name = new SimpleStringProperty(name);
        this.addressLine1 = new SimpleStringProperty(addressLine1);
        this.addressLine2 = new SimpleStringProperty(addressLine2);
        this.amount = new SimpleStringProperty(amount);
        this.additionalInfo = new SimpleStringProperty(additionalInfo);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAddressLine1() {
        return addressLine1.get();
    }

    public SimpleStringProperty addressLine1Property() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1.set(addressLine1);
    }

    public String getAddressLine2() {
        return addressLine2.get();
    }

    public SimpleStringProperty addressLine2Property() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2.set(addressLine2);
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getAdditionalInfo() {
        return additionalInfo.get();
    }

    public SimpleStringProperty additionalInfoProperty() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo.set(additionalInfo);
    }

    @Override
    public String toString() {
        return "QRBillInfo[" +
                "name=" + name + ", " +
                "addressLine1=" + addressLine1 + ", " +
                "addressLine2=" + addressLine2 + ", " +
                "amount=" + amount + ", " +
                "additionalInfo=" + additionalInfo + ']';
    }
}
