package com.android.apptime;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Login extends Activity  {
	public String IVLE_Token;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.login);  
    	
    	String currentToken = loginIVLE();
    }
    
    public String loginIVLE(){
        WebView wv = (WebView) findViewById(R.id.WebViewLogin);                  
        wv.getSettings().setJavaScriptEnabled(true);
        
        /* Register a new JavaScript interface called HTMLOUT */
        wv.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        wv.setWebViewClient(new WebViewClient() 
        {               
        	@Override               
        	public void onPageFinished(WebView view, String url) 
        	{     
        		// when login is complete, the url will be login_result.ashx?r=0
        		if (url.indexOf("/api/login/login_result.ashx") > 0)
        		{
        			// When login is successful, there will be a &r=0 in the url. It also means the return data is the token itself.
        			if (url.indexOf("&r=0") > 0)
        			{
        				view.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('body')[0].innerHTML);");        				
        			}
        		}
        	}
        });
    
        wv.loadUrl(getString(R.string.login_uri)+"?"+getString(R.string.login_apikeyfield)+"="+getString(R.string.apikey));
        
        return IVLE_Token;
    }
    
    public String GetIVLEToken(){
    	return IVLE_Token;
    }
    
    final Context myApp = this;

    /* An instance of this class will be registered as a JavaScript interface */
    class MyJavaScriptInterface
    {
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {
        	IVLE_Token = html;
        	
            // process the html as needed by the app
        	
        	new AlertDialog.Builder(myApp)  
    	    .setTitle("Token Value")  
    	    .setMessage(IVLE_Token)  
    	    .setPositiveButton(android.R.string.ok, null)  
    	    .setCancelable(false)
    	    .create().show();  
    	    
        	ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
        	listParams.add(new BasicNameValuePair(getString(R.string.apikeyfield),"AMie2EgILFdcDJP06BAOZ"));
        	listParams.add(new BasicNameValuePair(getString(R.string.authfield),IVLE_Token));
        	listParams.add(new BasicNameValuePair(getString(R.string.startdatefield),"01/06/2011"));
        	listParams.add(new BasicNameValuePair(getString(R.string.enddatefield),"10/08/2011"));
        	
        	String result = DataHandler.sendRequestUsingGet(getString(R.string.my_organizer_uri), listParams);
        	
        	//print out the token or save for next logon or to navigate to next API call.
        	Log.i("Token", result);
        }
    }
}