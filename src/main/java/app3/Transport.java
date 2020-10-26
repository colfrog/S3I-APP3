package app3;

import java.util.ArrayList;
import java.util.List;

public class Transport extends CoucheProto {
    private String nomFichier = null;
    private String[] paquets = null;
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

    public void recv(final String data) throws java.io.IOException, MissingPacketException {
        // Si c'est le dernier, vérifie et envoie à nextCouche, sauf s'il y a une erreur de vérification
        if (data == "FIN") {
            List<Integer> missing = getMissingPackets();
            if (missing.size() == 0) {
                String contenu = unpackData();
                nextCouche.recv(contenu);
            } else {
                throw new MissingPacketException(missing);
            }

            return;
        }

        // Si c'est le premier, il contient le nom et le nombre de paquets. Envoie le nom à nextCouche
        if (nomFichier == null) {
            readMetadata(data);
            nextCouche.recv(this.nomFichier);
            return;
        }

        // Si ce n'est pas le dernier, garde-le en mémoire
        int sep = data.indexOf(':');
        int id = Integer.parseInt(data.substring(0, sep));
        String morceau = data.substring(sep + 1);
        paquets[id] = morceau;
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

    private String unpackData() {
        String contenu = "";
        for (int i = 0; i < nPaquets; i++)
            contenu += paquets[i];

        return contenu;
    }

    private void readMetadata(final String data) {
        int sep = data.indexOf(':');
        nomFichier = data.substring(0, sep);
        nPaquets = Integer.parseInt(data.substring(sep + 1));
        paquets = new String[nPaquets];
    }

    private List<Integer> getMissingPackets() {
        List<Integer> missing = new ArrayList<Integer>();
        for (int i = 0; i < nPaquets; i++)
            if (paquets[i] == null)
                missing.add(i);

        return missing;
    }
}
