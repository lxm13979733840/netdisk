package com.ym.netdisk;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ym.http.HttpUtils;

import cz.msebera.android.httpclient.Header;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	private EditText register_user, register_password, register_password2;
	private Button register_btn;
	private ImageView register_title_returns;
	private String username;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void initView() {
		register_user = (EditText) findViewById(R.id.register_user);
		register_password = (EditText) findViewById(R.id.register_password);
		register_password2 = (EditText) findViewById(R.id.register_password2);
		register_btn = (Button) findViewById(R.id.register_btn);
		register_title_returns = (ImageView) findViewById(R.id.register_title_returns);
	}

	@Override
	public void initEvent() {
		register_btn.setOnClickListener(this);
		register_title_returns.setOnClickListener(this);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		setContentView(R.layout.activity_register);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.register_btn:
			username = register_user.getText().toString();
			password = register_password.getText().toString();
			String password2 = register_password2.getText().toString();
			if (password.equals(password2)) {
				if (password.length() > 5 && password2.length() > 5) {
					RequestParams params = new RequestParams();
					params.put("username", username);
					params.put("password", password);
					HttpUtils.post(HttpUtils.SERVLET_REGIST, params,
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode,
										Header[] headers, JSONArray response) {
									super.onSuccess(statusCode, headers,
											response);
									try {
										JSONObject object = response
												.getJSONObject(0);
										String resule = (String) object
												.get("result");
										if (resule.equals("true")) {
											HttpUtils.userLogin(
													RegisterActivity.this,
													username, password);
										} else {
											Toast.makeText(
													RegisterActivity.this,
													"用戶名已存在，請重新註冊",
													Toast.LENGTH_SHORT).show();
										}
									} catch (JSONException e) {

										e.printStackTrace();
									}
								}

								@Override
								public void onFailure(int statusCode,
										Header[] headers, Throwable throwable,
										JSONObject errorResponse) {
									super.onFailure(statusCode, headers,
											throwable, errorResponse);
									Toast.makeText(RegisterActivity.this,
											"服务器连接失败", Toast.LENGTH_SHORT)
											.show();
								}
							});
				} else {
					Toast.makeText(RegisterActivity.this, "输入的密码长度不能小于5",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				register_password.setText("");
				register_password2.setText("");
				Toast.makeText(RegisterActivity.this, "密码不一致请重新输入",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.register_title_returns:
			this.finish();
			break;
		default:
			break;
		}
	}

}
