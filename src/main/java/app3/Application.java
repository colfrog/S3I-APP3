package app3;

public class Application implements Couche {
    private String nomFichier;

    public void send(String data) {
        // data contient un nom de fichier, lit le fichier et envoie à nextSend
    }

    public void recv(String data) {
        // data contient le contenu d'un fichier, écrit le fichier
    }
}
