package app3;

public class Application extends CoucheProto {
    private String nomFichier = null;

    public void send(String data) {
        try {
            String in = Files.readString(Path.of(data), StandardCharsets.UTF_8);
            nextCouche.send(in);
        } catch (FileNotFoundException e) {
            System.err.println("Could not open file.");
        }
    }

    public void recv(String data) {
        if (nomFichier == null) {
            nomFichier = data;
            return;
        }

        try {
            FileWriter myWriter = new FileWriter(nomFichier);
            myWriter.write(data);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
