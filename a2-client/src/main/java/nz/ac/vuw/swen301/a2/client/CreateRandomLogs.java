package nz.ac.vuw.swen301.a2.client;

import com.google.gson.Gson;
import nz.ac.vuw.swen301.a2.client.LogEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.UUID;

public class CreateRandomLogs {

    public static String randomString(){
        Random random = new Random();
        String alphabet = "abcdefghijklmnopqrstuv";
        String s = "";
        for (int i =0; i < 10; i++){
            s += alphabet.charAt(random.nextInt((10)+1+0));
        }
        return s;
    }

    public static String randomDateTime(){
        Random random = new Random();
        String consta = "-07-29T09:12:33.001Z";
        String rand = Integer.toString((random.nextInt((9999 - 1000) + 1) + 1000));
        return (rand + consta);
    }

    public static String randomLevel(){
        LogEvent.level_enum[] list = LogEvent.getEnums();
        Random random = new Random();
        int rand = (random.nextInt((list.length-1 - 0) + 1) + 0);
        return list[rand].toString();
    }

    public static void main(String args[]) throws InterruptedException, IOException, URISyntaxException {
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        while(true){
            String uuid = UUID.randomUUID().toString();
            LogEvent le = new LogEvent(uuid,randomString(),randomDateTime(),"main","com.example.Foo",randomLevel());
            Gson gson = new Gson();
            String jsonInString = gson.toJson(le);
            appender.post_up(jsonInString);
            Thread.sleep(1000);
        }

    }
}
