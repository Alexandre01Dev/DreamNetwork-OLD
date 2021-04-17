package be.alexandre01.dreamzon.network.utils.console;

import be.alexandre01.dreamzon.network.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleReader {
    public BufferedReader reader;
    public ConsoleReader(){
        reader = new BufferedReader(new InputStreamReader(System.in));
    }
}
