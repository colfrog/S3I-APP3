package app3;

public interface Couche {
    /**
     * Définit la Couche c comme la prochaine couche après celle-ci.
     *
     * @param c  L'objet qui sera définit comme prochaine couche
     */
    void setNextCouche(Couche c);

    /**
     * Applique la méthode send sur des données, possiblement sur plusieurs couches
     *
     * @param data  Les données sur lesquelles appliquer la méthode send
     */
    void send(final String data) throws java.io.IOException;

    /**
     * Applique la méthode recv sur des données, possiblement sur plusieurs couches
     *
     * @param data  Les données sur lesquelles appliquer la méthode recv
     */
    boolean recv(final String data) throws java.io.IOException, MissingPacketsException, TransmissionErrorException;
}