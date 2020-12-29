package com.ym.sqlitehelp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class DataBaseHelper extends SQLiteOpenHelper {
	public static SQLiteDatabase sdb;
	public static int VERSION = 1;

	public static void DBhelper(Context context, String name) {
		DataBaseHelper dbh = new DataBaseHelper(context, name);
		sdb = dbh.getWritableDatabase();
	}

	public DataBaseHelper(Context context, String name) {
		super(context, name, null, VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("create table userinfo (user_id text primary key, sizes text ,used text, nick text, sex text,ifVip Integer)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
