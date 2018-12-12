import java.io.*;
import java.util.ArrayList;

/**
 * @author Giulia
 */
public class BuildRDF {

    /************************************************************************************************************
     * COSTRUZIONE DELLA DESCRIZIONE IN FORMATO RDF DI DOCUMENTI                                                *
     * - prima costruisco i vettori, mi serviranno per prendere i 2/3 termini che mi rappresentano il documento *
     * - rileggo la collezione per costruire la loro descrizione in formato RDF                                 *
     ************************************************************************************************************/
    public static void main(String[] args) throws IOException {
        Reader input = null;
        BufferedReader in = null;

        RDF rdf = new RDF();

        //String text = "";
        //Lemma lemma = new Lemma();
        //Dictionary dict = new Dictionary();

        ArrayList<String> text;
        ArrayList<String> infoDoc;
        ArrayList<ArrayList<String>> infoRDF = new ArrayList<>();

        File dir = new File("./news-collection");
        String[] children = dir.list();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                ArrayList<String> lemmi;
                text = new ArrayList<>();
                System.out.println(children[i]);
                input = new InputStreamReader(new FileInputStream("./news-collection/" + children[i]), "UTF8");
                in = new BufferedReader(input);
                String line = in.readLine();
                while (line != null) {
                    text.add(line);
                    //text += line+" ";
                    line = in.readLine();
                }
                //lemmi = lemma.stemming(text);
                //dict.insertWord(lemmi);
                //text = "";

                infoDoc = rdf.extractInfo(children[i], text);
                infoRDF.add(infoDoc);
            }
            //dict.save();
        }
        rdf.CreateRDF(infoRDF);
        rdf.saveRDF("newsCollection.rdf");
        in.close();
        input.close();
    }

}
