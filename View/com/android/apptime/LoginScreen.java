package com.android.apptime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends Activity {
	private EditText mEtUsername = null;
	private EditText mEtPassword = null;
	private EditText mEtPageCode = null;
	private Button mBtLogin = null;
	private String mStrUserName = "";
	private String mStrPassword = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginscreen);
		mEtUsername = (EditText) findViewById(R.id.etUsername);
		mEtPassword = (EditText) findViewById(R.id.etPassword);
		mBtLogin = (Button) findViewById (R.id.btLogin);
		mEtUsername.setOnFocusChangeListener(new OnFocusChangeResetText());
		mEtPassword.setOnFocusChangeListener(new OnFocusChangeResetText());
		mEtUsername.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
				{
					mStrUserName = mEtUsername.getText().toString();
					return false;
				}
				return true;
			}
		});
		mEtPassword.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
				{
					mStrPassword = mEtPassword.getText().toString();
					return false;
				}
				return false;
			}
		});
		
		
		mBtLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleLogInClicked();
			}
		});
		
	}

	private void handleLogInClicked() {
		mStrUserName = mEtUsername.getText().toString();
		mStrPassword = mEtPassword.getText().toString();
		if (mStrUserName.equals("") || mStrPassword.equals("")){
			Toast.makeText(getApplicationContext(), "Please fill in both username and password!", Toast.LENGTH_SHORT).show();
			if (mStrUserName.equals("")) mEtUsername.requestFocus();
			else mEtPassword.requestFocus();
		} else {
			login(mStrUserName, mStrPassword);
		}
	}
	
	private void login(String mStrUserName, String mStrPassword) {
		boolean result = true;//login(mStrUserName, mStrPassword);
		if (result){
			startActivity(new Intent(getApplicationContext(), Main.class));
			this.finish();
		} else {
			Toast.makeText(getApplicationContext(), "Incorrect username or password! Please try again.", Toast.LENGTH_SHORT).show();
			mEtUsername.requestFocus();
		}
	}

	

	private class OnFocusChangeResetText implements View.OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			EditText editText = (EditText) v;
			if (hasFocus){
				editText.setText("");
			}
			else {
			}
		}
	};
}
