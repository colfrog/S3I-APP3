package app3;

public interface Couche {
    public void setNextCouche(Couche c);
    public void send(final String data) throws java.io.IOException;
    public void recv(final String data) throws java.io.IOException, MissingPacketsException;
}