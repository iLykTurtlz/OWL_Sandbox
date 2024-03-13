package owlapi.tutorial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormatImpl;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;


@SuppressWarnings("unused")
public class OWLAPIFirst {
	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		
		
		
		//Creating an Ontology
		/*
		OWLOntology o;
		try {
			o = man.createOntology();
			System.out.println(o);
		}
		catch (OWLOntologyCreationException e) {
			System.out.println("Something went wrong.\n"+e.getMessage());
			e.printStackTrace();
		}
		*/
		
		
		
		
		//Loading from internet
		//THIS one does NOT work for some reason
		/*
		IRI pizzaontology = IRI.create("http://protege.stanford.edu/ontologies/pizza/pizza.owl");
		OWLOntology o;
		try {
			o = man.loadOntology(pizzaontology);
			System.out.println(o);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
		
		//Loading an ontology from a local file
		/*
		File file = new File("./pizza.owl"); //or pizza.owl.xml
		OWLOntology o;
		try {
			o = man.loadOntologyFromOntologyDocument(file);
			System.out.println(o);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
		
		
		//Here we load from internet, save to file, load from that file - it WORKS
		/*
		IRI familyOntology = IRI.create("http://www.cs.man.ac.uk/~stevensr/ontology/family.rdf.owl");
		OWLOntology o;
		try {
			o = man.loadOntology(familyOntology);
			File file = new File("./family.rdf.owl");
			man.saveOntology(o, new ManchesterSyntaxDocumentFormat(), new FileOutputStream(file)); //not well formed
			OWLOntologyManager man2 = OWLManager.createOWLOntologyManager();
			OWLOntology o2 = man2.loadOntologyFromOntologyDocument(file);
			System.out.println("Axioms: "+o2.getAxiomCount()+", Format: "+man2.getOntologyFormat(o2));
		} catch (OWLOntologyCreationException ooce) {
			ooce.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//OWLDocumentFormatImpl i; //ctrl + t to check the type hierarchy
		*/
		
		
		
		//Create an ontology and add a class to it
		/*
		IRI IOR = IRI.create("http://owl.api.tutorial");
		try {
			OWLOntology o = man.createOntology(IOR);
			OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
			OWLClass person = df.getOWLClass(IRI.create(IOR+"#Person")); //df.getOWLClass(IOR+"#Person") is the newer way
			OWLDeclarationAxiom da = df.getOWLDeclarationAxiom(person);
			man.addAxiom(o, da); //o.add(da); newer version
								 //AddAxiom ax = new AddAxiom(o, da); man.applyChange(ax); another way

			System.out.println(o);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
		
		//Create an ontology and add a class and a subclass of that class
//		IRI IOR = IRI.create("http://owl.api.tutorial");
//		try {
//			OWLOntology o = man.createOntology(IOR);
//			OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
//			OWLClass person = df.getOWLClass(IRI.create(IOR+"#Person"));
//			OWLClass female = df.getOWLClass(IRI.create(IOR+"#Female"));
//			OWLSubClassOfAxiom wSubP = df.getOWLSubClassOfAxiom(female, person);
//			man.addAxiom(o, wSubP);
//			OWLClass male = df.getOWLClass(IRI.create(IOR+"#Male"));
//			OWLSubClassOfAxiom mSubP = df.getOWLSubClassOfAxiom(male, person);
//			man.addAxiom(o, mSubP);
//			OWLDisjointClassesAxiom disjointAxiom = df.getOWLDisjointClassesAxiom(male, female);
//			man.addAxiom(o, disjointAxiom);			
//			System.out.println(o);
//			
//			// OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
//            // OWLReasoner reasoner = reasonerFactory.createReasoner(o);
//            // boolean consistent = reasoner.isConsistent();
//            // System.out.println("Ontology is consistent: " + consistent);
//            
//		} catch (OWLOntologyCreationException e) {
//			e.printStackTrace();
//		}
		
		
		//Read the Mushrooms dataset into 'data'
		List<String[]> data = readCSV("agaricus-lepiota.csv");
		
		
		
		String[] attributes = {"edible","capShape","capSurface","capColor","bruises","odor","gillAttachment","gillSpacing","gillSize","gillColor","stalkShape","stalkRoot","stalkSurfaceAboveRing","stalkSurfaceBelowRing","stalkColorAboveRing","stalkColorBelowRing","veilType","veilColor","ringNumber","ringType","sporePrintColor","population","habitat"};
		
		
		
		
        try {
            // Setup
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLDataFactory factory = manager.getOWLDataFactory();
            
            
            File file = new File("./mushroom.rdf"); //or pizza.owl.xml
            OWLOntology mushroomOntology = manager.loadOntologyFromOntologyDocument(file);
            System.out.println(mushroomOntology);
            
            Optional<IRI> possibleOntologyIRI = mushroomOntology.getOntologyID().getOntologyIRI();
            IRI mushroomOntologyIRI = null;
            if (possibleOntologyIRI.isPresent()) {
            	mushroomOntologyIRI = possibleOntologyIRI.get();
            	System.out.println(mushroomOntologyIRI);
            } else {
            	System.out.println("No IRI");
            }

            populateOntology(data, attributes, mushroomOntologyIRI, manager, mushroomOntology, factory);
            
            File populatedOntologyFile = new File("./mushroomPopulated.rdf");
            manager.saveOntology(mushroomOntology, IRI.create(populatedOntologyFile.toURI()));
            System.out.println("Ontology with samples saved to " + populatedOntologyFile.getAbsolutePath());
            
            for (OWLNamedIndividual individual : mushroomOntology.getIndividualsInSignature()) {
                System.out.println("Individual: " + individual.getIRI());

                // Print data property values
                for (OWLDataPropertyAssertionAxiom dataPropertyAssertionAxiom : mushroomOntology.getDataPropertyAssertionAxioms(individual)) {
                    System.out.println("Data Property: " + dataPropertyAssertionAxiom.getProperty().asOWLDataProperty().getIRI());
                    System.out.println("Value: " + dataPropertyAssertionAxiom.getObject());
                }

                // Add more code to handle other types of axioms if needed (object properties, classes, etc.)
                // ...

                System.out.println(); // Add a line break between individuals
            }
            
           
            System.out.println("Yay!");
            
            
            IRI ontologyIRI = IRI.create("http://example.org/mushroom");
            OWLOntology ontology = manager.createOntology(ontologyIRI);

            // Define Classes
            OWLClass mushroomClass = factory.getOWLClass(IRI.create(ontologyIRI + "#Mushroom"));
            OWLClass edibleClass = factory.getOWLClass(IRI.create(ontologyIRI + "#Edible"));
            OWLClass poisonousClass = factory.getOWLClass(IRI.create(ontologyIRI + "#Poisonous"));

            // Define Properties
            OWLObjectProperty hasCapShape = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasCapShape"));
            OWLObjectProperty hasCapColor = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasCapColor"));
            OWLObjectProperty hasHabitat = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasHabitat"));
            OWLObjectProperty hasEdibility = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasEdibility"));
            
            OWLNamedIndividual leavesHabitat = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Leaves"));
            OWLNamedIndividual whiteCapColor = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#White"));
            
            OWLClassExpression leavesHabitatCondition = factory.getOWLObjectSomeValuesFrom(hasHabitat, factory.getOWLClass(leavesHabitat.getIRI()));
            OWLClassExpression whiteCapColorCondition = factory.getOWLObjectSomeValuesFrom(hasCapColor, factory.getOWLClass(whiteCapColor.getIRI()));

            // Combine the conditions using an AND operator
            OWLClassExpression poisonousMushroomCondition = factory.getOWLObjectIntersectionOf(leavesHabitatCondition, whiteCapColorCondition);

            // Specify that mushrooms meeting these conditions are poisonous
            OWLAxiom axiom = factory.getOWLSubClassOfAxiom(poisonousMushroomCondition, poisonousClass);

            // Add the axiom to the ontology
            manager.addAxiom(ontology, axiom);

            // Add axioms to declare classes and properties in the ontology
            manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(mushroomClass));
            manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(edibleClass));
            manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(poisonousClass));
            manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(hasCapShape));
            manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(hasCapColor));
            manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(hasHabitat));
            manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(hasEdibility));
       
            
            //THE FACTORY CAN MAKE SWRLVariables!  Yay!
         
            SWRLVariable var = factory.getSWRLVariable(IRI.create(ontologyIRI + "#x"));
            
 
            
            
            

            Random rand = new Random();
            String[] capShapes = {"bell", "conical", "convex", "flat", "knobbed", "sunken"};
            String[] capColors = {"white", "yellow", "brown", "red", "green", "blue", "purple", "pink", "black", "orange"};
            String[] habitats = {"leaves", "woods", "meadows", "paths", "urban", "grass", "waste"};

            // Generate 10 sample mushrooms
            for (int i = 1; i <= 10; i++) {
                OWLNamedIndividual mushroom = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Mushroom" + i));
                String selectedShape = capShapes[rand.nextInt(capShapes.length)];
                String selectedHabitat = habitats[rand.nextInt(habitats.length)];
                String selectedCapColor = capColors[rand.nextInt(capColors.length)];

                // Assign properties to the mushroom
                OWLNamedIndividual shapeIndividual = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#" + selectedShape));
                OWLNamedIndividual habitatIndividual = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#" + selectedHabitat));
                OWLNamedIndividual capColorIndividual = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#" + selectedCapColor));

                manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(hasCapShape, mushroom, shapeIndividual));
                manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(hasCapColor, mushroom, capColorIndividual));
                manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(hasHabitat, mushroom, habitatIndividual));

                // Determine the edibility based on the conditions
                OWLNamedIndividual edibilityStatus;
                if (selectedHabitat.equals("leaves") && selectedCapColor.equals("white")) {
                    edibilityStatus = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Poisonous"));
                } else {
                    edibilityStatus = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Edible"));
                }

                // Explicitly assign the edibility status to the mushroom
                manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(hasEdibility, mushroom, edibilityStatus));
            }
            
            for (OWLNamedIndividual mushroom : ontology.getIndividualsInSignature()) {
                System.out.println("Mushroom: " + mushroom.getIRI().getShortForm());
                
                Set<OWLObjectPropertyAssertionAxiom> properties = ontology.getObjectPropertyAssertionAxioms(mushroom);
                for (OWLObjectPropertyAssertionAxiom property : properties) {
                    System.out.println("Property: " + property.getProperty().asOWLObjectProperty().getIRI().getShortForm() +
                                       ", Value: " + property.getObject().asOWLNamedIndividual().getIRI().getShortForm());
                }
                System.out.println("------");
            }

            // Save the ontology with the sample data
            File ontologyFile = new File("mushroomOntologyWithSamples.owl");
            manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
            System.out.println("Ontology with samples saved to " + ontologyFile.getAbsolutePath());
        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            e.printStackTrace();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("mushroomSamples.txt"))) {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            File file = new File("mushroomOntologyWithSamples.owl");
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
            
            writer.write("MushroomID,CapShape,CapColor,Edibility");
            writer.newLine();

            // Iterate over all individuals in the ontology
            for (OWLNamedIndividual mushroom : ontology.getIndividualsInSignature()) {
                String mushroomID = mushroom.getIRI().getShortForm();
                String capShape = "", capColor = "", edibility = "";

                Set<OWLObjectPropertyAssertionAxiom> properties = ontology.getObjectPropertyAssertionAxioms(mushroom);
                for (OWLObjectPropertyAssertionAxiom property : properties) {
                    OWLNamedIndividual object = property.getObject().asOWLNamedIndividual();
                    String propertyShortForm = property.getProperty().asOWLObjectProperty().getIRI().getShortForm();
                    String objectShortForm = object.getIRI().getShortForm();

                    if (propertyShortForm.equals("hasCapShape")) {
                        capShape = objectShortForm;
                    } else if (propertyShortForm.equals("hasCapColor")) {
                        capColor = objectShortForm;
                    } else if (propertyShortForm.equals("hasEdibility")) {
                        edibility = objectShortForm;
                    }
                }

                // Write the mushroom details to the file only if all attributes are present
                if (!mushroomID.startsWith("Mushroom")) {
                    continue; // Skip non-mushroom individuals
                }

                writer.write(mushroomID + "," + capShape + "," + capColor + "," + edibility);
                writer.newLine();
            }

            System.out.println("Sample data saved to mushroomSamples.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }


        
		
		
		
		
		

		
		
		
	}
	
	
	
	public static void populateOntology(List<String[]> rows, String[] attributes, IRI ontologyIRI, OWLOntologyManager man, OWLOntology o, OWLDataFactory df) {
		int exampleNumber = 0;
		for (String[] row : rows) {
			if (row.length != attributes.length) {
				System.out.println("Rows are the wrong length");
				return;
			}
			OWLNamedIndividual individual = df.getOWLNamedIndividual(IRI.create(ontologyIRI.toString()+"#"+exampleNumber));
			for (int i=0; i<row.length; i++) {
				OWLDataProperty dp = df.getOWLDataProperty(IRI.create(ontologyIRI.toString()+"#"+attributes[i]));
				OWLDataPropertyAssertionAxiom dpaa; 
				OWLLiteral literal;
				if (row[i].equals("edible")) {
					literal = df.getOWLLiteral(row[i], OWL2Datatype.XSD_BOOLEAN);
				} else {
					literal = df.getOWLLiteral(row[i], OWL2Datatype.XSD_STRING);	
				}
				dpaa = df.getOWLDataPropertyAssertionAxiom(dp, individual, literal);
				man.addAxiom(o, dpaa);
			}
			exampleNumber++;
		}
	}
	
	

		
	public static List<String[]> readCSV(String filename) {
		//"agaricus-lepiota.csv"
		CSVParser csvParser = new CSVParserBuilder()
				.withSeparator(',')
				.withIgnoreQuotations(true)
				.build();
		
		List<String[]> data = null;
		try {
			CSVReader csvReader = new CSVReaderBuilder(new FileReader(filename))
					.withSkipLines(3)
					.build();
			data = csvReader.readAll();
		} catch (FileNotFoundException fnfe) {
			System.out.println("Dude, where's my file!");
			fnfe.printStackTrace();
		} catch (CsvException ce) {
			System.out.println("Bruh, this thing you call a csv...it sucks.");
			ce.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("If you see this, you did something very wrong.");
			ioe.printStackTrace();
		}
		return data;
	}
	
}
