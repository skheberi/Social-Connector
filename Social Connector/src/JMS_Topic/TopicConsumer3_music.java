package cs578_topic;

import javax.jms.*;
import javax.naming.*;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Location;
import com.restfb.types.Page;
import com.restfb.types.Place;

import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class TopicConsumer3_music implements MessageListener {

	public static void main(String args[])
			throws Exception {
		System.out.println("ENtering example topic consumer 3(Music spotify)");
		// initial context is initialized
		Context context = TopicConsumer3_music.getInitialContext();
		// connectionFactory is set
		TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
		// Look up for topic is initialized
		Topic topic = (Topic) context.lookup("topic/zaneacademy_jms_tutorial_01");
		// connection is created with the topic
		TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
		// session is created
		TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		// subscriber is created for consuming message
		TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
		// connection is started
		topicConnection.start();
		//System.out.println("Exiting example topic consumer 3");

		while (true) {
			// receives the msg from topic and adds the song to the playlist of spotify
			TextMessage msg = (TextMessage) topicSubscriber.receive();
			JSONObject json_data = new JSONObject(msg.getText());
			JSONArray arr = json_data.getJSONArray("entry");
			JSONObject first = (JSONObject) arr.get(0);
			String field = (String) (((JSONObject) first.getJSONArray("changes").get(0)).get("field"));
			System.out.println(field);
			if (field.equals("music")) {
				String page_id = (String) ((JSONObject) ((JSONObject) first.getJSONArray("changes").get(0))
						.get("value")).get("page");

				System.out.println("id" + page_id);

				String accessToken = "EAAY88F8MVc8BANR93A6NFneJQ8Fnb286m4NT7hhdRX3hEFRe9z1Aq83Wo9MSsnIBiRjvkcbgpEqrHUP0iO0krZAIijDwG5GhVZAFQIK1wYwsxtgooGdJErrQxnnmdZBL2gNvuhp1KuXDx6dCE6Kb3ZBxeKzUIiAZD";
				FacebookClient fbclient = new DefaultFacebookClient(accessToken);
				Page me = (Page) fbclient.fetchObject(page_id, Page.class);
				String song_name = me.getName();
				System.out.println(song_name);
				SpotifyMusic sm = new SpotifyMusic();
				sm.setSpotify(song_name);
				System.out.println("song "+song_name+" got successfully added to placelist");
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
}
