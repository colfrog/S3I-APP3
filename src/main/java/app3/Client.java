package app3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) throws java.io.IOException {
        String nomFichier = "test.txt";
        InetAddress remote = InetAddress.getLocalHost();
        int port = 1337;
        DatagramSocket socket = new DatagramSocket();
        socket.connect(remote, port);

        Application application = new Application();
        Transport transport = new Transport();
        Liaison liaison = new Liaison(socket, remote, port);
        application.setNextCouche(transport);
        transport.setNextCouche(liaison);

        application.send(nomFichier);

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        String data;
        List<Integer> missing = new ArrayList<Integer>();
        while (true) {
            socket.receive(packet);
            data = new String(packet.getData()).substring(0, packet.getLength());
            System.out.println("<-- " + data);

            if (data.equals("OKTHX")) {
                System.out.println("done");
                return;
            } else if (data.startsWith("Missing")) {
                int sep = data.indexOf(':');
                String[] ids = data.substring(sep + 1).split(":");
                for (String id : ids)
                    missing.add(Integer.parseInt(id));

                transport.sendMissing(missing);
                transport.send("FIN");
            }
        }
    }
}
