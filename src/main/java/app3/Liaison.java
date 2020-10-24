package app3;

import java.io.IOException;

public class Liaison extends CoucheProto {
    public void send(String data) throws IOException {
        // data contient un paquet, calculer le checksum et envoyer à nextSend
    }

    public void recv(String data) throws IOException {
        // data contient un paquet avec checksum, vérifie le checksum et envoie à nextRecv
    }
}
