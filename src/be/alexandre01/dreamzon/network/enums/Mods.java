package be.alexandre01.dreamzon.network.enums;

public enum Mods {
    STATIC("/template/"),DYNAMIC("/temp/");

    private String path;
    Mods(String path){
        this.path = path;
    }


    public String getPath(){
        return path;
    }
}
