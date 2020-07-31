package com.wuming.netdisk;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wuming.http.HttpUtils;
import com.wuming.model.Search_M;
import com.wuming.model.User;
import com.wuming.storage.NNSharedpredpreferences;

import cz.msebera.android.httpclient.Header;

import android.os.Bundle;

import android.content.Intent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Search_classActivity extends BaseActivity implements
		OnClickListener {
	private EditText et;
	private Button btn;
	private RelativeLayout search_class_rl;
	ArrayList<Search_M> search_ms = new ArrayList<Search_M>();

	String searchclass = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		searchclass = getIntent().getStringExtra("searchclass");
	}

	public String switchingUnit(long lengths) {
		String sizes = null;
		if (lengths < 1024) {
			sizes = String.valueOf(lengths) + "B";
		} else if (lengths / 1024 < 1024) {
			sizes = String.valueOf(lengths / 1024) + "KB";
		} else if (lengths / (1024 * 1024) < 1024) {
			sizes = String.valueOf(lengths / (1024 * 1024)) + "MB";
		} else if (lengths / (1024 * 1024 * 1024) < 1024) {
			sizes = String.valueOf(lengths / (1024 * 1024 * 1024)) + "GB";
		}
		return sizes;
	}

	@Override
	public void initView() {
		et = (EditText) findViewById(R.id.search_class);
		btn = (Button) findViewById(R.id.search_btn);
		search_class_rl = (RelativeLayout) findViewById(R.id.search_class_rl);
	}

	@Override
	public void initEvent() {
		btn.setOnClickListener(this);
		search_class_rl.setOnClickListener(this);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		setContentView(R.layout.activity_search_class);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.search_btn:
			RequestParams params = new RequestParams();
			params.put("type", searchclass);
			params.put("filename", et.getText().toString());
			NNSharedpredpreferences shared = new NNSharedpredpreferences(
					Search_classActivity.this);
			User user = shared.getUser();
			params.put("userId", user.getUser_ID());
			HttpUtils.post(HttpUtils.SERVLET_SEARCHFILE, params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							for (int i = 0; i < response.length(); i++) {
								try {
									JSONObject jsonObject = response
											.getJSONObject(i);
									String abname = jsonObject
											.getString("fileAbsolutePath");
									String lasttime = jsonObject
											.getString("lastModified");
									String name=jsonObject.getString("filePath");
									int ifdir =jsonObject.getInt("ifDir");
									long lengths = jsonObject
											.getLong("fileLength");
									String sizes = switchingUnit(lengths);
									Search_M m = new Search_M(abname, name, lasttime, sizes, ifdir);		
									search_ms.add(m);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							Intent intent = new Intent(
									Search_classActivity.this,
									Search_listActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("list", search_ms);
							intent.putExtras(bundle);
							startActivity(intent);
							Search_classActivity.this.finish();
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
							Toast.makeText(Search_classActivity.this, "网络连接失败",
									Toast.LENGTH_SHORT).show();
						}
					});
			break;
		case R.id.search_class_rl:
			this.finish();
			break;
		default:
			break;
		}
	}

}
