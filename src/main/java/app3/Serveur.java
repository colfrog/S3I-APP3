package app3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Serveur {
    private static final Map<InetAddress, Couche> handlers = new HashMap<>();

    public static void main(String[] args) throws IOException, MissingPacketsException, TransmissionErrorException {
        int port = 1337;
        DatagramSocket socket = new DatagramSocket(port);
        byte[] buf = new byte[256];
        DatagramPacket dgram = new DatagramPacket(buf, buf.length);

        InetAddress remote;
        int remotePort;
        while (!socket.isClosed()) {
            socket.receive(dgram);
            remote = dgram.getAddress();
            remotePort = dgram.getPort();
            if (!handlers.containsKey(remote))
                handlers.put(remote, newHandler(socket, remote, remotePort));

            // recv retourne true s'il a terminé
            if (handlers.get(remote).recv(new String(dgram.getData()).trim()))
                handlers.remove(remote);

            // nettoie le buffer
            Arrays.fill(buf, (byte) 0);
        }
    }

    private static Couche newHandler(DatagramSocket socket, InetAddress remote, int port) {
        Physique physique = new Physique(socket, remote, port);
        Liaison liaison = new Liaison();
        Transport transport = new Transport();
        Application application = new Application();
        physique.setNextCouche(liaison);
        liaison.setNextCouche(transport);
        transport.setNextCouche(application);

        return physique;
    }
}
