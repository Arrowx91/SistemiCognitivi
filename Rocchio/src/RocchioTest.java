import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author giulia
 */
public class RocchioTest {

    /************************************************************************************************************
     * CLASSIFICAZIONE DEI DOCUMENTI                                                                            *
     * - costruisco il feature vector dei docs che voglio classificare                                          *
     * - classifico i docs tramite il calcolo del cosine similarity                                             *
     ************************************************************************************************************/

    public static void main(String[] args) throws IOException {

        Reader input = null;
        BufferedReader in = null;
        String text = "";

        FileWriter output = new FileWriter("./classification.txt");;
        BufferedWriter out = new BufferedWriter(output);

        Lemma lemma = new Lemma();
        Similarity sim = new Similarity();

        File dir = new File("./docsDaClassificare");
        String[] children = dir.list();
        ArrayList<String> lemmi;
        HashMap<String,Double> vector;
        for(int i=0; i<children.length; i++){
            input = new InputStreamReader(new FileInputStream("./docsDaClassificare/" + children[i]), "UTF8");
            in = new BufferedReader(input);
            String line = in.readLine();
            while (line != null) {
                text += line;
                line = in.readLine();
            }

            //calcolo dei lemmi
            lemmi = lemma.stemming(text);

            //construisco il vettore
            vector = sim.buildVector(lemmi,100);

            //calcolo la classe
            String clas = sim.classification(vector);
            System.out.println(children[i]+" --> "+clas.substring(0,clas.length()-4));

            out.append(children[i]+" --> "+clas.substring(0,clas.length()-4)+"\n");

            text="";
        }

        out.close();
        output.close();
        input.close();
        in.close();

    }
}
