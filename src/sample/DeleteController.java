package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DeleteController {
    private ControllerMain controller;

    void setParent (ControllerMain controller){
        this.controller = controller;
    }

    @FXML
    private Button DeleteButton;

    @FXML
    private Button CancelButton;

    @FXML
    private void deleteAccount(){
        controller.deleteAccount();
        Stage stage = (Stage) DeleteButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void closeWindow(){
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

}
