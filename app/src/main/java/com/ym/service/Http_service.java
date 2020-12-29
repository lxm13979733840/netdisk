package com.ym.service;

import org.json.JSONArray;
import org.json.JSONObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ym.http.HttpUtils;
import com.ym.model.User;
import com.ym.sqlitehelp.DataBaseHelper;
import com.ym.storage.NNSharedpredpreferences;

import cz.msebera.android.httpclient.Header;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class Http_service extends Service {
	User user ;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		NNSharedpredpreferences shared =new NNSharedpredpreferences(this);
		user =shared.getUser();
		RequestParams params =new RequestParams();
		try{
			params.put("userID", user.getUser_ID());
		}catch(Exception e){
			e.printStackTrace();
		}
		HttpUtils.post(HttpUtils.SERVLET_GETINFO, params , new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				super.onSuccess(statusCode, headers, response);
				try {
					JSONObject jsonObject =response.getJSONObject(0);
					String sizes=jsonObject.getString("capacity");
					//String used =jsonObject.getString("used");
					String nick =jsonObject.getString("nick");
					String sex =jsonObject.getString("sex");
					int ifVip=jsonObject.getInt("ifVip");
					DataBaseHelper.DBhelper(Http_service.this, user.getUser_name());
					ContentValues values =new ContentValues();
					values.put("user_id", user.getUser_ID());
					values.put("sizes", sizes);
					values.put("used", "0");
					values.put("nick", nick);
					values.put("sex", sex);
					values.put("ifVip",ifVip);
					DataBaseHelper.sdb.insert("userinfo", null, values );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(Http_service.this, "网络好像有问题哦", Toast.LENGTH_SHORT).show();
			}
		});
		return super.onStartCommand(intent, flags, startId);
	}

}
