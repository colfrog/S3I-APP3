package app3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Serveur {
    private static Map<InetAddress, Couche> handlers = new HashMap<InetAddress, Couche>();

    public static void main() throws IOException {
        int port = 1337;
        DatagramSocket socket = new DatagramSocket(port);
        byte[] buf = new byte[256];
        DatagramPacket dgram = new DatagramPacket(buf, buf.length);

        Couche handler;
        InetAddress remote;
        String response;
        while (true) {
            socket.receive(dgram);
            remote = dgram.getAddress();
            if (!handlers.containsKey(remote))
                handlers.put(remote, newHandler());

            response = null;
            handler = handlers.get(remote);
            response = handler.recv(new String(dgram.getData()));

            if (response != null)
                socket.send(new DatagramPacket(response.getBytes(), response.length(), remote, port));
        }
    }

    private static Couche newHandler() {
        Liaison liaison = new Liaison();
        Transport transport = new Transport();
        Application application = new Application();
        liaison.setNextCouche(transport);
        transport.setNextCouche(application);

        return liaison;
    }
}
