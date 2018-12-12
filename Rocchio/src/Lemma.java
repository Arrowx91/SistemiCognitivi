import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author giulia
 */
public class Lemma {

    private String word;
    private String lemma;

    //creates a StanfordCoreNLP object, with POS tagging, lemmatization
    private Properties props;
    private StanfordCoreNLP pipeline;
    public ArrayList<Lemma> array;


    /**
     * Costruttore
     * @throws IOException
     */
    public Lemma() throws IOException {
        // set up pipeline properties
        this.props = new Properties();
        // set the list of annotators to run
        this.props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        // build pipeline
        this.pipeline = new StanfordCoreNLP(props);
        array = readFileLemmi();
    }

    /**
     * Costruttore
     * @param w parola
     * @param l lemma della parola w
     */
    public Lemma(String w, String l){
        this.word = w;
        this.lemma = l;
    }

    /**
     * Metodo per la "pulizia" del documento, vengono eliminate le stopwords e cercati i lemmi
     * dei termini
     * @param text set di parole del documento
     * @return lista dei lemmi delle parole del documento
     * @throws IOException
     */
    public ArrayList<String> stemming(String text) throws IOException {

        String newtext = text.replaceAll("[=`.,:;\"()\\[\\]!?%$£^><+*\'|/&0-9\\-]+", " ");

        //1-tokenizzazione del testo
        // create an empty Annotation just with the given text
        Annotation sentence = new Annotation(newtext);
        // annotate document
        pipeline.annotate(sentence);
        ArrayList<String> token = new ArrayList<>();
        List<CoreMap> sents = sentence.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap s : sents) {
            for (CoreLabel t : s.get(CoreAnnotations.TokensAnnotation.class)) {
                token.add(t.originalText().toLowerCase());
            }
        }

        //2-eliminazione delle stopwords
        String[] new_sentence = filter(token);

        //3-cercati e salvati i lemmi delle parole del testo rimaste
        ArrayList<String> lemmi = new ArrayList<>();
        String l = "";
        for (int i = 0; i < new_sentence.length; i++) {
            l = searchLemma(array, new_sentence[i]);
            if(l != null)
                lemmi.add(l);
        }
        return lemmi;
    }

    /**
     * Lettura del file morph-it contenente i lemmi della lingua italiana
     * @return lista di lemmi
     * @throws IOException
     */
    private ArrayList<Lemma> readFileLemmi() throws IOException{
        FileReader file = new FileReader("./morph-it_048.txt");
        BufferedReader fm = new BufferedReader(file);
        array = new ArrayList<>();
        String s = fm.readLine();
        while(s!=null){
            String[] split = s.split("\t");
            array.add(new Lemma(split[0], split[1]));
            s = fm.readLine();
        }
        return array;
    }

    /**
     * Elimininazione delle stopwords dal testo del documento
     * @param s lista dei termini del documento
     * @return array dei termini del documento senza stopwords
     * @throws IOException
     */
    private static String[] filter(ArrayList<String> s) throws IOException{
        ArrayList<String> stop_words = readStopWorldFile();
        ArrayList<String> s_filter = new ArrayList<>();
        boolean flag = false;
        for(int i=0; i<s.size(); i++){
            for(int j=0; j<stop_words.size(); j++){
                if(s.get(i).equals(stop_words.get(j))) {
                    flag = true;
                }
            }
            if(!flag)
                s_filter.add(s.get(i));
            flag = false;
        }
        String[] s_new = new String[s_filter.size()];
        for(int i=0; i<s_new.length; i++){
            s_new[i] = s_filter.get(i);
        }
        return s_new;
    }

    /**
     * Lettura del file stopwords-it contenente le stopwords della lingua italiana
     * @return lista di stopwords
     * @throws IOException
     */
    private static ArrayList<String> readStopWorldFile() throws IOException {
        ArrayList<String> stop_world = new ArrayList<>();
        FileReader word = new FileReader("./stopwords-it.txt");
        BufferedReader br = new BufferedReader(word);
        String line = br.readLine();
        while(line != null){
            stop_world.add(line);
            line = br.readLine();
        }
        br.close();
        word.close();
        return stop_world;
    }

    /**
     * Ricerca del lemma di una specifica parola
     * @param array lista di lemmi
     * @param word parola di cui si vuole il lemma
     * @return lemma della parola w
     */
    private String searchLemma(ArrayList<Lemma> array, String word){
        for(int i=0; i<array.size(); i++){
            if(array.get(i).getWord().equals(word))
                return array.get(i).getLemma();
        }
        return null;
    }

    /**
     * Viene restituita la parola
     * @return word
     */
    public String getWord(){
        return this.word;
    }

    /**
     * Viene restituito il lemma
     * @return lemma
     */
    public String getLemma(){
        return this.lemma;
    }
}
