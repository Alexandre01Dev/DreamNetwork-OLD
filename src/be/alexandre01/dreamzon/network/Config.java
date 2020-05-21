package be.alexandre01.dreamzon.network;

import java.io.*;

public class Config {
    public static String getPath(String path){
        String resourcePath = null;
        if(System.getProperty("os.name").startsWith("Windows")){
            resourcePath = path.replaceAll("/","\\\\");
        }else {
            resourcePath = path;
        }
        return resourcePath;
    }
    public static boolean contains(String path){
        File theDir = new File(getPath(path));
        if (theDir.exists()) {
            return true;
        }
        return false;
    }
    public static void createDir(String path){
        File theDir = new File(getPath(path));

        if (!theDir.exists()) {
            System.out.println("creating directory... " + theDir.getName());
            boolean result = false;

            try{
                theDir.mkdirs();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
    }
    public static boolean removeDir(String path){
        while (contains(path)){
            File theDir = new File(getPath(path));

            if (theDir.exists()) {
                File[] allContents = theDir.listFiles();
                if (allContents != null) {
                    for (File file : allContents) {
                        removeDir(file.getAbsolutePath());
                    }
                }
                return theDir.delete();

            }
        }
        return false;

    }
    public static void copy(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    private static void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdir();
        }

        for (String f : source.list()) {
            copy(new File(source, f), new File(target, f));
        }
    }

    private static void copyFile(File source, File target) throws IOException {
        try (
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target)
        ) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

}
