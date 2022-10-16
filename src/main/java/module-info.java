module qrbillius {

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // QR-Bill generator library
    requires qrbill.generator;


    // FontAwesome Icons
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;


    exports qrbillius.errors;
    exports qrbillius.qrbill;
    exports qrbillius.views;
    opens qrbillius.views to javafx.fxml;
    exports qrbillius;
    opens qrbillius to javafx.fxml;
}