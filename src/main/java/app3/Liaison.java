package app3;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class Liaison extends CoucheProto {
    private final CRC32 crc = new CRC32();
    private int paquetsSabotes = 0;

    /**
     * Ajoute un checksum a l'information et l'envoie a la couche physique
     *
     * @param data  La String a envoyer a la couche physique
     */
    public void send(final String data) throws java.io.IOException {
        if (data.startsWith("%")) {
            nextCouche.send(data); // data contient une commande
            return;
        }

        crc.reset();
        crc.update(data.getBytes());

        String contenu = data;
        if (paquetsSabotes++ < 3)
            contenu = sabotage(contenu);

        nextCouche.send(contenu + ':' + crc.getValue()); // data contient id:morceau:crc
    }

    /**
     * Recoit l'information et verifie le checksum pour assurer la qualite de l'information
     *
     * @param data  La String recu par la couche physique
     */
    public boolean recv(final String data) throws java.io.IOException, MissingPacketsException, TransmissionErrorException {
        if (data.startsWith("%"))
            return nextCouche.recv(data); // data contient une commande

        // data contient un paquet avec checksum, vérifie le checksum et envoie à nextCouche
        int sep = data.lastIndexOf(':');
        if (sep == -1)
            return false;

        String paquet = data.substring(0, sep);
        String crcStr = data.substring(sep + 1);
        long crcPaquet = Long.parseLong(crcStr);

        crc.reset();
        crc.update(paquet.getBytes());
        if (crc.getValue() == crcPaquet) {
            return nextCouche.recv(paquet);
        } else {
            System.out.println("packet dropped: " + data);
        }

        return false;
    }

    /**
     * Corrompt manuellement un bit aleatoire dans une String
     *
     * @param data  La String a corrompre
     */
    public String sabotage(String data){
        byte[] byteArray = data.getBytes();
        int index = (int)(Math.random()*(byteArray.length));
        int bitToChange = (int)(Math.random()*8);
        byte oldValue = byteArray[index];

        int mask = 1 << bitToChange;
        byteArray[index] = (byte) ((byteArray[index] & ~mask) | mask);

        if(byteArray[index] == oldValue) {
            byteArray[index] = (byte) (byteArray[index] & ~mask);
        }

        return new String(byteArray, StandardCharsets.UTF_8);
    }
}
