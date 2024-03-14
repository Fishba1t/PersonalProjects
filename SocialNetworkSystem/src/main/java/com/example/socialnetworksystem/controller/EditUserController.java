package com.example.socialnetworksystem.controller;

import com.example.socialnetworksystem.domain.Utilizator;
import com.example.socialnetworksystem.service.ServiceDB;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Objects;

public class EditUserController extends Controller{
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldPassword;

    private ServiceDB service;
    Stage dialogStage;
    Utilizator user;
    Controller controller;

    public void setService(ServiceDB service, Stage stage, Utilizator u,Controller controller) {
        this.service = service;
        this.dialogStage = stage;
        this.user = u;
        this.controller=controller;
    }

    @FXML
    public void handleSave() {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();
        if(Objects.equals(firstName, "") || Objects.equals(lastName, "") || Objects.equals(email, "")){
            UserAlert.showErrorMessage(null,"It didn't work");
            return;
        }
        Utilizator u = new Utilizator (firstName, lastName, email, password);
        if (null == this.user)
            saveUser(u);
        controller.initModel();
    }

    @FXML
    public void handleUpdate(){
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        if(Objects.equals(firstName, "") || Objects.equals(lastName, "") || Objects.equals(email, "")){
            UserAlert.showErrorMessage(null,"Something went wrong!");
            return;
        }
        updateUser(user,lastName,firstName,email);
        controller.initModel();
    }

    @FXML
    private void updateUser(Utilizator u,String n,String l, String e) {
        try {
            this.service.modifica_utilizator(u,n,l,e);
            if (u == null)  {
                dialogStage.close();
            }
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "User updated", "The user has been updated");

        } catch (RuntimeException el) {
            UserAlert.showErrorMessage(null,"Something went wrong!");
        }
        dialogStage.close();
    }

    private void saveUser(Utilizator u) {
        try {
            if (this.service.userExist(u)) {
                UserAlert.showErrorMessage(null,"The user already exists! The email or password are already taken!");
            } else {
                this.service.adaugare_utilizator(u);
                UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "User saved", "The user has been saved");
            }
        } catch (RuntimeException e) {
            UserAlert.showErrorMessage(null, "Something went wrong!");
        } finally {
            dialogStage.close();
        }
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}
