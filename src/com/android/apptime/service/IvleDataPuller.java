package com.android.apptime.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.android.apptime.datahandler.Ivle;
import com.android.apptime.datahandler.IvleTimetableData;

public class IvleDataPuller extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		Ivle ivledata = new Ivle();
<<<<<<< HEAD
		List<IvleTimetableData>  timetable = ivledata.getTimetableList("2011-2012", "1");
=======
		//List<IvleTimetableData>  timetable = ivledata.getTimetable("2011/2012","1" );
>>>>>>> 1e2ad1b016fbb91de43ca56011297260e9613e2f
		
		Toast.makeText(this,"IVLE serv created", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service destroyed ...", Toast.LENGTH_LONG).show();
	}

		
}
