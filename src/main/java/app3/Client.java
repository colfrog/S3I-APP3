package app3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main() throws java.io.IOException {
        String nomFichier = "test.txt";
        int port = 1337;
        DatagramSocket socket = new DatagramSocket(port);

        Application application = new Application();
        Transport transport = new Transport();
        Liaison liaison = new Liaison();
        application.setNextCouche(transport);
        transport.setNextCouche(liaison);

        application.send(nomFichier);

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        String data;
        List<Integer> missing = new ArrayList<Integer>();
        while (true) {
            socket.receive(packet);
            data = new String(packet.getData());
            if (data.startsWith("Missing")) {
                int sep = data.indexOf(':');
                String[] ids = data.substring(sep + 1).split(":");
                for (String id : ids)
                    missing.add(Integer.parseInt(id));

                transport.sendMissing(missing);
            }
        }
    }
}
