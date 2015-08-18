package com.eventivities.android.servicioweb;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public abstract class EventivitiesApi {

	protected final static String host = "http://www.eventivitiesadm.eshost.es";
	protected final static int readTimeout = 30000; //20 seconds.
	protected final static int connectTimeout = 15000; //15 seconds.
	
	protected String payload = null;

	protected String api_url;

	public EventivitiesApi(String apiCall) {
		api_url = host+apiCall;
	}

	protected InputStream doConnection() throws IOException{
		InputStream is = null;
		String parameters = null;

		URL url = new URL(api_url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(getReadTimeout());
		conn.setConnectTimeout(getConnectTimeout());
		conn.setUseCaches(false);
		if(payload != null) {
		  parameters = "DATA="+URLEncoder.encode(payload, "utf-8");
		  conn.setDoInput(true);
		  conn.setDoOutput(true);
		  conn.setRequestMethod("POST");
		  conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		  conn.setRequestProperty("charset", "utf-8");
		  conn.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
		  DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		  wr.write(parameters.getBytes());
		  wr.flush();
		  wr.close();
		} else {
		  conn.connect();
		}
		
		is = conn.getInputStream();
		
		return is;
	}

	public String call() throws IOException{

	  Log.d("EventivitiesApi", "Calling: "+api_url);
		InputStream is = doConnection();

		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		StringBuilder total = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			total.append(line);
		}
		
		//There's no need to do that. Or at least it doesn't break.
		//is.close();

		return total.toString();
	}

	private static int getReadTimeout() {
		return readTimeout;
	}

	private static int getConnectTimeout() {
		return connectTimeout;
	}

}
