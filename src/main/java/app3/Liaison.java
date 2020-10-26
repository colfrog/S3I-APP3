package app3;

public class Liaison extends CoucheProto {
    public void send(String data) {
        // data contient un paquet, calculer le checksum et envoyer à nextCouche
        CRC32 crc;
        crc.update(data.getBytes());
        data += ':' + crc.getValue();
        nextCouche.send(data); // data contient id:fichier:crc
    }

    public void recv(String data) {
        // data contient un paquet avec checksum, vérifie le checksum et envoie à nextCouche
    }
}
