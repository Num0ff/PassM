package sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {
    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static String user;
    static final int REGISTRATION = 0;
    static final int LOGIN = 1;
    static final int LOAD_ACCOUNTS = 2;
    static final int ADD_ACCOUNT = 3;
    static final int EDIT_ACCOUNT = 4;
    static final int DELETE_ACCOUNT = 5;
    static final int QUIT = 9;

    Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        socket.setSoTimeout(2000);
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private static String send(String message) throws IOException{
        writer.println(message);
        String str = "";
        int a;
        try{
            while((a = reader.read()) != -1) {
                str += (char)a;
            }
        }catch (SocketTimeoutException e){
            return str;
        }
        return null;
    }
    static void setUser(String arg){
        user = arg;
    }

    static JSONObject request(int command, String ... args) throws IOException, ParseException {
        String message = "";
        JSONObject response = null;
        switch (command){

            case (REGISTRATION):{
                // Создание json-строки с командой
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", 0);
                jsonObject.put("user", args[0]);
                jsonObject.put("password", args[1]);
                message = jsonObject.toString();
                send(message);
                break;
            }
            case (LOGIN):{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", 1);
                jsonObject.put("user", args[0]);
                jsonObject.put("password", args[1]);
                message = jsonObject.toString();
                String result = send(message);
                System.out.println(result);
                jsonObject = new JSONObject();
                jsonObject.put("result", result);
                response = jsonObject;
                break;
            }
            case (ADD_ACCOUNT):{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", 3);
                jsonObject.put("user", user);
                jsonObject.put("account_name", args[0]);
                jsonObject.put("login", args[1]);
                jsonObject.put("password", args[2]);
                message = jsonObject.toString();
                send(message);
                break;
            }
            case (EDIT_ACCOUNT):{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", 4);
                jsonObject.put("user", user);
                jsonObject.put("account_name", args[0]);
                jsonObject.put("login", args[1]);
                jsonObject.put("password", args[2]);
                message = jsonObject.toString();
                send(message);
                break;
            }
            case (DELETE_ACCOUNT):{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", 5);
                jsonObject.put("user", user);
                jsonObject.put("account_name", args[0]);
                message = jsonObject.toString();
                send(message);
                /* String result = send(message);
                JSONParser parser = new JSONParser();
                response = (JSONObject) parser.parse(result);*/
                break;
            }
            case (QUIT):{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", 9);
                jsonObject.put("user", user);
                message = jsonObject.toString();
                send(message);
                break;
            }
        }
        return response;
    }
    static JSONArray loadingAccounts(String user) throws IOException, ParseException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", LOAD_ACCOUNTS);
        jsonObject.put("user", user);
        String message = jsonObject.toString();
        String result = send(message);
        System.out.println(result);
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(result);
    }

    static void sync(JSONArray array){
        JSONObject jobject;
        for(int i = 0; i<array.size(); i++){
            jobject = (JSONObject)array.get(i);
            jobject.put("user", user);
        }
        jobject = null;
        JSONObject message = new JSONObject();
        message.put("command", 8);
        message.put("data", array.toString());
        try {
            send(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}