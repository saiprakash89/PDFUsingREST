package pdfUsingRest;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.HttpClients;

/**
 * @author SaiPrakash
 *
 */
public class PostRequestHelper {
	
	/**
	 * @param postUrl -  Post Request endpoint 
	 * @param contentType - Content-Type header information
	 * @param authorization - Authorization token
	 * @return HttpPost - return the built request
	 */
	public static HttpPost buildPostRequest(String postUrl, String contentType, String authorization) {
		// Login requests must be POSTs
        HttpPost postRequest = new HttpPost(postUrl);
    	
        if(contentType.length() != 0) {
	        // Set Content Type
			postRequest.addHeader("Content-Type", contentType);
        }	
		
        if(authorization.length() != 0) {
        	// Set the Authorization header
        	postRequest.addHeader("Authorization", authorization);
        }
		
		return postRequest;

	}
    
    /**
     * @deprecated
     * @return HttpClient for Java version 7
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static HttpClient getJava7HttpClient() throws KeyManagementException, NoSuchAlgorithmException {
    	/* Construct the objects needed for the request with Java 7 */

    	// Throws KeyManagementException, NoSuchAlgorithmException
    	SSLContext sslContext = SSLContexts.custom().useTLS().build();

    	SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1", "TLSv1.1"}, null, null);

        return HttpClients.custom().setSSLSocketFactory(f).build();
    }
}