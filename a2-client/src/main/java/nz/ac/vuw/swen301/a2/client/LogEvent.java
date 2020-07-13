package nz.ac.vuw.swen301.a2.client;

import java.time.Instant;
import java.util.UUID;

public class LogEvent {

    public enum level_enum { ALL , DEBUG , INFO , WARN , ERROR , FATAL , TRACE , OFF};

    String id;
    String message;
    String timestamp;
    String thread;
    String logger;
    String level;
    String errorDetails;

    public LogEvent(String i,String m,String t,String th,String l,String le,String e){
        id = i;
        message = m;
        timestamp = t;
        thread = th;
        logger = l;
        level = le;
        errorDetails = e;
    }

    public LogEvent(String i,String m,String t,String th,String l,String le){
        id = i;
        message = m;
        timestamp = t;
        thread = th;
        logger = l;
        level = le;
        errorDetails = "";
    }

    public static level_enum[] getEnums(){
        return level_enum.values();
    }

    public String getMessage(){
        return message;
    }

    public String getThread(){
        return thread;
    }

    public String getLogger(){
        return logger;
    }

    public String getTimeStamp(){
        return timestamp;
    }

    public String getLevel(){
        return level;
    }

    public boolean validate(){
        if(!check_required()){
            return false;
        }
        if(!check_uuid()){
            return false;
        }
        if(!check_date()) {
            return false;
        }
        if(!is_enum(this.level)){
            return false;
        }
        return true;
    }

    public boolean check_required(){
        String[] params = new String[]{this.id, this.message, this.timestamp, this.thread, this.logger, this.level};
        // Check none empty
        for(int x =0; x < params.length; x++){
            if (params[x].equals("")){
                return false;
            }
        }
        return true;
    }

    public boolean check_uuid(){
        try {
            UUID.fromString(this.id);
        } catch(Exception e) {
            return false;
        }
        return true;
        /*
        Pattern pattern = Pattern.compile("/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/");
        Matcher matcher = pattern.matcher(this.id);
        if(matcher.find()){
            return true;
        }
        return false;
        */

    }

    public boolean check_date(){
        // 2019-07-29T09:12:33.001Z
        try {
            Instant instant = Instant.parse (timestamp) ;
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public static boolean is_enum(String lev){
        for (level_enum s : level_enum.values()){
            if (s.name().equals(lev)){
                return true;
            }
        }
        return false;
    }

    public static boolean is_min_enum(String lev,String lev2){
        int val = 0;
        int i = 0;
        for (level_enum s : level_enum.values()){
            if (s.name().equals(lev)){
                val = i;
            }
            i++;
        }
        int val1 = 0;
        i = 0;
        for (level_enum s : level_enum.values()){
            if (s.name().equals(lev2)){
                val1 = i;
            }
            i++;
        }
        if(val1 >= val ){
            return true;
        }
        return false;
    }


    public boolean is_same(LogEvent e){
        if(this.id.equals(e.id) &&  this.message.equals(e.message) && this.timestamp.equals(e.timestamp) && this.thread.equals(e.thread)
                && this.logger.equals(e.logger) && this.level.equals(e.level) && this.errorDetails.equals(e.errorDetails)){
            return true;
        }
        return false;
    }
}
