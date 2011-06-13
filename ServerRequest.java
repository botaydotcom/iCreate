package com.project.iCreate;

import sg.edu.nus.AndroidLogin.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;

public class ServerRequest extends Activity  {
	public String IVLE_Token;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);  
    	
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
    
        //wv.loadUrl(getString(R.string.ivle_login));
        wv.loadUrl("https://ivle.nus.edu.sg/api/login/?apikey=AMie2EgILFdcDJP06BAOZ");
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
    	    
			//print out the token or save for next logon or to navigate to next API call.
        	Log.i("Token", IVLE_Token);
        }
    }
}