package app3;

public class Transport extends CoucheProto {
    public void send(String data) throws IOException {
        List<String> morceaux = packageData(data);

        // envoyer le titre du fichier

        // envoyer les paquets et un identificateur pour chaque
        int id = 1;
        for (String morceau : morceaux)
            nextCouche.send(id++ + ':' + morceau); // data contient id:fichier
    }

    public void recv(String data) {
        // data contient un paquet
        // Si c'est le premier, il contient le nom. La couche Application doit savoir ce nom.
        // Si ce n'est pas le dernier, garde-le en mémoire
        // Si c'est le dernier, vérifie et envoie à nextRecv, sauf s'il y a une erreur de vérification
    }

    private List<String> packageData(String data) {
        // data contient un fichier complet. Sépare en paquets de 128 octets et envoie à nextSend.
        List<String> morceaux;
        String morceau = null;
        int i = 0, offset = 0, size = 128, remaining = 0;

        while (i < data.length()) {
            remaining = data.length() - offset;
            if (remaining < 128)
                size = remaining;

            morceau = data.substring(i + offset, i + offset + size);
            morceaux.add(morceau);

            i += size;
        }

        return morceaux;
    }
}
