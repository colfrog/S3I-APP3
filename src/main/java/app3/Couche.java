package app3;

public interface Couche {
    public void setNextCouche(Couche c);
    public String send(final String data) throws java.io.IOException;
    public String recv(final String data) throws java.io.IOException;
}