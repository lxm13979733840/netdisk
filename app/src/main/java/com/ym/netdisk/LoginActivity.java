package com.ym.netdisk;

import com.ym.http.HttpUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView login_register;
	private EditText login_user, login_password;
	private Button login_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		login_user = (EditText) findViewById(R.id.login_user);
		login_password = (EditText) findViewById(R.id.login_password);
		login_btn = (Button) findViewById(R.id.login_btn);
		login_register = (TextView) findViewById(R.id.login_register);
	}

	@Override
	public void initEvent() {
		login_btn.setOnClickListener(this);
		login_register.setOnClickListener(this);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login_btn:
			arg0.setClickable(false);
			Toast.makeText(LoginActivity.this, "正在登录...",Toast.LENGTH_SHORT).show();
			HttpUtils.userLogin(LoginActivity.this, login_user.getText()
					.toString(), login_password.getText().toString());
			break;
		case R.id.login_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		}

	}
}
