module com.example.oopcontactmanagementfxml {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.oopcontactmanagementfxml to javafx.fxml;
    exports com.example.oopcontactmanagementfxml;
}