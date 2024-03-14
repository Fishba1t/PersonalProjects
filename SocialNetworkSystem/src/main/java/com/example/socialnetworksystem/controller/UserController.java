package com.example.socialnetworksystem.controller;

import com.example.socialnetworksystem.domain.FriendRequest;
import com.example.socialnetworksystem.domain.Prietenie;
import com.example.socialnetworksystem.domain.Utilizator;
import com.example.socialnetworksystem.service.ServiceDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.socialnetworksystem.domain.FriendRequest.*;

public class UserController {
    private ServiceDB service;
    private Stage stage;
    private Utilizator user;

    public TextField number_of_entities;

    @FXML
    public ObservableList<Utilizator> model_utilizator = FXCollections.observableArrayList();

    @FXML
    public ObservableList<Prietenie> model_friendships = FXCollections.observableArrayList();

    @FXML
    public ObservableList<Prietenie> model_friendrequests = FXCollections.observableArrayList();
    @FXML
    public TableView<Utilizator> userTableView = new TableView<>();

    @FXML
    public TableColumn<Utilizator, String> userFirstName = new TableColumn<>();
    @FXML
    public TableColumn<Utilizator, String> userLastName = new TableColumn<>();

    @FXML
    public TableColumn<Utilizator, String> userEmail = new TableColumn<>();

    @FXML
    public TableView<Prietenie> friendRequestsTableView = new TableView<>();
    @FXML
    public TableColumn<Prietenie, String> from = new TableColumn<>();

    @FXML
    public TableColumn<Prietenie, String> to = new TableColumn<>();

    @FXML
    public TableColumn<Prietenie, FriendRequest> request = new TableColumn<>();

    @FXML
    public TableView<Prietenie> friendshipTableView = new TableView<>();
    @FXML
    public TableColumn<Prietenie, String> email1 = new TableColumn<>();

    @FXML
    public TableColumn<Prietenie, String> email2 = new TableColumn<>();

    @FXML
    public TableColumn<Prietenie, LocalDateTime> friendshipDate = new TableColumn<>();

    public void setService(ServiceDB serv,Stage stage,Utilizator u){
        this.service=serv;
        this.stage=stage;
        this.user=u;
        initModel();
    }

    @FXML
    public void initialize() {
        this.userFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.userLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.userEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.userTableView.setItems(this.model_utilizator);
        this.from.setCellValueFactory(new PropertyValueFactory<>("user1_email"));
        this.to.setCellValueFactory(new PropertyValueFactory<>("user2_email"));
        this.request.setCellValueFactory(new PropertyValueFactory<>("request"));
        this.friendRequestsTableView.setItems(this.model_friendrequests);
        this.email1.setCellValueFactory(new PropertyValueFactory<>("user1_email"));
        this.email2.setCellValueFactory(new PropertyValueFactory<>("user2_email"));
        this.friendshipDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.friendshipTableView.setItems(this.model_friendships);
    }

    public void initModel() {
        initialize();
        Iterable<Utilizator> allUsers = service.getAllUtilizatori();
        List<Utilizator> utilizatori = new ArrayList<>();
        for(Utilizator u : allUsers){
            if(!Objects.equals(u.getEmail(), this.user.getEmail())){
                utilizatori.add(u);
            }
        }
        Iterable<Prietenie>request=this.service.findAll_Request(this.user.getId());
        Iterable<Prietenie>friendships1=this.service.getAll_FriendshipsVerificare(this.user.getId());
        List<Prietenie> friendrequests = new ArrayList<>();
        List<Prietenie> friendships = new ArrayList<>();
        for(Prietenie p : request) {
            friendrequests.add(p);
        }

        for(Prietenie p : friendships1) {{
                friendships.add(p);
            }

        }
        this.model_utilizator.setAll(utilizatori);
        this.model_friendships.setAll(friendships);
        this.model_friendrequests.setAll(friendrequests);
    }


    public void send_fr(){
        Utilizator selected = userTableView.getSelectionModel().getSelectedItem();
        try {
            if (selected != null) {
                this.service.creazaPrietenie(this.user.getEmail(), selected.getEmail());
                initModel();
            } else {
                UserAlert.showErrorMessage(null, "YOU NEED TO SELECT AN USER");
            }
        }
        catch (RuntimeException e) {
            UserAlert.showErrorMessage(null, "The friendship ALREADY exists!");
        }

    }

    public void delete_fr(){
        Prietenie selected = friendshipTableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            this.service.stergePrietenie(selected.getUser1_email(), selected.getUser2_email());
            initModel();
        }
        else{
            UserAlert.showErrorMessage(null,"YOU NEED TO SELECT A FRIENDSHIP");
        }
    }

    public void accept_fr() {
        Prietenie selected = friendRequestsTableView.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.getUser1_email().equals(this.user.getEmail())) {
            selected.set_request(ACCEPTED);
            this.service.modificaPrietenie(selected);
            System.out.println("Emailurile sunt : " + selected.getUser1_email() + " si " + selected.getUser2_email());
            initModel();
        } else {
            UserAlert.showErrorMessage(null, "YOU NEED TO SELECT A FRIEND REQUEST OR YOU CANNOT ACCEPT YOUR OWN REQUEST");
        }
    }


    public void decline_fr(){
        Prietenie selected = friendRequestsTableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            selected.set_request(REJECTED);
            this.service.modificaPrietenie(selected);
            this.service.declineFriendRequest(selected.getUser1_email(), selected.getUser2_email());
            initModel();
        }
        else{
            UserAlert.showErrorMessage(null,"YOU NEED TO SELECT A FRIEND REQUEST");
        }
    }

    public void message(ActionEvent actionEvent) {
        showMessageEditDialog();
    }

    public void showMessageEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader new_loader = new FXMLLoader();
            new_loader.setLocation(getClass().getResource("/com/example/socialnetworksystem/messages.fxml"));


            AnchorPane root = new_loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Messages");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MessageController editMessageViewController = new_loader.getController();
            editMessageViewController.setService(this.service, dialogStage, this,this.user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handlePreviousFriendships() {
        int o = this.service.get_offset_friendships_user();
        int l = this.service.get_limit();
        if (o - l >= 0) {
            this.service.set_offset_friendships(o - l);
            initModel();
        } else {
            UserAlert.showErrorMessage(null, "There are no previous friendships!");
        }
    }


    public void handleNextFriendships() {
        int o = this.service.get_offset_friendships_user();
        int l = this.service.get_limit();
        int max = this.service.get_countFriendships(this.user.getId());
        if (o + l < max) {
            this.service.set_offset_friendships(o + l);
            initModel();
        } else {
            UserAlert.showErrorMessage(null, "There are no more friendships!");
        }
    }

    public void handleNextRequest() {
        int o = this.service.get_offset_requests();
        int l = this.service.get_limit();
        int max = this.service.get_number_of_request(this.user.getId());
        System.out.println(max);
        if (o + l < max) {
            this.service.set_offset_requests_user(o + l);
            initModel();
        } else {
            UserAlert.showErrorMessage(null, "There are no more requests!");
        }
    }

    public void handlePreviousRequest(){
        int o = this.service.get_offset_requests();
        int l = this.service.get_limit();
        if (o - l >= 0) {
            this.service.set_offset_requests_user(o - l);
            initModel();
        } else {
            UserAlert.showErrorMessage(null, "There are no previous requests!");
        }
    }
    public void showController() {
        String n = this.number_of_entities.getText();
        try {
            int x = Integer.parseInt(n);
            if (x > 0) {
                this.service.set_limit_offset(x, 0);
                initModel();
            }
        } catch (NumberFormatException eroare) {
            UserAlert.showErrorMessage(null, "YOU NEED TO SELECT A NUMBER OF USERS ON PAGE");
        }
    }

}
