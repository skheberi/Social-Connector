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
import com.sun.mail.iap.ProtocolException;

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
import java.util.Properties;

public class TopicConsumer2_maps implements MessageListener {

	public static void main(String args[])
			throws Exception {
		String place = null;
		Double lat=null,log=null;// initializing of variables
		System.out.println("Entering example topic consumer 2 (Google Maps)");
		// initialize context 
		Context context = TopicConsumer2_maps.getInitialContext();
		TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
		// consumser lookup for the topic
		Topic topic = (Topic) context.lookup("topic/zaneacademy_jms_tutorial_01");
		//create connection for topic
		TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
		// create topic session
		TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		// subscriber is created
		TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
		// connection is started
		topicConnection.start();
		//System.out.println("Exiting example topic consumer 2");

		while (true) {
			// gets the msg from topic and google maps is opened to save the location in your profile
			TextMessage msg = (TextMessage) topicSubscriber.receive();

			JSONObject json_data = new JSONObject(msg.getText());
			JSONArray arr = json_data.getJSONArray("entry");
			JSONObject first = (JSONObject) arr.get(0);
			String field = (String) (((JSONObject) first.getJSONArray("changes").get(0)).get("field"));
			if (field.equals("events")||field.equals("location")) { // this consumser accepts msg for event identifier events and location
				if(field.equals("location")){
			String page_id = (String) ((JSONObject) ((JSONObject) first.getJSONArray("changes").get(0)).get("value"))
					.get("page");
			System.out.println("id" + page_id);

			String accessToken = "EAAY88F8MVc8BANR93A6NFneJQ8Fnb286m4NT7hhdRX3hEFRe9z1Aq83Wo9MSsnIBiRjvkcbgpEqrHUP0iO0krZAIijDwG5GhVZAFQIK1wYwsxtgooGdJErrQxnnmdZBL2gNvuhp1KuXDx6dCE6Kb3ZBxeKzUIiAZD";
			FacebookClient fbclient = new DefaultFacebookClient(accessToken);
			Place me = (Place) fbclient.fetchObject(page_id, Place.class, Parameter.with("fields", "location"));
			Location loc = me.getLocation();
			 lat = loc.getLatitude();
			 log = loc.getLongitude();
				}
				if(field.equals("events")){
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
			      
			       obj = new JSONObject(obj.getString("place"));
			       obj = obj.getJSONObject("location");
			       lat = Double.parseDouble(obj.getString("latitude"));
			       System.out.println(lat);
			       
			       log = Double.parseDouble(obj.getString("longitude")) ;
			       System.out.println(log);
				}
			
			
			try {
		        URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+log+"&radius=1&key=AIzaSyDPpvkWBucabfDcJj3A-tlc-fX0wpEpqWg");
		        HttpURLConnection conn;
		            conn = (HttpURLConnection) url.openConnection();
		            conn.setRequestMethod("GET");
		        StringBuilder sb;
		        String line;
		            sb = new StringBuilder();
		            BufferedReader br;
		            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		            while (!((line = br.readLine()) == null)) {
		                sb.append(line);
		            }
		            System.out.println(sb.toString());
		            line = sb.toString();
		            JSONObject obj = (new JSONObject(line));
		            String val = obj.getString("results");
		            JSONArray arr1 = new JSONArray(val);
		            obj = (JSONObject) arr1.get(0);
		            
		             place = obj.getString("place_id");
		             obj = obj.getJSONObject("geometry").getJSONObject("location");
		             lat = obj.getDouble("lat");
		             log = obj.getDouble("lng");
		             System.out.println(lat+" "+log);
		            System.out.println("Place id "+place);
		            
		        br.close();
		    } catch (IOException e2) {
		            
		        e2.printStackTrace();
		    }
			String url1 = "file:///C:/Users/Monika/Desktop/578/hw/project/locationHTML.html?";
			String params = "lat="+lat+"?log="+log+"?place="+place;
			Desktop.getDesktop().browse(new URI(createHtmlLauncher(url1 + params)));

			
		}
		else
		{
			System.out.println("Publisher did not produce data needed for me");
		}
		}
	}

	private static String createHtmlLauncher(String targetUrl) throws Exception {
		String launcherFile = System.getProperty("java.io.tmpdir") + "local_launcher.html";
		File launcherTempFile = new File(launcherFile);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(launcherTempFile, "UTF-8");
		} catch (Exception e) {
			throw new Exception(
					"Error opening file for writing: " + launcherTempFile.getAbsolutePath() + " : " + e.getMessage());
		}
		writer.println("<meta http-equiv=\"refresh\" content=\"0; url=" + targetUrl + "\" />");
		writer.close();

		return "file:///" + launcherFile.replace("\\", "/");
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
}
