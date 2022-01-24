package sample;

import com.opencsv.CSVWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

public class AddController {

    private ControllerMain controller;
    private boolean passwordVisible;
    void setParent (ControllerMain controller){
        this.controller = controller;
    }

    @FXML
    private TextField AccountNameField;

    @FXML
    private TextField AccountLoginField;

    @FXML
    private PasswordField AccountPasswordField;

    @FXML
    private TextField AccountPasswordFieldV;

    @FXML
    private PasswordField RepeatPasswordField;

    @FXML
    private TextField RepeatPasswordFieldV;

    @FXML
    private Button CreationButton;

    @FXML
    private Button CancelButton;

    @FXML
    private ImageView VisibleImage;

    @FXML
    private void regIs(){
        if(RepeatPasswordField.getText().equals(AccountPasswordField.getText())&&
                !(RepeatPasswordField.getText().equals("")|| RepeatPasswordField.getText() == null) &&
                !(AccountPasswordField.getText().equals("")|| AccountPasswordField.getText() == null)){
            CreationButton.setDisable(false);
        }
        else{
            CreationButton.setDisable(true);
        }
    }
    @FXML
    private void createAccount()throws Exception{
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(getClass().getResource(
                    "/").getPath()+"\\sample\\data\\data.csv", true));
            String accountName = AccountNameField.getText();
            String login = AccountLoginField.getText();
            String password = controller.crypter.encrypt(AccountLoginField.getText(), AccountPasswordField.getText());
            String data = accountName + "," + login + "," + password;
            Client.request(Client.ADD_ACCOUNT,accountName, login, password);
            String[] record = data.split(",");
            writer.writeNext(record);
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        controller.addAccount(AccountNameField.getText(), AccountLoginField.getText(),
                AccountPasswordField.getText(), controller.getAccountsNumb());
        controller.setAccountsNumb(controller.getAccountsNumb()+1);
        Stage stage = (Stage) CreationButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void showPassword(){
        if(passwordVisible) {
            try {
                File file = new File(getClass().getResource(
                        "/").getPath()+"/sample/assets/Novisible32.png");
                VisibleImage.setImage(new Image(file.toURI().toURL().toString()));
                AccountPasswordFieldV.setVisible(false);
                AccountPasswordFieldV.setDisable(true);
                AccountPasswordField.setText(AccountPasswordFieldV.getText());
                AccountPasswordField.setVisible(true);
                AccountPasswordField.setDisable(false);
                RepeatPasswordFieldV.setVisible(false);
                RepeatPasswordFieldV.setDisable(true);
                RepeatPasswordField.setText(RepeatPasswordFieldV.getText());
                RepeatPasswordField.setVisible(true);
                RepeatPasswordField.setDisable(false);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            passwordVisible = false;
        }else{
            try {
                File file = new File(getClass().getResource(
                        "/").getPath()+"/sample/assets/Visible.png");
                VisibleImage.setImage(new Image(file.toURI().toURL().toString()));
                AccountPasswordField.setVisible(false);
                AccountPasswordField.setDisable(true);
                AccountPasswordFieldV.setText(AccountPasswordField.getText());
                AccountPasswordFieldV.setVisible(true);
                AccountPasswordFieldV.setDisable(false);
                RepeatPasswordField.setVisible(false);
                RepeatPasswordField.setDisable(true);
                RepeatPasswordFieldV.setText(RepeatPasswordField.getText());
                RepeatPasswordFieldV.setVisible(true);
                RepeatPasswordFieldV.setDisable(false);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            passwordVisible = true;
        }
    }

    @FXML
    void closeWindow(){
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize(){
        regIs();
        VisibleImage.setOnMouseClicked(event -> showPassword());
        passwordVisible = false;
    }
}
