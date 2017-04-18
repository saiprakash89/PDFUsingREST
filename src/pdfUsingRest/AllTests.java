package pdfUsingRest;


import org.junit.Test;

/**
 * @author SaiPrakash
 *
 */
public class AllTests {
	   
	   @Test
	   public void testQuoteRequest() {
		   try {
			   // Pass in a Name for the PDF file, Id of the SObject, and a description for the attachment
			   DocUpload.uploadPDF("TestUploadTest.pdf", "0014B00000Hgcyo", "Test Description");
		   } catch(Exception e) {
			   e.printStackTrace();
		   }
	   }
}