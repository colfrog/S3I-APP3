package app3;

public class Transport implements Couche {
    public void send(String data) {
        // data contient un fichier complet. Sépare en paquets de 200 octets et envoie à nextSend.
    }

    public void recv(String data) {
        // data contient un paquet
        // Si c'est le premier, il contient le nom. La couche Application doit savoir ce nom.
        // Si ce n'est pas le dernier, garde-le en mémoire
        // Si c'est le dernier, vérifie et envoie à nextRecv, sauf s'il y a une erreur de vérification
    }
}
