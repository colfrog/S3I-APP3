package app3;

import java.util.ArrayList;
import java.util.List;

public class Transport extends CoucheProto {
    private String nomFichier = null;
    private int nPaquets = 0;

    public void send(final String data) throws java.io.IOException {
        int sep = data.indexOf(':');
        nomFichier = data.substring(0, sep);
        String contenu = data.substring(sep + 1);

        List<String> morceaux = packageData(contenu);
        if (morceaux == null)
            return;

        // envoyer le titre du fichier et le nombre de paquets
        nextCouche.send(nomFichier + ':' + morceaux.size());

        // envoyer les paquets et un identificateur pour chaque
        int id = 0;
        for (String morceau : morceaux)
            nextCouche.send(id++ + ':' + morceau); // data contient id:morceau

        nextCouche.send("FIN");
    }

    public void recv(final String data) throws java.io.IOException {
        // data contient un paquet
        // Si c'est le premier, il contient le nom et le nombre de paquets. Envoie le nom à nextCouche
        // Si ce n'est pas le dernier, garde-le en mémoire
        // Si c'est le dernier, vérifie et envoie à nextRecv, sauf s'il y a une erreur de vérification
    }

    private List<String> packageData(final String data) {
        // data contient un fichier complet. Sépare en paquets de 128 octets et envoie à nextSend.
        List<String> morceaux = new ArrayList<String>();
        String morceau = null;

        if (data.length() == 0)
            return null;

        int i = 0;
        final int size = 128;
        while (i < data.length()) {
            morceau = data.substring(i, Math.min(i + size, data.length()));
            morceaux.add(morceau);

            i += size;
        }

        return morceaux;
    }
}
