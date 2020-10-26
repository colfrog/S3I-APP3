package app3;

import java.util.List;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class Liaison extends CoucheProto {
    private CRC32 crc = new CRC32();
    private DatagramSocket socket;
    private InetAddress remote;
    private int port;

    public Liaison(DatagramSocket socket, InetAddress remote, int port) {
        this.socket = socket;
        this.remote = remote;
        this.port = port;
    }

    public void send(final String data) throws java.io.IOException {
        crc.update(data.getBytes());
        String paquet = data + ':' + crc.getValue(); // data contient id:morceau:crc
        DatagramPacket dgram = new DatagramPacket(data.getBytes(), data.length(), this.remote, this.port);
        socket.send(dgram);
    }

    public void recv(final String data) throws java.io.IOException {
        // data contient un paquet avec checksum, vérifie le checksum et envoie à nextCouche
        int sep = data.lastIndexOf(':');
        String paquet = data.substring(0, sep);
        long crcPaquet = Long.parseLong(data.substring(sep + 1));

        crc.update(paquet.getBytes());
        if (crc.getValue() == crcPaquet)
            try {
                nextCouche.recv(paquet);
            } catch (MissingPacketsException e) {
                String message = "Missing";
                List<Integer> missing = e.getMissingPackets();
                for (int i = 0; i < missing.size(); i++) {
                    message += ":" + missing.get(i).toString();
                }

                send(message);
            }
    }

    public String Sabotage(String data){
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
