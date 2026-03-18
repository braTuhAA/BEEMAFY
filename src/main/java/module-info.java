module com.beemafy {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.beemafy to javafx.fxml;
    exports com.beemafy;
}