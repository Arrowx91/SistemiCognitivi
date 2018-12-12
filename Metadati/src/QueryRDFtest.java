import java.io.*;

/**
 * @author Giulia
 */
public class QueryRDFtest {

    /***********************************
     * INTERROGAZIONE DEL TRIPLE STORE *
     ***********************************/

    public static void main(String[] args) throws IOException {

        QueryRDF query = new QueryRDF("newsCollection.rdf");
        query.executeQuery();

    }
}
