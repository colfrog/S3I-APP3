package app3;

public class CoucheProto {
    private Couche nextSend = null;
    private Couche nextRecv = null;

    public void send(String data) {
        if (nextSend != null)
            nextSend.send(data);
    }

    public void setNextSend(Couche c) {
        nextSend = c;
    }

    public void recv(String data) {
        if (nextRecv != null)
            nextRecv.recv(data);
    }

    public void setNextRecv(Couche c) {
        nextRecv = c;
    }
}
