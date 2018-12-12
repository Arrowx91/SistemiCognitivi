import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Giulia
 */
public class BuildVector {

    /************************************************************************************************************
     * CREAZIONE DEI VETTORI E DEL DIZIONARIO DEI TERMINI                                                       *
     * - costruisco il feature vector per ogni docs                                                             *
     * - costruisco il dizionario dei termini contenuti nei docs                                                *
     ************************************************************************************************************/

    public static void main(String[] args) throws IOException {
        Reader input = null;
        BufferedReader in = null;
        String text = "";

        Lemma lemma = new Lemma();
        Dictionary dictionary = new Dictionary();
        HashMap<String, ArrayList<String>> docs = new HashMap<>();

        File dir = new File("./docs_200");
        String[] children = dir.list();
        if (children != null) {
            ArrayList<String> lemmi;
            //prendo i documenti dalla cartella
            for (int i = 0; i < children.length; i++) {
                System.out.println(children[i]);
                input = new InputStreamReader(new FileInputStream("./docs_200/" + children[i]), "UTF8");
                in = new BufferedReader(input);
                String line = in.readLine();
                while (line != null) {
                    text += line;
                    line = in.readLine();
                }

                //calcolo dei lemmi
                lemmi = lemma.stemming(text);

                // costruisco il dizionario con i lemmi
                dictionary.insertWord(lemmi);

                //salvo i lemmi
                docs.put(children[i], lemmi);

                text = "";
            }

            //salvo il dizionario
            dictionary.save();

        } else {
            System.out.println("Errore cartella vuota\nDocumenti non trovati");
        }

        in.close();
        input.close();

        //costruisco i vettori
        Vector vector = new Vector();

        Iterator it = docs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            System.out.println((String) entry.getKey());
            vector.buildVector((String) entry.getKey(), (ArrayList<String>) entry.getValue(), docs.size());
        }

    }
}
