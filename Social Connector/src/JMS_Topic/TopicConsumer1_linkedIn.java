package cs578_topic;

import javax.jms.*;
import javax.naming.*;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;
import facebook4j.FacebookException;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

import java.util.Properties;

public class TopicConsumer1_linkedIn implements MessageListener {
// first consumer of linkedIn to accept from topic
	public static void main(String args[]) throws JMSException, NamingException, JSONException {
		System.out.println("Entering example topic consumer");
		// context is initialized 
		Context context = TopicConsumer1_linkedIn.getInitialContext();
		TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
		// consumer lookup for the topic
		Topic topic = (Topic) context.lookup("topic/zaneacademy_jms_tutorial_01");
		// connection is established
		TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
		// session is created
		TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		// subscriber is created 
		TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
		//connection started
		topicConnection.start();
		// System.out.println("Exiting example topic consumer");

		while (true) {
			// receives the msg from the topic
			TextMessage msg = (TextMessage) topicSubscriber.receive();
			// connection is made with DataBase to check if the consumser has permission for the topic
			JDBCMySQLDemo jdbc = new JDBCMySQLDemo();
			
			if (jdbc.checkAccess("LinkedIn")) {
				// if there is permission for the topic, then data is fetched from topic and same is posted on linkedIn
				JSONObject json_data = new JSONObject(msg.getText());
				JSONArray arr = json_data.getJSONArray("entry");
				JSONObject first = (JSONObject) arr.get(0);
				String field = (String) (((JSONObject) first.getJSONArray("changes").get(0)).get("field"));
				if (field.equals("location")) {
					String page_id = (String) ((JSONObject) ((JSONObject) first.getJSONArray("changes").get(0))
							.get("value")).get("page");

					System.out.println("id" + page_id);

					String accessToken = "EAAY88F8MVc8BANR93A6NFneJQ8Fnb286m4NT7hhdRX3hEFRe9z1Aq83Wo9MSsnIBiRjvkcbgpEqrHUP0iO0krZAIijDwG5GhVZAFQIK1wYwsxtgooGdJErrQxnnmdZBL2gNvuhp1KuXDx6dCE6Kb3ZBxeKzUIiAZD";
					FacebookClient fbclient = new DefaultFacebookClient(accessToken);
					Page me = (Page) fbclient.fetchObject(page_id, Page.class);

					String url = "https://graph.facebook.com/" + page_id + "/picture?access_token=" + accessToken;

					System.out.println(url);

					Linkedin ln = new Linkedin();
					ln.post(me.getName(), url); // post to linkedIn
				} else {
					System.out.println("Producer did not publish data required for me");
				}
			} else {
				System.out.println("NO permission to access topic");
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
