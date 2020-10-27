package app3;

public class CoucheProto implements Couche {
    Couche nextCouche = null;

    /**
     * Définit la Couche c comme la prochaine couche après celle-ci.
     *
     * @param c  L'objet qui sera définit comme prochaine couche
     */
    public void setNextCouche(Couche c) { nextCouche = c; }

    /**
     * Envoie data à la prochaine couche si elle existe, selon la méthode send
     *
     * @param data  Les données reçues par la couche précédente
     */
    public void send(final String data) throws java.io.IOException {
        if (nextCouche != null)
            nextCouche.send(data);
    }

    /**
     * Envoie data à la prochaine couche si elle existe, selon la méthode recv
     *
     * @param data  Les données reçues par la couche précédente
     */
    public boolean recv(final String data) throws java.io.IOException, MissingPacketsException, TransmissionErrorException {
        if (nextCouche != null)
            return nextCouche.recv(data);

        return false;
    }
}
