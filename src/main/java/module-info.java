module com.projet.projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;

    opens com.projet.projet to javafx.fxml, com.google.gson;
    exports com.projet.projet;
}