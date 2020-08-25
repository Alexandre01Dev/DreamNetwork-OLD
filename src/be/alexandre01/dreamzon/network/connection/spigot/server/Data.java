package be.alexandre01.dreamzon.network.connection.spigot.server;

public class Data {
    String data;
    String first;
    String[] args;
    public Data(String data){
        this.data = data;
    }
    public String[] getArgs(){
        if(args == null){
            String[] datas = data.split(" ");
            String[] arr = new String[datas.length-1];
            for (int i = 1; i < datas.length; i++) {
                arr[i-1] = datas[i];
            }
            args = arr;
        }
       return args;
    }
    public String getFirst(){
        if(first == null){
            first = data.split(" ")[0];
        }
       return first;
    }


}
