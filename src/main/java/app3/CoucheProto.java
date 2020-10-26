package app3;

public class CoucheProto implements Couche {
    Couche nextCouche = null;

    public void setNextCouche(Couche c) { nextCouche = c; }

    public String send(final String data) throws java.io.IOException {
        if (nextCouche != null)
            return nextCouche.send(data);

        return null;
    }

    public String recv(final String data) throws java.io.IOException {
        if (nextCouche != null)
            return nextCouche.recv(data);

        return null;
    }
}
