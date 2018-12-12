import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.DC;

import java.io.*;
import java.util.*;

/**
 * @author Giulia
 */
public class RDF {

    private Model model;
    private  Lemma lemma = new Lemma();
    private Vector vector = new Vector();

    /**
     * Costruttore
     * @throws IOException
     */
    public RDF() throws IOException {
        // create an empty model
        this.model = ModelFactory.createDefaultModel();
    }

    /**
     * Estrazione delle informazioni dall'articolo per costruire il suo modello
     * @param name nome dell'articolo
     * @param text testo dell'articolo
     * @return lista delle informazioni
     * @throws IOException
     */
    public ArrayList<String> extractInfo(String name ,ArrayList<String> text) throws IOException {

        ArrayList<String> infoDoc = new ArrayList<>();
        String URI = text.get(1).replaceAll("[ #]+", "");
        String title = text.get(4);
        String description = text.get(6);

        String doc = "";
        for(int i=4; i<text.size()-2; i++){
            doc += text.get(i);
        }
        //costruzione del feature vector, mi serve per prendere i primi tre termini
        //più frequenti nell'articolo
        ArrayList<String> document = lemma.stemming(doc);
        vector.buildVector(name,document);

        ArrayList<String> sub = Vector.readVector(name);

        String subject = sub.get(0)+", "+sub.get(1)+", "+sub.get(2);
        String date = text.get(text.size()-1);
        String creator = "Giulia Monti";
        String publisher = "BBC";

        infoDoc.add(URI);
        infoDoc.add(title);
        infoDoc.add(description);
        infoDoc.add(subject);
        infoDoc.add(date);
        infoDoc.add(creator);
        infoDoc.add(publisher);

        return infoDoc;
    }

    /**
     * Creazione del modello RDF
     * @param infoRDF lista delle informazioni dell'articolo
     */
    public void CreateRDF(ArrayList<ArrayList<String>> infoRDF){

        // per inserire in modo casuale il creatore Anakin Skywalker
        int r1 = infoRDF.size()/2;
        int num1 = (int) (Math.random()* r1) + 1;
        int r2 = infoRDF.size() - infoRDF.size()/2 + 1;
        int num2 = (int) (Math.random()* r2) + infoRDF.size()/2;

        for(int i=0; i<infoRDF.size(); i++){
            model.createResource(infoRDF.get(i).get(0))
                    .addProperty(DC.publisher,infoRDF.get(i).get(6))
                    .addProperty(DC.title,infoRDF.get(i).get(1))
                    .addProperty(DC.description,infoRDF.get(i).get(2))
                    .addProperty(DC.subject,infoRDF.get(i).get(3))
                    .addProperty(DC.date,infoRDF.get(i).get(4));
            if(i == num1 || i == num2){
                model.getResource(infoRDF.get(i).get(0))
                        .addProperty(DC.creator,"Anakin Skywalker");
            }
            else{
                model.getResource(infoRDF.get(i).get(0))
                        .addProperty(DC.creator,infoRDF.get(i).get(5));
            }
        }
        this.model.write(System.out);
    }

    /**
     * Salva il modello RDF creato in un file esterno
     * @param name of the RDF file
     * @throws IOException
     */
    public void saveRDF(String name) throws IOException {
        OutputStream out = new FileOutputStream("./"+name);
        this.model.write(out, "RDF/XML-ABBREV");
        out.close();
    }

    /**
     * Restituisce il modello RDF
     * @return model
     */
    public Model getModel(){
        return this.model;
    }
}
