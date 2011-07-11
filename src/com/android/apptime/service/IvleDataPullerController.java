package com.android.apptime.service;




import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;

import com.android.apptime.R;

public class IvleDataPullerController extends TabActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startService(new Intent(IvleDataPullerController.this,IvleDataPuller.class));
        stopService(new Intent(IvleDataPullerController.this,IvleDataPuller.class));        
   }
}
