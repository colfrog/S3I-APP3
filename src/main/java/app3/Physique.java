package app3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class Physique extends CoucheProto {
    private DatagramSocket socket;
    private InetAddress remote;
    private int port;

    /**
     * Creer un objet de type physique qui permet de gerer un socket
     *
     * @param socket  L'objet de type socket a utiliser
     * @param remote L'addresse vers lequel envoyer l'information
     * @param port Le port vers lequel envoyer l'information
     */
    public Physique(DatagramSocket socket, InetAddress remote, int port) {
        this.socket = socket;
        this.remote = remote;
        this.port = port;
    }
    /**
     * Envoie la String final au serveur a l'aide d'un socket
     *
     * @param data  La String final a envoyer au client
     */

    public void send(final String data) throws IOException {
        DatagramPacket dgram = new DatagramPacket(data.getBytes(), data.getBytes().length, this.remote, this.port);
        System.out.println("--> " + data);
        socket.send(dgram);
    }
    /**
     * Recoit la String final du client a l'aide d'un socket et reenvoie les ids de paquets manquants
     *
     * @param data  La String recu par le client
     */

    public boolean recv(final String data) throws IOException {
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
        } catch (TransmissionErrorException | IOException e) {
            send("%ERROR%:" + e.toString());
        }

        return false;
    }
}
