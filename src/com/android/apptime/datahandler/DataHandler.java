package com.android.apptime.datahandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class DataHandler {
	private Context context;
	private static DefaultHttpClient theHttpClient = null;
	private static final int NOT_CONNECTED = -1;
	private static final int CONNECTED = 0;
	private static final int DEFAULT_BUFFER_LENGTH=2048;
	public static final String TAG = "datahandler";
	
	public DataHandler(Context context) throws Exception {
		this.context = context;
	}
	
	public static DefaultHttpClient getHttpClient(){
		if (theHttpClient==null) theHttpClient = new DefaultHttpClient();
		return theHttpClient;
	}
	
	public HttpEntity sendRequestUsingGet(String uri){
		return null;
	}
	
	public static String sendRequestUsingGet(String uri, ArrayList<NameValuePair> params){
		HttpGet httpGet = null;
		DefaultHttpClient httpClient = getHttpClient();
		HttpResponse response;
		String result="";
		StringBuilder sb = new StringBuilder(uri);
		Log.d(TAG, "post to:"+uri);
		try {
			sb.append("?");
			for (int i = 0; i<params.size(); i++){
				if (i>0) sb.append("&");
				sb.append(params.get(i).getName()).append("=").append(params.get(i).getValue());
			}
			String requestUri = sb.toString();
			httpGet = new HttpGet(requestUri); 
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			List<Cookie> cookies = httpClient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                Log.d(TAG, "no cookie??");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    Log.d(TAG, "- " + cookies.get(i).toString());
                }
            }

			InputStream stream = entity.getContent();
			int b;
			sb = new StringBuilder();
			while ((b = stream.read()) != -1) {
				sb.append((char) b);
			}
			result = sb.toString();
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Protocol Exception:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "IOException Exception:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static String sendRequestUsingPost(String uri, ArrayList<NameValuePair> params){
		HttpPost httpPost = new HttpPost(uri);
		DefaultHttpClient httpClient = getHttpClient();
		HttpResponse response;
		String result="";
		Log.d(TAG, "post to:"+uri);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params)); 
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			List<Cookie> cookies = httpClient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                Log.d(TAG, "no cookie??");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    Log.d(TAG, "- " + cookies.get(i).toString());
                }
            }

			InputStream stream = entity.getContent();
			int b;
			StringBuilder sb = new StringBuilder();
			while ((b = stream.read()) != -1) {
				sb.append((char) b);
			}
			result = sb.toString();
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Protocol Exception:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "IOException Exception:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getDataFromURLToString(String uri) throws Exception {
		try{
			Log.d(TAG, "get data from "+uri);
			DefaultHttpClient httpClient = getHttpClient();
			HttpGet request = new HttpGet(uri);
			HttpResponse result = httpClient.execute(request);
			List<Cookie> cookies = httpClient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                Log.d(TAG, "no cookie??");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    Log.d(TAG, "- " + cookies.get(i).toString());
                }
            }
			HttpEntity entity = result.getEntity();
			InputStream instream = entity.getContent();
			BufferedInputStream bif = new BufferedInputStream(instream);
	        byte[] tmp = new byte[DEFAULT_BUFFER_LENGTH];
	        StringBuilder sb = new StringBuilder();
	        int size;
	        do{
	        	size = instream.read(tmp);
	        	if (size==-1) break;
	        	String str = new String(tmp, 0, size);	        	
	        	sb.append(str);
	        } while (true);
	        
	        instream.close();
	        bif.close();
	        Log.d(TAG, "result: |"+sb.toString());
	        return sb.toString();
		} catch (Exception e)
		{
			throw(e);
		}
	}
	
	public static void getDataFromURLToFile(String fromUri, String toFile) throws Exception{
		try{
			Log.d(TAG, "get data from "+fromUri);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(fromUri);
			HttpResponse result = httpClient.execute(request);
			HttpEntity entity = result.getEntity();
			InputStream instream = entity.getContent();
			BufferedInputStream bif = new BufferedInputStream(instream);
			OutputStream outstream = new FileOutputStream(new File(toFile));
			BufferedOutputStream bof = new BufferedOutputStream(outstream);
	        byte[] tmp = new byte[DEFAULT_BUFFER_LENGTH];
	        StringBuilder sb = new StringBuilder();
	        int size = 0;
	        do {
	        	size = bif.read(tmp);
	        	if (size==-1) break;
	        	Log.d(TAG, "writing...");
	        	bof.write(tmp, 0, size);
	        } while (true);
	        Log.d(TAG, "writing finished");
	        bof.close();
	        outstream.close();	        
	        Log.d(TAG, "writing finished");
	        instream.close();
	        bif.close();	        
	        Log.d(TAG, "writing finished");
	        
		} catch (Exception e)
		{
			throw(e);
		}
	}
	
	public byte[] getDataFromFileToByteArray(String fileName) throws Exception{
		try{
			Log.d(TAG, "get data from file " + fileName);
			InputStream instream = context.openFileInput(fileName);
			BufferedInputStream bif = new BufferedInputStream(instream);
			byte[] tmp = new byte[instream.available()];
	        bif.read(tmp);
	        instream.close();
	        bif.close();
	        return tmp;
		} catch (Exception e)
		{
			throw(e);
		}
	}
	public int checkConnectionAndRoaming(){		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.d(TAG, "connectivity manager");
		NetworkInfo ni = cm.getActiveNetworkInfo();
		Log.d(TAG, "networkinfo");

		if(ni == null || ni.isConnected() == false){
			return NOT_CONNECTED;
		}
//		if(ni.isRoaming()){
//			return CONNECTED_AND_ROAMING;
//		}
		return CONNECTED;
	}
	}
