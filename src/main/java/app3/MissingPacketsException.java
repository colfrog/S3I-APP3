package app3;

import java.util.List;

public class MissingPacketsException extends Exception {
    List<Integer> missing;
    /**
     * Exception qui signal un paquet manquant a la fin de la transmission
     *
     * @param missing  Une list des ids de paquets manquants
     */
    MissingPacketsException(List<Integer> missing){
        this.missing = missing;
    }

    final List<Integer> getMissingPackets() {
        return missing;
    }
}
