package app3;

public interface Couche {
    public void send(final String data) throws java.io.IOException;
    public void setNextSend(Couche c);
    
    public void recv(final String data) throws java.io.IOException, MissingPacketException;
    public void setNextRecv(Couche c);
}