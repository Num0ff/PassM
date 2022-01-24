package sample;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Start {

    @FXML
    private TextField MyLoginField;

    @FXML
    private Button JoinButton;

    @FXML
    private PasswordField MyPasswordField;

    @FXML
    private Button RegButton;

    /*//Чтение файла с данными
    private void readCSV() throws Exception{
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(System.getProperty("user.dir")+"/src/sample/data/data.csv"));
            List<String[]> allRows = reader.readAll();
            int i = 0;
            for(String[] row : allRows){
                addAccount(row[0], row[1], crypter.decrypt(row[1],row[2]), i);
                i++;
            }
            accountsNumb = i;
            reader.close();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    void registration(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("registration.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage registrationWindow = new Stage();
        registrationWindow.initStyle(StageStyle.UNDECORATED);
        registrationWindow.setResizable(false);
        registrationWindow.setTitle("PassM");
        registrationWindow.setScene(new Scene(root));
        registrationWindow.show();

        Stage stage = (Stage) JoinButton.getScene().getWindow();
        stage.close();
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

    @FXML
    void autorization() throws NoSuchAlgorithmException, IOException, ParseException {
        // Получение значений логина и пароля
        String login = MyLoginField.getText();
        String password = MyPasswordField.getText();
        String pass = null;

        // Хэширование логина
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(login.getBytes(StandardCharsets.UTF_8));
        byte[]digest = md.digest();
        login = String.format("%064x", new BigInteger(1, digest));


        // Хэширование пароля 2 раза
        md.update(password.getBytes(StandardCharsets.UTF_8));
        digest = md.digest();
        password = String.format("%064x", new BigInteger(1, digest));
        md.update(password.getBytes(StandardCharsets.UTF_8));
        digest = md.digest();
        password = String.format("%064x", new BigInteger(1, digest));

        // Чтение хэша пароля из файла
        try{
            BufferedReader br = new BufferedReader(new FileReader(getClass().getResource("/").getPath()+
                    "\\sample\\data\\p.txt"));
            pass = br.readLine();
        }catch (FileNotFoundException e){
            pass = "";
        }

        // Отправка запроса на получение хэша пароля и
        try {
            JSONObject data = Client.request(Client.LOGIN, login, pass);
            String res = (String) data.get("result");
            if (!res.equals("0")){
                String path = getClass().getResource("/").getPath()+"\\sample\\data\\p.txt";
                File file = new File(path);
                FileWriter fr = new FileWriter(file);
                String result= (String) data.get("result");
                if (!result.equals("0")){
                    pass = result;
                    fr.write(result);
                }
                fr.flush();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Обработка
        if(password.equals(pass)){
            Client.setUser(login);

            JSONArray accountsFromServer = Client.loadingAccounts(login);
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
            JSONArray object = null;
            if (allRows != null) {
                for (Object afsrow: accountsFromServer){
                    object = (JSONArray) afsrow;
                    boolean alreadyHave = false;
                    for (String[] row: allRows) {
                        alreadyHave = (row[0].equals(object.get(2)));
                        if(alreadyHave){
                            if(!row[1].equals(object.get(3))) {
                                row[1] = (String) object.get(3);
                            }
                            if(!row[2].equals(object.get(4))) {
                                row[2] = (String) object.get(4);
                            }
                            break;
                        }
                    }
                    if(!alreadyHave){
                        String[] account = {(String)object.get(2), (String)object.get(3), (String)object.get(4)};
                        allRows.add(account);
                    }
                }
                CSVWriter writer = new CSVWriter(new FileWriter(getClass().getResource(
                        "/").getPath()+"\\sample\\data\\data.csv"));
                writer.writeAll(allRows);
                writer.flush();
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
                Parent activateParent;
                FXMLLoader loader = new FXMLLoader();
                activateParent = loader.load(getClass().getResource("sample.fxml"));
                Stage activateWindow = new Stage();
                activateWindow.initStyle(StageStyle.TRANSPARENT);
                activateWindow.setResizable(false);
                activateWindow.setTitle("PassM");
                activateWindow.setScene(new Scene(activateParent));
                activateWindow.show();
            }else{
                String path = getClass().getResource("/").getPath()+"/sample/data/data.csv";
                File file = new File(path);
                FileWriter fr = new FileWriter(file);
                fr.write(password);
                fr.flush();
            }

            Stage stage = (Stage) JoinButton.getScene().getWindow();
            stage.close();
        }else {
            System.out.println("Nothing!");
        }
    }
}

