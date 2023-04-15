module qrbillius {
    requires javafx.controls;
    requires javafx.fxml;

    requires commons.io;
    requires commons.csv;
    requires org.apache.pdfbox;

    requires qrbill.generator;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;
    requires java.xml;

    exports qrbillius;
    exports qrbillius.errors;
    exports qrbillius.views;
    exports qrbillius.qrbill;
    exports qrbillius.config;

    opens qrbillius to javafx.fxml;
    opens qrbillius.views to javafx.fxml;
}