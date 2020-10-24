package app3;

public interface Couche {
    public void send(String data);
    public void setNextSend(Couche c);
    
    public void recv(String data);
    public void setNextRecv(Couche c);
}