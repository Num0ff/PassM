package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registration {


    @FXML
    private TextField MyLoginField;

    @FXML
    private PasswordField MyPasswordField;

    @FXML
    private PasswordField MyPasswordField1;

    @FXML
    private Button JoinButton;

    @FXML
    private void regIs(){
        JoinButton.setDisable(!MyPasswordField.getText().equals(MyPasswordField1.getText()) ||
                (MyPasswordField.getText().equals("") || MyPasswordField.getText() == null) ||
                (MyPasswordField1.getText().equals("") || MyPasswordField1.getText() == null));
    }

    //Метод, сворачивающий программу
    @FXML
    private void hideWindow(){
        Stage stage = (Stage) JoinButton.getScene().getWindow();
        stage.setIconified(true);
    }

    //Метод, закрывающий программу
    @FXML
    void closeWindow(){
        Stage stage = (Stage) JoinButton.getScene().getWindow();
        stage.close();
    }

    String getHash(MessageDigest messageDigest, String str){
        messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        byte[]digest = messageDigest.digest();
        return String.format("%064x", new BigInteger(1, digest));
    }

    @FXML
    void auto(ActionEvent event) throws IOException, NoSuchAlgorithmException, InterruptedException {
        // Получение логина и пароля
        String login = MyLoginField.getText();
        String password = MyPasswordField.getText();

        // Хэширование логина
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        login = getHash(md, login);

        // Хэширование пароля
        password = getHash(md, password);
        password = getHash(md, password);

        String path = getClass().getResource("/").getPath()+"\\sample\\data\\" + login + ".txt";
        File file = new File(path);
        FileWriter fr = new FileWriter(file);
        fr.write(password);
        fr.flush();
        Thread.sleep(2000);
        JoinButton.setDisable(true);
        file.setWritable(false);
        file.setReadOnly();
        try {
            JSONObject data = Client.request(0, login, password);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("start.fxml"));
        Parent root = loader.getRoot();
        Stage startWindow = new Stage();
        startWindow.initStyle(StageStyle.UNDECORATED);
        startWindow.setResizable(false);
        startWindow.setTitle("PassM");
        startWindow.setScene(new Scene(root));
        startWindow.show();

        Stage stage = (Stage) JoinButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize(){
        regIs();
    }
}
