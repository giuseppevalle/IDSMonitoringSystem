package ids_Monitoring;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import com.google.gson.Gson;

import org.jpl7.*;


public class IDS_Monitoring {
	public static void main(String[] args) throws URISyntaxException {
		Query q = new Query("consult('ok.pl')");
		q.hasSolution();
		URI uri = new URI("http://www.semanticweb.org/valle/ontologies/2019/11/untitled-ontology-31");
		q = new Query("type('"+uri+"/Symptom',Y)");
	      System.out.println("Hello, World!");
	      
	      Map<String,Term>[] res = q.allSolutions();
	      
	      for(int i=0; i<res.length; i++) {
	    	  System.out.println(res[i]);
	      }
	      System.out.println(getPartialIri(res[0].toString()));
	      
	      Gson gson = new Gson();
	  	
	  	String json = "{\"rule\":{\"level\":7,\"comment\":\"Partition usage reached 100% (disk space monitor).\",\"sidid\":531,\"firedtimes\":1,\"groups\":[\"ossec\",\"low_diskspace\"]},\"id\":\"1584226899.0\",\"TimeStamp\":1584226899000,\"decoder\":\"ossec\",\"location\":\"df -P\",\"full_log\":\"ossec: output: 'df -P': /dev/loop0            15104   15104         0     100% /snap/gnome-characters/399\",\"hostname\":\"sannio-Standard-PC-i440FX-PIIX-1996\",\"decoder_desc\":{\"name\":\"ossec\"},\"agent_name\":\"sannio-Standard-PC-i440FX-PIIX-1996\",\"timestamp\":\"2020 Mar 15 00:01:39\",\"logfile\":\"df -P\"}";

	      // Convert JSON File to Java Object
	      OssecAlert alert = gson.fromJson(json, OssecAlert.class);
	      System.out.println(alert.getRuleComment());
	      System.out.println(alert.getID());

	      
		/*Query q = new Query("use_module(library(thea2/owl2_io)).");
		q.hasSolution();
		q = new Query("load_axioms('C:/Program Files/swipl/myont.owl').");
		q.hasSolution();
		System.out.println("Hello, World!");

		q = new Query("print(X)");
		Map<String,Term>[] res1 = q.allSolutions();
	      
	      for(int i=0; i<res1.length; i++) {
	    	  System.out.println(res1[i]);
	      }*/
	      
	}
	public static String getPartialIri(String input) {
		int iend = input.lastIndexOf("#");
		String subString = null;
		if (iend != -1) 
		{
		    subString= input.substring(iend +1 , input.length()); 
		} else {
			iend = input.lastIndexOf("/");
			subString= input.substring(iend +1 , input.length()); 
		}
		subString = subString.replace("'", "");
		subString = subString.replace("}", "");
		return subString;
    }
}
