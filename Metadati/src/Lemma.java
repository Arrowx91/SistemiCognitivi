import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
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

    private Properties props;
    private StanfordCoreNLP pipeline;

    /**
     * Costruttore
     */
    public Lemma(){
        this.props = new Properties();
        this.props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        this.pipeline = new StanfordCoreNLP(props);
    }

    /**
     * Metodo per la "pulizia" del documento, vengono eliminate le stopwords e cercati i lemma
     * dei termini
     * @param text set di parole del documento
     * @return lista dei lemmi delle parole del documento
     * @throws IOException
     */
    public ArrayList<String> stemming(String text) throws IOException {

        String newtext = text.replaceAll("[`.,:;'\"”–’()!?%$£><+*|/&#œ^€“™0-9-]+", " ");

        //1-tokenizzazione del testo
        Annotation sentence = new Annotation(newtext);
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
            l = searchLemma(new_sentence[i]);
            lemmi.add(l);
        }

        return lemmi;
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
     * Lettura del file stop_words_FULL con le stopwords della lingua inglese
     * @return lista di stopwords
     * @throws IOException
     */
    private static ArrayList<String> readStopWorldFile() throws IOException {
        ArrayList<String> stop_world = new ArrayList<>();
        FileReader word = new FileReader("./stop_words_FULL.txt");
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
     * Ricerca del lemma di una specifica parola utilizzando Morphology
     * @param word parola di cui si vuole il lemma
     * @return lemma della parola w
     */
    private String searchLemma(String word){
        String lemma="";
        Annotation w = new Annotation(word);
        pipeline.annotate(w);
        List<CoreMap> lword = w.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap l: lword) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel t: l.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the POS tag of the lemma
                String posTag = t.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                lemma = Morphology.lemmaStatic(word.toLowerCase(), posTag);
            }
        }
        return lemma;
    }

}
