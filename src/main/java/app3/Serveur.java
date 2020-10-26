package app3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Serveur {
    private static Map<InetAddress, Couche> handlers = new HashMap<InetAddress, Couche>();

    public static void main(String[] args) throws IOException, MissingPacketsException, TransmissionErrorException {
        int port = 1337;
        DatagramSocket socket = new DatagramSocket(port);
        byte[] buf = new byte[256];
        DatagramPacket dgram = new DatagramPacket(buf, buf.length);

        Couche handler;
        InetAddress remote;
        int remotePort;
        while (true) {
            socket.receive(dgram);
            remote = dgram.getAddress();
            remotePort = dgram.getPort();
            if (!handlers.containsKey(remote))
                handlers.put(remote, newHandler(socket, remote, remotePort));

            // recv retourne true s'il a terminé
            if (handlers.get(remote).recv(new String(dgram.getData()).substring(0, dgram.getLength()).trim()))
                handlers.remove(remote);

            // nettoie le buffer
            for (int i = 0; i < buf.length; i++)
                buf[i] = 0;
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
