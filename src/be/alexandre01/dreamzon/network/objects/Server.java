package be.alexandre01.dreamzon.network.objects;

import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.connection.client.Connect;
import be.alexandre01.dreamzon.network.enums.Mods;
import be.alexandre01.dreamzon.network.utils.console.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.ServerInstance;

import java.io.*;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Level;

public class Server {
    String name;
    Mods type;
    String xms;
    String xmx;
    String pathName;
    int port;
    boolean isConfig;
    Process proc;
    public Server(String name,String pathName, Mods type, String xms, String xmx, int port) {
        this.name = name;
        this.type = type;
        this.xms = xms;
        this.xmx = xmx;
        this.port = port;
        this.pathName = pathName;
        System.out.println(type.name());
    }

    public Server(String name, String pathName){
        this.name = name;
        this.pathName = pathName;
        try {
            for (String line : Config.getGroupsLines(System.getProperty("user.dir")+"/template/"+pathName+"/"+name+"/network.yml")){
                if(line.startsWith("type:")){
                    this.type = Mods.valueOf(line.replace("type:","").replaceAll(" ",""));
                }
                if(line.startsWith("xms:")){
                    this.xms = line.replace("xms:","").replaceAll(" ","");
                }
                if(line.startsWith("xmx:")){
                    this.xmx = line.replace("xmx:","").replaceAll(" ","");
                }
                if(line.startsWith("port:")){
                    this.port = Integer.parseInt(line.replace("port:","").replaceAll(" ",""));
                }

                isConfig = true;
            }
        }catch (IOException e){
           Console.print(Colors.ANSI_RED()+"Le serveur en question n'est pas encore configuré",Level.SEVERE);
           isConfig = false;
            return;
        }

    }
    public void startServer(){
        if(!start()){
            Console.print(Colors.ANSI_RED()+"Le serveur n'a pas pu démarré",Level.WARNING);
        }
    }
    private boolean start(){
        if(!isConfig) return false;

        updateConfigFile(pathName,name,type,xms,xmx,port);
        boolean proxy;
        int servers = 0;
        if(pathName.contains("server")){
            proxy = false;
        }else {
            proxy = true;
        }

        if(Main.getInstance().getProxy() == null && !proxy){
            Console.print(Colors.ANSI_RED()+"Veuillez d'abord allumer le Proxy avant d'ouvir un Serveur.", Level.INFO);
            return false;
        }
        for (String string : ServerInstance.getStartServerList()){
            if(string.startsWith(name+"-")){

                try{
                    int num = Integer.parseInt( string.replace(name+"-",""));
                    servers++;
                } catch (NumberFormatException e) {
                    Console.print("Une erreur dans la création du serveur",Level.WARNING);
                    return false;
                }
            }
        }

        Console.print("");
        // Console.print(Colors.ANSI_RED+new File(System.getProperty("user.dir")+Config.getPath("/template/"+name.toLowerCase()+"/"+name+"-"+servers)).getAbsolutePath(), Level.INFO);
        try {
            String finalname =  name+"-"+servers;

            if(type.equals(Mods.DYNAMIC)){
                if(Config.contains("temp/"+pathName+"/"+finalname+"/"+name)){
                    Config.removeDir("temp/"+pathName+"/"+finalname+"/"+name);
                }
                Config.createDir("temp/"+pathName+"/"+name+"/"+finalname);
                Config.copy(new File(Config.getPath(new File(System.getProperty("user.dir")+ Config.getPath("/template/"+pathName+"/"+name)).getAbsolutePath())),new File(Config.getPath("temp/"+pathName+"/"+name+"/"+finalname)));
            }

            if(port == 0){

                if(!ServerInstance.serversPortList.isEmpty()){
                    
                    for (Integer string : ServerInstance.serversPort.values()){
                        //System.out.println(string);
                    }
                    port = ServerInstance.serversPortList.get(ServerInstance.serversPortList.size()-1)+2;
                    while (ServerInstance.portsBlackList.contains(port)){
                        port = port + 2;
                    }
                    if(!ServerInstance.serversPort.isEmpty()){
                        for(Map.Entry<String,Integer> s : ServerInstance.serversPort.entrySet()){
                            if(s.getKey().startsWith("cache-")){
                                port = ServerInstance.serversPort.get(s.getKey());
                                ServerInstance.serversPort.remove(s.getKey(),s.getValue());
                                break;
                            }
                        }
                    }

                    // System.out.println(port);
                    changePort(type.getPath()+pathName,finalname,port,type);

                    port = getPort(type.getPath()+pathName,finalname,type);


                    //   System.out.println(port);
                    ServerInstance.serversPortList.add(port);
                    ServerInstance.serversPort.put(finalname,port);
                }

            }else {
                if(!ServerInstance.serversPortList.contains(port)){
                    for(Map.Entry<String,Integer> s : ServerInstance.serversPort.entrySet()){
                        if(s.getKey().startsWith("cache-")){
                            port = ServerInstance.serversPort.get(s.getKey());
                            ServerInstance.serversPort.remove(s.getKey(),s.getValue());
                            break;
                        }
                    }

                    if(type.equals(Mods.STATIC)){
                        changePort("/template/"+pathName,finalname,port,type);
                    }else {
                        if(type.equals(Mods.DYNAMIC)){
                            changePort("/temp/"+pathName,finalname,port,type);
                        }
                    }

                    ServerInstance.portsBlackList.add(port);
                    ServerInstance.serversPort.put(finalname,port);
                }else {
                    Console.print("Le port est déjà utilisé",Level.WARNING);
                    return false;
                }
            }
            String resourcePath = null;
            if(System.getProperty("os.name").startsWith("Windows")){
                if(type.equals(Mods.DYNAMIC)){
                    proc = Runtime.getRuntime().exec("cmd /c start java -Duser.language=fr -Djline.terminal=jline.UnsupportedTerminal -Xms"+xms+" -Xmx"+xmx+" -jar " + new File(System.getProperty("user.dir")+ Config.getPath("/temp/"+pathName+"/"+name+"/"+finalname)).getAbsolutePath()+"/spigot.jar nogui", null ,  new File(System.getProperty("user.dir")+Config.getPath("/temp/"+pathName+"/"+name+"/"+finalname)).getAbsoluteFile());
                }else {
                    if(type.equals(Mods.STATIC)){
                        proc = Runtime.getRuntime().exec("cmd /c start java -Duser.language=fr -Djline.terminal=jline.UnsupportedTerminal -Xms"+xms+" -Xmx"+xmx+" -jar " + new File(System.getProperty("user.dir")+ Config.getPath("/template/"+pathName+"/"+name)).getAbsolutePath()+"/spigot.jar nogui", null ,  new File(System.getProperty("user.dir")+Config.getPath("/template/"+pathName+"/"+name)).getAbsoluteFile());
                    }
                }

            }else {
                if(type.equals(Mods.DYNAMIC)){
                    proc = Runtime.getRuntime().exec("screen -dmS "+finalname+" java -Duser.language=fr -Djline.terminal=jline.UnsupportedTerminal -Xms"+xms+" -Xmx"+xmx+" -jar " + new File(System.getProperty("user.dir")+ Config.getPath("/temp/"+pathName+"/"+name+"/"+finalname)).getAbsolutePath()+"/spigot.jar nogui", null ,  new File(System.getProperty("user.dir")+Config.getPath("/temp/"+pathName+"/"+name+"/"+finalname)).getAbsoluteFile());
                }else {
                    if(type.equals(Mods.STATIC)){
                        proc = Runtime.getRuntime().exec("screen -dmS "+finalname+" java -Duser.language=fr -Djline.terminal=jline.UnsupportedTerminal -Xms"+xms+" -Xmx"+xmx+" -jar " + new File(System.getProperty("user.dir")+Config.getPath("/template/"+pathName+"/"+name)).getAbsolutePath()+"/spigot.jar nogui", null ,  new File(System.getProperty("user.dir")+Config.getPath("/template/"+pathName+"/"+name)).getAbsoluteFile());
                    }
                }


            }

            Console.print(Colors.ANSI_GREEN()+"Le serveur viens de démarrer le processus",Level.INFO);

            Console.print("Chemins d'accès : "+Colors.ANSI_RESET()+new File(System.getProperty("user.dir")+Config.getPath("/template/"+name.toLowerCase()+"/"+name+"-"+servers)).getAbsolutePath(), Level.INFO);

            // Main.getInstance().processInput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            //processServersInput.put(finalname,stdInput);
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


            ServerInstance.getStartServerList().add(finalname);
            ServerInstance.getProcessServers().put(name,proc);

            new Connect("localhost",port+1,"Console","8HetY4474XisrZ2FGwV5z",finalname);
            Timer timer = new Timer();
            return true;
        } catch (Exception e) {
            Console.print("Le serveur n'a pas pu démarré",Level.WARNING);
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePort(String pathName , String finalname , int port, Mods type){
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
        if(type.equals(Mods.DYNAMIC)){
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
            if(type.equals(Mods.DYNAMIC)){
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
            if(type.equals(Mods.DYNAMIC)){
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

    public Integer getPort(String pathName, String finalname, Mods type){
        // System.out.println(pathName);
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
        if(type.equals(Mods.DYNAMIC)){
            properties = new File(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+finalname+"/"+fileName));
        }else {
            String path;
            if(proxy){
                path = "proxy";
            }else {
                path = "server";
            }
            //   System.out.println(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name));
            properties= new File(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name));
        }

        if(!properties.exists()){
            return null;
        }
        try {
            // input the (modified) file content to the StringBuffer "input"
            //        System.out.println(System.getProperty("user.dir")+ Config.getPath(pathName+"/"+name+"/"+finalname+"/"+fileName));
            BufferedReader file;
            if(type.equals(Mods.DYNAMIC)){
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
                    //  System.out.println(readline);
                    port = Integer.parseInt(readline);
                    //  System.out.println(port);

                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();

            FileOutputStream fileOut;
            if(type.equals(Mods.DYNAMIC)){
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
    public void getLine(String finalname){
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
    public void updateConfigFile(String pathName, String finalName, Mods type, String Xms, String Xmx, int port){
        Config.createFile((System.getProperty("user.dir")+"/template/"+pathName+"/"+finalName+"/network.yml"));
        try {
            PrintWriter writer = new PrintWriter(System.getProperty("user.dir")+Config.getPath("/template/"+pathName+"/"+finalName+"/network.yml"),"utf-8");
            //  System.out.println(type.name());
            if(type != null){
                writer.println("type: "+type.name());
            }
            if(Xms != null){
                writer.println("xms: "+Xms);
            }
            if(Xmx != null){
                writer.println("xmx: "+Xmx);
            }
            if(port != 0){
                writer.println("port: "+port);
            }
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void stopServer(String name, String pathName){
        String finalName = name.split("-")[0];
        if(ServerInstance.getProcess(name) != null){
            System.out.println("DESTROY");
            ServerInstance.getProcess(name).destroy();
        }
        if(ServerInstance.getStartServerList().contains(name)){
            ServerInstance.getStartServerList().remove(name);

        }
        if(ServerInstance.serversPort.containsKey(name)){
            int port = ServerInstance.serversPort.get(name);
            ServerInstance.serversPort.put("cache-"+ServerInstance.cache,port);
            System.out.println(ServerInstance.serversPort.get("cache-"+ServerInstance.cache));
            ServerInstance.serversPort.remove(name);
        }

        if(ServerInstance.getProcessServers().containsKey(name)){
            ServerInstance.getProcessServers().remove(name);
        }
        // System.out.println("temp/"+pathName+"/"+finalName+"/"+name);
        if(Config.contains("temp/"+pathName+"/"+finalName+"/"+name)){

            Config.removeDir("temp/"+pathName+"/"+finalName+"/"+name);
        }
    }

    public Process getProcessus() {
        return proc;
    }

    public String getName() {
        return name;
    }

    public Mods getType() {
        return type;
    }

    public String getXms() {
        return xms;
    }

    public String getXmx() {
        return xmx;
    }

    public int getPort() {
        return port;
    }
}
