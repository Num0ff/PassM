package sample;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerMain {
    private AddController addChildren;
    private EditController editChildren;
    private DeleteController deleteChildren;
    private ArrayList<Account> accounts = new ArrayList<Account>();
    private int accountsNumb;
    private int selectedAccountIndex;
    private boolean passwordVisible;
    private String user;

    int getAccountsNumb(){
        return accountsNumb;
    }
    int getSelectedAccountIndex(){
        return selectedAccountIndex;
    }
    Account getAccount(){
        return accounts.get(selectedAccountIndex);
    }
    Crypter crypter = new Crypter();
    void setAccountsNumb(int i){
        accountsNumb = i;
    }

    @FXML
    private static AnchorPane MainPanel;

    @FXML
    private ScrollPane AccountScroller;

    @FXML
    private FlowPane ScrollerContent;

    @FXML
    private ImageView VisibleImage;

    @FXML
    private Label AccountNameLabel;

    @FXML
    private TextField AccountLoginField;

    @FXML
    private PasswordField AccountPasswordField;

    @FXML
    private TextField AccountPasswordFieldVis;

    @FXML
    private ToggleButton PasswordVisible;

    @FXML
    private Button ShowButton;

    @FXML
    private Button AddButton;

    @FXML
    private Button EditButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private Button HideButton;

    @FXML
    private Button CloseButton;

    //Копирование в буфер обмена
    private static void setClipboard(String str) {
        StringSelection ss = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

    //Чтение файла с данными
    private void readCSV() throws Exception{
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(getClass().getResource("/").getPath()+"\\sample\\data\\data.csv"));
            List<String[]> allRows = reader.readAll();
            int i = 0;
            for(String[] row : allRows){
                //System.out.println(row[0] + " " + row[1]);
                addAccount(row[0], row[1], crypter.decrypt(row[1],row[2]), i);
                i++;
            }
            accountsNumb = i;
            reader.close();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    //Методы для записи данных в буфер обмена
    @FXML
    private void copyLogin(){
        setClipboard(AccountLoginField.getText());
    }
    @FXML
    private void copyPassword(){
        setClipboard(AccountPasswordField.getText());
    }

    //Методы, вызывающие формы для основных кнопок
    @FXML
    private void openAddAccount(){
        Parent addParent;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add.fxml"));
            addParent = loader.load();
            Stage addWindow = new Stage();
            addWindow.initStyle(StageStyle.TRANSPARENT);
            addWindow.setResizable(false);
            addWindow.setTitle("Создание аккаунта");
            addWindow.setScene(new Scene(addParent));
            addWindow.show();
            addChildren = loader.getController();
            addChildren.setParent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openEditAccount(){
        if (selectedAccountIndex>=0) {
            Parent editParent;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("edit.fxml"));
                editParent = loader.load();
                Stage editWindow = new Stage();
                editWindow.initStyle(StageStyle.TRANSPARENT);
                editWindow.setResizable(false);
                editWindow.setTitle("Редактирование аккаунта");
                editWindow.setScene(new Scene(editParent));
                editWindow.show();
                editChildren = loader.getController();
                editChildren.setParent(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void openDeleteAccount(){
        if (selectedAccountIndex>=0) {
            Parent deleteParent;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("delete.fxml"));
                deleteParent = loader.load();
                Stage deleteWindow = new Stage();
                deleteWindow.initStyle(StageStyle.TRANSPARENT);
                deleteWindow.setResizable(false);
                deleteWindow.setScene(new Scene(deleteParent));
                deleteWindow.show();
                deleteChildren = loader.getController();
                deleteChildren.setParent(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Очистка полей и отмена выбора аккаунта
    @FXML
    private void cleanFields(){
        AccountNameLabel.setText("Имя аккаунта");
        AccountLoginField.setText("");
        AccountPasswordField.setText("");
        AccountPasswordFieldVis.setText("");
        selectedAccountIndex = -1;
    }

    //Вспомогательные методы для кнопок
    void addAccount(String name, String login, String password, int index) throws Exception{
        Account account = new Account(name, login, crypter.encrypt(login, password), index);
        account.setOnAction(event -> {
            AccountNameLabel.setText(account.getAccountName());
            AccountLoginField.setText(account.getAccountLogin());
            try{
                AccountPasswordField.setText(crypter.decrypt(account.getAccountLogin(),account.getAccountPassword()));
                AccountPasswordFieldVis.setText(crypter.decrypt(account.getAccountLogin(),account.getAccountPassword()));

            }catch (Exception e){
                e.printStackTrace();
            }
            selectedAccountIndex = account.getAccountIndex();
        });
        ScrollerContent.getChildren().add(account);
        accounts.add(account);
    }

    void editAccount(String name, String login, String password) throws Exception {
        accounts.get(selectedAccountIndex).setAccountName(name);
        accounts.get(selectedAccountIndex).setAccountLogin(login);
        password = crypter.encrypt(login ,password);
        accounts.get(selectedAccountIndex).setAccountPassword(password);
        ScrollerContent.getChildren().setAll();
        readCSV();
    }

    void deleteAccount(){
        try {
            Client.request(Client.DELETE_ACCOUNT,getAccount().getAccountName());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        accounts.remove(selectedAccountIndex);
        try {
            CSVReader reader = new CSVReader(new FileReader(getClass().getResource(
                    "/").getPath()+"\\sample\\data\\data.csv"));
            List<String[]> allRows = reader.readAll();
            allRows.remove(selectedAccountIndex);
            CSVWriter writer = new CSVWriter(new FileWriter(getClass().getResource(
                    "/").getPath()+"\\sample\\data\\data.csv", false));
            writer.writeAll(allRows);
            reader.close();
            writer.close();
        } catch (CsvException | IOException e){
            e.printStackTrace();
        }
        ScrollerContent.getChildren().setAll();
        try {
            readCSV();
        }catch (Exception e){
            e.printStackTrace();
        }
        cleanFields();
    }

    @FXML
    private void showPassword(){
        if(passwordVisible) {
            try {
                File file = new File(getClass().getResource(
                        "/").getPath()+"\\sample\\assets\\Novisible32.png");
                VisibleImage.setImage(new Image(file.toURI().toURL().toString()));
                AccountPasswordFieldVis.setVisible(false);
                AccountPasswordFieldVis.setDisable(true);
                AccountPasswordField.setText(AccountPasswordFieldVis.getText());
                AccountPasswordField.setVisible(true);
                AccountPasswordField.setDisable(false);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            passwordVisible = false;
        }else{
            try {
                File file = new File(getClass().getResource(
                        "/").getPath()+"\\sample\\assets\\Visible.png");
                VisibleImage.setImage(new Image(file.toURI().toURL().toString()));
                AccountPasswordField.setVisible(false);
                AccountPasswordField.setDisable(true);
                AccountPasswordFieldVis.setText(AccountPasswordField.getText());
                AccountPasswordFieldVis.setVisible(true);
                AccountPasswordFieldVis.setDisable(false);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            passwordVisible = true;
        }
    }

    void synchronizeServer(){
        CSVReader reader = null;
        List<String[]> allRows = null;
        try {
            reader = new CSVReader(new FileReader(getClass().getResource(
                    "/").getPath()+"\\sample\\data\\data.csv"));
            allRows = reader.readAll();
            reader.close();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        JSONArray array = new JSONArray();
        JSONObject jobject;
        for(String[] row: allRows){
            jobject = new JSONObject();
            jobject.put("name", row[0]);
            jobject.put("login", row[1]);
            jobject.put("password", row[2]);
            array.add(jobject);
        }
        Client.sync(array);
    }

    //Метод, сворачивающий программу
    @FXML
    private void hideWindow(){
        Stage stage = (Stage) HideButton.getScene().getWindow();
        stage.setIconified(true);
    }
    //Метод, закрывающий программу
    @FXML
    void closeWindow(){
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
        synchronizeServer();
    }

    @FXML
    void initialize() {
        selectedAccountIndex = -1;
        try {
            readCSV();
        }catch (Exception e){
            e.printStackTrace();
        }
        VisibleImage.setOnMouseClicked(event -> showPassword());
        passwordVisible = false;
    }
}
