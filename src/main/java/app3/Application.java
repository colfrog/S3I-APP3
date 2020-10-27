package app3;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application extends CoucheProto {
    private String nomFichier = null;
    /**
     * Envoie un String contenant l'information d'un fichier a la couche de transport
     *
     * @param data  Le nom du fichier a envoyer
     */
    public void send(final String data) throws java.io.IOException {
        nomFichier = data;
        String contenu = Files.readString(Path.of(nomFichier), StandardCharsets.UTF_8);
        nextCouche.send(nomFichier + ':' + contenu); // data contient nomFichier:contenu
    }

    /**
     * Recoit un String contenant l'information d'un fichier
     *
     * @param data  L'information recu par le serveur
     * @return true si le fichier est ecris avec succes
     */
    public boolean recv(final String data) throws java.io.IOException {
        if (nomFichier == null) {
            nomFichier = data;
            return false;
        }

        FileWriter writer = new FileWriter("resultats/" + nomFichier);
        writer.write(data);
        writer.close();
        return true;
    }
}
