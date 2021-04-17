package be.alexandre01.dreamzon.network.utils.language.file;

import be.alexandre01.dreamzon.network.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class LangFile {
    File file;
    public LangFile(File file){
        this.file = file;
    }

   public void load(LinkedHashMap<String,String> lhm) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                if(!line.replaceAll(" ","").startsWith("#")){
                    String[] separator = line.split("=");
                    lhm.put(separator[0],separator[1]);
                }

                line = br.readLine();
            }
    }

}
