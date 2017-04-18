package pdfUsingRest;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * @author SaiPrakash
 *
 */
public class OAuthHelper {
    
    private static final String USERNAME     = "yourUserName@org.com";
    private static final String PASSWORD     = "yourPasswordWithSecurityTokenAddedAtTheEnd";
    private static final String LOGINURL     = "https://test.salesforce.com";
    private static final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
	
    /* Set up a canvas app in Salesforce that allows OAuth, and populate the following fields */
    private static final String CLIENTID     = "canvasApp'sClientId";
    private static final String CLIENTSECRET = "canvasApp'sClientSecret";
    
    private static String REST_ENDPOINT = "/services/data";
    private static String API_VERSION = "/v32.0" ;
    
    /**
     * @name - getAuthToken
     * @description - Get a secure connection established between this app and Salesforce - through OAuth 2.0
     * @return - OAuthData - Auth Token, and BaseUri if successful, else null
     */
    public static OAuthData getAuthData() throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, JSONException {
 
        // Assemble the login request URL
        String loginURL = LOGINURL +
                          GRANTSERVICE +
                          "&client_id=" + CLIENTID +
                          "&client_secret=" + CLIENTSECRET +
                          "&username=" + USERNAME +
                          "&password=" + PASSWORD;
        
        // Set request endpoint
        HttpPost postRequest = PostRequestHelper.buildPostRequest(loginURL, "", "");
      	
      	// Throws KeyManagementException, NoSuchAlgorithmException
        HttpClient httpClient =  HttpClientBuilder.create().build(); // Java 8 HttpClient
		
        // Use the following if, for some reason, you are using Java 7
//        HttpClient httpClient = PostRequestHelper.getJava7HttpClient();
      	
      	/* Throws IOException, ClientProtocolException */
		// Execute Post Request
    	HttpResponse serverResponse = httpClient.execute(postRequest);
    	
        // Verify response is HTTP OK
        final int statusCode = serverResponse.getStatusLine().getStatusCode();
        
        if (statusCode != HttpStatus.SC_OK) {
        	System.out.println("Auth Response From Salesforce: " + serverResponse);
            System.out.println("Error Authenticating: "+ statusCode);
            return null;
        }
        
        // Return parsed response
        return parseAuthResponse(serverResponse);
    }
    
    /**
     * @param -serverResponse- Response from the OAuth Post callout
     * @return Parsed response of type OAuthData.class
     * @throws IOException
     * @throws JSONException
     */
    private static OAuthData parseAuthResponse(HttpResponse serverResponse) throws IOException, JSONException {
        /* Parse the response to get access token */

        // Throws IOException
        String getResult = EntityUtils.toString(serverResponse.getEntity());
        
        // Throws JSONException
        JSONObject jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
        String loginAccessToken = jsonObject.getString("access_token");
        String loginInstanceUrl = jsonObject.getString("instance_url");        
        
        OAuthData authInstance = new OAuthData(loginInstanceUrl + REST_ENDPOINT + API_VERSION, loginAccessToken);
        
        return authInstance;
    }
}
