package com.example.socialnetworksystem;

import com.example.socialnetworksystem.controller.StartController;
import com.example.socialnetworksystem.repository.FriendshipDatabase;
import com.example.socialnetworksystem.repository.MessageDatabase;
import com.example.socialnetworksystem.repository.UserDatabase;
import com.example.socialnetworksystem.service.ServiceDB;
import com.example.socialnetworksystem.validators.MessagesValidator;
import com.example.socialnetworksystem.validators.PrietenieValidator;
import com.example.socialnetworksystem.validators.UtilizatorValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    private ServiceDB service;
    @Override
    public void start(Stage primaryStage) throws IOException {

        String URL = "jdbc:postgresql://localhost:5432/socialnetwork89login";
        String username = "postgres";
        String password = "ovidiu123";
        UtilizatorValidator validatorUser = new UtilizatorValidator();
        PrietenieValidator prietenieValidator = new PrietenieValidator();
        MessagesValidator messagesValidator = new MessagesValidator();
        UserDatabase userDBRepository = new UserDatabase(URL, username, password, validatorUser);
        FriendshipDatabase friendshipDBRepository = new FriendshipDatabase(URL, username, password,prietenieValidator);
        MessageDatabase messageDBRepository = new MessageDatabase(URL, username, password, messagesValidator);

        this.service = new ServiceDB(userDBRepository, friendshipDBRepository,messageDBRepository);

        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("start.fxml"));
        AnchorPane userLayout = userLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        StartController startController = userLoader.getController();
        startController.setService(this.service,primaryStage);
    }

}