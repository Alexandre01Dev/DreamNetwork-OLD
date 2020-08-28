package be.alexandre01.dreamzon.network.utils.message;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Message extends LinkedHashMap<String, Object> {
    private static final Type HASH_MAP_TYPE = new TypeToken<Map<String, Object>>() {
    }.getType();


    public Message(Map<String, Object> map) {
        super(map);
    }
    public Message() {
        super(new LinkedHashMap<>());
    }
    public Message set(String id,Object value){
        put("DW-"+id,value);
        return this;
    }
    public boolean contains(String key){
        return containsKey("DW-"+key);
    }
    public HashMap<String, Object> getObjectData(){
        return this;
    }
    public Object getObject(String key){
        return get("DW-"+key);
    }
    public void setHeader(String header){
        set("header",header);
    }
    public String getHeader(){
        return getString("header");
    }
    public JsonObject toJsonObject() {
        return new Gson().toJsonTree(this).getAsJsonObject();
    }
    public String getString(String key){
        return String.valueOf(get("DW-"+key));
    }
    public int getInt(String key){
        return (Integer) get("DW-"+key);
    }
    public float getFloat(String key){
        return (float) get("DW-"+key);
    }
    public boolean getBoolean(String key){
        return (boolean) get("DW-"+key);
    }

    public static Message createFromJsonString(String json) {
        Message builder = new Message(new Gson().fromJson(json, HASH_MAP_TYPE));
        return builder;
    }

    public String toString() {
        String json = new Gson().toJson(this);
        return json;
    }

}
