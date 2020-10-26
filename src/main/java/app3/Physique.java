package app3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class Physique extends CoucheProto {
    private DatagramSocket socket;
    private InetAddress remote;
    private int port;

    public Physique(DatagramSocket socket, InetAddress remote, int port) {
        this.socket = socket;
        this.remote = remote;
        this.port = port;
    }

    public void send(final String data) throws java.io.IOException {
        DatagramPacket dgram = new DatagramPacket(data.getBytes(), data.getBytes().length, this.remote, this.port);
        System.out.println("--> " + data);
        socket.send(dgram);
    }

    public boolean recv(final String data) throws java.io.IOException {
        System.out.println("<-- " + data);
        try {
            boolean done = nextCouche.recv(data);
            if (done) {
                send("%OKTHX%");
                return done;
            }
        } catch (MissingPacketsException e) {
            String message = "%MISSING%";
            List<Integer> missing = e.getMissingPackets();
            for (int i = 0; i < missing.size(); i++) {
                message += ":" + missing.get(i).toString();
            }

            send(message);
        } catch (TransmissionErrorException e) {
            send("%ERROR%");
        }

        return false;
    }
}
