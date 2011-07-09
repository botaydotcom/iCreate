package com.android.apptime.service;




import com.android.apptime.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class IvleDataPullerController extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startService(new Intent(IvleDataPullerController.this,IvleDataPuller.class));
        stopService(new Intent(IvleDataPullerController.this,IvleDataPuller.class));
        
       
        
   }
}
