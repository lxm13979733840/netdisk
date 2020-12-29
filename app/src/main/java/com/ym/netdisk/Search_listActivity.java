package com.ym.netdisk;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ym.http.HttpUtils;
import com.ym.model.Search_M;

import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
  * @Copyright (C)
  * @Author: ym
  * @Date:
  * @Description:
  */
public class Search_listActivity extends BaseActivity implements
		OnClickListener {
	ArrayList<Search_M> list = new ArrayList<Search_M>();
	private ListView search_listview;
	ImageView search_title_icon;
	SearchAdapter adapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		 list = (ArrayList<Search_M>) bundle.getSerializable("list");
		adapter = new SearchAdapter();	
		search_listview.setAdapter(adapter);
		registerForContextMenu(search_listview);
	}

	@Override
	public void initView() {
		search_listview = (ListView) findViewById(R.id.search_listview);
		search_title_icon = (ImageView) findViewById(R.id.search_title_icon);

	}

	@Override
	public void initEvent() {
		search_title_icon.setOnClickListener(this);
	}

	@Override
	public void init(Bundle savedInstanceState) {
		setContentView(R.layout.activity_search_list);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, 1, 0, "删除");
		menu.add(0, 2, 0, "移动");
		menu.add(0, 3, 0, "重命名");
		menu.add(0, 4, 0, "下载");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = menuInfo.position;
		Search_M m = list.get(position);
		switch (item.getItemId()) {
		case 1:
			RequestParams params = new RequestParams();
			params.put("file", m.getAbname());
			HttpUtils.post(HttpUtils.SERVLET_DELETEFILE, params,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							boolean result = false;
							try {
								JSONObject object = response.getJSONObject(0);
								result = object.getBoolean("result");
								if (result) {
									Toast.makeText(Search_listActivity.this,
											"删除成功", Toast.LENGTH_SHORT).show();
									adapter.notifyDataSetInvalidated();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
							Toast.makeText(Search_listActivity.this, "网络连接失败",
									Toast.LENGTH_SHORT).show();
						}
					});
			break;
		case 2:
			Intent intent = new Intent(Search_listActivity.this,
					Search_FileActivity.class);
			intent.putExtra("move_file", m);
			startActivity(intent);
			break;
		case 3:
			Intent intents = new Intent(Search_listActivity.this,
					RenameFileActivity.class);
			intents.putExtra("reaname_file", m);
			startActivity(intents);
			break;
		case 4:
			break;
		default:
			break;
		}
		return true;
	}

	class SearchAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int arg0) {

			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null)
				arg1 = LayoutInflater.from(Search_listActivity.this).inflate(
						R.layout.serach_list, null);
			ImageView icon = (ImageView) arg1
					.findViewById(R.id.search_list_item_icon);
			TextView name = (TextView) arg1
					.findViewById(R.id.search_list_item_name);
			TextView sizes = (TextView) arg1
					.findViewById(R.id.search_list_item_size);
			TextView time = (TextView) arg1
					.findViewById(R.id.search_list_item_time);
			if (list.get(arg0).getIfdir() == 0) {
				icon.setBackgroundResource(R.drawable.folderfile);
			} else if (list.get(arg0).getName().endsWith(".txt")
					|| list.get(arg0).getName().endsWith(".doc")
					|| list.get(arg0).getName().endsWith(".docx")) {
				icon.setBackgroundResource(R.drawable.docfile);
			} else if (list.get(arg0).getName().endsWith(".jpg")
					|| list.get(arg0).getName().endsWith(".png")
					|| list.get(arg0).getName().endsWith(".gif")) {
				icon.setBackgroundResource(R.drawable.imgfile);
			} else if (list.get(arg0).getName().endsWith(".mp4")
					|| list.get(arg0).getName().endsWith(".avi")
					|| list.get(arg0).getName().endsWith(".rmvb")) {
				icon.setBackgroundResource(R.drawable.videofile);
			} else if (list.get(arg0).getName().endsWith(".mp3")
					|| list.get(arg0).getName().endsWith(".ape")) {
				icon.setBackgroundResource(R.drawable.micfile);
			} else {
				icon.setBackgroundResource(R.drawable.unknownfile);
			}
			name.setText(list.get(arg0).getName());
			sizes.setText(list.get(arg0).getSizes());
			time.setText(list.get(arg0).getLasttime());
			return arg1;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.search_title_icon:
			Search_listActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
