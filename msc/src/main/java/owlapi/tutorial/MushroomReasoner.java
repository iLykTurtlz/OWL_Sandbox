package owlapi.tutorial;

import java.io.File;
//import com.google.common.base.Optional;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;



import org.semanticweb.HermiT.ReasonerFactory;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
//import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.HermiT.ReasonerFactory;

//import org.semanticweb.HermiT.Reasoner;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDataPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubDataPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;

//import org.semanticweb.owlapi.model.OWLOntologyFormat;
//import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat ;



import org.semanticweb.owlapi.vocab.OWL2Datatype;


//import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import org.semanticweb.owlapi.io.OWLObjectRenderer; 


public class MushroomReasoner {
	private OWLOntology mushroomOntology;
	private final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private final OWLDataFactory factory = manager.getOWLDataFactory();
    private final File file;
    private IRI mushroomOntologyIRI;
    //private Reasoner hermit;
    private OWLReasoner reasoner;
    private String[] mushroomFeatures;
    private int individualsCreated;
    private InferredOntologyGenerator generator;
//    @SuppressWarnings("deprecation")
//	private PrefixOWLOntologyFormat prefixManager;  
    //private OWLOntologyFormat pm = manager.getOntologyFormat(ontology);//.asPrefixOWLOntologyFormat();
    
    
    public MushroomReasoner(String filename, String[] mushroomFeatures) throws OWLOntologyCreationException {
	    this.file = new File(filename); 
	    this.mushroomFeatures = mushroomFeatures;
	    individualsCreated = 0;
	
	    //load the ontology and the reasoner
    	mushroomOntology = manager.loadOntologyFromOntologyDocument(file);
    	
    	//OWLReasoner reasoner=new ReasonerFactory().createReasoner(mushroomOntology, new Configuration());

    	OWLReasonerFactory reasonerFactory = new ReasonerFactory();
    	reasoner = reasonerFactory.createReasoner(mushroomOntology);
    	
    	reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
    
    	
    	//PELLET SUCKS
//    	OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
//        reasoner = reasonerFactory.createReasoner(mushroomOntology, new SimpleConfiguration());
     	System.out.println("INFERENCE TYPES: "+reasoner.getPrecomputableInferenceTypes());
     	System.out.println("Name and version: "+reasoner.getReasonerName()+"; "+reasoner.getReasonerVersion());
    	

        // need the ontology's IRI
    	Optional<IRI> possibleOntologyIRI = mushroomOntology.getOntologyID().getOntologyIRI();
        mushroomOntologyIRI = null;
        if (possibleOntologyIRI.isPresent()) {
        	mushroomOntologyIRI = possibleOntologyIRI.get();
        	System.out.println(mushroomOntologyIRI);
        } else {
        	System.out.println("No IRI");
        }
        
        generator = new InferredOntologyGenerator(reasoner);
        
//        //need PrefixManager for reasoning
//        prefixManager = (PrefixOWLOntologyFormat) manager.getOntologyFormat(mushroomOntology);
//        prefixManager.setDefaultPrefix(mushroomOntologyIRI + "#");
    }
    
    public OWLNamedIndividual createIndividual(String [] example) {
    	//System.out.println("EXAMPLE LENGTH "+example.length+", MUSHROOM FEATURES LENGTH "+mushroomFeatures.length);
    	if (example.length != mushroomFeatures.length) {
			System.out.println("This example is the wrong length");
			return null;
		}
    	IRI thing = IRI.create(mushroomOntologyIRI.toString()+"#"+"mushroom"+(individualsCreated++));
		OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(mushroomOntologyIRI.toString()+"#"+"mushroom"+individualsCreated++));
		
		for (int i=0; i<example.length; i++) {
			OWLDataProperty dp = factory.getOWLDataProperty(IRI.create(mushroomOntologyIRI.toString()+"#"+mushroomFeatures[i]));
			OWLDataPropertyAssertionAxiom dpaa; 
			OWLLiteral literal = factory.getOWLLiteral(example[i], OWL2Datatype.XSD_STRING);
			dpaa = factory.getOWLDataPropertyAssertionAxiom(dp, individual, literal);
			manager.addAxiom(mushroomOntology, dpaa);
		}
		return individual;
    }
    
    public void listSWRLRules() { 
        OWLObjectRenderer renderer = new DLSyntaxObjectRenderer(); 
        for (SWRLRule rule : mushroomOntology.getAxioms(AxiomType.SWRL_RULE)) { 
            System.out.println(renderer.render(rule)); 
        } 
    } 
    
    
    public boolean isEdible(String [] example) {
    	OWLNamedIndividual mushroom = createIndividual(example);
    	OWLDataProperty edible = factory.getOWLDataProperty(IRI.create(mushroomOntologyIRI+"#"+"edible"));
    	OWLLiteral value = factory.getOWLLiteral("e", OWL2Datatype.XSD_STRING);
    	OWLDataPropertyAssertionAxiom mushroomIsEdible = factory.getOWLDataPropertyAssertionAxiom(edible, mushroom, value);
    	return reasoner.isEntailed(mushroomIsEdible);
    }
    
    
    public boolean isPoisonous(String [] example) {
    	OWLNamedIndividual mushroom = createIndividual(example);
    	//System.out.println(reasoner.getTypes(mushroom, false));
    	//OWLDataProperty edible = factory.getOWLDataProperty(IRI.create(mushroomOntologyIRI+"#"+"edible"));
    	//OWLLiteral value = factory.getOWLLiteral("p", OWL2Datatype.XSD_STRING);
//    	OWLDataPropertyAssertionAxiom mushroomIsPoisonous = factory.getOWLDataPropertyAssertionAxiom(edible, mushroom, value);
    	OWLClass poisonousMushroom = factory.getOWLClass(IRI.create(mushroomOntologyIRI+"#PoisonousMushroom"));
    	OWLClassAssertionAxiom isPoisonousMushroom = factory.getOWLClassAssertionAxiom(poisonousMushroom, mushroom);
    	//listAllDataPropertyValues(mushroom);
    	//return reasoner.isEntailed(mushroomIsPoisonous);
    	//System.out.println(reasoner.isEntailed(isPoisonousMushroom));
    	//reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
    	manager.addAxiom(mushroomOntology, isPoisonousMushroom);
    	reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
    	return reasoner.isEntailed(isPoisonousMushroom);
//    	OWLClass.getOWLClass(":poisonRule1")
//    	OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(null, null);
    }
  
    
    
    public void update() {
    	generator.fillOntology(factory, mushroomOntology);
    }
    
    public void reason() {
    	reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
    	InferredPropertyAssertionGenerator generator = new InferredPropertyAssertionGenerator();
    	//Set<OWLAxiom> axioms = 
    	System.out.println("HERE THEY ARE");
    	System.out.println( generator.createAxioms(factory, reasoner) );
    	System.out.println("DONE");
    	
    }
    
    public void think() throws OWLOntologyCreationException {
    	List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<>();
//        gens.add(new InferredSubClassAxiomGenerator());  
//        gens.add(new InferredClassAssertionAxiomGenerator());
//        gens.add( new InferredDisjointClassesAxiomGenerator());
//        gens.add( new InferredEquivalentClassAxiomGenerator());
//        gens.add( new InferredEquivalentDataPropertiesAxiomGenerator());
//        gens.add( new InferredEquivalentObjectPropertyAxiomGenerator());
//        gens.add( new InferredInverseObjectPropertiesAxiomGenerator());
//        gens.add( new InferredObjectPropertyCharacteristicAxiomGenerator());
//        gens.add( new InferredPropertyAssertionGenerator());
    	gens.add( new InferredSubDataPropertyAxiomGenerator());
//        gens.add( new InferredSubObjectPropertyAxiomGenerator());
    	gens.add(new InferredDataPropertyCharacteristicAxiomGenerator());
    	
    	
        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
        OWLOntology infOnt = manager.createOntology();
        iog.fillOntology(factory, infOnt);
        for (OWLAxiom oa: infOnt.getAxioms()) {
        	System.out.println(oa);
        }
        //manager.saveOntology(infOnt,new RDFXMLDocumentFormat(),IRI.create(new File("D://file.owl")));   
    }
    
//    public void listAllDataPropertyValues(OWLNamedIndividual individual) { 
////        OWLObjectRenderer renderer = new DLSyntaxObjectRenderer(); 
////        Map<OWLDataPropertyExpression, Set<OWLLiteral>> assertedValues = reasoner.getDataPropertyValues(individual, factory.getOWLDataProperty(IRI.create(mushroomOntologyIRI+"#"+"edible")));//individual.getDataPropertyValues(mushroomOntology); 
////        for (OWLDataProperty dataProp : mushroomOntology.getDataPropertiesInSignature(Imports.EXCLUDED)) { 
////            for (OWLLiteral literal : reasoner.getDataPropertyValues(individual, dataProp)) { 
////                //Set<OWLLiteral> literalSet = assertedValues.get(dataProp); 
////                //boolean asserted = (literalSet!=null&&literalSet.contains(literal)); 
////                //System.out.println((asserted ? "asserted" : "inferred") + " data property for "+renderer.render(individual)+" : " + renderer.render(dataProp) + " -> " + renderer.render(literal)); 
////                System.out.println(" data property for "+renderer.render(individual)+" : " + renderer.render(dataProp) + " -> " + renderer.render(literal)); 
////            } 
////        } F
//        //System.out.println(individual);
//    	OWLDataProperty dataProp = factory.getOWLDataProperty(IRI.create(mushroomOntologyIRI+"#"+"capShape"));
//    	OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();
//    	//System.out.println(reasoner.getDataPropertyValues(individual, dataProp));
//    	
//    
//    } 
    
//    public static void listAllDataPropertyValues(OWLNamedIndividual individual, OWLOntology ontology, OWLReasoner reasoner) { 
//        OWLObjectRenderer renderer = new DLSyntaxObjectRenderer(); 
//        Map<OWLDataPropertyExpression, Set<OWLLiteral>> assertedValues = individual.getDataPropertyValues(ontology); 
//        for (OWLDataProperty dataProp : ontology.getDataPropertiesInSignature(true)) { 
//            for (OWLLiteral literal : reasoner.getDataPropertyValues(individual, dataProp)) { 
//                Set<OWLLiteral> literalSet = assertedValues.get(dataProp); 
//                boolean asserted = (literalSet != null && literalSet.contains(literal)); 
//                System.out.println((asserted ? "asserted" : "inferred") + " data property for " + renderer.render(individual) + " : " 
//                        + renderer.render(dataProp) + " -> " + renderer.render(literal)); 
//            } 
//        } 
//    } 
    
    public void test() {
//    	reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
//    	OWLClass mushroom = factory.getOWLClass(IRI.create(mushroomOntologyIRI+"#Mushroom"));
//    	OWLClass poisonousMushroom = factory.getOWLClass(IRI.create(mushroomOntologyIRI+"#PoisonousMushroom"));
//    	OWLSubClassOfAxiom pm_sub_m = factory.getOWLSubClassOfAxiom(poisonousMushroom, mushroom);
//    	System.out.println(reasoner.isEntailed(pm_sub_m));
//    	//AxiomType<OWLClassAssertionAxiom> x = AxiomType<OWLClassAssertionAxiom>.get
//    	//System.out.println(reasoner.isEntailmentCheckingSupported(x));
    	
    	System.out.println(mushroomOntology.getClassesInSignature());
    	
    	for (OWLClass cls : mushroomOntology.getClassesInSignature()) {                            
            
            if  (cls.getIRI().getFragment().equals("PoisonousMushroom")){
                System.out.println("My class is : " + cls.getIRI().getShortForm());
                System.out.println("The IRI of my class is : "+ cls);                        
                System.out.println("-----------------------");
                
                
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(cls, true);
                System.out.println("The Individuals of my class : ");
            
                for (OWLNamedIndividual i : instances.getFlattened()) {
                      System.out.println(i.getIRI().getFragment());             
                }
         }
            if  (cls.getIRI().getFragment().equals("Mushroom")){
                System.out.println("My class is : " + cls.getIRI().getShortForm());
                System.out.println("The IRI of my class is : "+ cls);                        
                System.out.println("-----------------------");
                
                
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(cls, true);
                System.out.println("The Individuals of my class : ");
            
                for (OWLNamedIndividual i : instances.getFlattened()) {
                      System.out.println(i.getIRI().getFragment());             
                }
                
         }
            
            if  (cls.getIRI().getFragment().equals("EdibleMushroom")){
                System.out.println("My class is : " + cls.getIRI().getShortForm());
                System.out.println("The IRI of my class is : "+ cls);                        
                System.out.println("-----------------------");
                
                
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(cls, true);
                System.out.println("The Individuals of my class : ");
            
                for (OWLNamedIndividual i : instances.getFlattened()) {
                      System.out.println(i.getIRI().getFragment());             
                }
                
         }
            
    	
    	}
    	
    	
    }
    
    public void save(String filename) throws OWLOntologyStorageException {
    	File ontologyFile = new File(filename);
    	manager.saveOntology(mushroomOntology, IRI.create(ontologyFile.toURI()));
    	System.out.println("Ontology with samples saved to " + ontologyFile.getAbsolutePath());
    }

	public OWLOntology getMushroomOntology() {
		return mushroomOntology;
	}

	public void setMushroomOntology(OWLOntology mushroomOntology) {
		this.mushroomOntology = mushroomOntology;
	}

	public IRI getMushroomOntologyIRI() {
		return mushroomOntologyIRI;
	}

	public void setMushroomOntologyIRI(IRI mushroomOntologyIRI) {
		this.mushroomOntologyIRI = mushroomOntologyIRI;
	}

	public String[] getMushroomFeatures() {
		return mushroomFeatures;
	}

	public void setMushroomFeatures(String[] mushroomFeatures) {
		this.mushroomFeatures = mushroomFeatures;
	}

	public int getIndividualsCreated() {
		return individualsCreated;
	}

	public void setIndividualsCreated(int individualsCreated) {
		this.individualsCreated = individualsCreated;
	}

	public OWLOntologyManager getManager() {
		return manager;
	}
	
	public OWLDataFactory getFactory() {
		return factory;
	}
   
}
