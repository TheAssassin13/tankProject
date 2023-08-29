module com.example.tankproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tankproject to javafx.fxml;
    exports com.example.tankproject;
}