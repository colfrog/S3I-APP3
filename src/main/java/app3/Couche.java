package app3;

public interface Couche {
    public String send(final String data) throws java.io.IOException;
    public void setNextSend(Couche c);
    
    public String recv(final String data) throws java.io.IOException;
    public void setNextRecv(Couche c);
}