package app3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transport extends CoucheProto {
    public void send(String data) throws IOException {

        final int charPerPaquet = 100;

        if(data.length()==0){
            return;
        }
        List<String> dividedData = new ArrayList<String>();

        //dividedData.add("\0\0\0");

        int index = 0;
        while (index < data.length()) {
            dividedData.add(data.substring(index, Math.min(index + charPerPaquet,data.length())));
            index += charPerPaquet;
        }
        //dividedData.add("/0/0/0");
        for(int i=0;i<dividedData.size();i++){
            nextSend.send(dividedData.get(i));
        }





        // data contient un fichier complet. Sépare en paquets de 200 octets et envoie à nextSend.
    }

    public void recv(String data) throws IOException {
        // data contient un paquet
        // Si c'est le premier, il contient le nom. La couche Application doit savoir ce nom.
        // Si ce n'est pas le dernier, garde-le en mémoire
        // Si c'est le dernier, vérifie et envoie à nextRecv, sauf s'il y a une erreur de vérification
    }
}
