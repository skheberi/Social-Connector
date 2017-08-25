CONTENTS OF THIS FILE
---------------------
 * Introduction
 * Requirements
 * Installation
 * Steps to run the program 
 * Steps for setting up facebook

	 INTRODUCTION
	---------------
Our connector  is written in JAVA.
There are 2 producers and 4 consumsers, accessing 1 JMS topic. Producers publish message on JMS topic and same message will be consumed by interested consumers.

	 ADDITIONAL REQUIREMENTS
	--------------
	* MySQL
	* Java JMS
	* JBoss server
	* Eclipse
	
	 INSTALLATION
	--------------
* Install MySQL and create a database.
* Java JMS : Install Java JMS and configure JMS Topic for producer and consumer. 
* Configure the Destination-Services.xml file with topic name : softwareArchitecture.
* Install Jboss server in eclipse project.

	 STEPS TO RUN THE PROGRAM
	--------------------------
* Open eclipse and import the zip file "cs578_Social_Connector.zip".
* Make sure all the jars are included in the build path.
* Make sure JMS is configured with topic(softwareArchitecture).
* SETUP FACEBOOK
* Start the JBoss server.
* Run the all the consumer java files - "TopicConsumer1-linkedIn.java", "TopicConsumer2-maps.java", "TopicConsumer3-music.java",  "TopicConsumer4-calendar.java".
* Run the producer file - "TopicProducer.java".
* Change your current location in facebook - this will trigger an event and producer publishes the message on topic, which will be consumed by linkedIn and maps consumers.
* Like any music in your facebook application - this will trigger an event and same producer will publish the message on topic and music consumer will consume the event and liked music will be added to spotify playlist.
* Make any event as interested in your facebook - this will trigger an event and same producer will publish another message on topic and calendar consumer will add the date and time of event to calendar and event location will be consumed by maps to save the location in your google maps.	


	 STEPS FOR SETTING UP FACEBOOK
	-------------------------------
* Go to URL - https://developers.facebook.com/
* Create new web application.
* Add webhooks in products
* Setup subscription callback function with the url - "https://ericoid-ways.000webhostapp.com/"
* Subscribe to Location, music, events.