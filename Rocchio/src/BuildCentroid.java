import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Giulia
 */
public class BuildCentroid {

    /************************************************************************************************************
     * CALCOLO DEL CENTROIDE CON IL METODO DI ROCCHIO                                                           *
     * - costruisco gli insiemi POS  e NEG dal mio training set di documenti                                    *
     * - calcolo il centroide per ogni classe (10 classi in totale)                                             *
     ************************************************************************************************************/

    public static void main(String[] args) throws IOException {
        Rocchio rocchio = null;
        ArrayList<HashMap<String,Double>> pos = null;
        ArrayList<HashMap<String,Double>> neg = null;
        HashMap<String,Double> vect = null;

        File dir = new File("./docs_200");
        String[] children = dir.list();
        if(children != null){
            Reader inputVector;
            BufferedReader inVect;
            String delims = "[ ]+";
            String[] split;
            String[] tr = {"ambiente","cinema","cucina","economia_finanza","motori","politica","salute","scie_tecnologia","spettacoli","sport"};
            for(int j=0; j<tr.length; j++){
                System.out.println(tr[j]);
                pos = new ArrayList<>();
                neg = new ArrayList<>();
                for(int i=0; i<children.length; i++) {
                    vect = new HashMap<>();
                    inputVector = new InputStreamReader(new FileInputStream("./vettoriDocs/"+children[i]),"UTF8");
                    inVect = new BufferedReader(inputVector);
                    String line = inVect.readLine();
                    while (line != null) {
                        split = line.split(delims);
                        vect.put(split[0],Double.valueOf(split[1]));
                        line = inVect.readLine();
                    }

                    //costruisco gli insiemi POS e NEG
                    if(children[i].contains(tr[j])){
                        pos.add(vect);
                    }else{
                        neg.add(vect);
                    }
                    inVect.close();
                    inputVector.close();
                }

                //costruisco il centroide
                rocchio = new Rocchio(tr[j]);
                rocchio.buildCentroid(pos,neg);
            }
        }
        else{
            System.out.println("Errore cartella vuota\nDocumenti non trovati");
        }
    }
}
