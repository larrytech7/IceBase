package com.iceteck.icebase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import android.util.Base64;

/**
 * @author Larry Akah N
 * Provides a operations to do sync operations in the background and report status
 */
public class DB_SYNC implements Executor, Runnable{
	
	public String sync_server = "https://icebase.com/server/app";/* issue: Needs to be configurable by developer */
	public String authentication_name = "default"; //a user name used to authenticate with remote service
	public String authentication_pass = "default"; // the password used for authentication
	public File dbFile; //json document containing data;
	
	/**
	 * Initialize basic requirements for a sync
	 * @param server Server to sync the data with
	 * @param username User name for the authentication
	 * @param password Password for the given name
	 * @param data The data file containing data to sync
	 */
	public DB_SYNC(String server, String username, String password, File data){
		this.sync_server = server;
		this.authentication_name = username;
		this.authentication_pass = password;
		this.dbFile = data;
	}

	public DB_SYNC(){
		
	}
	
	public void setAuthentication(String user, String pass){
		this.authentication_name = user;
		this.authentication_pass = pass;
	}
	
	public void setServer(String server){
		this.sync_server = server;
	}
	
	public void setDbData(File dbfile){
		this.dbFile = dbfile;
	}

	@Override
	public void run() {
			
			URL serverurl;
			try {
				String creds = this.authentication_name+":"+this.authentication_pass;
				creds = Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
				serverurl = new URL(this.sync_server);
				HttpURLConnection connection = (HttpURLConnection) serverurl.openConnection();
				connection.setRequestProperty("Authorization", "Basic "+creds);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type","application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.connect();
				FileReader freader = new FileReader(this.dbFile);
				//FileInputStream finput = new FileInputStream(this.dbFile);
				DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
				char[] buffer = new char[255];
				while(freader.read(buffer)>0){
					dos.writeBytes(buffer.toString());
				}
				freader.close();
				dos.flush();
				dos.close();
				//use reader to get json status response. response is json by default except otherwise specified by 'Application' header
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String temp = "";
				while((temp = br.readLine()) != null){
					sb.append(temp);
				}
				br.close();
				//at this stage, a handler object could be used to publish stuff to the user
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	/**
	 * Launches the thread that does the sync with the online server
	 * @author Larry akah
	 */
	@Override
	public void execute(Runnable command) {
		// TODO Auto-generated method stub
		run();
	}

}
