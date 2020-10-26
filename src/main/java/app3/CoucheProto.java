package app3;

public class CoucheProto implements Couche {
    Couche nextCouche = null;

    public void setNextCouche(Couche c) { nextCouche = c; }

    public void send(final String data) throws java.io.IOException {
        if (nextCouche != null)
            nextCouche.send(data);
    }

    public boolean recv(final String data) throws java.io.IOException, MissingPacketsException, TransmissionErrorException {
        if (nextCouche != null)
            return nextCouche.recv(data);

        return false;
    }
}
