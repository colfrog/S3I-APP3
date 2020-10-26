package app3;

public class CoucheProto {
    Couche nextCouche = null;

    public void setNextCouche(Couche c) { nextCouche = c; }

    public void send(final String data) throws java.io.IOException {
        if (nextCouche != null)
            nextCouche.send(data);
    }

    public void recv(final String data) throws java.io.IOException, MissingPacketException {
        if (nextCouche != null)
            nextCouche.recv(data);
    }
}
