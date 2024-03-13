package owlapi.tutorial;

import java.io.File;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class MushroomReasoner {
	private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private OWLDataFactory factory = manager.getOWLDataFactory();
    private File file;
    //private Reasoner hermit;
    private OWLReasoner reasoner;
    private OWLAxiom
    
    public MushroomReasoner(String filename) {
	    this.file = new File(filename); 
	}
    
    public void initialize() {
        try {
	    	OWLOntology mushroomOntology = manager.loadOntologyFromOntologyDocument(file);
	    	reasoner = new Reasoner.ReasonerFactory().createReasoner(mushroomOntology);//new Reasoner(mushroomOntology);
	    } catch (OWLOntologyCreationException e) {
	    	e.printStackTrace();
	    }
    }
    
    public boolean isEdible() {
    	
    }
}
