package cs578_topic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CalendarDemo {
static	String result;
public static void main(String args[]) throws Exception
{
}
public void calendar(String timeStart, String summary, String dateStart,String dateEnd,String timeEnd) throws IOException{
//	String summary="Baba+Blacksheep";
//	String dateStart="2017/4/25";
//	String timeStart="4.30pm";
//	String dateEnd="2017/4/25";
//	String timeEnd="5.30pm"
summary=summary.replace(" ","+");
	URL url = new URL("http://30boxes.com/api/api.php?method=events.AddByElements&apiKey=8529624-9d57f59f534ae658c50e0f44a5a0630c&authorizedUserToken=8529624-28bb0d627b980b5e19df2e782ebe1695&summary="+summary+"&notes=Event+Generated+from+facebook&dateStart="+dateStart+"&timeStart="+timeStart+"&dateEnd="+dateEnd+"&timeEnd="+timeEnd);
    HttpURLConnection conn;
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
    String line;
        
    StringBuilder sb;
        sb = new StringBuilder();
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while (!((line = br.readLine()) == null)) {
       
        sb.append(line);
    }
   result = sb.toString();
   System.out.println(result);
}
}