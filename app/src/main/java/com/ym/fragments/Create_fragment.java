package com.ym.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ym.http.HttpUtils;
import com.ym.netdisk.R;

import cz.msebera.android.httpclient.Header;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class Create_fragment extends Fragment implements OnClickListener {
	private EditText createfile_name_ed;
	private Button createfile_name_btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_create, container, false);
		initView(view);

		return view;
	}

	private void initView(View view) {
		createfile_name_ed = (EditText) view
				.findViewById(R.id.createfile_name_ed);
		createfile_name_btn = (Button) view
				.findViewById(R.id.createfile_name_btn);
		createfile_name_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.createfile_name_btn:
			String url = null;
			if (MainPage_fragment.files.get(MainPage_fragment.flag).getName() == null) {
				url = MainPage_fragment.files.get(MainPage_fragment.flag)
						.getParent();
			} else {
				url = MainPage_fragment.files.get(MainPage_fragment.flag)
						.getParent()
						+ "\\"
						+ MainPage_fragment.files.get(MainPage_fragment.flag)
								.getName();
			}
			RequestParams params =new RequestParams();
			params.put("filePath", url);
			params.put("dirName", createfile_name_ed.getText().toString());
			HttpUtils.post(HttpUtils.SERVLET_CREATENEWDIR, params , new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONArray response) {
					super.onSuccess(statusCode, headers, response);
					JSONObject jsonObject;
					try {
						jsonObject = response.getJSONObject(0);
						boolean result =false ;
						result =jsonObject.getBoolean("result");
						if(result){
							Toast.makeText(getActivity(), "创建文件夹成功", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "创建文件夹失败", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, JSONObject errorResponse) {
					super.onFailure(statusCode, headers, throwable, errorResponse);
					Toast.makeText(getActivity(), "网络好像有问题哦", Toast.LENGTH_SHORT).show();
				}
			});
			break;
		}
	}

}
