package ids_Monitoring;


import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonParser {
	Gson gson = new Gson();
	
	String json = "{\"rule\":{\"level\":7,\"comment\":\"Partition usage reached 100% (disk space monitor).\",\"sidid\":531,\"firedtimes\":1,\"groups\":[\"ossec\",\"low_diskspace\"]},\"id\":\"1584226899.0\",\"TimeStamp\":1584226899000,\"decoder\":\"ossec\",\"location\":\"df -P\",\"full_log\":\"ossec: output: 'df -P': /dev/loop0            15104   15104         0     100% /snap/gnome-characters/399\",\"hostname\":\"sannio-Standard-PC-i440FX-PIIX-1996\",\"decoder_desc\":{\"name\":\"ossec\"},\"agent_name\":\"sannio-Standard-PC-i440FX-PIIX-1996\",\"timestamp\":\"2020 Mar 15 00:01:39\",\"logfile\":\"df -P\"}\r\n";

    // Convert JSON File to Java Object
    OssecAlert alert = gson.fromJson(json, OssecAlert.class);
		
		// print staff 

}
