package ids_Monitoring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jpl7.*;

public class IDS_Monitoring {
	private static List<String> symtomps;
	public static void main(String[] args) throws URISyntaxException {
		Query q = new Query("consult('System10.pl')");
		q.hasSolution();
		URI uri = new URI("http://www.semanticweb.org/valle/ontologies/2019/11/untitled-ontology-31");
		setDefaultValues();
		/*q = new Query("assert(class('mia'))."); //per creare nuovi fact senza modificare il file
		q.hasSolution();*/
		
		/*q = new Query("class(X).");
		  
		Map<String,Term>[] res = q.allSolutions();
		for(int i=0; i<res.length; i++) {
			System.out.println(res[i]);
		}*/
		
		/*q = new Query("classAssertion("+uri+"/Symptom',Y)");
		  
		Map<String,Term>[] res = q.allSolutions();
		q = new Query("assert(ontology(3))");
		q.hasSolution();
		q = new Query("ontology(X)");
		Map<String,Term>[] res1 = q.allSolutions();
		for(int i=0; i<res1.length; i++) {
			System.out.println(res1[i]);
		}
		
		
		for(int i=0; i<res.length; i++) {
			System.out.println(res[i]);
		}
		System.out.println(getPartialIri(res[0].toString()));
		
		*/
		List<OssecAlert> alerts = getListAlertsFromJSON();
		
		alerts.forEach(x -> {
			try {
				symtomps = new ArrayList<>(); //creo una var locale di sintomi per ogni alert
				symtomps = executeQueryGetSymptomFromAlert(x);
				/*for(int i=0; i<symtomps.size(); i++) {
					System.out.println("Result: "+cleanOutput(symtomps.get(i)));
					executeQueryGetAttackFromSymptom(cleanOutput(symtomps.get(i)));
					
				}*/
				//propertyAssertion(X,'http://www.semanticweb.org/valle/ontologies/2019/11/untitled-ontology-31#Level',Z). 
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		executeQueryAttackBySymptom();
		q = new Query("getAttackwithLevelNot0(X,Z)");
		  
		Map<String,Term>[] res = q.allSolutions();
		for(int i=0; i<res.length; i++) {
			System.out.println("\nThe following attack has been detected:"); //attacks that has a DetectedLevel != 0
			//1= Low, 2=Medium, 3=High
			System.out.println(res[i]);
		}
		
		q = new Query("getAttackScenariobyAttack(X,Y)"); //X è attacco, Z è penetration tester, A sono i threat, Y è attack scenario 
		//devo fare qualcosa che quando 2 threat dell'attack scenario sono stati rilevati allora è possibile che l'attack scenario sia in funzione
		//1: modificare ontologia (aggiungere individui dei threat e questa nuova proprietà dei threat, e anche proprietà detected per l'attack scenario) 
		//2: creare regole per azionare attack scenario se ci sono almeno 2 threat detected
		  
		Map<String,Term>[] res1 = q.allSolutions();
		for(int i=0; i<res.length; i++) { //devo aggiustare questo, funziona ma va scritto meglio
			System.out.println("\nThe following attack Scenario can be used:"); 
			System.out.println(res1[i]);
		}
		
		q.close();
	}
	public static List<OssecAlert> getListAlertsFromJSON() {
		Gson gson = new Gson();
		List<OssecAlert> alerts = new ArrayList<>();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"alerts1.json"));   //file dove si trovano gli alert
			String line = reader.readLine();
			while (line != null) {
				alerts.add(gson.fromJson(line, OssecAlert.class));
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	    return alerts;
	}
	public static String getPartialIri(String input) { //serve per ottenere la parte finale dell'IRI (cioè non tutto il link)
		int iend = input.lastIndexOf("#"); //da l'indice di dove si trova # nella stringa
		String subString = null;
		if (iend != -1) 
		{
		    subString= input.substring(iend +1 , input.length()); 
		} else { //nel caso in cui l'iri non è del tipo #partialIRI; questo potrei toglierlo dovrebbero essere tutti di questo tipo
			iend = input.lastIndexOf("/");
			subString= input.substring(iend +1 , input.length()); 
		}
		subString = subString.replace("'", "");//a volte la sottostringa ha ancora parti finali dell'IRI completo
		subString = subString.replace("}", "");
		return subString;
    }
	public static String getXSolution(String input) { //serve per ottenere la parte finale dell'IRI (cioè non tutto il link)
		int ifirst = input.lastIndexOf("X"); //da l'indice di dove si trova # nella stringa
		String subString = null;
		int iend = input.lastIndexOf(","); //da l'indice di dove si trova # nella stringa
		if (ifirst != -1 && iend != -1) 
		{
		    subString= input.substring(ifirst +2 , iend); 
		} 
		subString = subString.replace("'", "");//a volte la sottostringa ha ancora parti finali dell'IRI completo
		return subString;
    }
	public static String cleanOutput(String input) {
		String subString = input.substring(3 , input.length()); 
		subString = subString.replace("}", "");
		return subString;
	}
	public static List<String> executeQueryGetSymptomFromAlert(OssecAlert alert) throws URISyntaxException { //inserisco l'alert trovato e mi da i relativi sintomi
		//symptomByAlert(X,Y):-(propertyAssertion(X,'http://www.semanticweb.org/valle/ontologies/2019/11/untitled-ontology-31#OssecRuleID',Y),assert(propertyAssertion(X,'http://www.semanticweb.org/valle/ontologies/2019/11/untitled-ontology-31#isDetected',true))).
		List<String> output = new ArrayList<>();
		Query q = new Query("symptomByAlert(X,'"+alert.getRuleSidid()+"')");
		System.out.println("Query executed: symptomByAlert(X,'"+alert.getRuleSidid()+"')");
		Map<String,Term>[] res = q.allSolutions();
		if (res.length==0) {
			System.out.println("No output");
		}
		else {
			for(int i=0; i<res.length; i++) {
				System.out.println("The following symptom has been detected: "+String.valueOf(res[i]));
				output.add(String.valueOf(res[i]));
			}
		}
		return output;
	}
	public static List<String> executeQueryAttackBySymptom() throws URISyntaxException { 
		List<String> output = new ArrayList<>();
		Query q = new Query("attackBySymptom(X,Y)");
		System.out.println("\nQuery executed: attackBySymptom(X,Y)");
		Map<String,Term>[] res = q.allSolutions();
		if (res.length==0) {
			System.out.println("No output");
		}
		else {
			for(int i=0; i<res.length; i++) {
				output.add(String.valueOf(res[i]));	
				System.out.println("The following X is a detected attack: "+getXSolution(String.valueOf(res[i])));
				q = new Query("upgradeAttackLevel('"+getXSolution(String.valueOf(res[i]))+"')");
				System.out.println("Query executed: upgradeAttackLevel('"+getXSolution(String.valueOf(res[i]))+"')");
				q.allSolutions();

			}
		}
		return output;
	}
	
	public static String executeQueryGetAttackFromSymptom(String symptom) throws URISyntaxException { 
		
		URI uri = new URI("http://www.semanticweb.org/valle/ontologies/2019/11/untitled-ontology-31");
		//propertyAssertion(X,'http://www.semanticweb.org/valle/ontologies/2019/11/untitled-ontology-31#hasSymptom','http://www.semanticweb.org/valle/ontologies/2019/11/untitled-ontology-31#PortSentryAttackAlert').
		Query q = new Query("propertyAssertion(X,'"+uri+"#hasSymptom',"+symptom+")");

		System.out.println("Eseguo la query prolog: "+"propertyAssertion(X,'"+uri+"#hasSymptom',"+symptom+")");
		Map<String,Term>[] res = q.allSolutions();
		for(int i=0; i<res.length; i++) {
			System.out.println("Result: "+cleanOutput(String.valueOf(res[i])));
			System.out.println("Porto l'attackIndicator di "+getPartialIri(String.valueOf(res[i]))+" a 0.5");
		}
		return ""+String.valueOf(res[0]);
	}
	public static void setDefaultValues() {
		Query q = new Query("setDefaultLevelAttack(X)");//attack indicator set to null
		q.allSolutions();
		q = new Query("setDefaultDetectedSymptom(X)");//boolean detected var set to false
		q.allSolutions();
		System.out.println("Setting default values...");
	}
}
