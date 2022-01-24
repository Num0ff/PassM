package sample;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class EditController {

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
    private PasswordField RepeatPasswordField;

    @FXML
    private Button EditButton;

    @FXML
    private Button CancelButton;

    @FXML
    private TextField AccountPasswordFieldV;

    @FXML
    private TextField RepeatPasswordFieldV;

    @FXML
    private ImageView VisibleImage;


    @FXML
    private void EditAccount() throws Exception{
        String accountName = AccountNameField.getText();
        String login = AccountLoginField.getText();
        String password = controller.crypter.encrypt(AccountLoginField.getText(), AccountPasswordField.getText());
        try {
            CSVReader reader = new CSVReader(new FileReader(getClass().getResource("/").getPath()+"\\sample\\data\\data.csv"));
            List<String[]> allRows = reader.readAll();
            CSVWriter writer = new CSVWriter(new FileWriter(getClass().getResource("/").getPath()+"\\sample\\data\\data.csv", false));
            String[] strings = {AccountNameField.getText(),AccountLoginField.getText(),
                    controller.crypter.encrypt(AccountLoginField.getText(), AccountPasswordField.getText())};
            allRows.set(controller.getSelectedAccountIndex(), strings);
            writer.writeAll(allRows);
            reader.close();
            writer.close();
        } catch (CsvException | IOException e){
            e.printStackTrace();
        }
        controller.editAccount(accountName, login, password);
        Client.request(Client.EDIT_ACCOUNT,accountName, login, password);
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    void closeWindow(){
        Stage stage = (Stage) EditButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void regIs(){
        if (AccountPasswordField.getText().equals(controller.getAccount().getAccountPassword())){
            EditButton.setDisable(false);
        }else if(AccountPasswordField.getText().equals(RepeatPasswordField.getText())&&
                !(RepeatPasswordField.getText().equals("")|| RepeatPasswordField.getText() == null) &&
                !(AccountPasswordField.getText().equals("")|| AccountPasswordField.getText() == null)){
            EditButton.setDisable(false);
        }
        else{
            EditButton.setDisable(true);
        }
    }

    @FXML
    private void showPassword(){
        if(passwordVisible) {
            try {
                File file = new File(System.getProperty("user.dir") + "/src/sample/assets/Novisible32.png");
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
                File file = new File(System.getProperty("user.dir") + "/src/sample/assets/Visible.png");
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
    void initialize(){
        VisibleImage.setOnMouseClicked(event -> showPassword());
        passwordVisible = false;
        Runnable r = () ->{
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Account selectedAccount = controller.getAccount();
            AccountNameField.setText(selectedAccount.getAccountName());
            AccountLoginField.setText(selectedAccount.getAccountLogin());
            try {
                AccountPasswordField.setText(controller.crypter.decrypt(selectedAccount.getAccountLogin(),
                        selectedAccount.getAccountPassword()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            regIs();

        };
        new Thread(r, "Load").start();
    }

}
