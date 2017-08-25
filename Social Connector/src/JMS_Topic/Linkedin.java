package cs578_topic;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
public class Linkedin {
	private static String API_KEY = "86w6580o9nm6kg";
    private static String API_SECRET = "7Ez7sjk2TxLwkY7b";
    
      
    public static void main(String[] args) {
    	
    	
    	 
    	
//    	try{
//            File file = new File("service.dat");
//
//            if(file.exists()){
//                //if the file exists we assume it has the AuthHandler in it - which in turn contains the Access Token
//                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
//                AuthHandler authHandler = (AuthHandler) inputStream.readObject();
//                accessToken = authHandler.getAccessToken();
//                //System.out.println(accessToken);
//            } else {
//                System.out.println("There is no stored Access token we need to make one");
//                //In the constructor the AuthHandler goes through the chain of calls to create an Access Token
//                AuthHandler authHandler = new AuthHandler(service);
//                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("service.dat"));
//                outputStream.writeObject( authHandler);
//                outputStream.close();
//                accessToken = authHandler.getAccessToken();
//            }
//
//        }catch (Exception e){
//            System.out.println("Threw an exception when serializing: " + e.getClass() + " :: " + e.getMessage());
//        }
//    	
    	System.out.println();
//        System.out.println("********A basic user profile call********");
//        //The ~ means yourself - so this should return the basic default information for your profile in XML format
//        //https://developer.linkedin.com/documents/profile-api
//        String url = "http://api.linkedin.com/v1/people/~";
//        OAuthRequest request = new OAuthRequest(Verb.GET, url);
//        service.signRequest(accessToken, request);
//        Response response = request.send();
//        System.out.println(response.getBody());
//        System.out.println();System.out.println();
        
//        System.out.println("********Get the profile in JSON********");
//        //This basic call profile in JSON format
//        //You can read more about JSON here http://json.org
//        String url = "http://api.linkedin.com/v1/people/~:(first-name,last-name,email-address,picture-url,public-profile-url,summary,industry,location)";
//        //String url = "https://api.linkedin.com/v1/people-search:(people:(id,first-name,last-name,headline,picture-url,industry,positions:(id,title,summary,start-date,end-date,is-current,company:(id,name,type,size,industry,ticker)),educations:(id,school-name,field-of-study,start-date,end-date,degree,activities,notes)),num-results)?first-name=Mitun&last-name=George";
//        OAuthRequest request = new OAuthRequest(Verb.GET, url);
//        request.addHeader("x-li-format", "json");
//        service.signRequest(accessToken, request);
//        Response response = request.send();
//        System.out.println(response.getBody());
//        System.out.println();System.out.println();
    }
    
    public void post(String title,String picture){
    	System.out.println(title);
    	 Token accessToken = null;
    	OAuthService service = new ServiceBuilder()
                .provider(LinkedInApi.class)
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .build();
    	try{
            File file = new File("service_new.dat");

            if(file.exists()){
                //if the file exists we assume it has the AuthHandler in it - which in turn contains the Access Token
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                AuthHandler authHandler = (AuthHandler) inputStream.readObject();
                accessToken = authHandler.getAccessToken();
                //System.out.println(accessToken);
            } else {
                System.out.println("There is no stored Access token we need to make one");
                //In the constructor the AuthHandler goes through the chain of calls to create an Access Token
                AuthHandler authHandler = new AuthHandler(service);
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("service_new.dat"));
                outputStream.writeObject( authHandler);
                outputStream.close();
                accessToken = authHandler.getAccessToken();
            }

        }catch (Exception e){
            System.out.println("Threw an exception when serializing: " + e.getClass() + " :: " + e.getMessage());
        }
    	
        System.out.println("********Write to the  share - using JSON ********");
        //This basic shares some basic information on the users activity stream
        //https://developer.linkedin.com/documents/share-api
        //NOTE - a good troubleshooting step is to validate your JSON on jsonlint.org
        String url = "http://api.linkedin.com/v1/people/~/shares";
       OAuthRequest request = new OAuthRequest(Verb.POST, url);
        //set the headers to the server knows what we are sending
        request.addHeader("Content-Type", "application/json");
        request.addHeader("x-li-format", "json");
        //make the json payload using json-simple
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("comment", "Posting from the API using JSON");
        JSONObject contentObject = new JSONObject();
        contentObject.put("title", "Moved to location "+title);
        contentObject.put("submitted-url","http://www.linkedin.com");
        contentObject.put("submitted-image-url", picture);
        jsonMap.put("content", contentObject);
        JSONObject visibilityObject = new JSONObject();
        visibilityObject.put("code", "anyone");
        jsonMap.put("visibility", visibilityObject);
        request.addPayload(JSONValue.toJSONString(jsonMap));
        service.signRequest(accessToken, request);
       Response response = request.send();
        //again no body - just headers
        System.out.println(response.getBody());
        System.out.println(response.getHeaders().toString());
        System.out.println();System.out.println();
	
    }
    
    
    

}
