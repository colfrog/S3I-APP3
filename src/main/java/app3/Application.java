package app3;
import java.io.*;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application implements Couche {
    private String nomFichier;
    protected DatagramSocket socket = null;
    protected String in = null;

    public void send(String data) throws IOException {
        try {
            in = Files.readString(Path.of(data), StandardCharsets.US_ASCII);
        } catch (FileNotFoundException e) {
            System.err.println("Could not open file. ");
        }



    }
    public void recv(String data) throws IOException{
        File myFile = new File("filename.txt");
        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write(data);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
