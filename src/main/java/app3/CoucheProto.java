package app3;

public class CoucheProto {
    Couche nextCouche = null;

    public void setNextCouche(Couche c) { nextCouche = c; }

    public void send(String data) {
        if (nextSend != null)
            nextSend.send(data);
    }

    public void recv(String data) {
        if (nextRecv != null)
            nextRecv.recv(data);
    }
}
