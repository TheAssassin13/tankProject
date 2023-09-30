module com.example.tankproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.tankproject to javafx.fxml;
    exports com.example.tankproject;
}