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

public class TopicProducerFilter {
	static String prev;

	public static void main(String[] args) throws JMSException, NamingException, IOException, JSONException {
		// TODO Auto-generated method stub
		System.out.println("Entering Topic Producer");

		Context context = TopicProducer.getInitialContext();
		TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
		Topic topic = (Topic) context.lookup("topic/zaneacademy_jms_tutorial_01");
		TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
		TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		topicConnection.start();

		prev = "Fri, 28 Apr 2017 20:25:31 GMT";

		// new java.util.Timer().schedule(
		// new java.util.TimerTask()
		// {
		//
		// public void run() {

		while (true)
			//
			try{
			Thread.sleep(5000);
		URLConnection connection;

		connection = new URL("https://ericoid-ways.000webhostapp.com/my_data.txt").openConnection();
		String lastModified = connection.getHeaderField("Last-Modified");
		if (!lastModified.equals(prev)) {
			URL url = new URL("https://ericoid-ways.000webhostapp.com/my_data.txt");
			Scanner scanner = new Scanner(url.openStream());

			String text = scanner.useDelimiter("\\A").next();
			System.out.println(text);

			JDBCMySQLDemo jdbc = new JDBCMySQLDemo();
			jdbc.updateTable("LinkedIn",0);
			
			//
			TopicPublisher topicProducer = topicSession.createPublisher(topic);
			topicProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage msg = topicSession.createTextMessage();

			msg.setText(text);

			topicProducer.publish(msg);

			System.out.println("Exiting Topic Producer");
			prev = lastModified;
		
		} else
			System.out.println("Else part");
			}
		catch(Exception e)
		{}
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
