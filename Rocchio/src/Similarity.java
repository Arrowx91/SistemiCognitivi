import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author giulia
 */
public class Similarity {

    private HashMap<String,Double> dictionary;
    private Reader input = null;
    private BufferedReader in = null;

    /**
     * Costruttore
     * @throws IOException
     */
    public Similarity() throws IOException {
        this.dictionary = Dictionary.readDictionary();
    }

    /**
     * Metodo per la costruzione del feature vector del documento
     * @param lemmi lista dei lemmi del documento
     * @param N numero di documenti del training set
     * @throws IOException
     */
    public HashMap<String,Double> buildVector(ArrayList<String> lemmi, int N){
        HashMap<String,Double> vect = new HashMap<>();
        double count, tf, ntf, idf, apprIdf;

        //calcolo frequenza del termine
        Iterator it = this.dictionary.entrySet().iterator();
        while(it.hasNext()){
            count = 0.0;
            Map.Entry entry = (Map.Entry) it.next();
            if(lemmi.contains(entry.getKey())){
                for(int j=0; j<lemmi.size(); j++){
                    if(lemmi.get(j).equals(entry.getKey()))
                        count += 1.0;
                }
            }
            if(count != 0.0) {
                //normalizzazione della frequenza
                ntf = (count / (double) lemmi.size());
                tf = Math.round(ntf * 1000.0) / 1000.0;
                //calcolo dell'inverse document frequency
                idf = Math.log((N + 1) / ((double) (entry.getValue()) + 1.0)) / Math.log(2);
                apprIdf = Math.round(idf * 1000.0) / 1000.0;
                vect.put((String) entry.getKey(), tf * apprIdf);
            }
            else
                vect.put((String) entry.getKey(), 0.0);
        }
        return vect;
    }

    /**
     * Metodo per classificazione dei documenti
     * @param vector del documento
     * @return classe di appartenza del documento
     * @throws IOException
     */
    public String classification(HashMap<String,Double> vector) throws IOException {
        String delims = "[ ]+";
        String[] split;
        HashMap<String,Double> centr;
        double sim, bestsim=0.0;
        String clas = "";
        File dir = new File("./centroidi");
        String[] children = dir.list();
        if(children != null) {
            for(int i=0; i<children.length; i++){
                centr = new HashMap<>();
                input = new InputStreamReader(new FileInputStream("./centroidi/"+children[i]), "UTF8");
                in = new BufferedReader(input);
                String line = in.readLine();
                while (line != null) {
                    split = line.split(delims);
                    centr.put(split[0],Double.valueOf(split[1]));
                    line = in.readLine();
                }
                sim = cosineSimilarity(vector,centr);
                if(sim > bestsim){
                    bestsim = sim;
                    clas = ""+children[i];
                }
                in.close();
                input.close();
            }
        }
        return clas;
    }

    /**
     * Calcolo della similarità tra due vettori tramite la formula del "cosine-similarity":
     * i due vettori in questione sono il vettore del documento e il centroide
     * @param vector vettore del documento da classificare
     * @param centr centroide
     * @return similarità tra i due vettori
     */
    private double cosineSimilarity(HashMap<String,Double> vector, HashMap<String,Double> centr){
        Iterator itVector = vector.entrySet().iterator();
        double sumNum = 0.0, sumVector = 0.0, sumCentr = 0.0;
        while(itVector.hasNext()){
            Map.Entry entryVector = (Map.Entry) itVector.next();
            if(centr.containsKey(entryVector.getKey())){
                sumNum += ((double)entryVector.getValue() * centr.get(entryVector.getKey()));
                sumVector += Math.pow((double)entryVector.getValue(),2.0);
                sumCentr += Math.pow(centr.get(entryVector.getKey()),2.0);
            }
        }
        return (sumNum)/(Math.sqrt(sumVector)*Math.sqrt(sumCentr));
    }
}
