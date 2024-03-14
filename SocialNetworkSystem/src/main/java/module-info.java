module com.example.socialnetworksystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jshell;


    opens com.example.socialnetworksystem.service to javafx.fxml;
    exports com.example.socialnetworksystem.service;

    exports com.example.socialnetworksystem.controller;
    opens com.example.socialnetworksystem.controller to javafx.fxml;

    opens com.example.socialnetworksystem.repository to javafx.fxml;
    exports com.example.socialnetworksystem.repository;

    opens com.example.socialnetworksystem.domain to javafx.fxml;
    exports com.example.socialnetworksystem.domain;

    exports com.example.socialnetworksystem.validators;
    opens com.example.socialnetworksystem.validators to javafx.fxml;

    opens com.example.socialnetworksystem to javafx.fxml;
    exports com.example.socialnetworksystem;
}