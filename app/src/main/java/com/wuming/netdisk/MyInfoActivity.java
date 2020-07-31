package com.wuming.netdisk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wuming.http.HttpUtils;
import com.wuming.model.User;
import com.wuming.model.User_Info;
import com.wuming.sqlitehelp.DataBaseHelper;
import com.wuming.storage.NNSharedpredpreferences;

import cz.msebera.android.httpclient.Header;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends BaseActivity implements OnClickListener {
	private ImageView myinfo_title_icon;
	private TextView my_info_username, my_info_sex;
	private EditText my_info_nick;
	private RadioGroup sex;
	private RadioButton girl, boy;
	private User_Info info;
	private Button confirm_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NNSharedpredpreferences shared = new NNSharedpredpreferences(
				MyInfoActivity.this);
		User user = shared.getUser();
		try {
			DataBaseHelper.DBhelper(MyInfoActivity.this, user.getUser_name());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Cursor c = DataBaseHelper.sdb.rawQuery(
				" select * from userinfo where user_id ='" + user.getUser_ID()
						+ "' ", null);
		while (c.moveToNext()) {
			info = new User_Info(c.getString(c.getColumnIndex("user_id")),
					c.getString(c.getColumnIndex("nick")), c.getString(c
							.getColumnIndex("sex")), c.getString(c
							.getColumnIndex("sizes")), c.getString(c
							.getColumnIndex("used")), c.getInt(c
							.getColumnIndex("ifVip")));
		}
		my_info_username.setText(user.getUser_name());
		my_info_sex.setText(info.getSex());
		if (!(info.getSex().equals("未设置"))) {
			if (info.getSex().equals("男"))
				boy.setChecked(true);
			if (info.getSex().equals("女"))
				girl.setChecked(true);
		}
		my_info_nick.setText(info.getNick().toString());
	}

	@Override
	public void initView() {
		myinfo_title_icon = (ImageView) findViewById(R.id.myinfo_title_icon);
		my_info_username = (TextView) findViewById(R.id.my_info_username);
		my_info_nick = (EditText) findViewById(R.id.my_info_nick);
		my_info_sex = (TextView) findViewById(R.id.my_info_sex);
		sex = (RadioGroup) findViewById(R.id.sex);
		girl = (RadioButton) findViewById(R.id.girl);
		boy = (RadioButton) findViewById(R.id.boy);
		confirm_btn = (Button) findViewById(R.id.my_info_btn);
	}

	@Override
	public void initEvent() {
		sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == girl.getId()) {
					my_info_sex.setText("女");
				} else if (arg1 == boy.getId()) {
					my_info_sex.setText("男");
				}
			}
		});
		myinfo_title_icon.setOnClickListener(this);
		confirm_btn.setOnClickListener(this);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_info);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.myinfo_title_icon:
			this.finish();
			break;
		case R.id.my_info_btn:
			RequestParams params = new RequestParams();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("userId", info.getUser_id());
				jsonObject.put("nick", my_info_nick.getText().toString());
				jsonObject.put("sex", my_info_sex.getText().toString());
				jsonArray.put(0, jsonObject);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			params.put("json", jsonArray.toString());
			HttpUtils.post(HttpUtils.SERVLET_UPDATEINFO, params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							boolean result = false;
							try {
								JSONObject jsonObject = response
										.getJSONObject(0);
								result = jsonObject.getBoolean("result");
								if (result) {
									NNSharedpredpreferences nnSharedpredpreferences = new NNSharedpredpreferences(
											MyInfoActivity.this);
									DataBaseHelper.DBhelper(
											MyInfoActivity.this,
											nnSharedpredpreferences.getUser()
													.getUser_name());
									ContentValues values = new ContentValues();
									values.put("user_id", info.getUser_id());
									values.put("ifVip", info.getIfVip());
									values.put("sizes", info.getSizes());
									values.put("used", info.getUsed());
									values.put("sex", my_info_sex.getText()
											.toString());
									values.put("nick", my_info_nick.getText()
											.toString());
									try{
										DataBaseHelper.sdb
										.update("userinfo",
												values,
												"user_id=?",
												new String[] { nnSharedpredpreferences
														.getUser()
														.getUser_ID() });
									}catch(Exception e){
										e.printStackTrace();
									}
									
									MyInfoActivity.this.finish();
									Toast.makeText(MyInfoActivity.this, "修改成功",
											Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							Toast.makeText(MyInfoActivity.this, "网络好像有异常哦",
									Toast.LENGTH_SHORT).show();
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
						}
					});
		default:
			break;
		}

	}

}
