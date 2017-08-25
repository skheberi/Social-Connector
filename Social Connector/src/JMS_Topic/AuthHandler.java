package cs578_topic;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.io.Serializable;
import java.util.Scanner;

public class AuthHandler implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Token accessToken = null;

  public AuthHandler(OAuthService serviceProvider)
  {

    // this is our first time creating this object so we need to populate the
    // accessToken

    Scanner in = new Scanner(System.in);
    Token requestToken = serviceProvider.getRequestToken();
    System.out.println(serviceProvider.getAuthorizationUrl(requestToken));
    System.out.println("And paste the verifier here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());

    accessToken = serviceProvider.getAccessToken(requestToken, verifier);

  }

  /**
   * You only need to call this if you didn't reserialize an object
   * 
   * @return an Access token you can use to make API calls
   */
  public Token getAccessToken()
  {
    return this.accessToken;
  }

}
