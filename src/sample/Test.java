package sample;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class Test {
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    Test() throws IOException {
        this.socket = new Socket("localhost", 8989);
        this.socket.setSoTimeout(2000);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    String send(String message) throws IOException{
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
}

class TestMain{
    public static void main(String[] args) throws IOException, ParseException {
        Test test = new Test();
        String answer = test.send("{\"command\": 2,\"user\":\"user1\"}");
        System.out.println(answer);
        JSONParser parser = new JSONParser();
        JSONArray array = (JSONArray) parser.parse(answer);
        JSONArray arr = (JSONArray) array.get(1);
        System.out.println(array);
        System.out.println(array.get(1));
        System.out.println(arr.get(1));
        System.out.println(array.get(2));
        //System.out.println(array.get());
        //System.out.println(array.get());
    }
}


