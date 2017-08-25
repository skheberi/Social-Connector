package cs578_topic;

import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.jboss.util.Base64.InputStream;


public class FilePoller {
public static void main(String args[])
{
	System.out.println("Hello");
	try{
		WatchService service = FileSystems.getDefault().newWatchService();
		Map<WatchKey,Path> keymap = new HashMap<>();
//		URL url = new URL("ftp://ericoid-ways:SoftwareArchitecture@files.000webhost.com/public_html/");
//		URLConnection urlConn = url.openConnection();
//		//InputStream in = (InputStream) urlConn.getInputStream();
//		System.out.println(urlConn + "this is the fetched data");
//		urlConn.getURL();
//		System.out.println(urlConn.getURL());
////		urlConn.setDoInput(true);
////		urlConn.setDoOutput(dooutput);
		
		Path path = Paths.get("files");
		keymap.put(path.register(service,StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_MODIFY),path);
		
		WatchKey watchKey;
		do
		{
			watchKey = service.take();
			Path eventDir = keymap.get(watchKey);
			for(WatchEvent<?> event : watchKey.pollEvents()){
				WatchEvent.Kind<?> kind = event.kind();
				Path eventPath = (Path)event.context();
				
				System.out.println(eventDir + ":" + kind + ":"+ eventPath);
				
			}
			
		}while(watchKey.reset());
		
		
	}
	catch(Exception e){}
	
}
}

