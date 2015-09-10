package com.iceteck.icebase;

import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class contains all utility functions used by our database for basic functionality not directly related to the core
 * functionality but that aids a core functionality to operate efficiently.
 * This class is designed to be a singleton class
 * @author Larry Akah
 *
 */
public class DB_Utility {

	/**
	 * Returns a new JSONArray of new key values to be stored omitting the identified entry to be removed
	 * @param keys
	 * @param removeid
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray removeKey(JSONArray keys, String removeid) throws JSONException{
		ArrayList<String> mkeys = new ArrayList<String>();
		if(keys!=null){
		for(int i=0; i<keys.length(); i++){
			mkeys.add(keys.getString(i));
			}
		mkeys.remove(removeid);
		JSONArray nkeyArray = new JSONArray();
		for(String newkey: mkeys){
			nkeyArray.put(newkey);
		}
		return nkeyArray; //returns the new keys to save under the db;
		
		}
		else{
			return keys; //returns a null value indicating nothing to remove due to no keys available
		}	
	}
	
	/**
	 * Generates a random hexadecimal string to be used as keys(ids) for identifying each entry of an entity item. Proven to be collision resistant enough across API KEYS
	 * @author Larry Akah
	 */
	public static String generateRandomId(){
		return UUID.randomUUID().toString();
	}
	
}
