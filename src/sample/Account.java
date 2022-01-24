package sample;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

class Account extends Button {
    private int accountId;
    private String accountName;
    private String accountLogin;
    private String accountPassword;
    private int accountIndex;
    Account(String name, String login, String password, int index){
        super(name);
        accountName = name;
        accountLogin = login;
        accountPassword = password;
        accountIndex = index;

        this.setPrefSize(230,60);
        this.setStyle("-fx-background-color: #ffffff;");
        this.setCursor(Cursor.HAND);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(21.0);
        dropShadow.setWidth(21.0);
        dropShadow.setRadius(2.0);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.color(0.5, 0.5, 0.5));
        this.setEffect(dropShadow);
        this.setId("AccountButton"+index);
    }

    Account(int id, String name, String login, String password, int index){
        super(name);
        accountId = id;
        accountName = name;
        accountLogin = login;
        accountPassword = password;
        accountIndex = index;

        this.setPrefSize(230,60);
        this.setStyle("-fx-background-color: #ffffff;");
        this.setCursor(Cursor.HAND);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(21.0);
        dropShadow.setWidth(21.0);
        dropShadow.setRadius(2.0);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.color(0.5, 0.5, 0.5));
        this.setEffect(dropShadow);
        this.setId("AccountButton"+index);
    }

    // Getters
    String getAccountName(){
        return accountName;
    }
    String getAccountLogin(){
        return accountLogin;
    }
    String getAccountPassword(){
        return accountPassword;
    }
    int getAccountIndex(){
        return accountIndex;
    }

    // Setters
    void setAccountName(String input){
        this.accountName = input;
    }
    void setAccountLogin(String input){
        this.accountLogin = input;
    }
    void setAccountPassword(String input){
        this.accountPassword = input;
    }
    void setAccountIndex(int i){
        this.accountIndex = i;
    }
}
