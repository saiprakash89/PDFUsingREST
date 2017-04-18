# PDFUsingREST
Create and upload a PDF file to Salesforce from Java 7 &amp; Java 8

This is an implementation of a Java solution to upload a pdf/binary to Salesforce using the standard REST endpoint.
I used a canvas app to establish an OAuth connection between Salesforce and Java app.

JARs Used
* httpmime-4.3.1
* gson-2.6.2
* httpcore-4.4.1
* commons-logging-1.1.2
* httpclient-4.3.6
* junit-4.12
* itextpdf-5.4.1
* json-200080701
* hamcrest-all-1.3

# Using Existing PDF/Binary File (Images etc)
If you want to just upload an existing PDF, rather than creating a new one, you can use the following code in place of line 59 in DocUpload.java
  
  // Convert the following
  builder.addBinaryBody("Body", getNewDocStream(), ContentType.APPLICATION_OCTET_STREAM, "AnyTestName");
  
  // To
  File existingFile = new File("localPathToExistingFile");
  builder.addPart("Body", new FileBody(existingFile));
  
  // Also, fix the imports to include
  import java.io.File;
  import org.apache.http.entity.mime.content.FileBody;
