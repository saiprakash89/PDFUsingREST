package pdfUsingRest;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author SaiPrakash
 *
 */
public class DocUpload {

	/**
	 * @param pdfName - Name of the file attachment - Required
	 * @param parentId - Salesforce Id of the SObject - Required
	 * @param description - Text description, if any - Optional
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws JSONException
	 * @throws DocumentException
	 */
	public static void uploadPDF(String pdfName, String parentId, String description) throws IOException, KeyManagementException, NoSuchAlgorithmException, JSONException, DocumentException {

        // Create a MultipartEntityBuilder
    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        
    	// Set the Boundary String
    	String boundary = "boundary" + System.currentTimeMillis();
    	builder.setBoundary(boundary);
    	
    	// Set builder's Mode to Strict
    	builder.setMode(HttpMultipartMode.STRICT);

	    // Add Non-Binary grains of the request
    	NonBinaryData nbData = new NonBinaryData(pdfName, parentId, description);
	    builder.addTextBody("SomeTestName", nbData.getNonBinaryJSON(), ContentType.APPLICATION_JSON);

	    // Add binary body information
	    builder.addBinaryBody("Body", getNewDocStream(), ContentType.APPLICATION_OCTET_STREAM, "AnyTestName");
		
		// Pass the auth information and the data to do a POST using REST API
		attachDataToSObj(boundary, builder.build());
    }
	
	/**
	 * @param boundary - Boundary to be set
	 * @param build - HttpEntity created to set as entity of the request
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException 
	 */
	public static void attachDataToSObj(String boundary, HttpEntity build) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, JSONException {
		
		// Throws IOException, KeyManagementException, NoSuchAlgorithmException, JSONException
	    // Get an Auth Token and URI from Salesforce using OAuth 2.0
		OAuthData authData = OAuthHelper.getAuthData();
		
		// Set request endpoint
		HttpPost postRequest = PostRequestHelper.buildPostRequest(authData.getBaseUri() + "/sobjects/Attachment/", "multipart/form-data;boundary=" + boundary, "OAuth " + authData.getAuthToken());

		// Set Request Body
		postRequest.setEntity(build);
    	
    	// Throws KeyManagementException, NoSuchAlgorithmException
		HttpClient httpClient = HttpClientBuilder.create().build();  // Java 8 HttpClient
		
		// Use the following if, for some reason, you are using Java 7
//    	HttpClient httpClient = PostRequestHelper.getJava7HttpClient();
    	
    	/* Throws IOException, ClientProtocolException */
		// Execute Post Request
    	HttpResponse serverResponse = httpClient.execute(postRequest);
		
		// Convert Response to String 
		HttpEntity entity = serverResponse.getEntity();
		
		// Print response
		System.out.println("Response " + EntityUtils.toString(entity));

		// release connection once done
		postRequest.releaseConnection();
	}
	
	/**
	 * @return byte array
	 * @throws DocumentException
	 */
	public static byte[] getNewDocStream() throws DocumentException {
		
		// Create a new ByteArrayOutputStream to feed to the BinaryBody
		ByteArrayOutputStream newStream = new ByteArrayOutputStream();
		
		// Create a new PdfWrite to write to the OutputStream
		Document doc = new Document();
		PdfWriter.getInstance(doc, newStream);
		
		// Open the document, add some sample text to it, and close it
		doc.open();
		doc.add(new Paragraph("Some sample text"));
		doc.close();
		
		return newStream.toByteArray();
		
	}
}