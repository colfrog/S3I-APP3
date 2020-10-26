package app3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.zip.CRC32;

public class Liaison extends CoucheProto {
    private DatagramSocket socket = null;
    private InetAddress remote = null;
    private CRC32 crc = new CRC32();

    public Liaison(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void connect(InetAddress remote) {
        this.remote = remote;
        socket.connect(remote, socket.getPort());
    }

    public void send(final String data) throws java.io.IOException {
        crc.update(data.getBytes());
        String paquet = data + ':' + crc.getValue();
        DatagramPacket dgram = new DatagramPacket(paquet.getBytes(), paquet.length());
        socket.send(dgram); // data contient id:morceau:crc
    }

    // TODO: Envoyer dans Serveur.java, créer un objet pour chaque couche par client
    public void listen() throws java.io.IOException {
        byte[] buf = new byte[256];
        DatagramPacket dgram = new DatagramPacket(buf, buf.length);

        // Écoute sur la socket et envoie à recv
        while (true) {
            socket.receive(dgram);
            this.remote = dgram.getAddress();
            recv(new String(dgram.getData()));
        }
    }

    public void recv(final String data) throws java.io.IOException {
        // data contient un paquet avec checksum, vérifie le checksum et envoie à nextCouche
        int sep = data.lastIndexOf(':');
        String paquet = data.substring(0, sep);
        long crcPaquet = Long.parseLong(data.substring(sep + 1));

        crc.update(paquet.getBytes());
        if (crc.getValue() == crcPaquet)
            nextCouche.recv(paquet);
    }
}
