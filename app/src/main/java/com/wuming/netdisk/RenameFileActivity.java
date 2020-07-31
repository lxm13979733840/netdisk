package com.wuming.netdisk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wuming.http.HttpUtils;
import com.wuming.model.Search_M;

import cz.msebera.android.httpclient.Header;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RenameFileActivity extends Activity implements OnClickListener{
	ImageView renamefile_title_icon ;
	EditText renamefile_newname_ed;
	Button renamefile_newname_btn;
	Search_M file ;
	String [] names;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rename_file);
		initView();
		initEvent();
		file =(Search_M) getIntent().getSerializableExtra("rename_file");
		names =file.getName().toString().split("\\.");
		renamefile_newname_ed.setText(names[0]);
	}
	private void initEvent() {
		renamefile_title_icon.setOnClickListener(this);
		renamefile_newname_btn.setOnClickListener(this);
	}
	private void initView() {
		renamefile_title_icon =(ImageView) findViewById(R.id.renamefile_title_icon);
		renamefile_newname_ed =(EditText) findViewById(R.id.renamefile_newname_ed);
		renamefile_newname_btn =(Button) findViewById(R.id.renamefile_newname_btn);	
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.renamefile_title_icon:
			RenameFileActivity.this.finish();
			break;
		case R.id.renamefile_newname_btn:
			if(renamefile_newname_ed==null||renamefile_newname_ed.equals("")){
				Toast.makeText(RenameFileActivity.this, "文件夹名不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			RequestParams params = new RequestParams();
			params.put("renamefile", file.getAbname());
			if(names.length==1){
				params.put("newfile", renamefile_newname_ed.getText().toString());
			}else{
			params.put("newfile", renamefile_newname_ed.getText().toString()+"."+names[1]);}
			HttpUtils.post(HttpUtils.SERVLET_RENAMEFILE, params , new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONArray response) {
					super.onSuccess(statusCode, headers, response);
					boolean result =false ;
					try {
						JSONObject jsonObject =response.getJSONObject(0);
						result=jsonObject.getBoolean("result");
						if(result){
							Toast.makeText(RenameFileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
							RenameFileActivity.this.finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					Toast.makeText(RenameFileActivity.this, "网络好像出了问题哦", Toast.LENGTH_SHORT).show();
				}
			});
			break;
		default:
			break;
		}
	}

}
