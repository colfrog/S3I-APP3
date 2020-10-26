package app3;

import java.util.List;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class Liaison extends CoucheProto {
    private CRC32 crc = new CRC32();

    public void send(final String data) throws java.io.IOException {
        if (data.startsWith("%")) {
            nextCouche.send(data); // data contient une commande
            return;
        }

        crc.reset();
        crc.update(data.getBytes());
        String contenu = data;
        // contenu = sabotage(contenu);
        nextCouche.send(contenu + ':' + crc.getValue()); // data contient id:morceau:crc
    }

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

    public String sabotage(String data){
        byte[] byteArray = data.getBytes();
        int index = (int)(Math.random()*(byteArray.length));
        int bitToChange = (int)(Math.random()*8);
        byte oldValue = byteArray[index];

        int mask = 1 << bitToChange;
        byteArray[index] = (byte) ((byteArray[index] & ~mask) | ((1 << bitToChange) & mask));

        if(byteArray[index] == oldValue){
            byteArray[index] = (byte) ((byteArray[index] & ~mask) | ((0 << bitToChange) & mask));
        }

        return new String(byteArray, StandardCharsets.UTF_8);
    }
}
