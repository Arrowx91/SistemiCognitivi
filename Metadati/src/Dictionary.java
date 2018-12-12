import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author giulia
 */

public class Dictionary {

    public HashMap<String,Double> dict;

    /**
     * Costruttore
     */
    public Dictionary() throws IOException {
        this.dict = new HashMap<>();

    }

    /**
     * Inserimento delle parole, contenute nei documenti, nel dizionario:
     * se la parola è già presente, incremento il contatore ad esso assocciato (mi serve per sapere
     * in quanti documenti il termine appare); se invece il termine è nuovo, viene aggiunto al dizionario
     * con il suo contatore inizializzato a 1
     * @param lemmi lista dei lemmi delle parole del documento
     */
    public void insertWord(ArrayList<String> lemmi) {
        double count;
        for(int i=0; i<lemmi.size(); i++){
            if(this.dict.containsKey(lemmi.get(i))) {
                count = this.dict.get(lemmi.get(i));
                count++;
                this.dict.put(lemmi.get(i),count);
            }
            else
                this.dict.put(lemmi.get(i),1.0);
        }
    }

    /**
     * Lettura del dizionario dal file dictionary.txt
     * @return una HashMap contenente la coppia <w,n>
     * @throws IOException
     */
    public static HashMap<String,Double> readDictionary() throws IOException {
        HashMap<String,Double> dictionary = new HashMap<>();
        FileReader file = new FileReader("./dictionary.txt");
        BufferedReader dict = new BufferedReader(file);
        String delims = "[ ]+";
        String[] split;
        String line = dict.readLine();
        while (line != null) {
            split = line.split(delims);
            dictionary.put(split[0],Double.valueOf(split[1]));
            line = dict.readLine();
        }
        return dictionary;
    }

    /**
     * Salvataggio del dizionario nel file esterno dictionary.txt
     * @throws IOException
     */
    public void save() throws IOException {
        FileWriter output = new FileWriter("./dictionary.txt");;
        BufferedWriter out = new BufferedWriter(output);
        Iterator it = this.dict.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            out.append(""+entry.getKey()+" "+entry.getValue()+"\n");
        }
        out.close();
        output.close();
    }
}
