package app3;

import java.io.IOException;

public interface Couche {
    public void send(String data) throws IOException;
    public void setNextSend(Couche c);
    
    public void recv(String data) throws IOException;
    public void setNextRecv(Couche c);
}