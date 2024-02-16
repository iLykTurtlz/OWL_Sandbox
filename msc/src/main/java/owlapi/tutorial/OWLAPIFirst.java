package owlapi.tutorial;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormatImpl;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class OWLAPIFirst {
	public static void main(String[] args) {
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
		/*
		IRI IOR = IRI.create("http://owl.api.tutorial");
		try {
			OWLOntology o = man.createOntology(IOR);
			OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
			OWLClass person = df.getOWLClass(IRI.create(IOR+"#Person"));
			OWLClass woman = df.getOWLClass(IRI.create(IOR+"#Woman"));
			OWLSubClassOfAxiom wSubP = df.getOWLSubClassOfAxiom(woman, person);
			man.addAxiom(o, wSubP);
			System.out.println(o);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		*/
		
		
		
		
		
		
		

		
		
		
	}
}
