module com.example.a4mazilupaul {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.junit.jupiter.api;
    requires org.xerial.sqlitejdbc;

    opens com.example.a4mazilupaul to javafx.fxml;
    exports com.example.a4mazilupaul;
}