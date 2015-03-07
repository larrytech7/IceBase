package com.icetech.json_db;

import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * NoSQL implementation of a local database in Android
 * @author Larry Akah
 * @version 1.0.0
 */
public class JSON_DB {
	
	private String ENTITY_KEY;
	private Context context;
	private SharedPreferences msharedpreference;
	private ArrayList<String> ITEM_IDS;
	private StringBuilder jsonStringBuilder;
	private final String LIBTAG = getClass().getName();
	
	/**
	 * Initialize our NoSQL structure to start handling the CRUD operations 
	 * @author Larry Akah
	 * @param ctx Current application context which manipulates the model
	 * @param entittykey A string representing a Single entity like a Relational database Table 
	 */
	public JSON_DB(Context ctx, String entittyKey){
		context = ctx;
		ENTITY_KEY = entittyKey;
		jsonStringBuilder = new StringBuilder("{\""+ENTITY_KEY+"\":{");
		ITEM_IDS = new ArrayList<String>();
	}
	/**
	 * @author Larry Akah
	 */
	JSON_DB(){
		
	}
	
	/**
	 * Persists all data by making the data permanent in the preference file on the fileSystem
	 * Save all id's in a different preference
	 * @author Larry Akah
	 * @return true or false indicating whether the save was successful or not
	 */
	public boolean save(){
		jsonStringBuilder = jsonFilter(jsonStringBuilder);
		jsonStringBuilder.append("}}");
		msharedpreference = PreferenceManager.getDefaultSharedPreferences(context);
		try {
			saveid(msharedpreference, ITEM_IDS, ENTITY_KEY);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(LIBTAG, "Data: "+jsonStringBuilder.toString());
		
		return msharedpreference
				.edit()
				.putString(ENTITY_KEY, jsonStringBuilder.toString())
				.commit()
				?true:false;
	}
	
	/**
	 * save all keymap entries into a preference for reference when getting data
	 * @author Larry Akah
	 * @param sp SharedPreference object used to reference the application's. 
	 * @param iTEM_IDS2 the value to save as one of 
	 * @param key part of the global key used to reference all the id's in the JSON array
	 * @throws JSONException
	 */
	private void saveid(SharedPreferences sp,ArrayList<String> iTEM_IDS2, String key) throws JSONException{
		String sp_key = "entity_id"+key; //key used to save all id's for each entity value in the database
		if(null != sp){
		JSONObject json = new JSONObject(sp.getString(sp_key, "{\"id\":[]}"));
		JSONArray jArray = json.getJSONArray("id");
		//create a new builder to reconstruct the json string before restoring it
		StringBuilder builder = new StringBuilder("{\"id\":[");
		for(String it : iTEM_IDS2){
					  builder.append("\"").append(it).append("\"");
					  builder.append(",");
		}
		for(int i=0; i< jArray.length() ; i++)
		{
			builder.append("\"")
				.append(jArray.get(i))
				.append("\",");
		}
			builder.replace(builder.length() -1, builder.length(), " "); //removes the trailing comma when finished adding values to builder
		    builder.append("]}");
		    sp.edit().putString(sp_key, builder.toString()).commit();
		    
		    for(String it : iTEM_IDS2){
		    	Log.i(ENTITY_KEY, it);
		    }
		}
	}
	/**
	 * Adds an item into an entity entry
	 * @author Larry Akah
	 */
	public JSON_DB put(String item){
		
		String key = this.generateRandomId();
		jsonStringBuilder.append("\""+key+"\":[");
		jsonStringBuilder.append("\""+item+"\",");
		
		return this;
	}
	
	/**
	 * Adds an item into an entity entry
	 * @author Larry Akah
	 * @param items all items to be inserted as an entity value
	 * @throws JSONException occurs for errors during construction of the JSON data string.
	 */
	public JSON_DB put(ArrayList<String> items) throws JSONException{

		String item_id = this.generateRandomId();
		ITEM_IDS.add(item_id);
		
		jsonStringBuilder.append("\""+item_id+"\":[");
		for(String item: items){
			jsonStringBuilder.append("\""+item+"\",");
		}
		jsonStringBuilder = jsonFilter(jsonStringBuilder);
		jsonStringBuilder.append("],");

		return this;
	}
	//alternatives to set data as JSON string exists when using the JSON classes appropriately. 
	/**
	 * Demo
	 * 	JSONObject jobject = new JSONObject();
		jobject.put(ENTITY_KEY, key);
	 *  
	 *  JSONArray jarry = new JSONArray();
		jarry.put("item1");
		jarry.put("item2");
		jarry.put("item3"); .... etc
		jsonString = jarry.toString();
	 */
	
	/**
	 * Gets a list of entries of a given entity
	 * @author Larry Akah
	 * @return a json string representated as a json array. contains the values of a single entity entry
	 * @throws JSONException if a mapping fails to retrieve a result or value
	 * @throws NullPointerException when no data was been previously stored by this application or with the key provided as constructor
	 */
	public ArrayList<String> get() throws JSONException , NullPointerException{
		//read key and get existing data
		ArrayList<String> results = new ArrayList<String>();
		JSONObject mainJson = new JSONObject(msharedpreference.getString(ENTITY_KEY, "{}"));
		JSONObject subJson = mainJson.getJSONObject(ENTITY_KEY);
		
		Log.i(ENTITY_KEY, subJson.toString(2));
		
		JSONObject mJson = new JSONObject(msharedpreference.getString("entity_id"+ENTITY_KEY, "{}"));
		Log.i(ENTITY_KEY, mJson.toString(2));
		JSONArray keysJson = mJson.getJSONArray("id"); //retrieve a json array of ids for every entity entry
		Log.i(ENTITY_KEY, keysJson.toString(2));
		
		if(null != keysJson)
			for(int i=0; i<keysJson.length(); i++){
				//for each key, get the associated data
				try {
					JSONArray resultArray = subJson.getJSONArray(keysJson.getString(i));
					if(null != resultArray)
						for(int j=0; j<resultArray.length(); j++){
							results.add(resultArray.getString(j));
						}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
		return results;
	}
	/**
	 * Removes the trailing comma at the end of a complete entity entry;
	 * @author Larry Akah
	 */
 	private StringBuilder jsonFilter(StringBuilder toBuilder){
		return toBuilder.replace(toBuilder.length() - 1 , toBuilder.length(), "");
	}
	
	/**
	 * @author Larry Akah
	 */
	public String getEntityKey(){
		return this.ENTITY_KEY;
	}
	
	/**
	 * Generates a random string to be used as ids for identifying each entry of an entity. Proven to be collision resistant enough
	 * @author Larry Akah
	 */
	private String generateRandomId(){
		return UUID.randomUUID().toString();
	}
	
}
