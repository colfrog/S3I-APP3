package app3;

public class Transport extends CoucheProto {
    public void send(String data) {
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

        if (data.length() == 0)
            return;

        int index = 0;
        final int size = 128;
        while (index < data.length()) {
            morceau = data.substring(index, Math.min(i + size, data.length()));
            morceaux.add(morceau);

            i += size;
        }

        return morceaux;
    }
}
