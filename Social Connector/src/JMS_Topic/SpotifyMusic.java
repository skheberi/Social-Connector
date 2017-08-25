package cs578_topic;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import facebook4j.internal.org.json.JSONObject;

public class SpotifyMusic
{
	static JSONObject jsonObject;
	public static void main(String args[])
	{
		System.out.println("entering main");
	}
	
  public void setSpotify(String name)
   {
        try
        { name = name.replace(" ", "%20");
      
        	String contentType="Content-Type: application/json";
        
        	String authVal="Authorization: Bearer BQAPJbJI7lDLhdj6dCjplCOYlLvnXCJaZGZLCKRtVkK7x8cAfIAWm9Zjs3CsCU3gXnCNXgUpi_ceLcjk0--TcLwpYytTA9p9m6zB92n5TxSC3NhTsqM4AmkfaZGn6nTN9VH3-Lxjg1s_2ByxseVoPX4wsA0fxoOEPPNpa1PmARW0fjEWR4lye6UCtAzyiokT9cEFISvk3THdz1uYORRDtDfz3mh8ReKzAuy59PVl7ah0TgV9WhMbomSlILxz9CvkpvmOk4o17kVW7zySO82CoSu9SaDZWHgpQN86aFGACY5Wn4eM04sDOqNEWZNUIv1-doIFXivnD8maG8JxlczdWg";
        	String url="https://api.spotify.com/v1/search?q="+name+"&type=track";
        	System.out.println(url);
        	String[] command = {"C:\\Users\\Monika\\Downloads\\curl-7.54.0-win64-mingw\\bin\\curl.exe", "-X" ,"GET",url,"-H","Accept:application/json","-H",authVal};
      
            
        	 ProcessBuilder process = new ProcessBuilder(command); 
            Process p = process.start();
             BufferedReader reader =  new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ( (line = reader.readLine()) != null) {
                	
                        builder.append(line);
                        builder.append(System.getProperty("line.separator"));
                }
                String result = builder.toString();
            //    System.out.print(result);
                jsonObject = new JSONObject(result);
             //  JSONObject artist = jsonObject.getJSONObject("JSON");
               JSONObject tracks = jsonObject.getJSONObject("tracks");
              JSONObject obj= (JSONObject) tracks.getJSONArray("items").get(0);
              String uri= obj.getString("uri");
           //   System.out.println(uri);
           //   URI uri = new URI("https://open.spotify.com/artist/"+id);
            //  Desktop.getDesktop().browse(uri); 
                
              //Adding track to Playlist
              
              url="https://api.spotify.com/v1/users/21gfw4lnocyluhbagojuzpfmq/playlists/47sIgRdO5VFacFRpAfTANs/tracks?position=0&uris="+uri;
              //	String url="https://api.spotify.com/v1/users/21gfw4lnocyluhbagojuzpfmq/playlists/47sIgRdO5VFacFRpAfTANs/tracks?position=0&uris=spotify:track:1qMe2jbUwU3oEfw9SfSrh3";
              String[] commandSend = {"C:\\Users\\Monika\\Downloads\\curl-7.54.0-win64-mingw\\bin\\curl.exe", "-X" ,"POST",url,"-H","Accept:application/json","-H",authVal};
              process = new ProcessBuilder(commandSend); 
               p = process.start();
             reader =  new BufferedReader(new InputStreamReader(p.getInputStream()));
                builder = new StringBuilder();
                   line = null;
                  while ( (line = reader.readLine()) != null) {
                  	//System.out.print("Hello");
                          builder.append(line);
                          builder.append(System.getProperty("line.separator"));
                  }
                  result = builder.toString();
                  System.out.println(result);
            
              
              
              
              

        }
        catch (Exception e)
        {   System.out.print("error");
            e.printStackTrace();
        }
   }
}


