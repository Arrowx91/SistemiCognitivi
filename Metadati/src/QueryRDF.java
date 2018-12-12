import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.*;

/**
 * @author Giulia
 */
public class QueryRDF {

    private Model model;
    private InputStream in;

    /**
     * Costruttore
     * @param name of the file RDF
     * @throws IOException
     */
    public QueryRDF(String name) throws IOException {
        this.model = ModelFactory.createDefaultModel();
        in = new FileInputStream(new File("./"+name));
        this.model.read(in,null);
        in.close();
    }

    /**
     * Metodo per la costruzione delle query e la loro esecuzione
     */
    public void executeQuery(){
        this.model.write(System.out);

        //create new query
        String queryStr= "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                "SELECT ?title " +
                "WHERE {?predicate dc:title ?title . ?predicate dc:creator \"Anakin Skywalker\".}";
        Query query = QueryFactory.create(queryStr);
        //execute query and results
        QueryExecution exec = QueryExecutionFactory.create(query,this.model);
        ResultSet results = exec.execSelect();
        //output
        System.out.println("\nTitoli dei documenti che hanno come autore 'Anakin Skywalker'");
        ResultSetFormatter.out(System.out, results, query);
        exec.close();

        //create new query
        queryStr= "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                "SELECT ?description " +
                "WHERE {?predicate dc:description ?description . ?predicate dc:title \"Avatar breaks US DVD sales record\".}";
        query = QueryFactory.create(queryStr);
        //execute query and results
        exec = QueryExecutionFactory.create(query,this.model);
        results = exec.execSelect();
        //output
        System.out.println("\nDescrizione della risorsa che ha come titolo 'Avatar breaks US DVD sales record'");
        ResultSetFormatter.out(System.out, results, query);
        exec.close();

        //create new query
        queryStr= "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                "SELECT ?description " +
                "WHERE {?predicate dc:description ?description . ?title dc:title \"title doen't exist\".}";
        query = QueryFactory.create(queryStr);
        //execute query and results
        exec = QueryExecutionFactory.create(query,this.model);
        results = exec.execSelect();
        //output
        System.out.println("\nDescrizione della risorsa che come titolo 'title doen't exist'");
        ResultSetFormatter.out(System.out, results, query);
        exec.close();

        //create new query
        queryStr= "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                "SELECT ?description " +
                "WHERE {?predicate dc:description ?description . ?title dc:title \"title doen't exist\".}";
        query = QueryFactory.create(queryStr);
        //execute query and results
        exec = QueryExecutionFactory.create(query,this.model);
        results = exec.execSelect();
        //output
        System.out.println("\nDescrizione della risorsa che come titolo 'title doen't exist'");
        ResultSetFormatter.out(System.out, results, query);
        exec.close();
    }

}
