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

    public boolean recv(final String data) throws java.io.IOException {
        if (nomFichier == null) {
            nomFichier = data;
            return false;
        }

        FileWriter writer = new FileWriter(nomFichier);
        writer.write(data);
        writer.close();
        return true;
    }
}
