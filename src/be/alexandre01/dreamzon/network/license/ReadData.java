package be.alexandre01.dreamzon.network.license;

import be.alexandre01.dreamzon.network.utils.BasicCrypter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadData extends Thread{
    LicenseSocket remote;

    public ReadData(LicenseSocket remote){
        this.remote = remote;
    }

    public void  run() {
        while (!remote.getClient().isClosed()){
            try {
                InputStream in = remote.getClient().getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String data = null;
                while((data = reader.readLine()) != null){

                    String decoded = BasicCrypter.decode(data);
                    if(remote.getClient().isBound()){
                        remote.readData(decoded);
                    }



                }
                System.out.println("CLOSED !");
                remote.getClient().close();
            }catch (Exception e){
                System.out.println("Remote failed: Error #2");
                try{
                    remote.getClient().close();
                    String pathName;


                    this.stop();
                }catch (Exception f){
                    System.out.println("Remote failed: Error #3");
                }
            }
        }
        System.out.println("");


        this.stop();
    }
}
