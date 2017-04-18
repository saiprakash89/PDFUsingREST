package pdfUsingRest;


import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author SaiPrakash
 * @Description - This class is used to create Non-Binary part of the multipart request
 */
public class NonBinaryData {
	private String Name; //Required Field
	private String Description; //Required Field
	private String ParentId; //Optional field

	/**
	 * @param name - Document Name
	 * @param parentId - Id of the Account where the PDF needs to be attached to
	 * @param descrtipion - Text description, if any
	 * @throws IOException 
	 */
	public NonBinaryData(String name, String parentId, String descrtipion) throws IOException {
		super();
		if(name.length() == 0 || parentId.length() == 0) {
			throw new IOException("Name, and parentId are required fields.");
		}
		Name = name;
		ParentId = parentId;
		Description = descrtipion;
	}
	
	/**
	 * @return JSON string of this class type
	 */
	public String getNonBinaryJSON() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.toJson(this);
	}
}
