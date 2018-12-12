import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author giulia
 */

public class Vector {

    private HashMap<String,Double> dictionary;
    private FileWriter output;
    private BufferedWriter out;

    /**
     * Costruttore
     * @throws IOException
     */
    public Vector() throws IOException {
        this.dictionary = Dictionary.readDictionary();
    }

    /**
     * Metodo per la costruzione del feature vector del documento
     * @param name nome del documento
     * @param lemmi lista dei lemmi del documento
     * @param N numero di documenti presenti nel training set
     * @throws IOException
     */
    public void buildVector(String name, ArrayList<String> lemmi, int N) throws IOException {
        output = new FileWriter("./vettoriDocs/"+name);
        out = new BufferedWriter(output);
        double count, tf, ntf, idf, apprIdf;

        //calcolo frequenza del termine
        Iterator it = this.dictionary.entrySet().iterator();
        while(it.hasNext()){
            count = 0.0;
            Map.Entry entry = (Map.Entry) it.next();
            //conto quante volte compare nel doc
            if(lemmi.contains(entry.getKey())){
                for(int j=0; j<lemmi.size(); j++){
                    if(lemmi.get(j).equals(entry.getKey()))
                        count += 1.0;
                }
            }
            if(count != 0.0){
                //normalizzo la frequenza del termine
                ntf = (count/(double)lemmi.size());
                tf = Math.round(ntf*1000.0)/1000.0;
                //calcolo l'inverse document frequency
                idf = Math.log(N/(double)entry.getValue())/Math.log(2);
                apprIdf = Math.round(idf*1000.0)/1000.0;
                out.append(entry.getKey()+" "+(tf*apprIdf)+"\n");
            }
            else
                out.append(entry.getKey()+" 0.0\n");

        }
        out.close();
        output.close();
    }
}
