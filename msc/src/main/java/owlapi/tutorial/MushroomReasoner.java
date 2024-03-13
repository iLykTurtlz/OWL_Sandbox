package owlapi.tutorial;

import java.io.File;
import java.util.Optional;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

public class MushroomReasoner {
	private OWLOntology mushroomOntology;
	private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private OWLDataFactory factory = manager.getOWLDataFactory();
    private File file;
    private IRI mushroomOntologyIRI;
    //private Reasoner hermit;
    private OWLReasoner reasoner;
    private OWLAxiom edible;
    private String[] mushroomAttributes;
    private static int individualsCreated = 0;
    
    public MushroomReasoner(String filename, String[] mushroomAttributes) {
	    this.file = new File(filename); 
	    this.mushroomAttributes = mushroomAttributes;
	}
    
    public void initialize() {
    	// load ontology and reasoner
        try {
	    	mushroomOntology = manager.loadOntologyFromOntologyDocument(file);
	    	reasoner = new Reasoner.ReasonerFactory().createReasoner(mushroomOntology);//new Reasoner(mushroomOntology);
	    } catch (OWLOntologyCreationException e) {
	    	e.printStackTrace();
	    }
        
        // need the ontology's IRI
    	Optional<IRI> possibleOntologyIRI = mushroomOntology.getOntologyID().getOntologyIRI();
        IRI mushroomOntologyIRI = null;
        if (possibleOntologyIRI.isPresent()) {
        	mushroomOntologyIRI = possibleOntologyIRI.get();
        	System.out.println(mushroomOntologyIRI);
        } else {
        	System.out.println("No IRI");
        }
    }
    
    public OWLNamedIndividual createIndividual(String [] example) {
    	if (example.length != mushroomAttributes.length) {
			System.out.println("This example is the wrong length");
			return null;
		}
		OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(mushroomOntologyIRI.toString()+"#"+individualsCreated++));
		for (int i=0; i<example.length; i++) {
			OWLDataProperty dp = factory.getOWLDataProperty(IRI.create(mushroomOntologyIRI.toString()+"#"+mushroomAttributes[i]));
			OWLDataPropertyAssertionAxiom dpaa; 
			OWLLiteral literal = factory.getOWLLiteral
			if (example[i].equals("edible")) {
				literal = df.getOWLLiteral(row[i], OWL2Datatype.XSD_BOOLEAN);
			} else {
				literal = df.getOWLLiteral(row[i], OWL2Datatype.XSD_STRING);	
			}
			dpaa = df.getOWLDataPropertyAssertionAxiom(dp, individual, literal);
			man.addAxiom(o, dpaa);
		}
    	
    }
    
    
    public boolean isMushroomEdible(String [] example) {
    	
    	
    	return false;
    }
}
