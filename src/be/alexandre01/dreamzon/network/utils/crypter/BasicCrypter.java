package be.alexandre01.dreamzon.network.utils.crypter;

import java.util.Base64;

public class BasicCrypter {

    public static String encode(String value){
        try{
            return Base64.getEncoder().encodeToString(value.getBytes());
        }catch (Exception e){
            System.out.println("FAIL DANS LE CHIFFREMENT");
            return null;
        }
    }
    public static String decode(String value){
        try {
            byte[] decodedvalue =  Base64.getDecoder().decode(value);
            return new String(decodedvalue);
        }catch (Exception e){
            System.out.println("Fail dans le chiffrement de ");
            return null;
        }
    }
    public static boolean canDecode(String value){
        try {
            Base64.getDecoder().decode(value);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
