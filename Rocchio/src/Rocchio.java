import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author giulia
 */

public class Rocchio {

    private String tr; // nome del training set
    private HashMap<String, Double> centroide;
    private HashMap<String,Double> dictionary;

    /**
     * Costruttore
     * @param tr nome della classe
     * @throws IOException
     */
    public Rocchio(String tr) throws IOException {
        this.tr = tr;
        this.centroide = new HashMap<>();
        this.dictionary = Dictionary.readDictionary();
    }

    /**
     * Metodo per la costruzione del centroide
     * @param pos insieme di vettori dei documenti che si sanno appartenere alla classe
     * @param neg insieme di vettori dei documenti che si sanno non appartenere alla classe
     */
    public void buildCentroid(ArrayList<HashMap<String,Double>> pos, ArrayList<HashMap<String,Double>> neg) throws IOException {
        double wPos = 0.0, wNeg = 0.0, feature;
        Iterator itDict = this.dictionary.entrySet().iterator();
        while(itDict.hasNext()){
            Map.Entry entryDict = (Map.Entry) itDict.next();
            wPos = component((String) entryDict.getKey(), pos);
            wNeg = component((String) entryDict.getKey(), neg);
            feature = 16*wPos - 4*wNeg;
            if(feature >= 0.0)
                this.centroide.put((String) entryDict.getKey(),feature);
        }
        saveCentroid();
    }

    /**
     * Calcolo il peso della k-esima feature passata
     * @param word feature
     * @param comp insieme dei vettori POS o NEG
     * @return peso della componente
     */
    private double component(String word, ArrayList<HashMap<String,Double>> comp){
        double w, sum = 0.0;
        HashMap<String,Double> vector;
        // per ogni docs calcolo la componente
        for(int i=0; i<comp.size(); i++) {
            vector = comp.get(i);
            w = vector.get(word);
            sum += (w / Math.abs(comp.size()));
        }
        return sum;
    }

    /**
     * Salvo il centroide calcolato in un file esterno
     * @throws IOException
     */
    private void saveCentroid() throws IOException {
        FileWriter output = new FileWriter("./centroidi/"+this.tr+".txt");
        BufferedWriter out = new BufferedWriter(output);
        Iterator it = this.centroide.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            out.append(entry.getKey()+" "+entry.getValue()+"\n");
        }
        out.close();
        output.close();
    }

}
