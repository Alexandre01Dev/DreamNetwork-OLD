package be.alexandre01.dreamzon.network.utils.spiget;

import be.alexandre01.dreamzon.network.utils.spiget.exceptions.SearchRessourceException;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

enum Field{
    NAME,TAG
}
public class Ressource {
    String name;
    String tag;
    String version;
    public Ressource(String nameID){

    }
    public Ressource(String name,String tag,String version,int authorId,int downloads,String links,JsonObject file,ArrayList<String> testedVersions,int category,JsonObject rating,int likes){
        this.name = name;
        this.tag = tag;
        this.version = version;
    }



    public static ArrayList<Ressource> searchRessources(String searchName,int page,int size,int sort,Field field) throws SearchRessourceException{
        String link = "https://api.spiget.org/v2/search/resources/"+searchName+"?size="+size+"&page="+page+"&sort="+sort+"&field="+field.name().toLowerCase();

        JsonArray jsonArray = readJsonFromUrl(link);

        if(jsonArray == null) throw new SearchRessourceException("The search of "+searchName+" has failed.");
        ArrayList<Ressource> ressources = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String name =  jsonObject.get("name").getAsString();
            String tag = jsonObject.get("tag").getAsString();
            String version = jsonObject.get("version").getAsJsonObject().get("id").getAsString();
            int author = jsonObject.get("author").getAsJsonObject().get("id").getAsInt();
            int downloads = jsonObject.get("downloads").getAsInt();
            String links = jsonObject.get("links").getAsJsonObject().get("discussion").getAsString();

            //FILE
            JsonObject file = jsonObject.get("file").getAsJsonObject();

            //VERSIONS
            Type listType = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> testedVersions = new Gson().fromJson(jsonObject.get("testedVersions"),listType);
            int category = jsonObject.get("category").getAsJsonObject().get("id").getAsInt();
            JsonObject rating = jsonObject.get("rating").getAsJsonObject();
            int likes = jsonObject.get("likes").getAsInt();

            ressources.add(new Ressource(name,tag,version,author,downloads,links,file,testedVersions,category,rating,likes));
        }

        return ressources;
    }


    public static JsonArray readJsonFromUrl(String url) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            Object object = new JsonParser().parse(new InputStreamReader(connection.getInputStream()));
            JsonArray jsonArray = (JsonArray) object;
            return jsonArray;
        } catch (Exception ignored){

        }
        return null;
    }

    //TEST
    public static void main(String[] args){
        ArrayList<Ressource> ressources = null;
        try {
            ressources = Ressource.searchRessources("Plugin Test",1,100,1, Field.TAG);
        } catch (SearchRessourceException e) {
            System.out.println(e.getMessage());
            return;
        }
        for (Ressource ressource : ressources) {
            System.out.println("NAME >>"+ressource.name);
            System.out.println("TAG >>"+ressource.tag+"\n");
        }
    }
}