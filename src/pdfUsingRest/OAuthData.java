package pdfUsingRest;


/**
 * @author SaiPrakash
 * @Description - This class is used to deserialize OAuth response
 */
public class OAuthData {
	private String authToken;
	private String baseUri;
	
	public OAuthData (String baseUri, String authToken) {
		super();
		this.authToken = authToken;
		this.baseUri = baseUri;
	}
	
	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * @return the baseUri
	 */
	public String getBaseUri() {
		return baseUri;
	}

}