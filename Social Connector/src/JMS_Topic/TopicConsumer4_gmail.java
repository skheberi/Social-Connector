package cs578_topic;

import javax.jms.*;
import javax.naming.*;
import javax.net.ssl.HttpsURLConnection;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Location;
import com.restfb.types.Page;
import com.restfb.types.Place;

import antlr.debug.Event;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

public class TopicConsumer4_gmail implements MessageListener {

	public static void main(String args[])
			throws Exception {
		System.out.println("Entering example topic consumer 4");
		Context context = TopicConsumer4_gmail.getInitialContext();
		TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
		Topic topic = (Topic) context.lookup("topic/zaneacademy_jms_tutorial_01");
		TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
		TopicSession topicSession = topicConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
		topicConnection.start();
		//System.out.println("Exiting example topic consumer 4");

		while (true) {
			TextMessage msg = (TextMessage) topicSubscriber.receive();
			JSONObject json_data = new JSONObject(msg.getText());
			JSONArray arr = json_data.getJSONArray("entry");
			JSONObject first = (JSONObject) arr.get(0);
			String field = (String) (((JSONObject) first.getJSONArray("changes").get(0)).get("field"));
			System.out.println(field);
			if (field.equals("events")) {
				String event_id = (String) ((JSONObject) ((JSONObject) first.getJSONArray("changes").get(0))
						.get("value")).get("event_id");

				String accessToken = "EAAY88F8MVc8BANR93A6NFneJQ8Fnb286m4NT7hhdRX3hEFRe9z1Aq83Wo9MSsnIBiRjvkcbgpEqrHUP0iO0krZAIijDwG5GhVZAFQIK1wYwsxtgooGdJErrQxnnmdZBL2gNvuhp1KuXDx6dCE6Kb3ZBxeKzUIiAZD";
				FacebookClient fbclient = new DefaultFacebookClient(accessToken);
				
				String url1 = "https://graph.facebook.com/v2.8/" + event_id+"?access_token="+accessToken;
				URL url = new URL(url1);
		        HttpsURLConnection conn;
		            conn = (HttpsURLConnection) url.openConnection();
		            conn.setRequestMethod("GET");
		        String line;
		            
		        StringBuilder sb;
		            sb = new StringBuilder();
		            BufferedReader br;
		            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		            while (!((line = br.readLine()) == null)) {
		           
		            sb.append(line);
		        }
		       String result = sb.toString();
		      
		       JSONObject obj = new JSONObject(result);
		       String description = obj.getString("description");
		       String start_time = obj.getString("start_time");
		       String end_time = obj.getString("end_time");
		       String name = obj.getString("name");
		       obj = new JSONObject(obj.getString("place"));
		       obj = obj.getJSONObject("location");
		       String lat = obj.getString("latitude");
		       String longitude = obj.getString("longitude") ;
		       
		      System.out.println(convert(start_time));
		      System.out.println(time(start_time));
		       CalendarDemo cd = new CalendarDemo();
		       cd.calendar(convert(start_time), name, time(start_time), convert(end_time), time(end_time));
		       
				
				
			}
			else
			{
				System.out.println("Producer did not produce data needed for me");
			}
			
			
			
			
			
			

		}

	}

	

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			// System.out.println("Hello");
			System.out.println("Incoming message:" + ((TextMessage) message).getText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Context getInitialContext() throws JMSException, NamingException {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", "localhost:1099");
		Context context = new InitialContext(props);
		return context;

	}
	
	public static String convert(String start_time)
	{
//		 SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
//	        String newDateStr = postFormater.format(start_time);
//	        return String.valueOf(newDateStr);
	    String result = start_time;
	    String date = result.split("T")[0];
	    date = date.replaceAll("-", "/");
	   
	    return date;
		
	}
	public static String time (String start_time)
	{ 
		//String result = null;
		String result = start_time;
		boolean flag = false;
		 String time1  = result.split("T")[1].split(":")[0];
		    String time2  = result.split("T")[1].split(":")[1];
		    int t = Integer.parseInt(time1);
		    if( t> 12){
		    	t = t-12;
		    	flag = true;
		    }
		    String time = t + "." + time2;
		    if(flag){
		    	time = time+"pm";
		    }
		    else{
		    	time = time+"am";
		    }
		    return time;
	}
	
	
}
