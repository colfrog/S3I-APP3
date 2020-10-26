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

    public String send(final String data) throws java.io.IOException {
        crc.update(data.getBytes());
        return data + ':' + crc.getValue(); // data contient id:morceau:crc
    }

    public String recv(final String data) throws java.io.IOException {
        // data contient un paquet avec checksum, vérifie le checksum et envoie à nextCouche
        int sep = data.lastIndexOf(':');
        String paquet = data.substring(0, sep);
        long crcPaquet = Long.parseLong(data.substring(sep + 1));

        crc.update(paquet.getBytes());
        String returnValue = "";
        if (crc.getValue() == crcPaquet)
            returnValue = nextCouche.recv(paquet);
        if(returnValue != null && returnValue != "OKTHX"){
            return returnValue;
        }
        return null;
    }
}
