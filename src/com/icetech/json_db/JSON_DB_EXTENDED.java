package com.icetech.json_db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * @author Larry Akah
 * @version 1.0.0
 * This class is developed as a means to simplify the code base of the initial json_db implementation.
 * It aims at using fully the java JSON API available to implement the CRUD functionality of the json_db library.
 * This class is much more scalable and light-weight than the first implementation
 */
public class JSON_DB_EXTENDED {
	
	private String ENTITY_KEY;
	private Context context;
	private SharedPreferences msharedpreference;
	private ArrayList<String> ITEM_IDS;
	private StringBuilder jsonStringBuilder;
	private final String LIBTAG = getClass().getName();
	private JSONObject DBJSON;
	private JSONObject dataobj; //hold all entries for a given ENTITY_KEY
	
	
	/**
	 * Constructor initializes a basic data store environment
	 * @param ctx a reference to the application's context or sand box
	 * @param entityKey The main data store key for each entity space/schema
	 */
	public JSON_DB_EXTENDED(Context ctx, String entityKey){
		context = ctx;
		ENTITY_KEY = entityKey;
		DBJSON = new JSONObject();
		ITEM_IDS = new ArrayList<String>();
		dataobj = DBJSON;
	}
	
	/**
	 * Inserts an entity set('row') into the data store
	 * @param items values for each 'column' in the entity
	 * @return the same instance for chaining multiple calls to this method.
	 * @throws JSONException
	 */
	public JSON_DB_EXTENDED put(ArrayList<Object> items) throws JSONException{

		String item_id = this.generateRandomId();
		ITEM_IDS.add(item_id);
		JSONArray jarray = new JSONArray();
		for(Object item: items){
			jarray.put(item);
		}
		dataobj.put(item_id, jarray);
		Log.d(LIBTAG, ""+dataobj.toString(2));
		return this;
	}
	
	/**
	 * Returns all entity entries from the data store. Each item of an entity is accompanied with the key or unique id of the items.
	 * @author Larry Akah
	 * @throws JSONException for errors during construction of a JSON data string.
	 * @throws NullPointerException for any null accessed variable
	 */
	public List<ArrayList<Object>> get() throws JSONException , NullPointerException{
				msharedpreference = PreferenceManager.getDefaultSharedPreferences(context);
				//read key and get existing data
				List<ArrayList<Object>> results = new ArrayList<ArrayList<Object>>();
				JSONObject mainJson = new JSONObject(msharedpreference.getString(ENTITY_KEY, ENTITY_KEY+":{}"));
				
				Log.i(ENTITY_KEY, mainJson.toString(2));
				
				JSONArray keysJson = mainJson.getJSONArray("ids"); //retrieve a json array of ids for every entity entry
				Log.i(ENTITY_KEY, keysJson.toString(2));
				
				if(null != keysJson)
					for(int i=0; i<keysJson.length(); i++){
						//for each key, get the associated data
						try {
							JSONArray resultArray = mainJson.getJSONArray(keysJson.getString(i));
							ArrayList<Object> mlist = new ArrayList<Object>();
							if(null != resultArray)
								for(int j=0; j<resultArray.length(); j++){
									mlist.add(resultArray.getString(j));
									
								}
							mlist.add(keysJson.getString(i));
							results.add(mlist);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;
						}
					}
				return results;
	}
	
	/**
	 * updates a single item entry of an entity
	 * @param itemId provides the id of the item to be updated
	 * @param newItem the new set of data to be used to update the entry 
	 * @return boolean indicating whether the item was successfully updated or not. True means item was updated, false means otherwise.
	 */
	public boolean update(String itemId, ArrayList<Object> newItem){
		boolean operationSucceeded = false;
		
		JSONArray jarray = new JSONArray();
		for(Object item: newItem){
			jarray.put(item);
		}
		try {
			dataobj.put(itemId, jarray);
			Log.d(LIBTAG, ""+dataobj.toString(2));
			operationSucceeded = true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			operationSucceeded = false;
		}finally{
			return operationSucceeded;
		}
	}
	
	/**
	 * Updates all the items of a given entity
	 * @return the number of items successfully updated.
	 */
	public int updateAll(List<ArrayList<String>> items){
		return 0;
	}
	
	/**
	 * Gets an entry of an entity from the data store
	 * @author Larry Akah
	 * @param id The id of the item('row') in question to return
	 * @throws JSONException for errors during construction of a JSON data string.
	 * @throws NullPointerException for any null accessed variable
	 */
	public ArrayList<Object> get(String id) throws JSONException , NullPointerException{
				msharedpreference = PreferenceManager.getDefaultSharedPreferences(context);
				//read key and get existing data
				ArrayList<Object> results = new ArrayList<Object>();
				JSONObject mainJson = new JSONObject(msharedpreference.getString(ENTITY_KEY, ENTITY_KEY+":{}"));
				
				Log.i(ENTITY_KEY, mainJson.toString(2));
				
				JSONArray keysJson = mainJson.getJSONArray("ids"); //retrieve a json array of ids for every entity entry
				Log.i(ENTITY_KEY, keysJson.toString(2));
				
				if(null != keysJson)
					for(int i=0; i<keysJson.length(); i++){
						//for each key, get the associated data
						try {
							JSONArray resultArray = mainJson.getJSONArray(keysJson.getString(i));
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
	 * Persists all data by making the data permanent in the preference file on the fileSystem
	 * Save all id's in a different preference
	 * @author Larry Akah
	 * @return true or false indicating whether the save was successful or not
	 */
	public boolean save(){		
		msharedpreference = PreferenceManager.getDefaultSharedPreferences(context);
		boolean saved = false;
		if(dataobj != null)
			try {
				saved = saveid(ITEM_IDS);
				if(saved)
					msharedpreference.edit().putString(ENTITY_KEY, dataobj.toString()).apply();
					return saved;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		else
			return false;
	}
	
	/**
	 * save the ids of all entity entries
	 * @author Larry Akah
	 * @param ids A list of auto-generated ids that point to each set of entity data in the data store
	 * @throws JSONException
	 */
	private boolean saveid(ArrayList<String> ids) throws JSONException{
		JSONArray jarray = new JSONArray();
		for(Object item: ids){
			jarray.put(item);
		}
		dataobj.put("ids", jarray);
		
		return jarray.length() == ids.size()? true:false;
	}
	
	/**
	 * Adds an item into an entity entry
	 * @author Larry Akah
	 */
	public JSON_DB_EXTENDED putItem(String item){
		
		String key = this.generateRandomId();
		jsonStringBuilder.append("\""+key+"\":[");
		jsonStringBuilder.append("\""+item+"\",");
		
		return this;
	}
	
	/**
	 * Generates a random string to be used as ids for identifying each entry of an entity. Proven to be collision resistant enough
	 * @author Larry Akah
	 */
	private String generateRandomId(){
		return UUID.randomUUID().toString();
	}
}
