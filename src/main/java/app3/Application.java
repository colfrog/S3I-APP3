package app3;
import java.io.*;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application implements  Couche {
    protected DatagramSocket socket = null;
    protected String in = null;

    public void send(String data) throws IOException {
        socket = new DatagramSocket(4445);
        try {
            in =  Files.readString(Path.of(data), StandardCharsets.US_ASCII);
        } catch (FileNotFoundException e) {
            System.err.println("Could not open file. ");
        }

    }
    public void recv(String data) {}
}
