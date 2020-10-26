package app3;

import java.util.List;

public class MissingPacketsException extends Exception {
    List<Integer> missing;

    MissingPacketsException(List<Integer> missing){
        this.missing = missing;
    }

    final List<Integer> getMissingPackets() {
        return missing;
    }
}
