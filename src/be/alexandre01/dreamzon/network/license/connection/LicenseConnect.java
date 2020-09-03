package be.alexandre01.dreamzon.network.license.connection;

public class LicenseConnect {
    public LicenseConnect(String adresse, int port, String username, String password, String processName){
        try {
            new LicenseThreadConnection(adresse,port,username,password,processName).timer();
        }catch (Exception e){
            System.out.println("FAIL #1");
        }
    }

    }

