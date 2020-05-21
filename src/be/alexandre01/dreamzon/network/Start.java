package be.alexandre01.dreamzon.network;

import be.alexandre01.dreamzon.network.license.License;
import be.alexandre01.dreamzon.network.utils.Colors;
import be.alexandre01.dreamzon.network.utils.Console;

public class Start {
    public static void main(String[] args){
        //new License();
        System.out.println("License Loading...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("License FAILED ...");
        Console.print(Colors.ANSI_GREEN+"██████  ██████  ███████  █████  ███    ███ ███    ██ ███████ ████████ ██     ██  ██████  ██████  ██   ██ \n" +
                " ██   ██ ██   ██ ██      ██   ██ ████  ████ ████   ██ ██         ██    ██     ██ ██    ██ ██   ██ ██  ██  \n" +
                " ██   ██ ██████  █████   ███████ ██ ████ ██ ██ ██  ██ █████      ██    ██  █  ██ ██    ██ ██████  █████   \n" +
                " ██   ██ ██   ██ ██      ██   ██ ██  ██  ██ ██  ██ ██ ██         ██    ██ ███ ██ ██    ██ ██   ██ ██  ██  \n" +
                " ██████  ██   ██ ███████ ██   ██ ██      ██ ██   ████ ███████    ██     ███ ███   ██████  ██   ██ ██   ██ \n" +
                "                                                                                                         \n" +Colors.ANSI_CYAN+
                " _   _  __    __   ______      _        \n" +
                "| | | |/  |  /  |  | ___ \\    | |       \n" +
                "| | | |`| |  `| |  | |_/ / ___| |_ __ _ \n" +
                "| | | | | |   | |  | ___ \\/ _ \\ __/ _` |\n" +
                "\\ \\_/ /_| |___| |_ | |_/ /  __/ || (_| |\n" +
                " \\___(_)___(_)___/ \\____/ \\___|\\__\\__,_|\n" +
                "                                                                              "+Colors.ANSI_RED+"                          \uD835\uDCD1\uD835\uDD02 \uD835\uDCD0\uD835\uDCF5\uD835\uDCEE\uD835\uDD01\uD835\uDCEA\uD835\uDCF7\uD835\uDCED\uD835\uDCFB\uD835\uDCEE01                                     \n" +Colors.ANSI_CYAN+
                "                                                                          ");

        new Main().init();


    }

}
