package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

public class Main extends Application {
    public static Client client;


    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            client = new Client("localhost", 8989);
            Parent root = FXMLLoader.load(getClass().getResource("start.fxml"));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setResizable(false);
            primaryStage.setTitle("PassM");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (ConnectException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка соединения с сервером");
            alert.setContentText("Сервер временно недоступен");
            alert.showAndWait();
            this.stop();
        }
        /*boolean fileIsExist;
        try{
            File file = new File(getClass().getResource("/").getPath()+ "\\sample\\data\\p.txt");
            fileIsExist = file.exists();
        }catch (NullPointerException e){
            fileIsExist = false;
        }*/


    }

    public static void main(String[] args) {
        launch(args);
    }

}
