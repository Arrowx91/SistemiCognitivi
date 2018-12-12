import java.io.*;
import java.util.*;

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
    public void buildVector(String name, ArrayList<String> lemmi) throws IOException {
        output = new FileWriter("./vettoriDocs/"+name);
        out = new BufferedWriter(output);
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
            if(count != 0.0){
                //normalizzo la frequenza del termine
                ntf = (count/(double)lemmi.size());
                tf = Math.round(ntf*1000.0)/1000.0;
                out.append(entry.getKey()+" "+tf+"\n");
            }
        }
        out.close();
        output.close();
    }

    /**
     * Lettura del feature vector del documento
     * @param name nome del file contenenete il feature vector
     * @return feature vector ordinato in base alla frequenza dei termini
     * @throws IOException
     */
    public static ArrayList<String> readVector(String name) throws IOException {
        HashMap<String,Double> vector = new HashMap<>();
        BufferedReader in = new BufferedReader(new FileReader("vettoriDocs/"+name));
        String line = in.readLine();
        String delims = "[ ]+";
        String[] split;
        while (line != null) {
            split = line.split(delims);
            vector.put(split[0],Double.valueOf(split[1]));
            line = in.readLine();
        }
        in.close();

        //ordino il vettore
        SortedVector vect = new SortedVector(vector);
        TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(vect);
        sorted_map.putAll(vector);

        ArrayList<String> subject = new ArrayList<>();
        Iterator it = sorted_map.entrySet().iterator();
        int i=0;
        //prendo i 3 termini più frequenti
        while(it.hasNext() && i<3){
            Map.Entry entry = (Map.Entry) it.next();
            subject.add((String)entry.getKey());
            i++;
        }
        return subject;
    }
}


/**
 * Classe utilizzata  per l'ordinamento del feature vector
 */
class SortedVector implements Comparator<String> {

    Map<String, Double> map;

    public SortedVector(Map<String, Double> map) {
        this.map = map;
    }

    @Override
    public int compare(String a, String b) {
        if (map.get(a) >= map.get(b))
            return -1;
        else
            return 1;
    }
}
