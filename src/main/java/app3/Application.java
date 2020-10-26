package app3;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application extends CoucheProto {
    private String nomFichier = null;

    public void send(final String data) throws java.io.IOException {
        nomFichier = data;
        String contenu = Files.readString(Path.of(nomFichier), StandardCharsets.UTF_8);
        nextCouche.send(nomFichier + ':' + contenu); // data contient nomFichier:contenu
    }

    public void recv(final String data) throws java.io.IOException, MissingPacketException {
        if (nomFichier == null) {
            nomFichier = data;
            return;
        }

        FileWriter myWriter = new FileWriter(nomFichier);
        myWriter.write(data);
        myWriter.close();
        System.out.println("Successfully wrote " + nomFichier);
    }
}
