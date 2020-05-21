package be.alexandre01.dreamzon.network.utils;

import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.enums.Type;
import be.alexandre01.dreamzon.network.remote.client.Connect;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class ServerInstance {
    private static ArrayList<String> serverList = new ArrayList<>();
    private static ArrayList<String> startServerList = new ArrayList<>();

    private static HashMap<String,Process> processServers = new HashMap<>();
    private static HashMap<String,BufferedReader> processServersInput = new HashMap<>();
    private static ArrayList<Integer> serversPortList = new ArrayList<>();
    private static ArrayList<Integer> portsBlackList = new ArrayList<>();
    private static HashMap<String,Integer> serversPort = new HashMap<>();
    private static Integer cache = 0;
    public static HashMap<String, Integer> getServersPort() {
        return serversPort;
    }

    public static void addServer(String name){
      serverList.add(name);
    }
    public static boolean startServer(String name, String pathName, Type type,String Xms,String Xmx,int port){

        int servers = 0;
        for (String string : startServerList){
            if(string.startsWith(name+"-")){

                try{
                    int num = Integer.parseInt( string.replace(name+"-",""));
                    servers++;
                } catch (NumberFormatException e) {
                    System.out.println("Une erreur dans la création du serveur");
                   return false;
                }
            }
        }


            Console.print(Colors.ANSI_RED+new File(System.getProperty("user.dir")+Config.getPath("/template/"+name.toLowerCase()+"/"+name+"-"+servers)).getAbsolutePath());
        try {
            String finalname =  name+"-"+servers;
            if(type.equals(Type.DYNAMIC)){
                if(Config.contains("temp/"+pathName+"/"+finalname+"/"+name)){
                    Config.removeDir("temp/"+pathName+"/"+finalname+"/"+name);
                }
                Config.createDir("temp/"+pathName+"/"+name+"/"+finalname);
                Config.copy(new File(Config.getPath(new File(System.getProperty("user.dir")+ Config.getPath("/template/"+pathName+"/"+name)).getAbsolutePath())),new File(Config.getPath("temp/"+pathName+"/"+name+"/"+finalname)));
            }




            if(port == 0){

            if(!serversPortList.isEmpty()){

                    for (Integer string : serversPort.values()){
                        System.out.println(string);
                    }
                    port = serversPortList.get(serversPortList.size()-1)+2;
                    while (portsBlackList.contains(port)){
                        port = port + 2;
                    }
            if(!serversPort.isEmpty()){
                for(Map.Entry<String,Integer> s : serversPort.entrySet()){
                    if(s.getKey().startsWith("cache-")){
                        port = serversPort.get(s.getKey());
                        serversPort.remove(s.getKey(),s.getValue());
                        break;
                    }
                }
            }

                    System.out.println(port);
                if(type.equals(Type.STATIC)){
                    changePort("/template/"+pathName,finalname,port,type);
                }else {
                    if(type.equals(Type.DYNAMIC)){
                        changePort("/temp/"+pathName,finalname,port,type);
                    }
                }

                    serversPortList.add(port);
                    serversPort.put(finalname,port);
                }else {
                if(type.equals(Type.STATIC)){
                    System.out.println("template/"+pathName);
                    port = getPort("/template/"+pathName,finalname,type);
                }else{
                    if(type.equals(Type.DYNAMIC)){
                        port = getPort("temp/"+pathName,finalname,type);
                    }
                }

                     System.out.println(port);
                    serversPortList.add(port);
                    serversPort.put(finalname,port);
                }

            }else {
                if(!serversPortList.contains(port)){
                    for(Map.Entry<String,Integer> s : serversPort.entrySet()){
                        if(s.getKey().startsWith("cache-")){
                            port = serversPort.get(s.getKey());
                            serversPort.remove(s.getKey(),s.getValue());
                            break;
                        }
                    }

                    if(type.equals(Type.STATIC)){
                        changePort("/template/"+pathName,finalname,port,type);
                    }else {
                        if(type.equals(Type.DYNAMIC)){
                            changePort("/temp/"+pathName,finalname,port,type);
                        }
                    }

                    portsBlackList.add(port);
                    serversPort.put(finalname,port);
                }else {
                    System.out.println("Le port est déjà utilisé");
                    return false;
                }
            }
            Process proc= null;
            String resourcePath = null;
            if(System.getProperty("os.name").startsWith("Windows")){
                if(type.equals(Type.DYNAMIC)){
                    proc = Runtime.getRuntime().exec("cmd /c start java -Duser.language=fr -Djline.terminal=jline.UnsupportedTerminal -Xms"+Xms+" -Xmx"+Xmx+" -jar " + new File(System.getProperty("user.dir")+ Config.getPath("/temp/"+pathName+"/"+name+"/"+finalname)).getAbsolutePath()+"/spigot.jar nogui", null ,  new File(System.getProperty("user.dir")+Config.getPath("/temp/"+pathName+"/"+name+"/"+finalname)).getAbsoluteFile());
                }else {
                    if(type.equals(Type.STATIC)){
                        proc = Runtime.getRuntime().exec("cmd /c start java -Duser.language=fr -Djline.terminal=jline.UnsupportedTerminal -Xms"+Xms+" -Xmx"+Xmx+" -jar " + new File(System.getProperty("user.dir")+ Config.getPath("/template/"+pathName+"/"+name)).getAbsolutePath()+"/spigot.jar nogui", null ,  new File(System.getProperty("user.dir")+Config.getPath("/template/"+pathName+"/"+name)).getAbsoluteFile());
                    }
                }

            }else {
                if(type.equals(Type.DYNAMIC)){
                    proc = Runtime.getRuntime().exec("screen -dmS "+finalname+" java -Duser.language=fr -Djline.terminal=jline.UnsupportedTerminal -Xms"+Xms+" -Xmx"+Xmx+" -jar " + new File(System.getProperty("user.dir")+ Config.getPath("/temp/"+pathName+"/"+name+"/"+finalname)).getAbsolutePath()+"/spigot.jar nogui", null ,  new File(System.getProperty("user.dir")+Config.getPath("/temp/"+pathName+"/"+name+"/"+finalname)).getAbsoluteFile());
                }else {
                    if(type.equals(Type.STATIC)){
                        proc = Runtime.getRuntime().exec("screen -dmS "+finalname+" java -Duser.language=fr -Djline.terminal=jline.UnsupportedTerminal -Xms"+Xms+" -Xmx"+Xmx+" -jar " + new File(System.getProperty("user.dir")+Config.getPath("/template/"+pathName+"/"+name)).getAbsolutePath()+"/spigot.jar nogui", null ,  new File(System.getProperty("user.dir")+Config.getPath("/template/"+pathName+"/"+name)).getAbsoluteFile());
                    }
                }


            }



           // Main.getInstance().processInput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            processServersInput.put(finalname,stdInput);
            /*String s = null;
            try {
                System.out.println("cc1");
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                    System.out.println("cc");
                }


            }catch (Exception e){
                System.out.println("error");

            }*/
            startServerList.add(finalname);
            processServers.put(name,proc);
            new Connect("localhost",port+1,"Console","8HetY4474XisrZ2FGwV5z",finalname);
            Timer timer = new Timer();





            return true;
        } catch (Exception e) {
            System.out.println("Le serveur n'a pas pu démarré");
            e.printStackTrace();
            return false;
        }
        }


    public static void stopServer(String name, String pathName){
        String finalName = name.split("-")[0];
        if(getProcess(name) != null){
            System.out.println("DESTROY");
            getProcess(name).destroy();
        }
        if(startServerList.contains(name)){
            startServerList.remove(name);

        }
        if(serversPort.containsKey(name)){
            int port = serversPort.get(name);
            serversPort.put("cache-"+cache,port);
            System.out.println(serversPort.get("cache-"+cache));
            serversPort.remove(name);
        }

        if(processServers.containsKey(name)){
            processServers.remove(name);
        }
        System.out.println("temp/"+pathName+"/"+finalName+"/"+name);
        if(Config.contains("temp/"+pathName+"/"+finalName+"/"+name)){

            Config.removeDir("temp/"+pathName+"/"+finalName+"/"+name);
        }
    }

    public static Process getProcess(String processName) {
        return processServers.get(processName);
    }
    public static BufferedReader getProcessInput(String processName) {
        return processServersInput.get(processName);
    }
    public static ArrayList<String> getServerList() {
        return serverList;
    }

    public static ArrayList<String> getStartServerList() {
        return startServerList;
    }

    public static HashMap<String, Process> getProcessServers() {
        return processServers;
    }

    public static void setServerList(ArrayList<String> serverList) {
        ServerInstance.serverList = serverList;
    }

    public static void setStartServerList(ArrayList<String> startServerList) {
        ServerInstance.startServerList = startServerList;
    }

    public static void setProcessServers(HashMap<String, Process> processServers) {
        ServerInstance.processServers = processServers;
    }

    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    public static boolean changePort(String pathName , String finalname ,int port,Type type){
        String name = finalname.split("-")[0];
        String fileName;
        String checker;
        boolean proxy = false;
        if(pathName.contains("server")){
            fileName = "server.properties";
            checker = "server-port=";
        }else {
            proxy = true;
            fileName = "config.yml";
            checker = "host: 0.0.0.0:";
        }
        File properties;
        if(type.equals(Type.DYNAMIC)){
            properties = new File(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+finalname+"/"+fileName));
        }else {
            properties = new File(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+fileName));
        }

        if(!properties.exists()){
            return false;
        }
        try {
            BufferedReader file;
            // input the (modified) file content to the StringBuffer "input"
            if(type.equals(Type.DYNAMIC)){
                file = new BufferedReader( new FileReader(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+finalname+"/"+fileName)));

            }else {
                    file = new BufferedReader(new FileReader(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+fileName)));
            }

            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                if(line.startsWith("server-port=")){
                    line = "server-port= "+ port;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut;
            if(type.equals(Type.DYNAMIC)){
                fileOut = new FileOutputStream(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+finalname+"/"+fileName));
            }else {
                fileOut = new FileOutputStream(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+fileName));
            }
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            return true;
        } catch (Exception e) {
            System.out.println("Problem reading file.");
            return false;
        }

    }

    public static Integer getPort(String pathName,String finalname,Type type){
        System.out.println(pathName);
        String fileName;
        String checker;
        boolean proxy = false;
        if(pathName.contains("server")){
            fileName = "server.properties";
            checker = "server-port=";
        }else {
            proxy = true;
            fileName = "config.yml";
            checker = "host: 0.0.0.0:";
        }
        String name = finalname.split("-")[0];
        File properties;
        if(type.equals(Type.DYNAMIC)){
            properties = new File(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+finalname+"/"+fileName));
        }else {
            String path;
            if(proxy){
                path = "proxy";
            }else {
                path = "server";
            }
            System.out.println(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name));
            properties= new File(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name));
        }

        if(!properties.exists()){
            return null;
        }
        try {
            // input the (modified) file content to the StringBuffer "input"
            System.out.println(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+finalname+"/"+fileName));
            BufferedReader file;
            if(type.equals(Type.DYNAMIC)){
                 file = new BufferedReader( new FileReader(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+finalname+"/"+fileName)));
            }else {
                 file = new BufferedReader( new FileReader(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+fileName)));
            }

            StringBuffer inputBuffer = new StringBuffer();

            String line;
            Integer port = null;

            while ((line = file.readLine()) != null) {

                if(line.contains(checker)){

                    String readline = line.replace(checker,"").replaceAll(" ","");
                    System.out.println(readline);
                    port = Integer.parseInt(readline);
                    System.out.println(port);

                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();

            FileOutputStream fileOut;
            if(type.equals(Type.DYNAMIC)){
                fileOut = new FileOutputStream(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+finalname+"/"+fileName));
            }else {
                fileOut = new FileOutputStream(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+fileName));
            }
            // write the new string with the replaced line OVER the same file

            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

                return port;

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
        return null;
    }
    public static void getLine(String finalname){
        String name = finalname.split("-")[0];
        File properties = new File(System.getProperty("user.dir")+ Config.getPath("/temp/server/"+name+"/"+finalname+"/logs/latest.log"));
        if(!properties.exists()){
            return;
        }
        try {
            // input the (modified) file content to the StringBuffer "input"
            BufferedReader file = new BufferedReader( new FileReader(System.getProperty("user.dir")+ Config.getPath("/temp/server/"+name+"/"+finalname+"/logs/latest.log")));

            String line;
            Integer port = null;

            file.close();

            // write the new string with the replaced line OVER the same file



        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
        return;
    }

}
