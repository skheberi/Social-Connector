package cs578_topic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Scanner;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;

//import Page;
import facebook4j.FacebookException;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

import javax.jms.JMSException;
import javax.jms.*;
import javax.naming.*;

public class TopicProducer {
	static String prev;

	public static void main(String[] args) throws JMSException, NamingException, IOException, JSONException {
		// TODO Auto-generated method stub
		System.out.println("Entering Topic Producer");
		// initialize the context 
		Context context = TopicProducer.getInitialContext();
		// set initial connectionFactory for Topic
		TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
		//initialise lookup topic name
		Topic topic = (Topic) context.lookup("topic/zaneacademy_jms_tutorial_01");
		// create topic connection
		TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
		// create topic session connections
		TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		// start the connection
		topicConnection.start();
		// initialize variable prev for comparision of current time.
		prev = "Fri, 28 Apr 2017 20:25:31 GMT";
		// while loops runs for ever until the program is stopped
		while (true)
			//
			try{
				// delay of thread by 5secs
			Thread.sleep(1000);
			// connecting with URL connection
		URLConnection connection;

		// opening connection with webserver where the file is hosted
		connection = new URL("https://ericoid-ways.000webhostapp.com/my_data.txt").openConnection();
		// comparision of the last modified time of the hosted file
		String lastModified = connection.getHeaderField("Last-Modified");
		if (!lastModified.equals(prev)) {
			// if lastmodified is different from previous run then fetch the file from the server
			URL url = new URL("https://ericoid-ways.000webhostapp.com/my_data.txt");
			Scanner scanner = new Scanner(url.openStream());

			String text = scanner.useDelimiter("\\A").next();
			System.out.println(text);

			// publish the data in the server to topic so that consumers can consume them
			TopicPublisher topicProducer = topicSession.createPublisher(topic);
			topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage msg = topicSession.createTextMessage();

			msg.setText(text);
			// publish the msg
			topicProducer.publish(msg);

			System.out.println("Exiting Topic Producer");
			prev = lastModified;
		
		} else// if file in server not modified do nothing
			System.out.println("Else part");
			}
		catch(Exception e)
		{System.out.println("Execption caught : "+e.toString());}
	}
	
	// initializing the context of the topic
	public static Context getInitialContext() throws JMSException, NamingException {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", "localhost:1099");
		Context context = new InitialContext(props);
		return context;

	}

}
