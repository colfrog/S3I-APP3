package app3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.zip.CRC32;

public class Liaison extends CoucheProto {
    private DatagramSocket socket = null;

    public Liaison(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void connect(InetAddress remote) {
        socket.connect(remote, socket.getPort());
    }

    public void send(final String data) throws java.io.IOException {
        CRC32 crc = new CRC32();
        crc.update(data.getBytes());
        String paquet = data + ':' + crc.getValue();
        DatagramPacket dgram = new DatagramPacket(paquet.getBytes(), paquet.length());
        socket.send(dgram); // data contient id:morceau:crc
    }

    public void listen() {
         // Écoute sur la socket et envoie à recv
    }

    public void recv(final String data) throws java.io.IOException {
        // data contient un paquet avec checksum, vérifie le checksum et envoie à nextCouche
    }
}
