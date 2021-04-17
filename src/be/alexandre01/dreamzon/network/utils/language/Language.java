package be.alexandre01.dreamzon.network.utils.language;

import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.utils.language.file.LangFile;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

public class Language extends LangFile implements ILanguage{
    LinkedHashMap linkedHashMap = new LinkedHashMap<String, String >();
    public Language(String file){
        super(new File(Config.pathConvert(file)));
        try {
            super.load(linkedHashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sort(){
    }

    @Override
    public String getName() {
        return null;
    }

    public String get(String id){
        return (String) linkedHashMap.get(id);
    }
}
