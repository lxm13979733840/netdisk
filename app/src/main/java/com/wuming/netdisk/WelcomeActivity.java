package com.wuming.netdisk;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wuming.http.HttpUtils;
import com.wuming.model.User;
import com.wuming.storage.NNSharedpredpreferences;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class WelcomeActivity extends BaseActivity {
	Handler handler;
	User user;
	String result= "false" ;
	public static final int TIME = 5000;
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (user.getUser_name().equals("")
					|| user.getUser_password().equals("")) {
				startActivity(new Intent(WelcomeActivity.this,
						LoginActivity.class));
				WelcomeActivity.this.finish();
			} else {
				RequestParams params = new RequestParams();
				params.put("username", user.getUser_name());
				params.put("password", user.getUser_password());
				HttpUtils.post(HttpUtils.SERVLET_LOGIN, params,
						new JsonHttpResponseHandler() {
							public void onSuccess(
									int statusCode,
									cz.msebera.android.httpclient.Header[] headers,
									org.json.JSONArray response) {
								try {
									JSONObject jsonObject = response.getJSONObject(0);
									result = jsonObject.getString("result");
									if(result.equals("false")){
										startActivity(new Intent(WelcomeActivity.this,
												LoginActivity.class));
										WelcomeActivity.this.finish();
									}else{
										startActivity(new Intent(WelcomeActivity.this,
												MainActivity.class));
										WelcomeActivity.this.finish();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							};

							public void onFailure(
									int statusCode,
									cz.msebera.android.httpclient.Header[] headers,
									Throwable throwable,
									org.json.JSONObject errorResponse) {
								Toast.makeText(WelcomeActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
							};
						});
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NNSharedpredpreferences shared = new NNSharedpredpreferences(
				WelcomeActivity.this);
		user = shared.getUser();
		handler = new Handler();
		handler.postAtTime(runnable, TIME);
	}

	@Override
	public void initView() {

	}

	@Override
	public void initEvent() {

	}

	@Override
	public void init(Bundle savedInstanceState) {
		setContentView(R.layout.activity_welcome);
	}

}
