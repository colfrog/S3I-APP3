package app3;

import java.util.ArrayList;
import java.util.List;

public class Transport extends CoucheProto {
    private String nomFichier = null;
    private String[] paquets = null;
    private int nPaquets = 0;
    private int nErrors = 0;

    /**
     * Separe, index et envoie l'information du fichier a la couche de liaison
     *
     * @param data  La String contenant l'information du fichier
     */
    public void send(final String data) throws java.io.IOException {
        int sep = data.indexOf(':');
        if (sep == -1)
            return;

        nomFichier = data.substring(0, sep);
        String contenu = data.substring(sep + 1);

        List<String> morceaux = packageData(contenu);

        // envoyer le titre du fichier et le nombre de paquets
        nextCouche.send('%' + nomFichier + ':' + morceaux.size());

        // envoyer les paquets et un identificateur pour chaque
        Integer id = 0;
        nPaquets = morceaux.size();
        paquets = new String[nPaquets];
        for (String morceau : morceaux) {
            paquets[id] = morceau;
            nextCouche.send((id++).toString() + ':' + morceau); // data contient id:morceau
        }

        nextCouche.send("%FIN%");
    }
    /**
     * Demande a la couche de liaison de renvoyer les paquets manquants
     *
     * @param missing  La List des ids de paquets manquants
     */
    public void sendMissing(final List<Integer> missing) throws java.io.IOException {
        for (Integer id : missing) {
            System.out.println(id);
            nextCouche.send(id.toString() + ':' + paquets[id]);
        }

        nextCouche.send("%FIN%");
    }
    /**
     * Demande a la couche de liaison de renvoyer les paquets manquants
     *
     * @throws MissingPacketsException si des paquest sont manquants dans la transmission
     * @throws TransmissionErrorException si plus de 3 erreurs de paquets surviennent
     * @return true si le mots clef de fin est obtenu et il ne manque aucun paquet
     * @return false si la toute l'information n'a pas encore ete recu
     * @param data  L'information contenu dans le paquet recu
     */
    public boolean recv(final String data) throws java.io.IOException, MissingPacketsException, TransmissionErrorException {
        // Si c'est le dernier, vérifie et envoie à nextCouche, sauf s'il y a une erreur de vérification
        if (data.equals("%FIN%")) {
            List<Integer> missing = getMissingPackets();
            if (missing.size() == 0) {
                String contenu = unpackData();
                return nextCouche.recv(contenu);
            } else {
                if (++nErrors < 3)
                    throw new MissingPacketsException(missing);
                else
                    throw new TransmissionErrorException();
            }
        }

        // Si c'est la première commande, data contient le nom et le nombre de paquets. Envoie le nom à nextCouche
        if (nomFichier == null && data.startsWith("%")) {
            readMetadata(data);
            nextCouche.recv(this.nomFichier);
            return false;
        }

        // Si ce n'est pas le dernier, garde-le en mémoire
        int sep = data.indexOf(':');
        if (sep == -1)
            return false;

        int id = Integer.parseInt(data.substring(0, sep));
        String morceau = data.substring(sep + 1);
        paquets[id] = morceau;
        return false;
    }

    /**
     * Sépare en paquets de 128 octets.
     *
     * @return une liste de String divise
     * @param data Contient le contenu d'un fichier sous format String
     */
    private List<String> packageData(final String data) {
        List<String> morceaux = new ArrayList<String>();
        String morceau = null;

        int i = 0;
        final int size = 128;
        while (i < data.length()) {
            morceau = data.substring(i, Math.min(i + size, data.length()));
            morceaux.add(morceau);

            i += size;
        }

        return morceaux;
    }

    /**
     * Reconstruit le contenu du fichier en concatenant les paquets ensemble, en ordre
     *
     * @return Le contenu du fichier reconstruit
     */
    private String unpackData() {
        String contenu = "";
        for (int i = 0; i < nPaquets; i++)
            contenu += paquets[i];

        return contenu;
    }

    /**
     * Lit les métadonnées du paquet spécial dans data et place-les dans l'objet
     *
     * @param data  Un paquet contenant des métadonnées d'un fichier
     */
    private void readMetadata(final String data) {
        int sep = data.indexOf(':');
        nomFichier = data.substring(1, sep);
        nPaquets = Integer.parseInt(data.substring(sep + 1));
        paquets = new String[nPaquets];
    }

    /**
     * Detect les paquets manquant a la fin de la transmission
     *
     * @return La list contenant les ids de paquets manquant
     */
    private List<Integer> getMissingPackets() {
        List<Integer> missing = new ArrayList<Integer>();
        for (int i = 0; i < nPaquets; i++)
            if (paquets[i] == null)
                missing.add(i);

        return missing;
    }
}
