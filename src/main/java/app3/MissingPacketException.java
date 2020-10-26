package app3;

import java.util.List;

public class MissingPacketException extends Exception{
    List<Integer> missing;

    MissingPacketException(List<Integer> list){
        missing = list;
    }
}
