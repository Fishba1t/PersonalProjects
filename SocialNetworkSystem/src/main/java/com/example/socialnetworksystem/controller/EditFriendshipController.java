package com.example.socialnetworksystem.controller;

import com.example.socialnetworksystem.domain.FriendRequest;
import com.example.socialnetworksystem.domain.Prietenie;
import com.example.socialnetworksystem.service.ServiceDB;
import com.example.socialnetworksystem.domain.Utilizator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditFriendshipController extends Controller{

    @FXML
    private TextField textFieldEmail1;

    @FXML
    private TextField textFieldEmail2;

    private ServiceDB service;
    Stage dialogStage;

    Controller controller;

    Prietenie prietenie;

    public void setService(ServiceDB service, Stage stage,Controller controller,Prietenie prietenie) {
        this.service = service;
        this.dialogStage = stage;
        this.controller=controller;
        this.prietenie=prietenie;
        if(this.prietenie!=null){
            textFieldEmail1.setText(prietenie.getUser1_email());
            textFieldEmail2.setText(prietenie.getUser2_email());
        }
    }

    @FXML
    public void setRequestACCEPTED(){
        prietenie.set_request(FriendRequest.ACCEPTED);
        service.modificaPrietenie(prietenie);
        this.controller.initModel();
    }

    @FXML
    public void setRequestREJECTED(){
        prietenie.set_request(FriendRequest.REJECTED);
        service.modificaPrietenie(prietenie);
        this.controller.initModel();
    }

    @FXML
    public void handleSave() {
        String email1 = textFieldEmail1.getText();
        String email2 = textFieldEmail2.getText();
        savePrietenie(email1,email2);
        controller.initModel();
    }


    private void savePrietenie(String email1, String email2) {
        try {
            if (email1 == null || email2 == null) {
                dialogStage.close();
            } else {
                Iterable<Prietenie> prietenii = service.findAll_DBNoLimitOffset();

                // Check if the friendship already exists
                boolean friendshipExists = false;
                for (Prietenie prietenie : prietenii) {
                    Utilizator u1 = prietenie.getUser1();
                    Utilizator u2 = prietenie.getUser2();

                    // Check if the friendship matches either way
                    if ((u1.getEmail().equals(email1) && u2.getEmail().equals(email2)) ||
                            (u1.getEmail().equals(email2) && u2.getEmail().equals(email1))) {
                        friendshipExists = true;
                        break;
                    }
                }

                if (!friendshipExists) {
                    // Friendship doesn't exist, create a new one
                    this.service.creazaPrietenie(email1, email2);
                    UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Friendship saved", "The Friendship has been saved");
                } else {
                    // Friendship already exists, show a message or handle accordingly
                    UserAlert.showMessage(null, Alert.AlertType.WARNING, "Friendship exists", "The Friendship already exists");
                }
            }
        } catch (RuntimeException e) {
            UserAlert.showErrorMessage(null, "Something went wrong!");
        }
        dialogStage.close();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}

