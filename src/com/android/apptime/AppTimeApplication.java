package com.android.apptime;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import android.R.integer;
import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class AppTimeApplication extends Application {
	private static AppTimeApplication instance = null;

	void Application() {
		instance = this;
	}

	public static AppTimeApplication getApplication() {
		return instance;
	}
}
