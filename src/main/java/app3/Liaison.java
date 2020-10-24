package app3;

public class Liaison implements Couche {
    public void send(String data) {
        // data contient un paquet, calculer le checksum et envoyer à nextSend
    }

    public void recv(String data) {
        // data contient un paquet avec checksum, vérifie le checksum et envoie à nextRecv
    }
}
