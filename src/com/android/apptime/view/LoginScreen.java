//package com.android.apptime.view;
//
//import com.android.apptime.Main;
//import com.android.apptime.R;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnKeyListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//public class LoginScreen extends Activity {
//	private EditText mEtUsername = null;
//	private EditText mEtPassword = null;
//	private EditText mEtPageCode = null;
//	private Button mBtLogin = null;
//	private String mStrUserName = "";
//	private String mStrPassword = "";
//
//	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.loginscreen);
//		mEtUsername = (EditText) findViewById(R.id.etUsername);
//		mEtPassword = (EditText) findViewById(R.id.etPassword);
//		mBtLogin = (Button) findViewById (R.id.btLogin);
//		mEtUsername.setOnFocusChangeListener(new OnFocusChangeResetText());
//		mEtPassword.setOnFocusChangeListener(new OnFocusChangeResetText());
//		mEtUsername.setOnKeyListener(new OnKeyListener() {
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (event.getAction() == KeyEvent.ACTION_DOWN)
//				{
//					mStrUserName = mEtUsername.getText().toString();
//					return false;
//				}
//				return true;
//			}
//		});
//		mEtPassword.setOnKeyListener(new OnKeyListener() {
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (event.getAction() == KeyEvent.ACTION_DOWN)
//				{
//					mStrPassword = mEtPassword.getText().toString();
//					return false;
//				}
//				return false;
//			}
//		});
//		
//		
//		mBtLogin.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				handleLogInClicked();
//			}
//		});
//		
//	}
//
//	private void handleLogInClicked() {
//		mStrUserName = mEtUsername.getText().toString();
//		mStrPassword = mEtPassword.getText().toString();
//		if (mStrUserName.equals("") || mStrPassword.equals("")){
//			Toast.makeText(getApplicationContext(), "Please fill in both username and password!", Toast.LENGTH_SHORT).show();
//			if (mStrUserName.equals("")) mEtUsername.requestFocus();
//			else mEtPassword.requestFocus();
//		} else {
//			login(mStrUserName, mStrPassword);
//		}
//	}
//	
//	private void login(String mStrUserName, String mStrPassword) {
//		boolean result = true;//login(mStrUserName, mStrPassword);
//		if (result){
//			startActivity(new Intent(getApplicationContext(), Main.class));
//			this.finish();
//		} else {
//			Toast.makeText(getApplicationContext(), "Incorrect username or password! Please try again.", Toast.LENGTH_SHORT).show();
//			mEtUsername.requestFocus();
//		}
//	}
//
//	
//
//	private class OnFocusChangeResetText implements View.OnFocusChangeListener {
//		@Override
//		public void onFocusChange(View v, boolean hasFocus) {
//			EditText editText = (EditText) v;
//			if (hasFocus){
//				editText.setText("");
//			}
//			else {
//			}
//		}
//	};
//}

package com.android.apptime.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.apptime.Main;
import com.android.apptime.R;

public class LoginScreen extends Activity {
	protected static final String TAG = "login";
	private String IVLE_Token;

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// log in to the IVLE Network
		loginIVLE();
	}

	// Displays a new WebView to accept IVLE credentials and update the auth
	// Token
	public void loginIVLE() {
		final WebView wv = (WebView) findViewById(R.id.WebViewLogin);
		wv.getSettings().setJavaScriptEnabled(true);

		/* Register a new JavaScript interface called HTMLOUT */
		wv.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		wv.setWebViewClient(new WebViewClient() {
			ProgressDialog dialog = null;

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (url.indexOf("/api/login/login_result.ashx") > 0) {
					wv.setVisibility(View.INVISIBLE);
					dialog = new ProgressDialog(LoginScreen.this);
					dialog.setMessage("You are entering the wrong place!!!!");
					dialog.show();

				}
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// when login is complete, the url will be login_result.ashx?r=0
				if (url.indexOf("/api/login/login_result.ashx") > 0) {
					// When login is successful, there will be a &r=0 in the
					// url. It also means the return data is the token itself.
					wv.setVisibility(View.INVISIBLE);
				}
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// when login is complete, the url will be login_result.ashx?r=0
				if (url.indexOf("/api/login/login_result.ashx") > 0) {
					Log.d(TAG, "finish loading");
					dialog.hide();
					// When login is successful, there will be a &r=0 in the
					// url. It also means the return data is the token itself.
					if (url.indexOf("&r=0") > 0) {
						finish();
						view.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('body')[0].innerHTML);");
					}
				}
			}
		});

		wv.loadUrl(getString(R.string.login_uri) + "?"
				+ getString(R.string.login_apikeyfield) + "="
				+ getString(R.string.apikey));
	}

	final Context myApp = this;

	/* An instance of this class will be registered as a JavaScript interface */
	class MyJavaScriptInterface {
		@SuppressWarnings("unused")
		public void processHTML(String html) {
			IVLE_Token = html;
			SharedPreferences settings = getSharedPreferences(Main.SHARED_PREF_FILE, MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("IVLE_Token", IVLE_Token);
			editor.commit();
			Log.d(TAG, "set token to" + IVLE_Token);

			// process the html as needed by the app

			/*
			 * new AlertDialog.Builder(myApp) .setTitle("Token Value")
			 * .setMessage(IVLE_Token) .setPositiveButton(android.R.string.ok,
			 * null) .setCancelable(false) .create().show(); /*
			 * 
			 * ArrayList<NameValuePair> listParams = new
			 * ArrayList<NameValuePair>(); listParams.add(new
			 * BasicNameValuePair(
			 * getString(R.string.apikeyfield),"AMie2EgILFdcDJP06BAOZ"));
			 * listParams.add(new
			 * BasicNameValuePair(getString(R.string.authfield),IVLE_Token));
			 * listParams.add(new
			 * BasicNameValuePair(getString(R.string.startdatefield
			 * ),"01/06/2011")); listParams.add(new
			 * BasicNameValuePair(getString(
			 * R.string.enddatefield),"10/08/2011"));
			 * 
			 * String result =
			 * DataHandler.sendRequestUsingGet(getString(R.string
			 * .my_organizer_uri), listParams);
			 * 
			 * //print out the token or save for next logon or to navigate to
			 * next API call. Log.i("Token", result);
			 */
		}
	}
}
